package com.utcs.mad.umad.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.utcs.mad.umad.models.Helper;
import com.utcs.mad.umad.R;
import com.utcs.mad.umad.activities.MainActivity;
import com.utcs.mad.umad.activities.SponsorWebviewActivity;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SponsorsFragment extends Fragment {

    static final String LOG_TAG = "SponsorsFragment";
    private static final int padding = 50;
    private static final int GOLD_SPONSOR = 2;
    private static final int SILVER_SPONSOR = 1;
    private static final int BRONZE_SPONSOR = 0;
    private static final int SPONSORS_PER_ROW = 2;

    private GridLayout sponsorGrid;

    // Required empty public constructor
    public SponsorsFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_sponsors, container, false);

        sponsorGrid = (GridLayout) rootView.findViewById(R.id.sponsorGrid);
        getSponsorParseData();

        return rootView;
    }

    private void getSponsorParseData() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Sponsors");
        query.orderByDescending("sponsorLevel");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if( e == null) {
                    int rows = 1;
                    int columns = 0;
                    // Cycle through sponsors to create the layout
                    for(ParseObject sponsor : parseObjects) {
                        int sponsorLevel = (int) sponsor.getNumber("sponsorLevel");

                        if(sponsorLevel >= BRONZE_SPONSOR) { // Show only paying sponsored entities
                            createSponsorView(sponsor, rows, columns);

                            // Update rows & columns
                            if(sponsorLevel == GOLD_SPONSOR) {
                                rows++;
                            } else {
                                columns++;
                                if(columns == SPONSORS_PER_ROW) {
                                    rows++;
                                    columns = 0;
                                }
                            }
                        }
                    }
                } else {
                    Log.e(LOG_TAG, "exception parse");
                }
            }
        });
    }

    private void createSponsorView(ParseObject sponsor, int rows, int columns) {
        int sponsorLevel = (int) sponsor.getNumber("sponsorLevel");
        String sponsorWebsite = sponsor.getString("companyWebsite");
        String sponsorName = sponsor.getString("companyName");
        ParseFile logo = (ParseFile) sponsor.get("companyImage");

        ImageView sponsorView = createLayoutParamsForSponsorLogo(rows, columns, sponsorLevel);

        sponsorView.setOnClickListener(getSponsorViewListener(sponsorWebsite, sponsorName));
        logo.getDataInBackground(getSponsorLogoImageOnView(sponsorView, sponsorLevel));
    }

    private ImageView createLayoutParamsForSponsorLogo(int rows, int columns, int sponsorLevel) {
        ImageView result;
        GridLayout.LayoutParams gridLP;

        // Create logo view
        result = new ImageView(getActivity().getApplicationContext());

        // Set layout param
        gridLP = new GridLayout.LayoutParams(GridLayout.spec(rows - 1), (sponsorLevel == GOLD_SPONSOR) ? GridLayout.spec(0, SPONSORS_PER_ROW) : GridLayout.spec(columns));
        gridLP.height = MainActivity.screenWidth / 4;
        gridLP.width = (sponsorLevel == GOLD_SPONSOR) ? MainActivity.screenWidth : MainActivity.screenWidth / SPONSORS_PER_ROW;
        sponsorGrid.setRowCount(rows);

        // Create padding and add to the layout
        result.setPadding(padding, padding / 4, padding, padding / 4);
        sponsorGrid.addView(result, gridLP);
        return result;
    }

    private View.OnClickListener getSponsorViewListener(final String sponsorWebsite, final String sponsorName) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), SponsorWebviewActivity.class);
                i.putExtra("sponsorWebsite", sponsorWebsite);
                i.putExtra("sponsorName", sponsorName);
                startActivity(i);
            }
        };
    }

    private GetDataCallback getSponsorLogoImageOnView(final ImageView sponsorView, final int sponsorLevel) {
        return new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (e == null) {
                    sponsorView.setImageBitmap(Helper.decodeBitmapFromByteArray(bytes,
                            (sponsorLevel == GOLD_SPONSOR) ? MainActivity.screenWidth - padding : MainActivity.screenWidth / SPONSORS_PER_ROW - padding,
                            MainActivity.screenWidth / 4 - padding / 2));
                }
            }
        };
    }

    public static SponsorsFragment newInstance(String text) {
        SponsorsFragment f = new SponsorsFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }


}
