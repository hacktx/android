package hacktx.hacktx2015.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import hacktx.hacktx2015.BuildConfig;
import hacktx.hacktx2015.R;
import hacktx.hacktx2015.utils.HackTXUtils;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Drew on 7/22/2015.
 */
public class TwitterFragment extends BaseFragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView twitterListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_twitter, container, false);

        setupToolbar((Toolbar) root.findViewById(R.id.toolbar), R.string.fragment_twitter_title);

        setupTwitter(root);
        setupSwipeRefreshLayout(root);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        HackTXUtils.getGoogleAnalyticsTracker(getActivity()).setScreenName("Screen~" + "Twitter");
        HackTXUtils.getGoogleAnalyticsTracker(getActivity()).send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void setupTwitter(ViewGroup root) {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET);
        Fabric.with(getActivity(), new Twitter(authConfig));
        twitterListView = (ListView) root.findViewById(R.id.twitterList);
        updateTwitterTimeline();
    }

    private void setupSwipeRefreshLayout(ViewGroup root) {
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.accent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateTwitterTimeline();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateTwitterTimeline() {
        UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("hacktx")
                .build();
        TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(getActivity(), userTimeline);
        twitterListView.setAdapter(adapter);
    }
}
