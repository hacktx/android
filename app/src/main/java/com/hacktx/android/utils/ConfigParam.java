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
