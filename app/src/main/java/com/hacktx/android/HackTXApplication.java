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

package com.hacktx.android;

import android.app.Application;
import android.content.Intent;

import com.estimote.sdk.EstimoteSDK;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hacktx.android.services.BeaconService;

public class HackTXApplication extends Application {
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        if (Constants.FEATURE_BEACONS) {
            initBeacons();
        }

        if (Constants.FIREBASE_NOTIFICATIONS_ENABLED) {
            FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
            firebaseMessaging.subscribeToTopic(getString(R.string.notif_topic_hacktx));
            firebaseMessaging.subscribeToTopic(getString(R.string.notif_topic_android));
            firebaseMessaging.subscribeToTopic(getString(R.string.notif_topic_announcements));

            if (BuildConfig.DEBUG) {
                firebaseMessaging.subscribeToTopic(getString(R.string.notif_topic_debug));
            }
        }
    }

    private void initBeacons() {
        EstimoteSDK.initialize(this, BuildConfig.ESTIMOTE_APP_ID, BuildConfig.ESTIMOTE_APP_TOKEN);
        EstimoteSDK.enableDebugLogging(true);

        Intent serviceIntent = new Intent(getApplicationContext(), BeaconService.class);
        startService(serviceIntent);
    }
}
