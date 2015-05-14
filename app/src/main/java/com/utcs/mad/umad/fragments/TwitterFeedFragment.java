package com.utcs.mad.umad.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.utcs.mad.umad.views.adapters.CustomTweetViewAdapter;
import com.utcs.mad.umad.R;
import com.utcs.mad.umad.activities.MainActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwitterFeedFragment extends Fragment {

    CustomTweetViewAdapter adapter;
    StatusesService statusesService;
    SwipeRefreshLayout swipeLayout;

    // Required empty public constructor
    public TwitterFeedFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_twitter_feed, container, false);
        setupTwitterList(root);
        setupSwipeRefreshLayout(root);

        return root;
    }

    // Gather the tweets from MAD account and put them into the list adapter
    private void setupTwitterList(View root) {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(MainActivity.twitterSession);

        ListView timeline = (ListView) root.findViewById(R.id.timeline);
        adapter = new CustomTweetViewAdapter(getActivity().getApplicationContext());
        timeline.setAdapter(adapter);

        statusesService = twitterApiClient.getStatusesService();
        getTwitterFeed();
    }

    private void setupSwipeRefreshLayout(View root) {
        swipeLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(R.color.primary_dark, R.color.primary, R.color.primary_accent);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTwitterFeed();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    private void getTwitterFeed() {
        if(statusesService == null)
            return;

        statusesService.userTimeline(null, getString(R.string.twitter_mad_username), null, null, null, null, null, null, null,
                new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> listResult) {
                        adapter.setTweets(listResult.data);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Log.e("TwitterFeed", e.toString());
                    }
        });
    }

    public static TwitterFeedFragment newInstance(String text) {
        TwitterFeedFragment f = new TwitterFeedFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}
