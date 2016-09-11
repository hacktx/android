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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hacktx.android.R;
import com.hacktx.android.models.Sponsors;
import com.hacktx.android.network.HackTxClient;
import com.hacktx.android.network.services.HackTxService;
import com.hacktx.android.views.SpacesItemDecoration;
import com.hacktx.android.views.adapters.SponsorsRecyclerView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SponsorFragment extends BaseFragment {

    private ArrayList<Sponsors> sponsorsList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RelativeLayout mEmptyLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_sponsors, container, false);
        sponsorsList = new ArrayList<>();

        setupToolbar((Toolbar) root.findViewById(R.id.toolbar), R.string.fragment_sponsor_title);

        mEmptyLayout = (RelativeLayout) root.findViewById(R.id.empty_view);

        setupEmptyLayout((TextView) root.findViewById(R.id.fragment_empty_title), (Button) root.findViewById(R.id.fragment_empty_btn));
        getSponsors();
        setupRecyclerView(root);

        return root;
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
                hideEmptyView();
                sponsorsList.clear();
                sponsorsList.addAll(sponsors);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("SponsorsFragment", error.getMessage());
                showEmptyView();
            }
        });
    }

    private void setupEmptyLayout(TextView text, Button retryBtn) {
        text.setText(R.string.fragment_sponsor_empty_title);

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSponsors();
            }
        });
    }

    private void showEmptyView() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        mEmptyLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
