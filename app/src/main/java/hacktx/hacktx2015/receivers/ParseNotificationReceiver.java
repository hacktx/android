package hacktx.hacktx2015.receivers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import hacktx.hacktx2015.activities.MainActivity;

/**
 * Created by britne on 7/22/15.
 */
public class ParseNotificationReceiver extends ParsePushBroadcastReceiver {

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        Log.d("NotifReceiver", "notification opened!");
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("open", "announcements");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
