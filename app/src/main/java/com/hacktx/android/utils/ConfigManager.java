/*
 * Originally from Zephyr <https://goo.gl/bggO4r>. Modified for HackTX.
 *
 * Copyright 2016 Thomas Gaubert.
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

package com.hacktx.android.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.hacktx.android.Constants;
import com.hacktx.android.R;

public class ConfigManager {

    private final String TAG = this.getClass().getSimpleName();
    private final int CACHE_EXPIRATION_IN_SECONDS = 900;

    private Context mContext;
    private FirebaseRemoteConfig mRemoteConfig;

    public ConfigManager(Context context) {
        mContext = context;

        if (Constants.FIREBASE_REMOTE_CONFIG_ENABLED) {
            FirebaseRemoteConfigSettings configSettings =
                    new FirebaseRemoteConfigSettings.Builder()
                            .setDeveloperModeEnabled(Constants.FIREBASE_REMOTE_CONFIG_DEV_MODE)
                            .build();

            mRemoteConfig = FirebaseRemoteConfig.getInstance();
            mRemoteConfig.setConfigSettings(configSettings);
            mRemoteConfig.setDefaults(R.xml.remote_config_defaults);

            Log.i(TAG, "Firebase Remote Config enabled (dev "
                    + (configSettings.isDeveloperModeEnabled() ? "enabled)" : "disabled)"));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRemoteConfig.fetch(CACHE_EXPIRATION_IN_SECONDS)
                            .addOnSuccessListener(
                                    new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            boolean result = mRemoteConfig.activateFetched();
                                            Log.i(TAG, "Remote config data fetched (" + result + ")");
                                        }
                                    }
                            )
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "Failed to retrieve remote config: " + e.getMessage());
                                        }
                                    }
                            );
                }
            }, 0);
        } else {
            Log.i(TAG, "Firebase Remote Config disabled");
        }
    }

    public boolean getValue(ConfigParam configParam) {
        return getBoolean(configParam.getKey(), configParam.getDefaultValue());
    }

    public boolean needLocationPerms() {
        return getValue(ConfigParam.BEACONS);
    }

    public boolean grantedLocationPerms() {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean getBoolean(String key, boolean fallback) {
        boolean result;
        if (Constants.FIREBASE_REMOTE_CONFIG_ENABLED) {
            result = mRemoteConfig.getBoolean(key);
        } else {
            result = fallback;
        }

        Log.i(TAG, key + " --> " + result + " (" + fallback + ")");
        return result;
    }
}
