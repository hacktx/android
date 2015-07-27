package hacktx.hacktx2015.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import hacktx.hacktx2015.R;
import hacktx.hacktx2015.fragments.AnnouncementFragment;
import hacktx.hacktx2015.fragments.BaseFragment;
import hacktx.hacktx2015.fragments.MapFragment;
import hacktx.hacktx2015.fragments.ScheduleMainFragment;
import hacktx.hacktx2015.fragments.SponsorFragment;
import hacktx.hacktx2015.fragments.TwitterFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int navSelect = getIntent().getIntExtra("navSelect", 0);

        setupTaskActivityInfo();
        setupDrawerContent(this, (DrawerLayout) findViewById(R.id.drawer_layout),
                (NavigationView) findViewById(R.id.nav_view), navSelect);
        setupFragmentContent(savedInstanceState, navSelect);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                Snackbar.make(findViewById(android.R.id.content), R.string.action_settings, Snackbar.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setupTaskActivityInfo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String appName = getString(R.string.app_name);
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            int color = getResources().getColor(R.color.primaryDark);
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(appName, icon, color);
            setTaskDescription(taskDesc);
        }
    }

    protected void setupDrawerContent(final Context context, final DrawerLayout drawerLayout, NavigationView navigationView, int navSelect) {
        this.drawerLayout = drawerLayout;

        navigationView.getMenu().getItem(navSelect).setChecked(true);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(final MenuItem menuItem) {
                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.nav_schedule:
                                switchFragment(new ScheduleMainFragment());
                                break;
                            case R.id.nav_announcement:
                                switchFragment(new AnnouncementFragment());
                                break;
                            case R.id.nav_twitter:
                                switchFragment(new TwitterFragment());
                                break;
                            case R.id.nav_map:
                                switchFragment(new MapFragment());
                                break;
                            case R.id.nav_sponsors:
                                switchFragment(new SponsorFragment());
                                break;
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    protected void setupFragmentContent(Bundle savedInstanceState, int navSelect) {
        // Setup fragments
        Log.v("main", "before");
        if (savedInstanceState == null) {
            Log.v("main", "start");
            switch (navSelect) {
                case 0: switchFragment(new ScheduleMainFragment()); break;
                case 1: switchFragment(new AnnouncementFragment()); break;
                case 2: switchFragment(new TwitterFragment()); break;
                case 3: switchFragment(new MapFragment()); break;
                case 4: switchFragment(new SponsorFragment()); break;
                default: switchFragment(new ScheduleMainFragment()); break;
            }
        }
    }

    protected void switchFragment(BaseFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_fragment, fragment);
        transaction.commit();
    }
}
