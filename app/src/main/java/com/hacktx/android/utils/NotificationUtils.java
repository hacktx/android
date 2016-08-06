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

package com.hacktx.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.hacktx.android.R;

public class NotificationUtils {

    public static int getId(Context context, String group) {
        return getIdBase(context, group) + getIdOffset(context, group);
    }

    public static int getIdBase(Context context, String group) {
        if (group.contains(context.getString(R.string.notif_topic_hacktx))) {
            return 0;
        } else if (group.contains(context.getString(R.string.notif_topic_android))) {
            return 10;
        } else if (group.contains(context.getString(R.string.notif_topic_announcements))) {
            return 20;
        } else if (group.contains(context.getString(R.string.notif_topic_debug))) {
            return 30;
        } else {
            return 40;
        }
    }

    private static int getIdOffset(Context context, String group) {
        SharedPreferences prefs = getPrefs(context);
        String key = context.getString(R.string.prefs_notif_offset) + '-' + group;
        int offset = prefs.getInt(key, 1);
        prefs.edit().putInt(key, offset + 1 <= 5 ? offset + 1 : 1).apply();
        return offset;
    }

    private static SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
