package com.hacktx.android;

import android.app.Application;
import android.content.Intent;

import com.estimote.sdk.EstimoteSDK;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.hacktx.android.services.BeaconService;

public class HackTXApplication extends Application {
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        if (Constants.FEATURE_BEACONS) {
            initBeacons();
        }
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(BuildConfig.GOOG_ANALYTICS_ID);

            // Provide unhandled exceptions reports. Do that first after creating the tracker
            mTracker.enableExceptionReporting(true);

            // Enable automatic activity tracking for your app
            mTracker.enableAutoActivityTracking(true);
        }
        return mTracker;
    }

    private void initBeacons() {
        EstimoteSDK.initialize(this, BuildConfig.ESTIMOTE_APP_ID, BuildConfig.ESTIMOTE_APP_TOKEN);
        EstimoteSDK.enableDebugLogging(true);

        Intent serviceIntent = new Intent(getApplicationContext(), BeaconService.class);
        startService(serviceIntent);
    }
}
