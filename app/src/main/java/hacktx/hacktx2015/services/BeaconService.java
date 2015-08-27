package hacktx.hacktx2015.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import hacktx.hacktx2015.beacons.HackTXBeaconManager;

public class BeaconService extends Service {
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            HackTXBeaconManager.start((NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE)
                    , this, intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HackTXBeaconManager.stop();
    }
}
