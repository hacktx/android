package hacktx.hacktx2015.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import hacktx.hacktx2015.R;

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

        StringBuilder stringBuilder = new StringBuilder();

        try {
            URL url = new URL("https://slack.com/api/channels.history?token=xoxp-4100074345-4874523927-7052863236-611ca8&channel=C042Y26AM&count=2&pretty=1");
            URLConnection urlConnection = url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            int b;
            //creates json as string
            while ((b = in.read()) != -1) {
                stringBuilder.append((char) b);
            }

            in.close();
        }
        catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

    }
}
