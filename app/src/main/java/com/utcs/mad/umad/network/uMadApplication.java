package com.utcs.mad.umad.network;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.utcs.mad.umad.BuildConfig;
import com.utcs.mad.umad.R;

/**
 * uMAD Application
 * This is the place to init the parse functionality
 */
public class uMadApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, BuildConfig.PARSE_ID, BuildConfig.PARSE_CLIENT_KEY);

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
