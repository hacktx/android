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
import com.hacktx.android.network.HackTxClient;
import com.hacktx.android.network.UserStateStore;
import com.hacktx.android.utils.ConfigManager;
import com.hacktx.android.utils.ConfigParam;

public class DebugFragment extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences_debug);

        ConfigManager configManager = new ConfigManager(getActivity());

        final PreferenceScreen configCheckIn = (PreferenceScreen) findPreference(getString(R.string.debug_config_check_in_key));
        configCheckIn.setSummary(configManager.getValue(ConfigParam.CHECK_IN) ? R.string.debug_config_enabled : R.string.debug_config_disabled);

        final PreferenceScreen configFeedback = (PreferenceScreen) findPreference(getString(R.string.debug_config_feedback_key));
        configFeedback.setSummary(configManager.getValue(ConfigParam.EVENT_FEEDBACK) ? R.string.debug_config_enabled : R.string.debug_config_disabled);

        final PreferenceScreen configBundledNotif = (PreferenceScreen) findPreference(getString(R.string.debug_config_bundled_notif_key));
        configBundledNotif.setSummary(configManager.getValue(ConfigParam.BUNDLED_NOTIFICATIONS) ? R.string.debug_config_enabled : R.string.debug_config_disabled);

        final CheckBoxPreference mockServer = (CheckBoxPreference) findPreference(getString(R.string.prefs_network_mock));
        mockServer.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                UserStateStore.setMockServerEnabled(getActivity(), (boolean) o);
                HackTxClient.rebuildInstance();
                return true;
            }
        });

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
