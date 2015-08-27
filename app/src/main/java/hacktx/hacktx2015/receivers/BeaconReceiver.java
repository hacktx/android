package hacktx.hacktx2015.receivers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import hacktx.hacktx2015.services.BeaconService;

public class BeaconReceiver extends BroadcastReceiver {

    private Intent beaconServiceIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR);
            switch (state) {
                case BluetoothAdapter.STATE_TURNING_OFF:
                    if (beaconServiceIntent != null) {
                        context.stopService(beaconServiceIntent);
                        beaconServiceIntent = null;
                    }
                    break;
                case BluetoothAdapter.STATE_ON:
                    if (beaconServiceIntent == null) {
                        beaconServiceIntent = new Intent(context,
                                BeaconService.class);
                        context.startService(beaconServiceIntent);
                    }
                    break;
            }
        }
    }
}