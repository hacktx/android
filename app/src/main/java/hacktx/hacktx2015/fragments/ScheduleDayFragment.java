package hacktx.hacktx2015.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import hacktx.hacktx2015.R;
import hacktx.hacktx2015.models.ScheduleCluster;
import hacktx.hacktx2015.views.adapters.ScheduleClusterRecyclerView;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Drew on 6/28/15.
 */
public class ScheduleDayFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<ScheduleCluster> scheduleList;
    private int day;
    private boolean doneLoading;

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

        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.accent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scheduleList.clear();
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

        new ScheduleDataAsyncTask(false).execute();

        recyclerView = (RecyclerView) root.findViewById(R.id.scheduleRecyclerView);
        RecyclerView.Adapter scheduleAdapter = new ScheduleClusterRecyclerView(scheduleList);
        recyclerView.setAdapter(scheduleAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        return root;
    }

    class ScheduleDataAsyncTask extends AsyncTask<String, String, Void> {

        private ArrayList<ScheduleCluster> scheduleClusters;
        private boolean overrideCache;

        public ScheduleDataAsyncTask(boolean overrideCache) {
            this.overrideCache = overrideCache;

            doneLoading = false;
        }

        @Override
        protected Void doInBackground(String... params) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            long lastUpdated = preferences.getLong("scheduleLastUpdated" + day, 0);

            if((System.currentTimeMillis() - lastUpdated < 3600000 && !overrideCache) || !isNetworkAvailable()) {
                Log.v("ScheduleDayFragment", "Loading from file! (day " + day + ")");
                scheduleClusters = getDataFromFile();
            } else {
                Log.v("ScheduleDayFragment", "Loading from URL! (day " + day + ")");
                scheduleClusters = getDataFromUrl();
            }

            doneLoading = true;

            return null;
        }

        protected void onPostExecute(Void v) {
            if(scheduleClusters.size() == 0) {
                Log.v("ScheduleDayFragment", "Offline and no cache available! (day " + day + ")");
            }

            RecyclerView.Adapter scheduleAdapter = new ScheduleClusterRecyclerView(scheduleClusters);
            recyclerView.setAdapter(scheduleAdapter);

            swipeRefreshLayout.setRefreshing(false);
        }

        private ArrayList<ScheduleCluster> getDataFromUrl() {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://demo9695027.mockable.io")
                    .build();

            ScheduleService scheduleService = restAdapter.create(ScheduleService.class);
            ArrayList<ScheduleCluster> scheduleClusters = scheduleService.getSchedule(1);
            saveCache(new Gson().toJson(scheduleClusters));
            return scheduleService.getSchedule(day);
        }

        private ArrayList<ScheduleCluster> getDataFromFile() {
            String cache = readCache();
            if(!cache.equals("")) {
                return new Gson().fromJson(cache, new TypeToken<ArrayList<ScheduleCluster>>() {
                }.getType());
            } else {
                if(isNetworkAvailable()) {
                    return getDataFromUrl();
                } else {
                    return new ArrayList<>();
                }
            }
        }

        private void saveCache(String data) {
            FileOutputStream outputStream;
            try {
                outputStream = getActivity().openFileOutput("schedule-" + day + ".json", Context.MODE_PRIVATE);
                outputStream.write(data.getBytes());
                outputStream.close();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong("scheduleLastUpdated" + day, System.currentTimeMillis());
                editor.apply();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String readCache() {
            String ret = "";

            try {
                InputStream inputStream = getActivity().openFileInput("schedule-" + day + ".json");

                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString;
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((receiveString = bufferedReader.readLine()) != null) {
                        stringBuilder.append(receiveString);
                    }

                    inputStream.close();
                    ret = stringBuilder.toString();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return ret;
        }

        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }

    interface ScheduleService {
        @GET("/api/schedules/{day}")
        ArrayList<ScheduleCluster> getSchedule(@Path("day") int day);
    }
}
