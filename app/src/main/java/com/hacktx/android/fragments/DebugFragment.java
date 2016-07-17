package com.hacktx.android.fragments;

import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.hacktx.android.BuildConfig;
import com.hacktx.android.Constants;
import com.hacktx.android.R;

public class DebugFragment extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences_debug);

        final PreferenceScreen buildConfigCheckIn = (PreferenceScreen) findPreference(getString(R.string.debug_build_config_check_in_key));
        buildConfigCheckIn.setSummary(Constants.FEATURE_CHECK_IN ? R.string.debug_build_config_enabled : R.string.debug_build_config_disabled);

        final PreferenceScreen buildConfigFeedback = (PreferenceScreen) findPreference(getString(R.string.debug_build_config_feedback_key));
        buildConfigFeedback.setSummary(Constants.FEATURE_EVENT_FEEDBACK ? R.string.debug_build_config_enabled : R.string.debug_build_config_disabled);

        final PreferenceScreen bleStatus = (PreferenceScreen) findPreference(getString(R.string.debug_beacon_status_key));
        bleStatus.setSummary(doesDeviceSupportBle() ? R.string.debug_ble_support_true : R.string.debug_ble_support_false);

        final CheckBoxPreference hacktxUtilsOverride = (CheckBoxPreference) findPreference(getString(R.string.debug_hacktx_utils_override_key));
        setHackTXUtilOverridesEnabled(hacktxUtilsOverride.isChecked());
        hacktxUtilsOverride.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                setHackTXUtilOverridesEnabled((boolean) newValue);
                return true;
            }
        });
    }

    private void setHackTXUtilOverridesEnabled(boolean enabled) {
        CheckBoxPreference hacktxUtilsStarted = (CheckBoxPreference) findPreference(getString(R.string.debug_hacktx_utils_started_key));
        CheckBoxPreference hacktxUtilsEnded = (CheckBoxPreference) findPreference(getString(R.string.debug_hacktx_utils_ended_key));

        hacktxUtilsStarted.setEnabled(enabled);
        hacktxUtilsEnded.setEnabled(enabled);
    }

    private boolean doesDeviceSupportBle() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 &&
                BluetoothAdapter.getDefaultAdapter() != null &&
                getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }
}
