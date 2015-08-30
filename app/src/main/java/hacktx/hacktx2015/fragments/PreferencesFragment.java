package hacktx.hacktx2015.fragments;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.util.Log;

import hacktx.hacktx2015.R;
import hacktx.hacktx2015.services.BeaconService;

public class PreferencesFragment extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        final CheckBoxPreference beaconPreference = (CheckBoxPreference) findPreference("beaconsEnabled");
        beaconPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(!(boolean) newValue) {
                    Log.i("PreferencesFragment", "Stopping BeaconService.");
                    getActivity().stopService(new Intent(getActivity(), BeaconService.class));
                } else {
                    if(doesDeviceSupportBle()) {
                        Log.i("PreferencesFragment", "Starting BeaconService.");
                        getActivity().startService(new Intent(getActivity(), BeaconService.class));
                        Snackbar.make(getView(), R.string.fragment_settings_beacons_failed, Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(getView(), R.string.fragment_settings_beacons_failed, Snackbar.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });
    }

    private boolean doesDeviceSupportBle() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 &&
                BluetoothAdapter.getDefaultAdapter() != null &&
                getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }
}
