package hacktx.hacktx2015.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import hacktx.hacktx2015.R;

public class UserStateStore {

    /**
     * Quickly get default <code>SharedPreferences</code> for a given <code>Context</code>.
     *
     * @param context Context by which to get default <code>SharedPreferences</code>
     * @return
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
}
