package hacktx.hacktx2015.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hacktx.hacktx2015.R;
import hacktx.hacktx2015.enums.EventType;
import hacktx.hacktx2015.models.ScheduleCluster;
import hacktx.hacktx2015.models.ScheduleEvent;
import hacktx.hacktx2015.models.ScheduleSpeaker;
import hacktx.hacktx2015.views.adapters.ScheduleClusterRecyclerView;

/**
 * Created by Drew on 6/28/15.
 */
public class ScheduleDayFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<ScheduleCluster> scheduleList;

    public ScheduleDayFragment() {
    }

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

        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.accent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scheduleList.clear();
                new ScheduleDataAsyncTask().execute();
            }
        });

        new ScheduleDataAsyncTask().execute();

        recyclerView = (RecyclerView) root.findViewById(R.id.scheduleRecyclerView);
        RecyclerView.Adapter scheduleAdapter = new ScheduleClusterRecyclerView(scheduleList);
        recyclerView.setAdapter(scheduleAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        return root;
    }

    class ScheduleDataAsyncTask extends AsyncTask<String, String, Void> {

        JSONArray jsonArray = null;
        private ProgressDialog progressDialog = new ProgressDialog(getActivity());

        protected void onPreExecute() {
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(String... params) {

            String url = "http://texasgamer.me/projects/hacktx/schedule.json";
            InputStream is = null;
            String json = "";

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            try {
                jsonArray = new JSONArray(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

            return null;
        }

        protected void onPostExecute(Void v) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            try {
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject schedClusterJson = jsonArray.getJSONObject(x);
                    JSONArray eventsArrayJson = schedClusterJson.getJSONArray("eventsList");
                    ArrayList<ScheduleEvent> events = new ArrayList<>();

                    for (int n = 0; n < eventsArrayJson.length(); n++) {
                        JSONObject event = eventsArrayJson.getJSONObject(n);
                        EventType type;
                        switch (event.getString("type")) {
                            case "food":
                                type = EventType.FOOD;
                                break;
                            case "education":
                                type = EventType.EDUCATION;
                                break;
                            case "talk":
                                type = EventType.TALK;
                                break;
                            default:
                                type = EventType.TALK;
                                break;
                        }

                        ArrayList<ScheduleSpeaker> speakers = new ArrayList<>();
                        JSONArray speakerArray = event.getJSONArray("speakerList");
                        for (int k = 0; k < speakerArray.length(); k++) {
                            JSONObject speaker = speakerArray.getJSONObject(k);
                            speakers.add(new ScheduleSpeaker(speaker.getInt("id"), speaker.getString("name"),
                                    speaker.getString("organization"), speaker.getString("description"),
                                    speaker.getString("imageUrl")));
                        }

                        events.add(new ScheduleEvent(event.getInt("id"), type, event.getString("name"),
                                formatter.parse(event.getString("startDate")),
                                formatter.parse(event.getString("endDate")),
                                event.getString("location"), event.getString("description"), speakers));
                    }

                    scheduleList.add(new ScheduleCluster(schedClusterJson.getInt("id"),
                            schedClusterJson.getString("name"), events));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            RecyclerView.Adapter scheduleAdapter = new ScheduleClusterRecyclerView(scheduleList);
            recyclerView.setAdapter(scheduleAdapter);

            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
