package hacktx.hacktx2015.services;

import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import hacktx.hacktx2015.beacons.HackTXBeaconManager;
import hacktx.hacktx2015.network.UserStateStore;

public class BeaconService extends Service {
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(doesDeviceSupportBle() && UserStateStore.getBeaconsEnabled(getApplicationContext())) {
            try {
                HackTXBeaconManager.start((NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE),
                        this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.i("BeaconReceiver", "User has disabled beacons, stopping BeaconService.");
            stopSelf();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (doesDeviceSupportBle() && UserStateStore.getBeaconsEnabled(getApplicationContext())) {
            HackTXBeaconManager.stop();
        }
    }

    private boolean doesDeviceSupportBle() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 &&
                BluetoothAdapter.getDefaultAdapter() != null &&
                getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }
}
