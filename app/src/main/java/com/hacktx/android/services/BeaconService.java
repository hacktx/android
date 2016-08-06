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

package com.hacktx.android.services;

import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.hacktx.android.beacons.HackTXBeaconManager;
import com.hacktx.android.network.UserStateStore;

public class BeaconService extends Service {

    private final String TAG = getClass().getSimpleName();

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(doesDeviceSupportBle()) {
            if (UserStateStore.getBeaconsEnabled(getApplicationContext())) {
                try {
                    HackTXBeaconManager.start((NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE),
                            this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.i(TAG, "User has disabled beacons, stopping BeaconService.");
                stopSelf();
            }
        } else {
            Log.i(TAG, "Device does not support BLE, stopping BeaconService.");
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
