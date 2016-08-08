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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hacktx.android.R;
import com.hacktx.android.models.Messages;
import com.hacktx.android.network.HackTxClient;
import com.hacktx.android.network.services.HackTxService;
import com.hacktx.android.views.SpacesItemDecoration;
import com.hacktx.android.views.adapters.AnnouncementsRecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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

        setupToolbar((Toolbar) root.findViewById(R.id.toolbar), R.string.fragment_announcement_title);

        setupSwipeRefreshLayout(root);
        setupCollapsibleToolbar((AppBarLayout) root.findViewById(R.id.appBar), swipeRefreshLayout);
        setupRecyclerView(root);
        getAnnouncements();

        return root;
    }

    private void setupSwipeRefreshLayout(ViewGroup root) {
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.announcementsSwipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.accent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAnnouncements();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    private void setupCollapsibleToolbar(AppBarLayout appBarLayout, final SwipeRefreshLayout swipeRefreshLayout) {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i == 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });
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

        mRecyclerView.addItemDecoration(new SpacesItemDecoration(20));

        // specify an adapter (see also next example)
        mAdapter = new AnnouncementsRecyclerView(announcements);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getAnnouncements() {
        HackTxService hackTxService = HackTxClient.getInstance().getApiService();
        hackTxService.getMessages(new Callback<ArrayList<Messages>>() {
            @Override
            public void success(ArrayList<Messages> messages, Response response) {
                Log.d(TAG, "messages retrieved!");
                announcements.clear();
                announcements.addAll(messages);
                Collections.sort(announcements, Messages.MessagesComparator);
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.toString());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
