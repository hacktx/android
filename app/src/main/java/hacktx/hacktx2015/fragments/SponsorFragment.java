package hacktx.hacktx2015.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hacktx.hacktx2015.R;
import hacktx.hacktx2015.models.Sponsors;
import hacktx.hacktx2015.network.HackTxClient;
import hacktx.hacktx2015.network.services.HackTxService;
import hacktx.hacktx2015.views.SpacesItemDecoration;
import hacktx.hacktx2015.views.adapters.AnnouncementsRecyclerView;
import hacktx.hacktx2015.views.adapters.SponsorsRecyclerView;
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

        setupToolbar((Toolbar) root.findViewById(R.id.toolbar));
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
                    case SponsorsRecyclerView.TYPE_ZERO:
                    case SponsorsRecyclerView.TYPE_ONE:
                        return 2;
                    case SponsorsRecyclerView.TYPE_TWO:
                    case SponsorsRecyclerView.TYPE_THREE:
                        return 1;
                    default:
                        return -1;  //shouldn't occur
                }
            }
        });
        mRecyclerView.setLayoutManager(manager);

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
