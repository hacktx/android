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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.hacktx.android.R;

import java.util.Calendar;

public class HackTXUtils {

    public static boolean hasHackTxStarted(Context context) {
        if(!isOverrideEnabled(context)) {
            Calendar now = Calendar.getInstance();
            Calendar hackTx = Calendar.getInstance();
            hackTx.set(Calendar.MONTH, Calendar.OCTOBER);
            hackTx.set(Calendar.DAY_OF_MONTH, 28);
            hackTx.set(Calendar.YEAR, 2017);

            return now.after(hackTx);
        } else {
            return getStartOverride(context);
        }
    }

    public static boolean hasHackTxEnded(Context context) {
        if(!isOverrideEnabled(context)) {
            Calendar now = Calendar.getInstance();
            Calendar hackTx = Calendar.getInstance();
            hackTx.set(Calendar.MONTH, Calendar.OCTOBER);
            hackTx.set(Calendar.DAY_OF_MONTH, 30);
            hackTx.set(Calendar.YEAR, 2017);

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
