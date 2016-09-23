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

package com.hacktx.android.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hacktx.android.R;
import com.hacktx.android.fragments.AnnouncementFragment;
import com.hacktx.android.fragments.GoogleMapFragment;
import com.hacktx.android.fragments.OldMapFragment;
import com.hacktx.android.fragments.ScheduleMainFragment;
import com.hacktx.android.fragments.SponsorFragment;
import com.hacktx.android.fragments.TwitterFragment;
import com.hacktx.android.network.UserStateStore;
import com.hacktx.android.utils.ConfigParam;

import java.util.List;

public class MainActivity extends BaseActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDrawerContent((DrawerLayout) findViewById(R.id.drawer_layout), (NavigationView) findViewById(R.id.nav_view));
        setupFragmentContent(savedInstanceState);

        String extra = getIntent().getStringExtra("open");
        if (extra != null && extra.equals("announcements")) {
            ((NavigationView) findViewById(R.id.nav_view)).getMenu().getItem(1).setChecked(true);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_fragment, new AnnouncementFragment());
            transaction.commit();
        } else if(extra != null && extra.equals("maps")) {
            ((NavigationView) findViewById(R.id.nav_view)).getMenu().getItem(3).setChecked(true);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_fragment, new OldMapFragment());
            transaction.commit();
        }

        displayWelcome();
        displaySlackAlert();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ((TextView) navView.getHeaderView(0).findViewById(R.id.navHeaderEmail)).setText(UserStateStore.getUserEmail(this));
        if (!mConfigManager.getValue(ConfigParam.CHECK_IN)) {
            navView.getMenu().getItem(5).setEnabled(false);
            navView.getMenu().getItem(5).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setupDrawerContent(final DrawerLayout drawerLayout, NavigationView navigationView) {
        this.drawerLayout = drawerLayout;
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.navHeaderEmail)).setText(UserStateStore.getUserEmail(this));

        if (!mConfigManager.getValue(ConfigParam.CHECK_IN)) {
            navigationView.getMenu().getItem(5).setEnabled(false);
            navigationView.getMenu().getItem(5).setVisible(false);
        }

        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(final MenuItem menuItem) {
                        menuItem.setChecked(true);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        switch (menuItem.getItemId()) {
                            case R.id.nav_schedule:
                                transaction.replace(R.id.content_fragment, new ScheduleMainFragment());
                                transaction.commit();
                                break;
                            case R.id.nav_announcement:
                                transaction.replace(R.id.content_fragment, new AnnouncementFragment());
                                transaction.commit();
                                break;
                            case R.id.nav_twitter:
                                transaction.replace(R.id.content_fragment, new TwitterFragment());
                                transaction.commit();
                                break;
                            case R.id.nav_map:
                                transaction.replace(R.id.content_fragment, new GoogleMapFragment());
                                transaction.commit();
                                break;
                            case R.id.nav_sponsors:
                                transaction.replace(R.id.content_fragment, new SponsorFragment());
                                transaction.commit();
                                break;
                            case R.id.nav_check_in:
                                startActivity(new Intent(MainActivity.this, CheckInActivity.class));
                                break;
                            case R.id.nav_settings:
                                startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
                                break;
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    protected void setupFragmentContent(Bundle savedInstanceState) {
        Log.v("main", "before");
        if (savedInstanceState == null) {
            Log.v("main", "start");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_fragment, new ScheduleMainFragment())
                    .commit();
        }
    }

    private void displayWelcome() {
        if(UserStateStore.isFirstLaunch(this)) {
            Log.i(TAG, "Starting WelcomeActivity...");
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void displaySlackAlert() {
        if (isSlackInstalled() && !UserStateStore.getSlackAlertShown(this)) {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_slack, null);
            dialogBuilder.setView(dialogView);

            final AlertDialog dialog = dialogBuilder.create();
            dialog.show();

            dialogView.findViewById(R.id.btn_slack_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserStateStore.setAnnouncementNotificationsEnabled(MainActivity.this, false);
                    dialog.dismiss();
                }
            });

            dialogView.findViewById(R.id.btn_slack_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            UserStateStore.setSlackAlertShown(MainActivity.this, true);
        }
    }

    private boolean isSlackInstalled() {
        final PackageManager packageManager = getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage("com.Slack");

        if (intent == null) {
            return false;
        }

        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}
