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

package com.hacktx.android.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

/**
 * Logs Activity lifecycle events.
 */
public class LifecycleListener implements Application.ActivityLifecycleCallbacks {

    private String TAG = getClass().getSimpleName();

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.v(TAG, "onCreate: " + getActivityIdentifier(activity));
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.v(TAG, "onStart: " + getActivityIdentifier(activity));
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.v(TAG, "onResume: " + getActivityIdentifier(activity));
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.v(TAG, "onPause: " + getActivityIdentifier(activity));
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.v(TAG, "onStop: " + getActivityIdentifier(activity));
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        Log.v(TAG, "onActivitySaveInstanceState: " + getActivityIdentifier(activity));
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.v(TAG, "onDestroy: " + getActivityIdentifier(activity));
    }

    private String getActivityIdentifier(Activity activity) {
        return activity.getPackageName() + "." + activity.getLocalClassName();
    }
}
