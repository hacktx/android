package hacktx.hacktx2015.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import hacktx.hacktx2015.R;

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
        setupEmailCard();
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

    private void setupEmailCard() {
        findViewById(R.id.emailCardOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailEditText = (EditText) findViewById(R.id.emailCardEditText);
                String email = emailEditText.getText().toString();
                if(!email.isEmpty() && email.contains("@")) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(emailEditText.getWindowToken(), 0);

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Verify Email");
                    builder.setMessage("Is " + email + " correct?");
                    builder.setPositiveButton("Yes", null);
                    builder.setNegativeButton("No", null);
                    builder.show();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Invalid email address.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

}
