package hacktx.hacktx2015.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import hacktx.hacktx2015.R;

/**
 * Created by Drew on 7/25/15.
 */
public class UserStateStore {
    public static SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static Long getScheduleLastUpdated(Context context, int day) {
        return getPrefs(context).getLong(context.getString(R.string.prefs_schedule_last_updated) + day, 0);
    }

    public static void setScheduleLastUpdated(Context context, int day, Long scheduleLastUpdated) {
        getPrefs(context).edit().putLong(context.getString(R.string.prefs_schedule_last_updated) + day, scheduleLastUpdated).apply();
    }
}
