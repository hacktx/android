package hacktx.hacktx2015.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import hacktx.hacktx2015.R;

/**
 * Created by Drew on 6/29/15.
 */
public class AnnouncementsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);
        setupToolbar((Toolbar) findViewById(R.id.toolbar));
        setupDrawerContent(this, (DrawerLayout) findViewById(R.id.drawer_layout), (NavigationView) findViewById(R.id.nav_view));
    }

    private void test() {

    }
}
