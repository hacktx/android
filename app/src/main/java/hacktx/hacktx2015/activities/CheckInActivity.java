package hacktx.hacktx2015.activities;

import android.app.ActivityManager;
import android.app.ProgressDialog;
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
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import hacktx.hacktx2015.R;
import hacktx.hacktx2015.utils.HackTXUtils;

public class CheckInActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.activity_check_in_title);
        }

        setupTaskActivityInfo();
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

    protected void setupTaskActivityInfo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String appName = getString(R.string.app_name);
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            int color = ContextCompat.getColor(this, R.color.primaryDark);
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(appName, icon, color);
            setTaskDescription(taskDesc);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.hacktx_blue_dark));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.hacktx_red));
        }
    }

    private void setupCards() {

        if(!HackTXUtils.hasHackTxStarted()) {
            findViewById(R.id.welcomeCard).setVisibility(View.GONE);
            findViewById(R.id.comingSoonCard).setVisibility(View.VISIBLE);
        }

        View.OnClickListener backOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };

        findViewById(R.id.emailCardOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailEditText = (EditText) findViewById(R.id.emailCardEditText);
                final String email = emailEditText.getText().toString();
                if(!email.isEmpty() && email.contains("@")) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(emailEditText.getWindowToken(), 0);

                    AlertDialog.Builder builder = new AlertDialog.Builder(CheckInActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Verify Email");
                    builder.setMessage("Is " + email + " correct?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            findViewById(R.id.emailCard).setVisibility(View.GONE);
                            if(!HackTXUtils.hasHackTxStarted()) {
                                findViewById(R.id.finishedSoonCard).setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            } else {
                                findViewById(R.id.codeCard).setVisibility(View.VISIBLE);
                                dialog.dismiss();

                                loadQrCode(email);
                            }
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Invalid email address.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.emailCardCancel).setOnClickListener(backOnClickListener);

        findViewById(R.id.codeCardReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.codeCard).setVisibility(View.GONE);
                findViewById(R.id.codeCardCode).setVisibility(View.GONE);
                findViewById(R.id.codeCardButtons).setVisibility(View.GONE);
                findViewById(R.id.codeCardProgressBar).setVisibility(View.VISIBLE);
                ((EditText) findViewById(R.id.emailCardEditText)).setText("");
                findViewById(R.id.emailCard).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.codeCardDone).setOnClickListener(backOnClickListener);
        findViewById(R.id.finishedSoonOk).setOnClickListener(backOnClickListener);
    }

    private void loadQrCode(final String email) {
        new Thread(new Runnable() {
            public void run() {
                QRCodeWriter writer = new QRCodeWriter();
                try {
                    BitMatrix bitMatrix = writer.encode(email, BarcodeFormat.QR_CODE, 512, 512);
                    int width = bitMatrix.getWidth();
                    int height = bitMatrix.getHeight();
                    final Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : ContextCompat.getColor(CheckInActivity.this, R.color.cardview_light_background));
                        }
                    }

                    CheckInActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageView) findViewById(R.id.codeCardCode)).setImageBitmap(bmp);
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

}
