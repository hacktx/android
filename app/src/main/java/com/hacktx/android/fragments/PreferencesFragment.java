/*
 * Copyright 2017 HackTX.
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

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.hacktx.android.BuildConfig;
import com.hacktx.android.Constants;
import com.hacktx.android.R;
import com.hacktx.android.activities.DebugActivity;
import com.hacktx.android.utils.MetricsManager;

public class PreferencesFragment extends PreferenceFragment {

    private MetricsManager mMetricsManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMetricsManager = new MetricsManager(getActivity());

        addPreferencesFromResource(R.xml.preferences);

        final PreferenceScreen debug = (PreferenceScreen) findPreference(getString(R.string.prefs_debug));
        debug.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getActivity().startActivity(new Intent(getActivity(), DebugActivity.class));
                return false;
            }
        });

        if (!Constants.DEBUG_MENU) {
            final PreferenceCategory debugCategory = (PreferenceCategory) findPreference(getString(R.string.fragment_preferences_debug_key));
            getPreferenceScreen().removePreference(debugCategory);
        }

        final CheckBoxPreference notifAnnouncementPref = (CheckBoxPreference) findPreference(getString(R.string.prefs_notif_announcements_enabled));
        notifAnnouncementPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mMetricsManager.logEvent(R.string.analytics_event_toggle_notifications, null);
                Log.i("PreferencesFragment", "Announcement notifications "
                        + ((boolean) newValue ? "enabled" : "disabled") + ".");

                return true;
            }
        });

        final PreferenceScreen notifShortcut = (PreferenceScreen) findPreference(getString(R.string.prefs_notif_shortcut));
        notifShortcut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            @TargetApi(26)
            public boolean onPreferenceClick(Preference preference) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent i = new Intent("android.settings.APP_NOTIFICATION_SETTINGS");
                    i.putExtra(Settings.EXTRA_APP_PACKAGE, BuildConfig.APPLICATION_ID);
                    startActivity(i);
                } else {
                    Toast.makeText(getContext(), getText(R.string.fragment_preferences_notif_shortcut_error), Toast.LENGTH_SHORT).show();
                    Log.e("PreferencesFragment", "Wrong API level when attempting to open notification settings.");
                }

                return false;
            }
        });

        // On API 26 and above, use system's notification settings... otherwise use in-app toggle
        PreferenceCategory notifCategory = ((PreferenceCategory) findPreference("notifications"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notifAnnouncementPref.setChecked(true);
            notifCategory.removePreference(notifAnnouncementPref);
        } else {
            notifCategory.removePreference(notifShortcut);
        }

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
                mMetricsManager.logEvent(R.string.analytics_event_licenses, null);
                final Dialog licenseDialog = new Dialog(getActivity());
                licenseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                licenseDialog.setContentView(R.layout.dialog_licenses);
                WindowManager.LayoutParams licenseParams = licenseDialog.getWindow().getAttributes();
                licenseParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                licenseDialog.getWindow().setAttributes(licenseParams);
                licenseDialog.show();

                WebView licenseWebView = (WebView) licenseDialog.findViewById(R.id.licenseWebView);
                licenseWebView.setWebViewClient(new WebViewClient() {
                    public void onPageFinished(WebView view, String url) {
                        licenseDialog.findViewById(R.id.dialog_spinner).setVisibility(View.GONE);
                    }
                });
                licenseWebView.loadUrl("file:///android_asset/open_source_licenses.html");
                return true;
            }
        });
    }
}
