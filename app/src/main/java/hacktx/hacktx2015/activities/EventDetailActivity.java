package hacktx.hacktx2015.activities;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import hacktx.hacktx2015.R;

public class EventDetailActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbar;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle("El Capitan");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.elcap);

        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                collapsingToolbar.setContentScrimColor(palette.getMutedColor(R.attr.colorPrimary));
                collapsingToolbar.setStatusBarScrimColor(palette.getDarkMutedColor(R.color.primaryDark));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    String appName = getString(R.string.app_name);
                    Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    int color = palette.getDarkMutedColor(R.color.primaryDark);
                    ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(appName, icon, color);
                    setTaskDescription(taskDesc);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
