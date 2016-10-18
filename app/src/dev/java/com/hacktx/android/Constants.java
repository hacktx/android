package com.hacktx.android;

public class Constants {

    /* Debugging */
    public static final boolean DEBUG_MENU = true;

    /* Feature Flags */
    public static final boolean FEATURE_CHECK_IN = true;
    public static final boolean FEATURE_EVENT_FEEDBACK = false;
    public static final boolean FEATURE_BUNDLED_NOTIFICATIONS = false;
    public static final boolean FEATURE_REMOTE_MAP = true;

    /* Configuration */
    public static final String REMOTE_MAP_URL = "http://hacktx.com"; // TODO: Replace this

    /* Firebase */
    public static final boolean FIREBASE_ANALYTICS_ENABLED = false;
    public static final boolean FIREBASE_NOTIFICATIONS_ENABLED = true;
    public static final boolean FIREBASE_REMOTE_CONFIG_ENABLED = true;
    public static final boolean FIREBASE_REMOTE_CONFIG_DEV_MODE = true;

    /* Crashlytics */
    public static final boolean FABRIC_CRASHLYITCS_ENABLED = false;
    public static final boolean FABRIC_ANSWERS_ENABLED = false;
}
