package hacktx.hacktx2015.fragments;

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

import java.util.ArrayList;

import hacktx.hacktx2015.R;
import hacktx.hacktx2015.models.ScheduleCluster;
import hacktx.hacktx2015.network.FileUtils;
import hacktx.hacktx2015.network.HackTxClient;
import hacktx.hacktx2015.network.NetworkUtils;
import hacktx.hacktx2015.network.UserStateStore;
import hacktx.hacktx2015.network.services.HackTxService;
import hacktx.hacktx2015.views.adapters.ScheduleClusterRecyclerView;

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
            case "Sept 26": day = 1; break;
            case "Sept 27": day = 2; break;
            default: day = 1;
        }

        setupRecyclerView(root);
        setupSwipeRefresh(root);
        setupCollapsibleToolbar((AppBarLayout) getActivity().findViewById(R.id.appBar), swipeRefreshLayout);

        new ScheduleDataAsyncTask(false).execute();

        return root;
    }

    private void setupRecyclerView(View root) {
        recyclerView = (RecyclerView) root.findViewById(R.id.scheduleRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ScheduleClusterRecyclerView(scheduleList));
    }

    private void setupSwipeRefresh(View root) {
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.accent);
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
            if(willLoadFromFile()) {
                Log.v("ScheduleDayFragment", "Loading day " + day + " schedule from file.");
                scheduleClusters = getDataFromFile();
            } else {
                Log.v("ScheduleDayFragment", "Loading day " + day + " schedule from URL.");
                scheduleClusters = getDataFromUrl();
            }

            doneLoading = true;

            return null;
        }

        protected void onPostExecute(Void v) {
            if(scheduleClusters.size() == 0) {
                Log.v("ScheduleDayFragment", "Offline and no cache available for day " + day + " schedule.");
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
