package hacktx.hacktx2015.activities;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import hacktx.hacktx2015.BuildConfig;
import hacktx.hacktx2015.R;
import hacktx.hacktx2015.fragments.AnnouncementFragment;
import hacktx.hacktx2015.fragments.MapFragment;
import hacktx.hacktx2015.fragments.ScheduleMainFragment;
import hacktx.hacktx2015.fragments.SponsorFragment;
import hacktx.hacktx2015.fragments.TwitterFragment;
import hacktx.hacktx2015.network.UserStateStore;
import hacktx.hacktx2015.utils.HackTXUtils;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupTaskActivityInfo();
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
            transaction.replace(R.id.content_fragment, new MapFragment());
            transaction.commit();
        }

        displayWelcome();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((TextView) drawerLayout.findViewById(R.id.navHeaderEmail)).setText(UserStateStore.getUserEmail(this));
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

    protected void setupTaskActivityInfo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String appName = getString(R.string.app_name);
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            int color = ContextCompat.getColor(this, R.color.taskbarColor);
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(appName, icon, color);
            setTaskDescription(taskDesc);
        }
    }

    protected void setupDrawerContent(final DrawerLayout drawerLayout, NavigationView navigationView) {
        this.drawerLayout = drawerLayout;
        ((TextView) drawerLayout.findViewById(R.id.navHeaderEmail)).setText(UserStateStore.getUserEmail(this));

        if(!BuildConfig.IN_APP_CHECK_IN) {
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
                                transaction.replace(R.id.content_fragment, new MapFragment());
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
        if(UserStateStore.isFirstLaunch(this) && !UserStateStore.isUserEmailSet(this) && BuildConfig.IN_APP_CHECK_IN) {
            if(!HackTXUtils.hasHackTxStarted(MainActivity.this)) {
                final Dialog d = displayDialog(R.layout.dialog_welcome_early);
                d.findViewById(R.id.welcomeDialogStart).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                        startActivity(new Intent(MainActivity.this, CheckInActivity.class));
                    }
                });

                d.findViewById(R.id.welcomeDialogNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
            } else if(!HackTXUtils.hasHackTxEnded(MainActivity.this)) {
                final Dialog d = displayDialog(R.layout.dialog_welcome);
                d.findViewById(R.id.welcomeDialogStart).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                        startActivity(new Intent(MainActivity.this, CheckInActivity.class));
                    }
                });

                d.findViewById(R.id.welcomeDialogNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
            } else {
                final Dialog d = displayDialog(R.layout.dialog_welcome_late);
                d.findViewById(R.id.welcomeDialogOk).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
            }

            UserStateStore.setFirstLaunch(this, false);
        }
    }

    private Dialog displayDialog(int layout) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);
        dialog.show();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            dialog.findViewById(R.id.skyline).setVisibility(View.GONE);
        }

        return dialog;
    }
}
