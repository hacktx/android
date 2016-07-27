package com.hacktx.android.activities;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.hacktx.android.R;
import com.hacktx.android.utils.MetricsManager;

public class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    protected MetricsManager mMetricsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMetricsManager = new MetricsManager(this);

        setupTaskActivityInfo();
    }

    private void setupTaskActivityInfo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String appName = getString(R.string.app_name);
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            int color = ContextCompat.getColor(this, R.color.taskbarColor);
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(appName, icon, color);
            setTaskDescription(taskDesc);
            getWindow().setStatusBarColor(color);
        }
    }
}