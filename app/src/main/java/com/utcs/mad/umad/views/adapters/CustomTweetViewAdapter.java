package com.utcs.mad.umad.views.adapters;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.BaseTweetView;
import com.twitter.sdk.android.tweetui.TweetViewAdapter;

import java.util.List;

/**
 * Twitter View Adapter
 * This is a custom twitter view adapter, which is needed to remove the ability to click on the
 * tweets, as this can cause a crash from the intent permissions, which I am unsure how to resolve
 */
public class CustomTweetViewAdapter extends TweetViewAdapter {

    private Context mContext;
    public CustomTweetViewAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View rootView = super.getView(position, convertView, parent);

        gackToFixTweetError(rootView);

        return rootView;
    }

    // This allows for the the twitter view to not be clickable
    private void gackToFixTweetError(View rootView) {
        rootView.setOnClickListener(null);
        rootView.setOnLongClickListener(null);
        rootView.setOnTouchListener(null);

        ViewGroup vg1 = (ViewGroup) rootView;
        ViewGroup vg2 = (ViewGroup) vg1.getChildAt(0);
        vg2.getChildAt(4).setOnClickListener(null);
    }

    // Recursive method to clear all possible listeners for each view in a certain view
    private void getChildFromViewGroup(ViewGroup viewGroup) {
        for(int child = 0; child < viewGroup.getChildCount(); child++) {
            if(viewGroup.getChildAt(child) instanceof ViewGroup) {
                getChildFromViewGroup((ViewGroup) viewGroup.getChildAt(child));
            } else {
                viewGroup.getChildAt(child).setOnClickListener(null);
                viewGroup.getChildAt(child).setOnLongClickListener(null);
                viewGroup.getChildAt(child).setOnTouchListener(null);
            }
        }
    }
}
