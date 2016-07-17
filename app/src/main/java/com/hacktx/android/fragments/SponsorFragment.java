package com.hacktx.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;

import java.util.ArrayList;

import com.hacktx.android.R;
import com.hacktx.android.models.Sponsors;
import com.hacktx.android.network.HackTxClient;
import com.hacktx.android.network.services.HackTxService;
import com.hacktx.android.utils.HackTXUtils;
import com.hacktx.android.views.SpacesItemDecoration;
import com.hacktx.android.views.adapters.SponsorsRecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Drew on 7/22/2015.
 */
public class SponsorFragment extends BaseFragment {

    private ArrayList<Sponsors> sponsorsList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_sponsors, container, false);
        sponsorsList = new ArrayList<>();

        setupToolbar((Toolbar) root.findViewById(R.id.toolbar), R.string.fragment_sponsor_title);

        getSponsors();
        setupRecyclerView(root);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        HackTXUtils.getGoogleAnalyticsTracker(getActivity()).setScreenName("Screen~" + "Sponsor");
        HackTXUtils.getGoogleAnalyticsTracker(getActivity()).send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void setupRecyclerView(ViewGroup root) {
        mRecyclerView = (RecyclerView) root.findViewById(R.id.sponsors_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        // specify an adapter (see also next example)
        mAdapter = new SponsorsRecyclerView(sponsorsList, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mAdapter.getItemViewType(position)) {
                    case 0:
                    case 1:
                        return 2;
                    case 2:
                    case 3:
                        return 1;
                    default:
                        return -1;  //shouldn't occur
                }
            }
        });
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(25));

    }

    private void getSponsors() {
        HackTxService hackTxService = HackTxClient.getInstance().getApiService();
        hackTxService.getSponsors(new Callback<ArrayList<Sponsors>>() {
            @Override
            public void success(ArrayList<Sponsors> sponsors, Response response) {
                Log.d("SponsorsFragment", "sponsors retrieved!");
                sponsorsList.clear();
                sponsorsList.addAll(sponsors);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("SponsorsFragment", error.getMessage());
            }
        });
    }
}
