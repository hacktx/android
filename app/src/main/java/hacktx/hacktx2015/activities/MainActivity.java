package hacktx.hacktx2015.activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

import hacktx.hacktx2015.fragments.ScheduleDayFragment;
import hacktx.hacktx2015.models.ScheduleDay;
import hacktx.hacktx2015.models.ScheduleEvent;
import hacktx.hacktx2015.models.ScheduleSpeaker;
import hacktx.hacktx2015.R;

public class MainActivity extends BaseActivity {
    private ArrayList<ScheduleDay> scheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar((Toolbar) findViewById(R.id.toolbar));
        setupDrawerContent(this, (DrawerLayout) findViewById(R.id.drawer_layout), (NavigationView) findViewById(R.id.nav_view));

        scheduleList = new ArrayList<>();
        getFakeData();

        ViewPager viewPager = (android.support.v4.view.ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    private void getFakeData() {
        ScheduleSpeaker batman = new ScheduleSpeaker(0,
                "Batman",
                "Justice Inc",
                "Hero can be anyone. Even a man knowing something as simple and reassuring as putting a coat around a young boy shoulders to let him know the world hadn't ended.",
                "http://cdn.bgr.com/2015/04/batman-v-superman-trailer.png");
        ScheduleSpeaker superman = new ScheduleSpeaker(1,
                "Superman",
                "Peace Inc",
                " You will travel far, my little Kal-El. But we will never leave you... even in the face of our death. The richness of our lives shall be yours. All that I have, all that I've learned, everything I feel... all this, and more, I... I bequeath you, my son. You will carry me inside you, all the days of your life. You will make my strength your own, and see my life through your eyes, as your life will be seen through mine. The son becomes the father, and the father the son. This is all I... all I can send you, Kal-El.",
                "http://1.bp.blogspot.com/-w9vnmSGyhBU/VRL-ioqskHI/AAAAAAAAATA/WpM7LR8Pg1k/s1600/superman01.png");

        ArrayList<ScheduleSpeaker> batmanList = new ArrayList<>();
        batmanList.add(batman);
        ArrayList<ScheduleSpeaker> supermanList = new ArrayList<>();
        supermanList.add(superman);
        ArrayList<ScheduleSpeaker> superAndBatList = new ArrayList<>();
        batmanList.add(batman);
        supermanList.add(superman);

        ScheduleEvent batman0 = new ScheduleEvent(0, "discussion", "BatScript: The real dev language",
                "September 26 8:00am", "September 26 9:00am",
                "SAC Mainroom", "We be talkin about stuff", batmanList);
        ScheduleEvent batman1 = new ScheduleEvent(1, "discussion", "BatPython: The gangsta dev language",
                "September 26 9:00am", "September 26 10:00am",
                "SAC Mainroom", "We be talkin about stuff", batmanList);
        ScheduleEvent superman0 = new ScheduleEvent(2, "discussion", "SuperJava: The caffinated dev language",
                "September 26 9:00am", "September 26 10:00am",
                "SAC Mainroom", "We be talkin about stuff", supermanList);
        ArrayList<ScheduleEvent> sept26List = new ArrayList<>();
        sept26List.add(batman0);
        sept26List.add(batman1);
        sept26List.add(superman0);
        ScheduleDay sept26 = new ScheduleDay(0, "Sept 26", sept26List);

        scheduleList.add(sept26);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new ScheduleDayFragment(), "Sept 26");
        adapter.addFragment(new ScheduleDayFragment(), "Sept 27");
        viewPager.setAdapter(adapter);
        Log.v("Main", " "  + viewPager.getAdapter().getCount());
    }

    static class Adapter extends FragmentPagerAdapter {
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
