package com.hacktx.android.receivers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.hacktx.android.network.UserStateStore;
import com.hacktx.android.services.BeaconService;

public class BeaconReceiver extends BroadcastReceiver {

    private Intent beaconServiceIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (doesDeviceSupportBle(context)) {
            if(UserStateStore.getBeaconsEnabled(context)) {
                    if (beaconServiceIntent == null) {
                        beaconServiceIntent = new Intent(context, BeaconService.class);
                        context.startService(beaconServiceIntent);
                    }
            } else {
                Log.i("BeaconReceiver", "User has disabled beacons, not starting BeaconService.");
            }
        } else {
            Log.i("BeaconReceiver", "Device does not support BLE, not starting BeaconService.");
        }
    }

    private boolean doesDeviceSupportBle(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 &&
                BluetoothAdapter.getDefaultAdapter() != null &&
                context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }
}