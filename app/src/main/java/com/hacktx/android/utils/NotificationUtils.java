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

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.hacktx.android.BuildConfig;
import com.hacktx.android.Constants;
import com.hacktx.android.R;

public class NotificationUtils {

    /**
     * Returns notification ID given group provided as part of the notification payload
     * and age of notification.
     */
    public static int getId(Context context, String group) {
        return getIdBase(context, group) + getIdOffset(context, group);
    }

    /**
     * Returns base notification ID given group provided as part of the notification payload.
     */
    public static int getIdBase(Context context, String group) {
        if (context == null || group == null) {
            return 40;
        }

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

    /**
     * Returns offset notification ID given "age" of notification. The offset ensures five
     * notifications are shown, with the oldest notification eventually being replaced.
     */
    private static int getIdOffset(Context context, String group) {
        if (context == null || group == null) {
            return 0;
        }

        SharedPreferences prefs = getPrefs(context);
        String key = context.getString(R.string.prefs_notif_offset) + '-' + group;
        int offset = prefs.getInt(key, 1);
        prefs.edit().putInt(key, offset + 1 <= 5 ? offset + 1 : 1).apply();
        return offset;
    }

    /**
     * Returns Android notification channel for notification given group as part of notification
     * payload. Used on Android 8.0 Oreo and above.
     */
    public static String getNotificationChannel(Context context, String group) {
        if (context == null || group == null) {
            return null;
        }

        if (group.contains(context.getString(R.string.notif_topic_debug))) {
            return context.getString(R.string.notif_ch_debug_id);
        } else {
            return context.getString(R.string.notif_ch_announcement_id);
        }
    }

    /**
     * Returns if debug notification should be shown.
     */
    public static boolean shouldShowDebugNotification(Context context, String channel) {
        return channel.equals(context.getString(R.string.notif_ch_debug_id)) && BuildConfig.DEBUG;
    }

    /**
     * Creates Android notification channels. Used on Android 8.0 Oreo and above.
     */
    @TargetApi(26)
    public static void setupNotificationChannels(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        {
            // Announcements
            NotificationChannel mChannel = new NotificationChannel(context.getString(R.string.notif_ch_announcement_id),
                    context.getString(R.string.notif_ch_announcement_title), NotificationManager.IMPORTANCE_DEFAULT);

            int intColor = ContextCompat.getColor(context, R.color.primary);
            String hexColor = String.format("#%06X", (0xFFFFFF & intColor));

            mChannel.enableLights(true);
            mChannel.setLightColor(Color.parseColor(hexColor));
            mChannel.enableVibration(true);
            mChannel.setShowBadge(true);
            mChannel.setDescription(context.getString(R.string.notif_ch_announcement_text));

            mNotificationManager.createNotificationChannel(mChannel);
        }

        if (BuildConfig.DEBUG || Constants.DEBUG_MENU) {
            // Debug
            NotificationChannel mChannel = new NotificationChannel(context.getString(R.string.notif_ch_debug_id),
                    context.getString(R.string.notif_ch_debug_title), NotificationManager.IMPORTANCE_DEFAULT);

            mChannel.setDescription(context.getString(R.string.notif_ch_debug_text));

            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    /**
     * Returns default `SharedPreferences`.
     */
    private static SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
