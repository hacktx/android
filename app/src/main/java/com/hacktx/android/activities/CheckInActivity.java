/*
 * Copyright 2017 HackTX.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hacktx.android.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hacktx.android.R;
import com.hacktx.android.io.UserStateStore;
import com.hacktx.android.utils.HackTXUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CheckInActivity extends BaseActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.activity_check_in_title);
        }

        if (getIntent().getBooleanExtra("fromShortcut", false)
                && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            shortcutManager.reportShortcutUsed("check-in");
        }

        setupStatusBar();
        setupCards();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatusFlag(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatusFlag(false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final View scrollView = findViewById(R.id.scrollView);
            final int bigMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());

            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) scrollView.getLayoutParams();
            p.setMargins(0, bigMargin, 0, 0);
            scrollView.requestLayout();
        }
    }

    private void setTranslucentStatusFlag(boolean on) {
        if (Build.VERSION.SDK_INT >= 19) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

    private void setupCards() {
        if (!HackTXUtils.hasHackTxStarted(CheckInActivity.this)) {
            findViewById(R.id.welcomeCard).setVisibility(View.GONE);
            findViewById(R.id.comingSoonCard).setVisibility(View.VISIBLE);
        } else if (HackTXUtils.hasHackTxEnded(CheckInActivity.this)) {
            findViewById(R.id.welcomeCard).setVisibility(View.GONE);
            findViewById(R.id.emailCard).setVisibility(View.GONE);
            findViewById(R.id.codeCard).setVisibility(View.GONE);
            findViewById(R.id.endedCard).setVisibility(View.VISIBLE);
        }

        if (UserStateStore.isUserEmailSet(this)) {
            String email = UserStateStore.getUserEmail(this);

            findViewById(R.id.emailCard).setVisibility(View.GONE);
            if (!HackTXUtils.hasHackTxStarted(CheckInActivity.this)) {
                findViewById(R.id.finishedSoonCard).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.finishedSoonCardText)).setText(getString(R.string.activity_check_in_finish_soon_text, email));
            } else if (!HackTXUtils.hasHackTxEnded(CheckInActivity.this)) {
                findViewById(R.id.codeCard).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.codeCardText)).setText(getString(R.string.activity_check_in_code_text, email));
                loadQrCode(email);
                increaseBrightness();
            }
        }

        View.OnClickListener backOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };

        View.OnClickListener resetOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMetricsManager.logEvent(R.string.analytics_event_check_in_reset, null);

                UserStateStore.setUserEmail(CheckInActivity.this, "");
                deleteSavedCode();

                findViewById(R.id.codeCard).setVisibility(View.GONE);
                findViewById(R.id.codeCardCode).setVisibility(View.GONE);
                findViewById(R.id.codeCardButtons).setVisibility(View.GONE);
                findViewById(R.id.codeCardProgressBar).setVisibility(View.VISIBLE);
                findViewById(R.id.finishedSoonCard).setVisibility(View.GONE);
                ((EditText) findViewById(R.id.emailCardEditText)).setText("");
                findViewById(R.id.emailCard).setVisibility(View.VISIBLE);
            }
        };

        findViewById(R.id.emailCardOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailEditText = (EditText) findViewById(R.id.emailCardEditText);
                final String email = emailEditText.getText().toString();
                if (!email.isEmpty() && email.contains("@")) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(emailEditText.getWindowToken(), 0);

                    AlertDialog.Builder builder = new AlertDialog.Builder(CheckInActivity.this, R.style.DialogStyle);
                    builder.setTitle(getString(R.string.activity_check_in_dialog_verify_title));
                    builder.setMessage(getString(R.string.activity_check_in_dialog_verify_body, email));
                    builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mMetricsManager.logEvent(R.string.analytics_event_set_email, null);
                            UserStateStore.setUserEmail(CheckInActivity.this, email);
                            findViewById(R.id.emailCard).setVisibility(View.GONE);
                            if (!HackTXUtils.hasHackTxStarted(CheckInActivity.this)) {
                                findViewById(R.id.finishedSoonCard).setVisibility(View.VISIBLE);
                                ((TextView) findViewById(R.id.finishedSoonCardText)).setText(getString(R.string.activity_check_in_finish_soon_text, email));
                            } else {
                                findViewById(R.id.codeCard).setVisibility(View.VISIBLE);
                                ((TextView) findViewById(R.id.codeCardText)).setText(getString(R.string.activity_check_in_code_text, email));
                                increaseBrightness();
                            }

                            dialog.dismiss();
                            loadQrCode(email);
                        }
                    });
                    builder.setNegativeButton(getString(R.string.no), null);
                    builder.show();
                } else {
                    mMetricsManager.logEvent(R.string.analytics_event_invalid_email, null);
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.activity_check_in_invalid_email), Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.finishedSoonCardReset).setOnClickListener(resetOnClickListener);
        findViewById(R.id.codeCardReset).setOnClickListener(resetOnClickListener);

        findViewById(R.id.emailCardCancel).setOnClickListener(backOnClickListener);
        findViewById(R.id.codeCardDone).setOnClickListener(backOnClickListener);
        findViewById(R.id.finishedSoonOk).setOnClickListener(backOnClickListener);
        findViewById(R.id.endedCardOk).setOnClickListener(backOnClickListener);
    }

    private void loadQrCode(final String email) {
        new Thread(new Runnable() {
            public void run() {
                QRCodeWriter writer = new QRCodeWriter();
                final Bitmap code;
                try {
                    if(!isCodeSaved()) {
                        BitMatrix bitMatrix = writer.encode(email, BarcodeFormat.QR_CODE, 512, 512);
                        int width = bitMatrix.getWidth();
                        int height = bitMatrix.getHeight();
                        code = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                        for (int x = 0; x < width; x++) {
                            for (int y = 0; y < height; y++) {
                                code.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : ContextCompat.getColor(CheckInActivity.this, R.color.cardview_light_background));
                            }
                        }

                        saveCodeToInternalStorage(code);
                    } else {
                        code = loadCodeFromInternalStorage();
                    }

                    CheckInActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageView) findViewById(R.id.codeCardCode)).setImageBitmap(code);
                            findViewById(R.id.codeCardProgressBar).setVisibility(View.GONE);
                            findViewById(R.id.codeCardCode).setVisibility(View.VISIBLE);
                            findViewById(R.id.codeCardButtons).setVisibility(View.VISIBLE);
                        }
                    });
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean saveCodeToInternalStorage(Bitmap image) {
        try {
            FileOutputStream fos = this.openFileOutput("qr.png", Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private Bitmap loadCodeFromInternalStorage() {
        try {
            File f = new File(getFilesDir().getPath(), "qr.png");
            return BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean isCodeSaved() {
        return new File(getFilesDir().getPath(), "qr.png").exists();
    }

    private boolean deleteSavedCode() {
        return new File(getFilesDir().getPath(), "qr.png").delete();
    }

    private void increaseBrightness() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = 1;
        getWindow().setAttributes(layoutParams);
    }

}
