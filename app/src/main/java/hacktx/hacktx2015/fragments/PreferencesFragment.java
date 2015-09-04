package hacktx.hacktx2015.fragments;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import hacktx.hacktx2015.BuildConfig;
import hacktx.hacktx2015.R;
import hacktx.hacktx2015.activities.DebugActivity;
import hacktx.hacktx2015.services.BeaconService;

public class PreferencesFragment extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        final PreferenceScreen debug = (PreferenceScreen) findPreference(getString(R.string.prefs_debug));
        debug.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getActivity().startActivity(new Intent(getActivity(), DebugActivity.class));
                return false;
            }
        });

        if(!BuildConfig.IN_APP_DEBUG) {
            final PreferenceCategory debugCategory = (PreferenceCategory) findPreference(getString(R.string.fragment_preferences_debug_key));
            getPreferenceScreen().removePreference(debugCategory);
        }

        final CheckBoxPreference beaconPreference = (CheckBoxPreference) findPreference(getString(R.string.prefs_beacons_enabled));
        beaconPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (!(boolean) newValue) {
                    Log.i("PreferencesFragment", "Stopping BeaconService.");
                    getActivity().stopService(new Intent(getActivity(), BeaconService.class));
                } else {
                    if (doesDeviceSupportBle()) {
                        Log.i("PreferencesFragment", "Starting BeaconService.");
                        getActivity().startService(new Intent(getActivity(), BeaconService.class));
                    } else {
                        Snackbar.make(getView(), R.string.fragment_preferences_beacons_failed, Snackbar.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        final PreferenceScreen about = (PreferenceScreen) findPreference(getString(R.string.prefs_about));
        String version;
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            version = "???";
        }
        about.setSummary(getString(R.string.fragment_preferences_about_version, version));

        final PreferenceScreen licenses = (PreferenceScreen) findPreference(getString(R.string.prefs_licenses));
        licenses.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final Dialog licenseDialog = new Dialog(getActivity());
                licenseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                licenseDialog.setContentView(R.layout.dialog_licenses);
                WindowManager.LayoutParams licenseParams = licenseDialog.getWindow().getAttributes();
                licenseParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                licenseDialog.getWindow().setAttributes(licenseParams);
                licenseDialog.show();

                WebView licenseWebView = (WebView) licenseDialog.findViewById(R.id.licenseWebView);
                licenseWebView.loadUrl("file:///android_asset/open_source_licenses.html");
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
