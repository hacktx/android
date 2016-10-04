package com.hacktx.android.utils;

import com.hacktx.android.Constants;

public enum ConfigParam {

    BUNDLED_NOTIFICATIONS("bundled_notifications", Constants.FEATURE_BUNDLED_NOTIFICATIONS),

    CHECK_IN("check_in", Constants.FEATURE_CHECK_IN),

    EVENT_FEEDBACK("event_feedback", Constants.FEATURE_EVENT_FEEDBACK),

    REMOTE_MAP("remote_map", Constants.FEATURE_REMOTE_MAP);

    private String mKey;
    private boolean mDefaultValue;

    ConfigParam(String key, boolean defaultValue) {
        mKey = key;
        mDefaultValue = defaultValue;
    }

    public String getKey() {
        return mKey;
    }

    public boolean getDefaultValue() {
        return mDefaultValue;
    }
}
