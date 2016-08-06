package com.hacktx.android.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.hacktx.android.R;

public class UserStateStore {

    /**
     * Quickly get default <code>SharedPreferences</code> for a given <code>Context</code>.
     *
     * @param context Context by which to get default <code>SharedPreferences</code>
     * @return SharedPreferences instance
     */
    public static SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Get the last time, in milliseconds, the schedule for a given <code>day</code> was modified.
     *
     * @param context Context by which to retrieve data
     * @param day Day of schedule
     * @return <code>Long</code> representing last time schedule was updated in milliseconds
     */
    public static Long getScheduleLastUpdated(Context context, int day) {
        return getPrefs(context).getLong(context.getString(R.string.prefs_schedule_last_updated) + day, 0);
    }

    /**
     * Set the last time, in milliseconds, the schedule for a given <code>day</code> was modified.
     *
     * @param context Context by which to save data
     * @param day Day of schedule
     * @param scheduleLastUpdated <code>Long</code> representing last time schedule was updated in milliseconds
     */
    public static void setScheduleLastUpdated(Context context, int day, Long scheduleLastUpdated) {
        getPrefs(context).edit().putLong(context.getString(R.string.prefs_schedule_last_updated) + day, scheduleLastUpdated).apply();
    }

    /**
     * Get if the user has submitted feedback for an event for a given <code>id</code>.
     *
     * @param context Context by which to retrieve data
     * @param id Event id
     * @return <code>boolean</code> representing if the user has submitted feedback for the respective event
     */
    public static Boolean getFeedbackSubmitted(Context context, int id) {
        return getPrefs(context).getBoolean(context.getString(R.string.prefs_feedback_submitted) + id, false);
    }

    /**
     * Set if the user has submitted feedback for an event for a given <code>id</code>.
     *
     * @param context Context by which to save data
     * @param id Event id
     * @param feedbackSubmitted <code>boolean</code> representing if feedback was submitted for the respective event
     */
    public static void setFeedbackSubmitted(Context context, int id, boolean feedbackSubmitted) {
        getPrefs(context).edit().putBoolean(context.getString(R.string.prefs_feedback_submitted) + id, feedbackSubmitted).apply();
    }

    /**
     * Get if the user has elected to not send feedback for an event for a given <code>id</code>.
     *
     * @param context Context by which to retrieve data
     * @param id Event id
     * @return <code>boolean</code> representing if the user has elected to not send feedback for the respective event
     */
    public static Boolean getFeedbackIgnored(Context context, int id) {
        return getPrefs(context).getBoolean(context.getString(R.string.prefs_feedback_ignore) + id, false);
    }

    /**
     * Set if the user has elected to not send feedback for an event for a given <code>id</code>.
     *
     * @param context Context by which to save data
     * @param id Event id
     * @param ignored <code>boolean</code> representing if the user has elected to not send feedback for the respective event
     */
    public static void setFeedbackIgnored(Context context, int id, boolean ignored) {
        getPrefs(context).edit().putBoolean(context.getString(R.string.prefs_feedback_ignore) + id, ignored).apply();
    }

    /**
     * Get if announcement notifications are enabled by the user.
     *
     * @param context Context by which to retrieve data
     * @return <code>boolean</code> representing if announcement notifications are enabled
     */
    public static boolean getAnnouncementNotificationsEnabled(Context context) {
        return getPrefs(context).getBoolean(context.getString(R.string.prefs_notif_announcements_enabled), true);
    }

    /**
     * Get if beacons are enabled by the user.
     *
     * @param context Context by which to retrieve data
     * @return <code>boolean</code> representing if beacons are enabled
     */
    public static boolean getBeaconsEnabled(Context context) {
        return getPrefs(context).getBoolean(context.getString(R.string.prefs_beacons_enabled), true);
    }

    /**
     * Get the user's email address. Returns an empty string if not set.
     *
     * @param context Context by which to retrieve data
     * @return <code>String</code> representing the user's email address
     */
    public static String getUserEmail(Context context) {
        return getPrefs(context).getString(context.getString(R.string.prefs_user_email), "");
    }

    /**
     * Set the user's email address.
     *
     * @param context Context by which to save data
     * @param email User email address to store
     */
    public static void setUserEmail(Context context, String email) {
        getPrefs(context).edit().putString(context.getString(R.string.prefs_user_email), email).apply();
    }

    /**
     * Get if the user's email address has been stored.
     *
     * @param context Context by which to retrieve data
     * @return <code>boolean</code> representing if the user's email address is stored
     */
    public static boolean isUserEmailSet(Context context) {
        return !getUserEmail(context).isEmpty();
    }

    /**
     * Set if the application has never run before.
     *
     * @param context Context by which to retrieve data
     * @param firstLaunch If the app has never run before
     */
    public static void setFirstLaunch(Context context, boolean firstLaunch) {
        getPrefs(context).edit().putBoolean(context.getString(R.string.prefs_first_launch), firstLaunch).apply();
    }

    /**
     * Get if the application has never been run before.
     *
     * @param context Context by which to retrieve data
     * @return <code>boolean</code> representing if the app has never been run before
     */
    public static boolean isFirstLaunch(Context context) {
        return getPrefs(context).getBoolean(context.getString(R.string.prefs_first_launch), true);
    }

    /**
     * Set if Bluetooth beacon notifications are enabled.
     *
     * @param context Context by which to retrieve data
     * @param enabled If Bluetooth beacon notifications are enabled
     */
    public static void setBeaconNotifEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(context.getString(R.string.prefs_beacon_notif), enabled).apply();
    }

    /**
     * Get if Bluetooth beacon notifications are enabled.
     *
     * @param context Context by which to retrieve data
     * @return <code>boolean</code> representing if Bluetooth beacon notifications are enabled
     */
    public static boolean getBeaconNotifEnabled(Context context) {
        return getPrefs(context).getBoolean(context.getString(R.string.prefs_beacon_notif), true);
    }
}
