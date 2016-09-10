/*
 * Copyright 2016 HackTX.
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

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.hacktx.android.R;
import com.hacktx.android.utils.ConfigManager;
import com.hacktx.android.utils.MetricsManager;

public class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    protected MetricsManager mMetricsManager;
    protected ConfigManager mConfigManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMetricsManager = new MetricsManager(this);
        mConfigManager = new ConfigManager(this);

        setupTaskActivityInfo();
    }

    private void setupTaskActivityInfo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String appName = getString(R.string.app_name);
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            int recentsColor = ContextCompat.getColor(this, R.color.primary);
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(appName, icon, recentsColor);
            setTaskDescription(taskDesc);
        }
    }
}