package hacktx.hacktx2015.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import hacktx.hacktx2015.HackTXApplication;
import hacktx.hacktx2015.R;

/**
 * Created by Drew on 7/22/2015.
 */
public class MapFragment extends BaseFragment {

    public static final int SAC = 0;
    public static final int CLA = 1;

    private ImageView floorImage;
    private LinearLayout levelLayout;
    private Tracker mTracker;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);

        setupToolbar((Toolbar) root.findViewById(R.id.toolbar), R.string.fragment_maps_title);

        setHasOptionsMenu(true);
        setupMapButtons(root);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupGoogleAnalyticsTracker();
    }

    private void setupGoogleAnalyticsTracker() {
        // Obtain the shared Tracker instance.
        HackTXApplication application = (HackTXApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("Screen~" + "Maps");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_maps, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner buildingSpinner = (Spinner) MenuItemCompat.getActionView(item);
        String[] data = {"SAC", "CLA"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, data);
        adapter.setDropDownViewResource(R.layout.row_spinner_text);
        buildingSpinner.setAdapter(adapter); // set the adapter to provide layout of rows and content
        buildingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(android.R.color.white));
                if (position == CLA) {
                    setMapView(CLA, 1);
                } else {
                    setMapView(SAC, getActivity().getIntent().getIntExtra("level", 1));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        buildingSpinner.setSelection(getActivity().getIntent().getIntExtra("building", SAC));
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupMapButtons(ViewGroup root) {
        levelLayout = (LinearLayout) root.findViewById(R.id.levelLayout);
        floorImage = (ImageView) root.findViewById(R.id.mapImage);

        final Button floor1 = (Button) root.findViewById(R.id.button1);
        final Button floor2 = (Button) root.findViewById(R.id.button2);
        final Button floor3 = (Button) root.findViewById(R.id.button3);

        floor1.setBackgroundColor(getResources().getColor(R.color.tw__medium_gray));

        floor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMapView(SAC, 1);
            }
        });
        floor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMapView(SAC, 2);
            }
        });
        floor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMapView(SAC, 3);
            }
        });
    }

    private void setMapView(int building, int level) {
        if (building == CLA) {
            floorImage.setImageResource(R.drawable.cla_first_level);
            levelLayout.setVisibility(View.GONE);
        } else {
            levelLayout.setVisibility(View.VISIBLE);
            final Button floor1 = (Button) levelLayout.findViewById(R.id.button1);
            final Button floor2 = (Button) levelLayout.findViewById(R.id.button2);
            final Button floor3 = (Button) levelLayout.findViewById(R.id.button3);

            switch (level) {
                case 1:
                    floorImage.setImageResource(R.drawable.sac_first_level);
                    floor1.setBackgroundColor(getResources().getColor(R.color.tw__medium_gray));
                    floor2.setBackgroundColor(getResources().getColor(R.color.tw__light_gray));
                    floor3.setBackgroundColor(getResources().getColor(R.color.tw__light_gray));
                    break;
                case 2:
                    floorImage.setImageResource(R.drawable.sac_second_level);
                    floor1.setBackgroundColor(getResources().getColor(R.color.tw__light_gray));
                    floor2.setBackgroundColor(getResources().getColor(R.color.tw__medium_gray));
                    floor3.setBackgroundColor(getResources().getColor(R.color.tw__light_gray));
                    break;
                case 3:
                    floorImage.setImageResource(R.drawable.sac_third_level);
                    floor1.setBackgroundColor(getResources().getColor(R.color.tw__light_gray));
                    floor2.setBackgroundColor(getResources().getColor(R.color.tw__light_gray));
                    floor3.setBackgroundColor(getResources().getColor(R.color.tw__medium_gray));
                    break;
            }
        }
    }
}
