/*
 * Copyright 2016 HackTX.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hacktx.android.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hacktx.android.BuildConfig;
import com.hacktx.android.R;
import com.hacktx.android.network.NetworkUtils;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import io.fabric.sdk.android.Fabric;

public class TwitterFragment extends BaseFragment {

    private ConstraintLayout mEmptyLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView twitterListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_twitter, container, false);

        setHasOptionsMenu(true);

        mEmptyLayout = (ConstraintLayout) root.findViewById(R.id.empty_view);

        setupToolbar((Toolbar) root.findViewById(R.id.toolbar), R.string.fragment_twitter_title);

        setupSwipeRefreshLayout(root);
        setupTwitter(root);
        setupEmptyLayout((TextView) root.findViewById(R.id.fragment_empty_title), (Button) root.findViewById(R.id.fragment_empty_btn));

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_twitter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_in_twitter:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/HackTX")));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupTwitter(ViewGroup root) {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET);
        Fabric.with(getActivity(), new Twitter(authConfig));
        twitterListView = (ListView) root.findViewById(R.id.twitterList);
        updateTwitterTimeline();
    }

    private void setupSwipeRefreshLayout(ViewGroup root) {
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.hacktx_blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateTwitterTimeline();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateTwitterTimeline() {
        if (NetworkUtils.canConnect(getContext())) {
            hideEmptyView();
            UserTimeline userTimeline = new UserTimeline.Builder()
                    .screenName("hacktx")
                    .build();
            TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(getActivity(), userTimeline);
            twitterListView.setAdapter(adapter);
        } else {
            showEmptyView();
        }
    }

    private void setupEmptyLayout(TextView text, Button retryBtn) {
        text.setText(R.string.twitter_empty_title);

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTwitterTimeline();
            }
        });
    }

    private void showEmptyView() {
        swipeRefreshLayout.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        mEmptyLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
    }
}
