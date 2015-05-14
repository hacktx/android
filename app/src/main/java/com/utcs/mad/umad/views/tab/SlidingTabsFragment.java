package com.utcs.mad.umad.views.tab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.utcs.mad.umad.views.tab.SlidingTabLayout;
import com.utcs.mad.umad.R;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class SlidingTabsFragment extends Fragment {

    static final String LOG_TAG = "SlidingTabsFragment";
    public static int actionBarHeight;


    /**
     * A custom ViewPager title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A ViewPager which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;


    public SlidingTabsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sliding_tabs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new TabsPagerAdapter(getActivity().getSupportFragmentManager()));



        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        //mSlidingTabLayout.setCustomTabView(R.layout.custom_tab_title, R.id.tabtext);

        mSlidingTabLayout.setDividerColors(getResources().getColor(R.color.primary_accent));
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.primary_accent));
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.primary));

        mSlidingTabLayout.setCustomTabView(R.layout.tab_custom_layout, 0);

        mSlidingTabLayout.setViewPager(mViewPager);
    }



}
