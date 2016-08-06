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

    private final String TAG = getClass().getSimpleName();
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
                Log.i(TAG, "User has disabled beacons, not starting BeaconService.");
            }
        } else {
            Log.i(TAG, "Device does not support BLE, not starting BeaconService.");
        }
    }

    private boolean doesDeviceSupportBle(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 &&
                BluetoothAdapter.getDefaultAdapter() != null &&
                context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }
}