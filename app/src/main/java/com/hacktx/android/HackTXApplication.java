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

import com.google.firebase.messaging.FirebaseMessaging;
import com.hacktx.android.utils.ConfigManager;
import com.hacktx.android.utils.LifecycleListener;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class HackTXApplication extends Application {

    private static HackTXApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        registerActivityLifecycleCallbacks(new LifecycleListener());

        if (Constants.FIREBASE_NOTIFICATIONS_ENABLED) {
            FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
            firebaseMessaging.subscribeToTopic(getString(R.string.notif_topic_hacktx));
            firebaseMessaging.subscribeToTopic(getString(R.string.notif_topic_android));
            firebaseMessaging.subscribeToTopic(getString(R.string.notif_topic_announcements));

            if (BuildConfig.DEBUG) {
                firebaseMessaging.subscribeToTopic(getString(R.string.notif_topic_debug));
            }
        }

        // Set Picasso's disk cache to 25 MB
        Picasso picasso =  new Picasso.Builder(this)
                .downloader(new OkHttpDownloader(getCacheDir(), 25000000))
                .build();
        Picasso.setSingletonInstance(picasso);
    }

    public static HackTXApplication getInstance() {
        return sInstance;
    }
}
