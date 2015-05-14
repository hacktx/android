package com.utcs.mad.umad.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.utcs.mad.umad.views.ScrollViewWithListener;
import com.utcs.mad.umad.models.EventInfo;
import com.utcs.mad.umad.models.Helper;
import com.utcs.mad.umad.R;

/**
 * Event Activity
 * This activity is the main page to display information from a specific event that was selected from
 * the user. It shows the event info, such as speaker, time, location, etc.
 */
public class EventActivity extends ActionBarActivity {

    private Toolbar toolbar;

    ImageView companyImage_ImageView;

    TextView description_TextView;
    ScrollViewWithListener scrollView;

    LinearLayout linearLayout;
    LinearLayout eventItemLinearLayout;
    TextView eventItemSession;
    TextView eventItemSpeakerName;
    TextView eventItemTime;
    TextView eventItemRoom;

    ImageView companyThumbnail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        EventInfo eventInfo = MainActivity.eventInfoListCache.get(getIntent().getIntExtra("id", 0));

        // Setup non-network needed information
        setupToolbar(eventInfo);
        setupEmptyCoverImage();
        setupEventInfoViews(eventInfo);

        // Call parse for the image values ( cant cache this info, as it is too big )
        updateCoverImage(eventInfo);
        updateThumbnail(eventInfo);
    }

    private void setupToolbar(EventInfo eventInfo) {
        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle(eventInfo.getCompanyName());

        // Setups sticky toolbar
        scrollView = (ScrollViewWithListener) findViewById(R.id.sticky_scroll);
        linearLayout = (LinearLayout) scrollView.getChildAt(0);
        scrollView.setOnScrollViewListener(new ScrollViewWithListener.OnScrollViewListener() {
            @Override
            public void onScrollChanged(ScrollViewWithListener v, int l, int t, int oldl, int oldt) {
                int scrollViewY = scrollView.getScrollY();
                companyImage_ImageView.setTranslationY(scrollViewY * 0.5f);
            }
        });
    }

    private void setupEmptyCoverImage() {
        // Get the height of the screen in order to set the cover image height
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, displaymetrics.heightPixels / 3);
        
        companyImage_ImageView = (ImageView) findViewById(R.id.companyImageEventValue);
        companyImage_ImageView.setLayoutParams(layoutParams);
        companyImage_ImageView.setBackgroundColor(Color.TRANSPARENT);
    }

    private void setupEventInfoViews(EventInfo eventInfo) {
        // Find event views
        description_TextView = (TextView) findViewById(R.id.descriptionEventValue);
        eventItemLinearLayout = (LinearLayout) findViewById(R.id.event_item_sticky_wrapper);
        companyThumbnail = (ImageView) findViewById(R.id.companyThumbnail);
        eventItemSession = (TextView) findViewById(R.id.sessionName);
        eventItemSpeakerName = (TextView) findViewById(R.id.speakerName);
        eventItemTime = (TextView) findViewById(R.id.eventTime);
        eventItemRoom = (TextView) findViewById(R.id.eventRoom);

        // Update event info
        description_TextView.setText(eventInfo.getDescription());
        eventItemSession.setText(eventInfo.getSessionName());
        eventItemTime = (TextView) findViewById(R.id.eventTime);
        eventItemSpeakerName.setText(eventInfo.getSpeaker());
        eventItemRoom.setText(eventInfo.getRoom());
    }

    // Gets the companyImage value from parse in order to set the cover image to the correct image
    private void updateCoverImage(EventInfo eventInfo) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.whereEqualTo("company", eventInfo.getCompanyName());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null) {
                    ParseFile thumbnail = (ParseFile) parseObject.get("companyImage");
                    thumbnail.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, ParseException e) {
                            if(e == null) {
                                DisplayMetrics displaymetrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                                int width = displaymetrics.widthPixels;

                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, displaymetrics.heightPixels / 3);

                                companyImage_ImageView.setLayoutParams(layoutParams);
                                companyImage_ImageView.setImageBitmap(Helper.decodeBitmapFromByteArray(bytes, width, width));
                            }
                        }
                    });
                }
            }
        });
    }

    // Gets the thumbnail value from parse to update the event info header thumbnail
    private void updateThumbnail(EventInfo eventInfo) {
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Sponsors");
        query2.whereEqualTo("companyName", eventInfo.getCompanyName());
        query2.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null) {
                    ParseFile thumbnail = (ParseFile) parseObject.get("thumbnail");
                    thumbnail.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, ParseException e) {
                            if(e == null) {
                                DisplayMetrics displaymetrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                                int width = displaymetrics.widthPixels / 4;

                                companyThumbnail.setImageBitmap(Helper.decodeBitmapFromByteArray(bytes, width, width));
                            }
                        }
                    });
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.about_us) {
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
