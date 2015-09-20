package hacktx.hacktx2015.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import hacktx.hacktx2015.R;
import hacktx.hacktx2015.activities.MainActivity;

/**
 * Created by britne on 7/22/15.
 */
public class ParseNotificationReceiver extends ParsePushBroadcastReceiver {

    public static final String PARSE_DATA_KEY = "com.parse.Data";

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        JSONObject data = getDataFromIntent(intent);
        String msg = "";
        try {
            msg = data.getString("alert");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_alert)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(msg).setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, R.color.accent))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL).build();

        notificationManager.notify(0, notification);

    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        Log.d("NotifReceiver", "notification opened!");
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("open", "announcements");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    private JSONObject getDataFromIntent(Intent intent) {
        JSONObject data = null;
        try {
            data = new JSONObject(intent.getExtras().getString(PARSE_DATA_KEY));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
