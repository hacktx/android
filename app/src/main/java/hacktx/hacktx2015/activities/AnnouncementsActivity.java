package hacktx.hacktx2015.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import hacktx.hacktx2015.BuildConfig;
import hacktx.hacktx2015.R;
import hacktx.hacktx2015.models.Messages;
import hacktx.hacktx2015.models.AnnouncementResponse;
import hacktx.hacktx2015.network.RestAdapterClient;
import hacktx.hacktx2015.views.adapters.AnnouncementsRecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Drew on 6/29/15.
 */
public class AnnouncementsActivity extends BaseActivity {

    private static final String TAG = "AnnouncementsActivity";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Messages> announcements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);
        setupToolbar((Toolbar) findViewById(R.id.toolbar));
        setupDrawerContent(this, (DrawerLayout) findViewById(R.id.drawer_layout), (NavigationView) findViewById(R.id.nav_view));

        announcements = new ArrayList<>();
        createFakeData();
        setupRecyclerView();

    }

    private void createFakeData() {
        SimpleDateFormat formatFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat formatTo = new SimpleDateFormat("MM-dd hh:mm a", Locale.US);

        try {
            Messages announcement1 = new Messages("brit'ne is totally cool", formatTo.parse("09-27 12:50 pm").toString());
            Messages announcement2 = new Messages("this is an announcement", formatTo.parse("09-27 9:45 pm").toString());
            Messages announcement3 = new Messages("what's the hip haps", formatTo.parse("09-27 11:30 am").toString());
            Messages announcement4 = new Messages("bibbity bobbity boo", formatTo.parse("09-26 5:50 pm").toString());

            announcements.add(announcement1);
            announcements.add(announcement2);
            announcements.add(announcement3);
            announcements.add(announcement4);
        }
        catch (ParseException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    /*
    show time as 12:34pm for same day
    Jul 23, 12:34pm for previous day
     */
    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.announcement_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new AnnouncementsRecyclerView(announcements);
        mRecyclerView.setAdapter(mAdapter);
    }

    /*
    time will be returned like this: "ts": "1436149996.000272"
    so delete last three numbers and decimal place to get actual date of message
     */
    /*
    get messages using channel id
     */
    private void test() {

        RestAdapterClient.getRestClient().getMessages("nucleus something here",
                new Callback<AnnouncementResponse>() {
                    @Override
                    public void success(AnnouncementResponse announcementResponse, Response response) {
                        Log.d(TAG, "slack messages retrieved!");
                        //announcements.removeAll(announcements);
                        announcements.clear();
                        announcements.addAll(announcementResponse.getMessages());
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "error retrieving messages: " + error.getMessage());
                    }
                });

    }
}
