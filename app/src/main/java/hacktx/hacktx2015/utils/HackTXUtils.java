package hacktx.hacktx2015.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

import hacktx.hacktx2015.R;

public class HackTXUtils {

    public static boolean hasHackTxStarted(Context context) {
        if(!isOverrideEnabled(context)) {
            Calendar now = Calendar.getInstance();
            Calendar hackTx = Calendar.getInstance();
            hackTx.set(Calendar.MONTH, Calendar.SEPTEMBER);
            hackTx.set(Calendar.DAY_OF_MONTH, 24);

            return now.after(hackTx);
        } else {
            return getStartOverride(context);
        }
    }

    public static boolean hasHackTxEnded(Context context) {
        if(!isOverrideEnabled(context)) {
            Calendar now = Calendar.getInstance();
            Calendar hackTx = Calendar.getInstance();
            hackTx.set(Calendar.MONTH, Calendar.SEPTEMBER);
            hackTx.set(Calendar.DAY_OF_MONTH, 27);

            return now.after(hackTx);
        } else {
            return getEndOverride(context);
        }
    }

    private static SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static boolean isOverrideEnabled(Context context) {
        return getPrefs(context).getBoolean(context.getString(R.string.debug_hacktx_utils_override_key), false);
    }

    private static boolean getStartOverride(Context context) {
        return getPrefs(context).getBoolean(context.getString(R.string.debug_hacktx_utils_started_key), false);
    }

    private static boolean getEndOverride(Context context) {
        return getPrefs(context).getBoolean(context.getString(R.string.debug_hacktx_utils_ended_key), false);
    }
}
