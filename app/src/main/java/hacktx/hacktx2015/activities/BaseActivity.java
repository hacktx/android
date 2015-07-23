package hacktx.hacktx2015.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import hacktx.hacktx2015.R;

/**
 * Created by Drew on 6/28/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String appName = getString(R.string.app_name);
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            int color = getResources().getColor(R.color.primaryDark);
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(appName, icon, color);
            setTaskDescription(taskDesc);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setupToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    protected void setupDrawerContent(final Context context, final DrawerLayout drawerLayout, NavigationView navigationView) {
        this.drawerLayout = drawerLayout;
        final int navSelect = getIntent().getIntExtra("navSelect", 0);

        navigationView.getMenu().getItem(navSelect).setChecked(true);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(final MenuItem menuItem) {
                        menuItem.setChecked(true);
                        switch(menuItem.getItemId()) {
                            case R.id.nav_schedule:
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(navSelect != 0) {
                                            Intent intent = new Intent(context, MainActivity.class);
                                            intent.putExtra("navSelect", 0);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                }, 200);
                                break;
                            case R.id.nav_announcement:
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(navSelect != 1) {
                                            Intent intent = new Intent(context, AnnouncementsActivity.class);
                                            intent.putExtra("navSelect", 1);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                }, 200);
                                break;
                            case R.id.nav_twitter:
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(navSelect != 2) {
                                            Intent intent = new Intent(context, TwitterActivity.class);
                                            intent.putExtra("navSelect", 2);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                }, 200);
                                break;
                            case R.id.nav_map:
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(navSelect != 3) {
                                            Intent intent = new Intent(context, MapActivity.class);
                                            intent.putExtra("navSelect", 3);
                                            startActivity(intent);
                                        }
                                    }
                                }, 200);
                                break;
                            case R.id.nav_sponsors:
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(navSelect != 4) {
                                            Intent intent = new Intent(context, SponsorActivity.class);
                                            intent.putExtra("navSelect", 4);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                }, 200);
                                break;
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
}
