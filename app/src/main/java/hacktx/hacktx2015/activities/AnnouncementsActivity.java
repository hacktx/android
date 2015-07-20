package hacktx.hacktx2015.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import hacktx.hacktx2015.BuildConfig;
import hacktx.hacktx2015.R;
import hacktx.hacktx2015.models.SlackChannelResponse;
import hacktx.hacktx2015.network.RestAdapterClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Drew on 6/29/15.
 */
public class AnnouncementsActivity extends BaseActivity {

    private static final String TAG = "AnnouncementsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);
        setupToolbar((Toolbar) findViewById(R.id.toolbar));
        setupDrawerContent(this, (DrawerLayout) findViewById(R.id.drawer_layout), (NavigationView) findViewById(R.id.nav_view));

        test();
    }

    /*
    time will be returned like this: "ts": "1436149996.000272"
    so delete last three numbers and decimal place to get actual date of message
     */
    private void test() {

        RestAdapterClient.getRestClient().getChannelsList(BuildConfig.SLACK_TOKEN, new Callback<SlackChannelResponse>() {
            @Override
            public void success(SlackChannelResponse slackChannelResponse, Response response) {
                // success!
                Log.i(TAG, "first channel name: " + slackChannelResponse.getChannels().get(0).getName());
                Log.i(TAG, "second channel name: " + slackChannelResponse.getChannels().get(1).getName());
            }

            @Override
            public void failure(RetrofitError error) {
                // something went wrong
                Log.e(TAG, error.getMessage());
            }
        });

    }
}
