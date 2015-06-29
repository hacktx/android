package hacktx.hacktx2015.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import hacktx.hacktx2015.R;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Drew on 6/28/15.
 */
public class MapActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setupToolbar((Toolbar) findViewById(R.id.toolbar));
        setupDrawerContent(this, (DrawerLayout) findViewById(R.id.drawer_layout), (NavigationView) findViewById(R.id.nav_view));

        setupMapButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setupMapButtons() {
        final ImageView floorImage = (ImageView) findViewById(R.id.mapImage);

        Button floor1 = (Button) findViewById(R.id.button1);
        floor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floorImage.setImageResource(R.drawable.sac_first_level);
            }
        });
        Button floor2 = (Button) findViewById(R.id.button2);
        floor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floorImage.setImageResource(R.drawable.sac_second_level);
            }
        });
        Button floor3 = (Button) findViewById(R.id.button3);
        floor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floorImage.setImageResource(R.drawable.sac_third_level);
            }
        });
    }
}
