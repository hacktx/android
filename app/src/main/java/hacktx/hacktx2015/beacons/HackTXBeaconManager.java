package hacktx.hacktx2015.beacons;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.concurrent.TimeUnit;

import hacktx.hacktx2015.BuildConfig;
import hacktx.hacktx2015.R;
import hacktx.hacktx2015.activities.CheckInActivity;
import hacktx.hacktx2015.network.UserStateStore;
import hacktx.hacktx2015.utils.HackTXUtils;

public class HackTXBeaconManager {

    private static final int NOTIFICATION_ID = 22;
    private static BeaconManager beaconManager;
    private static NotificationManager notificationManager;
    private static final String HACKTX_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region HACKTX_BEACONS = new Region("hacktx", HACKTX_PROXIMITY_UUID, null, null);
    private static Context currentContext;

    public static void start(NotificationManager notificationMngr, Context context) {
        try {
            notificationManager = notificationMngr;
            currentContext = context;

            beaconManager = new BeaconManager(currentContext);
            beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(5), TimeUnit.SECONDS.toMillis(25));

            beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
                @Override
                public void onEnteredRegion(Region region, List<Beacon> beacons) {
                    if(HackTXUtils.hasHackTxStarted(currentContext)
                            && UserStateStore.getBeaconNotifEnabled(currentContext)
                            && BuildConfig.IN_APP_CHECK_IN) {
                        postNotificationIntent(currentContext.getString(R.string.notif_sign_in_title),
                                currentContext.getString(R.string.notif_sign_in_text));
                    }
                }

                @Override
                public void onExitedRegion(Region region) {
                    notificationManager.cancel(NOTIFICATION_ID);
                }
            });

            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    try {
                        beaconManager.startMonitoring(HACKTX_BEACONS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void postNotificationIntent(String title, String msg) {
        Intent intent = new Intent(currentContext, CheckInActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(currentContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new NotificationCompat.Builder(currentContext)
                .setSmallIcon(R.drawable.ic_beacon_alert).setContentTitle(title)
                .setContentText(msg).setAutoCancel(true)
                .setColor(ContextCompat.getColor(currentContext, R.color.accent))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent).build();

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public static void stop() {
        try {
            beaconManager.stopMonitoring(HACKTX_BEACONS);
            beaconManager.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

