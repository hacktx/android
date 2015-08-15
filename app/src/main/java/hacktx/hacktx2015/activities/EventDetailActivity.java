package hacktx.hacktx2015.activities;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import hacktx.hacktx2015.R;
import hacktx.hacktx2015.models.ScheduleEvent;
import hacktx.hacktx2015.models.ScheduleSpeaker;
import hacktx.hacktx2015.views.CircularImageView;

public class EventDetailActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbar;
    private ScheduleEvent event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        setupEventData(getIntent().getExtras().getString("eventData"));
        setupToolbar((Toolbar) findViewById(R.id.toolbar));
        setupTaskActivityInfo();
        setupCards();
        setupEventDetails();
        setupEventDescription();
        setupSpeakers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_map:
                /*
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("navSelect", 3);
                startActivity(intent);
                */
                Snackbar.make(findViewById(android.R.id.content), "To be implemented...", Snackbar.LENGTH_SHORT).show();
                return true;
            case R.id.action_share:
                String shareBody = getString(R.string.event_share_body, event.getName());
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.event_share_subject));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.event_share_dialog_title)));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupEventData(String eventData) {
        Log.i("EventDetailActivity", "Passed event data: " + eventData);
        try {
            event = new Gson().fromJson(eventData, new TypeToken<ScheduleEvent>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: Gracefully handle JSON parse error
        }
    }

    private void setupToolbar(Toolbar toolbar) {
        Picasso.with(this).load(event.getImageUrl()).into((ImageView) findViewById(R.id.header));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle(event.getName());
    }

    private void setupTaskActivityInfo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String appName = getString(R.string.app_name);
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            int color = getResources().getColor(R.color.primaryDark);
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(appName, icon, color);
            setTaskDescription(taskDesc);
        }
    }

    private void setupCards() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Calendar now = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        final CardView rateEventCard = (CardView) findViewById(R.id.rateEventCard);

        try {
            end.setTime(formatter.parse(event.getEndDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(!end.before(now)) {
            findViewById(R.id.rateEventCard).setVisibility(View.GONE);
        } else {
            findViewById(R.id.rateEventCardOk).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(findViewById(android.R.id.content), "Rate event", Snackbar.LENGTH_SHORT).show();
                }
            });

            findViewById(R.id.rateEventCardNoThanks).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rateEventCard.setVisibility(View.GONE);
                }
            });
        }
    }

    private void setupEventDetails() {
        int eventIconId;
        switch(event.getType()) {
            case TALK: eventIconId = R.drawable.ic_event_talk; break;
            case EDUCATION: eventIconId = R.drawable.ic_event_education; break;
            case BUS: eventIconId = R.drawable.ic_event_bus; break;
            case FOOD: eventIconId = R.drawable.ic_event_food; break;
            case DEV: eventIconId = R.drawable.ic_event_dev; break;
            default: eventIconId = R.drawable.ic_event_default; break;
        }

        ImageView eventIcon = (ImageView) findViewById(R.id.eventIcon);
        eventIcon.setImageResource(eventIconId);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Calendar start = Calendar.getInstance();

        try {
            start.setTime(formatter.parse(event.getStartDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ((TextView) findViewById(R.id.eventLocation)).setText(event.getLocation());
        ((TextView) findViewById(R.id.eventDateTime)).setText(start.getDisplayName(Calendar.MONTH,
                Calendar.LONG, Locale.getDefault()) + " " + start.get(Calendar.DAY_OF_MONTH)
                + " | " + event.getEventTimes());
    }

    private void setupEventDescription() {
        ((TextView) findViewById(R.id.eventDescription)).setText(event.getDescription());
    }

    private void setupSpeakers() {
        LinearLayout speakersContainer = (LinearLayout) findViewById(R.id.speakerHolderLayout);
        ArrayList<ScheduleSpeaker> speakers = event.getSpeakerList();
        speakersContainer.removeAllViews();

        if(speakers.size() == 0) {
            View speakerTitle = findViewById(R.id.speakersTitle);
            speakerTitle.setVisibility(View.GONE);
        }

        for(int child = 0; child < speakers.size(); child++) {
            final ScheduleSpeaker speaker = speakers.get(child);
            View childView = LayoutInflater.from(speakersContainer.getContext())
                    .inflate(R.layout.event_detail_speaker, speakersContainer, false);

            CircularImageView speakerIcon = (CircularImageView) childView.findViewById(R.id.speakerIcon);
            Picasso.with(this).load(speaker.getImageUrl())
                    .resize(150, 150)
                    .centerCrop()
                    .placeholder(R.drawable.ic_profile)
                    .into(speakerIcon);
            ((TextView) childView.findViewById(R.id.speakerTitle)).setText(speaker.getName()
                    + " | " + speaker.getOrganization());
            ((TextView) childView.findViewById(R.id.speakerDescription)).setText(speaker.getDescription());

            speakersContainer.addView(childView);
        }
    }
}
