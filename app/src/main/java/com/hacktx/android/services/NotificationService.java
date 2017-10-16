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

package com.hacktx.android.services;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hacktx.android.Constants;
import com.hacktx.android.R;
import com.hacktx.android.activities.MainActivity;
import com.hacktx.android.network.UserStateStore;
import com.hacktx.android.utils.ConfigManager;
import com.hacktx.android.utils.ConfigParam;
import com.hacktx.android.utils.NotificationUtils;

import java.util.Map;

public class NotificationService extends FirebaseMessagingService {

    private final String TAG = getClass().getSimpleName();
    private ConfigManager mConfigManager;

    @Override
    public void onCreate(){
        super.onCreate();
        mConfigManager = new ConfigManager();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        String group = remoteMessage.getFrom();

        if (UserStateStore.getAnnouncementNotificationsEnabled(this)) {
            if (mConfigManager.getValue(ConfigParam.BUNDLED_NOTIFICATIONS)) {
                sendBundledNotification(group, remoteMessage.getData());
            } else {
                sendNotification(group, remoteMessage.getData());
            }
        }
    }

    private void sendBundledNotification(String group, Map<String, String> data) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        int id = NotificationUtils.getId(this, group);
        String title = data.get("title") != null ? data.get("title") : getString(R.string.app_name);
        String text = data.get("text") != null ? data.get("text") : getString(R.string.notif_new_notifications);
        boolean vibrate = Boolean.parseBoolean(data.get("vibrate") != null ? data.get("vibrate") : "false");

        NotificationCompat.Builder summaryNotifBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_alert)
                .setGroup(group)
                .setGroupSummary(true)
                .setColor(ContextCompat.getColor(this, R.color.primary))
                .setContentTitle(getString(R.string.app_name))
                .setAutoCancel(true)
                .setChannelId(NotificationUtils.getNotificationChannel(this, group))
                .setContentIntent(pendingIntent);

        notificationManager.notify(NotificationUtils.getIdBase(this, group), summaryNotifBuilder.build());

        NotificationCompat.Builder notificationBuilder = getBaseNotificationBuilder(title, text, vibrate)
                .setContentIntent(pendingIntent)
                .setChannelId(NotificationUtils.getNotificationChannel(this, group))
                .setGroup(group);

        notificationManager.notify(id, notificationBuilder.build());
    }

    private void sendNotification(String group, Map<String, String> data) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        int id = NotificationUtils.getIdBase(this, group);
        String title = data.get("title") != null ? data.get("title") : getString(R.string.app_name);
        String text = data.get("text") != null ? data.get("text") : getString(R.string.notif_new_notifications);
        boolean vibrate = Boolean.parseBoolean(data.get("vibrate") != null ? data.get("vibrate") : "false");

        NotificationCompat.Builder notificationBuilder = getBaseNotificationBuilder(title, text, vibrate)
                .setChannelId(NotificationUtils.getNotificationChannel(this, group))
                .setContentIntent(pendingIntent);

        notificationManager.notify(id, notificationBuilder.build());
    }

    private NotificationCompat.Builder getBaseNotificationBuilder(String title, String text, boolean vibrate) {
        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_alert)
                .setColor(ContextCompat.getColor(this, R.color.primary))
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text))
                .setAutoCancel(true)
                .setVibrate(vibrate ? new long[] {0, 250} : new long[] {});
    }
}
