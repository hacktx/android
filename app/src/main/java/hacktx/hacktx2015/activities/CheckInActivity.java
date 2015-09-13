package hacktx.hacktx2015.activities;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import hacktx.hacktx2015.R;
import hacktx.hacktx2015.network.UserStateStore;
import hacktx.hacktx2015.utils.HackTXUtils;

public class CheckInActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 22;
    private boolean previousNotifStatus;
    private boolean shouldStopBeaconNotif = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        previousNotifStatus = UserStateStore.getBeaconNotifEnabled(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.activity_check_in_title);
        }

        hideNotification();
        setupTaskActivityInfo();
        setupCards();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HackTXUtils.getGoogleAnalyticsTracker(this).setScreenName("Screen~" + "CheckIn");
        HackTXUtils.getGoogleAnalyticsTracker(this).send(new HitBuilders.ScreenViewBuilder().build());
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

    @Override
    public void onBackPressed() {
        if(previousNotifStatus) {
            UserStateStore.setBeaconNotifEnabled(CheckInActivity.this, !shouldStopBeaconNotif);
        }

        super.onBackPressed();
    }

    protected void setupTaskActivityInfo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String appName = getString(R.string.app_name);
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            int color = ContextCompat.getColor(this, R.color.taskbarColor);
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(appName, icon, color);
            setTaskDescription(taskDesc);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.hacktx_blue_dark));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.hacktx_red));
        }
    }

    private void hideNotification() {
        UserStateStore.setBeaconNotifEnabled(this, false);
        NotificationManager notifMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifMgr.cancel(NOTIFICATION_ID);
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
                shouldStopBeaconNotif = true;
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
                UserStateStore.setUserEmail(CheckInActivity.this, "");
                deleteSavedCode();
                shouldStopBeaconNotif = false;

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

                    AlertDialog.Builder builder = new AlertDialog.Builder(CheckInActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Verify Email");
                    builder.setMessage("Is " + email + " correct?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UserStateStore.setUserEmail(CheckInActivity.this, email);
                            findViewById(R.id.emailCard).setVisibility(View.GONE);
                            if (!HackTXUtils.hasHackTxStarted(CheckInActivity.this)) {
                                findViewById(R.id.finishedSoonCard).setVisibility(View.VISIBLE);
                                ((TextView) findViewById(R.id.finishedSoonCardText)).setText(getString(R.string.activity_check_in_finish_soon_text, email));
                            } else {
                                shouldStopBeaconNotif = true;
                                findViewById(R.id.codeCard).setVisibility(View.VISIBLE);
                                ((TextView) findViewById(R.id.codeCardText)).setText(getString(R.string.activity_check_in_code_text, email));
                                increaseBrightness();
                            }

                            dialog.dismiss();
                            loadQrCode(email);
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Invalid email address.", Snackbar.LENGTH_SHORT).show();
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
