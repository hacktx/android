package com.hacktx.android.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

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
