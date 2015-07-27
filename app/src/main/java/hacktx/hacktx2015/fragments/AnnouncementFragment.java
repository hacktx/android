package hacktx.hacktx2015.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;

import hacktx.hacktx2015.R;
import hacktx.hacktx2015.models.AnnouncementResponse;
import hacktx.hacktx2015.models.Messages;
import hacktx.hacktx2015.network.HackTxClient;
import hacktx.hacktx2015.network.services.HackTxService;
import hacktx.hacktx2015.views.adapters.AnnouncementsRecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Drew on 7/22/2015.
 */

public class AnnouncementFragment extends BaseFragment {

    private static final String TAG = "AnnouncementsActivity";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Messages> announcements;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_announcement, container, false);
        announcements = new ArrayList<>();

        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.announcementsSwipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.accent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);

                //this is only to test how refreshing looks
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        addNewAnnouncements();
                    }
                }, 1000);
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

            }
        });

        createFakeData();
        setupRecyclerView(root);
        setupToolbar((Toolbar) root.findViewById(R.id.toolbar));

        return root;
    }

    private void addNewAnnouncements() {
        SimpleDateFormat formatTo = new SimpleDateFormat("MM-dd hh:mm a", Locale.US);

        List<Messages> mess = new ArrayList<>(announcements);
        announcements.clear();

        try {
            Messages m1 = new Messages("testing refresh 1", formatTo.format(formatTo.parse("09-27 2:45 pm")));
            Messages m2 = new Messages("testing refresh 2", formatTo.format(formatTo.parse("09-27 7:50 pm")));

            mess.add(0, m1);
            mess.add(1, m2);
            announcements.addAll(mess);
            mAdapter.notifyDataSetChanged();
        }
        catch (ParseException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void createFakeData() {
        //parse old date format string into date object
        SimpleDateFormat formatFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        //format date object to new date
        SimpleDateFormat formatTo = new SimpleDateFormat("MM-dd hh:mm a", Locale.US);

        try {
            Messages announcement1 = new Messages("brit'ne is totally cool", formatTo.format(formatTo.parse("09-27 12:50 pm")));
            Messages announcement2 = new Messages("this is an announcement a really long announcement" +
                    " because I need to test multiple line announcements. how many lines?" +
                    " where does it end? nobody knows ooh so spooky~ I bet you thought that was the" +
                    " end well you thought wrong, buddy", formatTo.format(formatTo.parse("09-27 9:45 pm")));
            Messages announcement3 = new Messages("what's the hip haps", formatTo.format(formatTo.parse("09-27 11:30 am")));
            Messages announcement4 = new Messages("bibbity bobbity boo", formatTo.format(formatTo.parse("09-26 5:50 pm")));
            Messages announcement5 = new Messages("enough announcements to scroll", formatTo.format(formatTo.parse("09-26 4:50 pm")));
            Messages announcement6 = new Messages("testtestetsttttttestetstesttse", formatTo.format(formatTo.parse("09-26 3:50 pm")));
            Messages announcement7 = new Messages("MORE ANNOUNCEMENTS PLEASE\nMORE\nANNOUNCEMENTS", formatTo.format(formatTo.parse("09-26 2:50 pm")));

            announcements.add(announcement1);
            announcements.add(announcement2);
            announcements.add(announcement3);
            announcements.add(announcement4);
            announcements.add(announcement5);
            announcements.add(announcement6);
            announcements.add(announcement7);
        }
        catch (ParseException e) {
            Log.d("announcementsfrag", e.getMessage());
        }
    }

    /*
    show time as 12:34pm for same day
    Jul 23, 12:34pm for previous day
     */
    private void setupRecyclerView(ViewGroup root) {
        mRecyclerView = (RecyclerView) root.findViewById(R.id.announcement_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new AnnouncementsRecyclerView(announcements);
        mRecyclerView.setAdapter(mAdapter);
    }

    /*
    get messages using channel id
     */
    private void test() {
        HackTxService hackTxService = HackTxClient.getInstance().getApiService();
        List<Messages> newAnn = hackTxService.getMessages();

        Log.d(TAG, "messages retrieved!");
        announcements.clear();
        announcements.addAll(newAnn);
        mAdapter.notifyDataSetChanged();
    }
}
