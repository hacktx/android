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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.ArrayList;

import com.hacktx.android.R;
import com.hacktx.android.activities.EventDetailActivity;
import com.hacktx.android.models.ScheduleCluster;
import com.hacktx.android.models.ScheduleEvent;
import com.hacktx.android.network.FileUtils;
import com.hacktx.android.network.HackTxClient;
import com.hacktx.android.network.NetworkUtils;
import com.hacktx.android.network.UserStateStore;
import com.hacktx.android.network.services.HackTxService;
import com.hacktx.android.views.adapters.ScheduleClusterRecyclerView;
import com.squareup.picasso.Picasso;

public class ScheduleDayFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<ScheduleCluster> scheduleList;
    private boolean doneLoading;
    private int day;

    public static ScheduleDayFragment newInstance(String request) {
        Bundle args = new Bundle();
        args.putString("request", request);

        ScheduleDayFragment fragment = new ScheduleDayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_schedule_day, container, false);
        scheduleList = new ArrayList<>();

        switch(getArguments().getString("request")) {
            case "Oct 22": day = 1; break;
            case "Oct 23": day = 2; break;
            default: day = 1;
        }

        setupRecyclerView(root);
        setupSwipeRefresh(root);
        setupCollapsibleToolbar((AppBarLayout) getActivity().findViewById(R.id.appBar), swipeRefreshLayout);
        setupRetryButton(root.findViewById(R.id.scheduleEmptyTryAgain));

        new ScheduleDataAsyncTask(false).execute();

        return root;
    }

    private void setupRecyclerView(View root) {
        recyclerView = (RecyclerView) root.findViewById(R.id.scheduleRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ScheduleClusterRecyclerView(scheduleList,
                new ScheduleClusterRecyclerView.ScheduleItemClickListener() {
            @Override
            public void onItemClick(View v, ScheduleEvent e) {
                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                intent.putExtra("eventData", new Gson().toJson(e));
                startActivity(intent);
            }
        }));
    }

    private void setupSwipeRefresh(View root) {
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.hacktx_blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ScheduleDataAsyncTask(true).execute();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (!doneLoading) {
                    swipeRefreshLayout.setRefreshing(true);
                }
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

    private void setupRetryButton(View retryBtn) {
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ScheduleDataAsyncTask(true).execute();
            }
        });
    }

    /**
     * <code>AsyncTask</code> which manages the fetching of schedule data either from
     * the network or from local cache, depending on several conditions. Also handles
     * caching new data to disk.
     */
    class ScheduleDataAsyncTask extends AsyncTask<String, String, Void> {

        final static int HOUR = 3600000;

        private ArrayList<ScheduleCluster> scheduleClusters;
        private boolean overrideCache;

        public ScheduleDataAsyncTask(boolean overrideCache) {
            this.overrideCache = overrideCache;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                if (willLoadFromFile()) {
                    Log.v("ScheduleDayFragment", "Loading day " + day + " schedule from file.");
                    scheduleClusters = getDataFromFile();
                } else {
                    Log.v("ScheduleDayFragment", "Loading day " + day + " schedule from URL.");
                    scheduleClusters = getDataFromUrl();
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                scheduleClusters = new ArrayList<>();
            }

            // Pre-load event images
            for (ScheduleCluster cluster : scheduleClusters) {
                for (ScheduleEvent event : cluster.getEventsList()) {
                    Picasso.with(getActivity()).load(event.getImageUrl()).fetch();
                }
            }

            doneLoading = true;

            return null;
        }

        protected void onPostExecute(Void v) {
            if(scheduleClusters.size() == 0) {
                Log.v("ScheduleDayFragment", "Offline and no cache available for day " + day + " schedule.");
                swipeRefreshLayout.setVisibility(View.GONE);
            } else {
                swipeRefreshLayout.setVisibility(View.VISIBLE);
            }

            scheduleList.clear();
            scheduleList.addAll(scheduleClusters);
            recyclerView.getAdapter().notifyDataSetChanged();

            swipeRefreshLayout.setRefreshing(false);
        }

        private boolean willLoadFromFile() {
            return (isFileRecent() || !NetworkUtils.canConnect(getActivity()));
        }

        private boolean isFileRecent() {
            return (System.currentTimeMillis() - UserStateStore.getScheduleLastUpdated(getActivity(), day)
                    < HOUR && !overrideCache);
        }

        private ArrayList<ScheduleCluster> getDataFromUrl() {
            HackTxService hackTxService = HackTxClient.getInstance().getApiService();
            ArrayList<ScheduleCluster> scheduleClusters = hackTxService.getScheduleDayData(day);

            FileUtils.setScheduleCache(getActivity(), day, scheduleClusters);
            return scheduleClusters;
        }

        private ArrayList<ScheduleCluster> getDataFromFile() {
            ArrayList<ScheduleCluster> result = FileUtils.getScheduleCache(getActivity(), day);

            if(result.isEmpty() && NetworkUtils.canConnect(getActivity())) {
                result = getDataFromUrl();
            }

            return result;
        }
    }
}
