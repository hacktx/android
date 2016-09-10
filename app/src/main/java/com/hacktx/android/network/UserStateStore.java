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

package com.hacktx.android.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.hacktx.android.R;

public class UserStateStore {

    /**
     * Quickly get default <code>SharedPreferences</code> for a given <code>Context</code>.
     */
    public static SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Get the last time, in milliseconds, the schedule for a given <code>day</code> was modified.
     */
    public static Long getScheduleLastUpdated(Context context, int day) {
        return getPrefs(context).getLong(context.getString(R.string.prefs_schedule_last_updated) + day, 0);
    }

    /**
     * Set the last time, in milliseconds, the schedule for a given <code>day</code> was modified.
     */
    public static void setScheduleLastUpdated(Context context, int day, Long scheduleLastUpdated) {
        getPrefs(context).edit().putLong(context.getString(R.string.prefs_schedule_last_updated) + day, scheduleLastUpdated).apply();
    }

    /**
     * Get if the user has submitted feedback for an event for a given <code>id</code>.
     */
    public static Boolean getFeedbackSubmitted(Context context, int id) {
        return getPrefs(context).getBoolean(context.getString(R.string.prefs_feedback_submitted) + id, false);
    }

    /**
     * Set if the user has submitted feedback for an event for a given <code>id</code>.
     */
    public static void setFeedbackSubmitted(Context context, int id, boolean feedbackSubmitted) {
        getPrefs(context).edit().putBoolean(context.getString(R.string.prefs_feedback_submitted) + id, feedbackSubmitted).apply();
    }

    /**
     * Get if the user has elected to not send feedback for an event for a given <code>id</code>.
     */
    public static Boolean getFeedbackIgnored(Context context, int id) {
        return getPrefs(context).getBoolean(context.getString(R.string.prefs_feedback_ignore) + id, false);
    }

    /**
     * Set if the user has elected to not send feedback for an event for a given <code>id</code>.
     */
    public static void setFeedbackIgnored(Context context, int id, boolean ignored) {
        getPrefs(context).edit().putBoolean(context.getString(R.string.prefs_feedback_ignore) + id, ignored).apply();
    }

    /**
     * Get if announcement notifications are enabled by the user.
     */
    public static boolean getAnnouncementNotificationsEnabled(Context context) {
        return getPrefs(context).getBoolean(context.getString(R.string.prefs_notif_announcements_enabled), true);
    }

    /**
     * Set if announcement notifications are enabled by the user.
     */
    public static void setAnnouncementNotificationsEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(context.getString(R.string.prefs_notif_announcements_enabled), enabled).apply();
    }

    /**
     * Get the user's email address. Returns an empty string if not set.
     */
    public static String getUserEmail(Context context) {
        return getPrefs(context).getString(context.getString(R.string.prefs_user_email), "");
    }

    /**
     * Set the user's email address.
     */
    public static void setUserEmail(Context context, String email) {
        getPrefs(context).edit().putString(context.getString(R.string.prefs_user_email), email).apply();
    }

    /**
     * Get if the user's email address has been stored.
     */
    public static boolean isUserEmailSet(Context context) {
        return !getUserEmail(context).isEmpty();
    }

    /**
     * Set if the application has never run before.
     */
    public static void setFirstLaunch(Context context, boolean firstLaunch) {
        getPrefs(context).edit().putBoolean(context.getString(R.string.prefs_first_launch), firstLaunch).apply();
    }

    /**
     * Get if the application has never been run before.
     */
    public static boolean isFirstLaunch(Context context) {
        return getPrefs(context).getBoolean(context.getString(R.string.prefs_first_launch), true);
    }

    /**
     * Get if the mock server is enabled.
     */
    public static boolean getMockServerEnabled(Context context) {
        return getPrefs(context).getBoolean(context.getString(R.string.prefs_network_mock), false);
    }

    /**
     * Set if the mock server is enabled.
     */
    public static void setMockServerEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(context.getString(R.string.prefs_network_mock), enabled).apply();
    }

    /**
     * Get if Slack notification alert has been shown.
     */
    public static boolean getSlackAlertShown(Context context) {
        return getPrefs(context).getBoolean(context.getString(R.string.prefs_slack_alert_shown), false);
    }

    /**
     * Set if Slack notification alert has been shown.
     */
    public static void setSlackAlertShown(Context context, boolean shown) {
        getPrefs(context).edit().putBoolean(context.getString(R.string.prefs_slack_alert_shown), shown).apply();
    }
}
