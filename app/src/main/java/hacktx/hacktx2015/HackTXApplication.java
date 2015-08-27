package hacktx.hacktx2015;

import android.app.Application;
import android.util.Log;

import com.estimote.sdk.EstimoteSDK;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class HackTXApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initParse();
        initBeacons();
    }

    private void initParse() {
        Parse.initialize(this, BuildConfig.PARSE_APP_ID, BuildConfig.PARSE_CLIENT_ID);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParsePush.subscribeInBackground("Announcements", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }

    private void initBeacons() {
        EstimoteSDK.initialize(this, BuildConfig.ESTIMOTE_APP_ID, BuildConfig.ESTIMOTE_APP_TOKEN);
        EstimoteSDK.enableDebugLogging(true);
    }
}
