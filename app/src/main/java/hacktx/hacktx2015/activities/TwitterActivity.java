package hacktx.hacktx2015.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import hacktx.hacktx2015.BuildConfig;
import hacktx.hacktx2015.R;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Drew on 6/28/15.
 */
public class TwitterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);
        setupTwitter();
        setupToolbar((Toolbar) findViewById(R.id.toolbar));
        setupDrawerContent(this, (DrawerLayout) findViewById(R.id.drawer_layout), (NavigationView) findViewById(R.id.nav_view));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setupTwitter() {

        TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("hacktx")
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(this, userTimeline);
        ListView twitterListView = (ListView) findViewById(R.id.twitterList);
        twitterListView.setAdapter(adapter);
    }
}
