/**
 * Copyright (C) 2016 Thomas Gaubert.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hacktx.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.LoginEvent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hacktx.android.Constants;
import com.hacktx.android.R;

import java.util.UUID;

import io.fabric.sdk.android.Fabric;

public class MetricsManager {

    private final String TAG = this.getClass().getSimpleName();

    private FirebaseAnalytics mFirebaseAnalytics;
    private Context mContext;

    public MetricsManager(Context context) {
        mContext = context;

        String uuid = getUuid();

        if (Constants.FIREBASE_ANALYTICS_ENABLED) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
            mFirebaseAnalytics.setUserId(uuid);
        }


        if (Constants.FABRIC_CRASHLYITCS_ENABLED) {
            if (!Fabric.isInitialized()) {
                Fabric.with(context, new Crashlytics());
            }
            Crashlytics.setUserIdentifier(uuid);
        }
    }

    public void logEvent(@StringRes int iri, Bundle extras) {
        Log.i(TAG, mContext.getString(iri) + ": " + (extras != null ? extras.toString() : "null"));
        firebaseEvent(iri, extras);
        fabricEvent(iri, extras);
    }

    public void logLogin(String method, boolean success) {
        Log.i(TAG, "login: " + method + " " + success);

        if (Constants.FIREBASE_ANALYTICS_ENABLED) {
            Bundle b = new Bundle();
            b.putString(mContext.getString(R.string.analytics_param_login_method), method);
            firebaseEvent(R.string.analytics_event_login, b);
        }

        if (!Constants.FABRIC_ANSWERS_ENABLED) {
            if (!Fabric.isInitialized()) {
                Fabric.with(mContext, new Crashlytics());
            }

            Answers.getInstance().logLogin(new LoginEvent().putMethod(method).putSuccess(success));
        }
    }

    private void firebaseEvent(@StringRes int iri, Bundle extras) {
        if (Constants.FIREBASE_ANALYTICS_ENABLED) {
            mFirebaseAnalytics.logEvent(mContext.getString(iri), extras);
        }
    }

    private void fabricEvent(@StringRes int iri, Bundle extras) {
        if (!Constants.FABRIC_ANSWERS_ENABLED) {
            return;
        }

        if (!Fabric.isInitialized()) {
            Fabric.with(mContext, new Crashlytics());
        }

        CustomEvent event = new CustomEvent(mContext.getString(iri));
        if(extras != null) {
            for (String key : extras.keySet()) {
                event.putCustomAttribute(key, extras.get(key).toString());
            }
        }

        Answers.getInstance().logCustom(event);
    }

    private String getUuid() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String uuid = preferences.getString(mContext.getString(R.string.prefs_uuid), "");

        if (uuid.isEmpty()) {
            uuid = UUID.randomUUID().toString();
            preferences.edit().putString(mContext.getString(R.string.prefs_uuid), uuid).apply();
        }

        return uuid;
    }
}