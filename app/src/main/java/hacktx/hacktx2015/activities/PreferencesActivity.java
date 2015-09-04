package hacktx.hacktx2015.activities;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import hacktx.hacktx2015.HackTXApplication;
import hacktx.hacktx2015.R;
import hacktx.hacktx2015.fragments.PreferencesFragment;

public class PreferencesActivity extends AppCompatActivity {

    private Tracker mTracker;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        setupGoogleAnalyticsTracker();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.activity_preferences_title);
        }

        setupTaskActivityInfo();

        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new PreferencesFragment())
                .commit();
    }

    private void setupGoogleAnalyticsTracker() {
        // Obtain the shared Tracker instance.
        HackTXApplication application = (HackTXApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Screen~" + "CheckIn");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setupTaskActivityInfo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String appName = getString(R.string.app_name);
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            int color = ContextCompat.getColor(this, R.color.primaryDark);
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(appName, icon, color);
            setTaskDescription(taskDesc);
            getWindow().setStatusBarColor(color);
        }
    }
}
