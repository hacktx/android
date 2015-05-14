package com.utcs.mad.umad.views.tab;

/**
 * Created by tomasrodriguez on 1/27/15.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.utcs.mad.umad.R;
import com.utcs.mad.umad.fragments.ScheduleFragment;
import com.utcs.mad.umad.fragments.SponsorsFragment;
import com.utcs.mad.umad.fragments.TwitterFeedFragment;

/**
 * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
 * The individual pages are simple and just display two lines of text. The important section of
 * this class is the  #getPageTitle(int) method which controls what is displayed in the
 * SlidingTabLayout.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    private final int[] ICONS = new int[] {
            R.drawable.schedule_tab,
            R.drawable.twitter_tab,
            R.drawable.sponsors_tab
    };

    /**
     * default constructor for the FragmentPagerAdapter
     * @param fm
     */
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * @return the number of pages to display
     */
    @Override
    public int getCount() {
        return ICONS.length;
    }

    public int getDrawableId(int position) {
        return ICONS[position];
    }

//        /**
//         * @return true if the value returned from  #instantiateItem(ViewGroup, int)} is the
//         * same object as the View added to the ViewPager.
//         */
//        @Override
//        public boolean isViewFromObject(View view, Object o) {
//            return o == view;
//        }

//
//    /**
//     * Return the title of the item at position. This is important as what this method
//     * returns is what is displayed in the SlidingTabLayout.
//     *
//     * Here we construct one using the position value, but for real application the title should
//     * refer to the item's contents.
//     */
//    @Override
//    public CharSequence getPageTitle(int position) {
//
//        switch (position)
//        {
//            case 0:
//                return "Schedule";
//            case 1:
//                return "Twitter Feed";
//            case 2:
//                return "Sponsors";
//        }
//
//        return "Item " + (position + 1);
//    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0: return ScheduleFragment.newInstance("CalendarFragment");
            case 1: return TwitterFeedFragment.newInstance("TwitterFeedFragment");
            case 2: return SponsorsFragment.newInstance("SponsorsFragment");
        }

        return null;
    }

//        /**
//         * Instantiate the View which should be displayed at {@code position}. Here we
//         * inflate a layout from the apps resources and then change the text view to signify the position.
//         */
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            // Inflate a new layout from our resources
//            View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
//                    container, false);
//            // Add the newly created View to the ViewPager
//            container.addView(view);
//
//            // Retrieve a TextView from the inflated View, and update it's text
//            TextView title = (TextView) view.findViewById(R.id.item_title);
//            title.setText(String.valueOf(position + 1));
//
//            Log.i(LOG_TAG, "instantiateItem() [position: " + position + "]");
//
//            // Return the View
//            return view;
//        }

//        /**
//         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
//         * {@link View}.
//         */
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View) object);
//            Log.i(LOG_TAG, "destroyItem() [position: " + position + "]");
//        }




}
