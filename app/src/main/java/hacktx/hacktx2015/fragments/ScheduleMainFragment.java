package hacktx.hacktx2015.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;

import hacktx.hacktx2015.HackTXApplication;
import hacktx.hacktx2015.R;

/**
 * Created by Drew on 7/22/2015.
 */
public class ScheduleMainFragment extends BaseFragment {

    private Tracker mTracker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_schedule_main, container, false);

        setupToolbar((Toolbar) root.findViewById(R.id.toolbar), R.string.fragment_schedule_main_title);

        final ViewPager viewPager = (android.support.v4.view.ViewPager) root.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupGoogleAnalyticsTracker();
    }

    private void setupGoogleAnalyticsTracker() {
        // Obtain the shared Tracker instance.
        HackTXApplication application = (HackTXApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("Screen~" + "Schedule");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getFragmentManager());
        adapter.addFragment(new ScheduleDayFragment(), "Sept 26");
        adapter.addFragment(new ScheduleDayFragment(), "Sept 27");
        viewPager.setAdapter(adapter);
        Log.v("ScheduleMainFrag", " " + viewPager.getAdapter().getCount());
    }

    static class Adapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return ScheduleDayFragment.newInstance(mFragmentTitles.get(position));
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
