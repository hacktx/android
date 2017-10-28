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
    public static final String[] EVENT_DAYS = {"Oct 28", "Oct 29"};
    public static final String REMOTE_MAP_URL = "https://hacktx.com/map";
    public static final String SLACK_PACKAGE = "com.Slack";

    /* Firebase */
    public static final boolean FIREBASE_ANALYTICS_ENABLED = false;
    public static final boolean FIREBASE_NOTIFICATIONS_ENABLED = true;
    public static final boolean FIREBASE_REMOTE_CONFIG_ENABLED = true;
    public static final boolean FIREBASE_REMOTE_CONFIG_DEV_MODE = true;

    /* Crashlytics */
    public static final boolean FABRIC_CRASHLYITCS_ENABLED = false;
    public static final boolean FABRIC_ANSWERS_ENABLED = false;
}
