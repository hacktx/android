package hacktx.hacktx2015.activities;

import android.app.ActivityManager;
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

        Picasso.with(this).load("http://i.imgur.com/I5qI3BC.jpg").into((ImageView) findViewById(R.id.header));

        setupEventData(getIntent().getExtras().getString("eventData"));
        setupToolbar((Toolbar) findViewById(R.id.toolbar));
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
            case R.id.action_settings:
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
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ((ImageView) findViewById(R.id.header)).setImageBitmap(bitmap);
                setupPalette(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(this).load("http://i.imgur.com/I5qI3BC.jpg").into(target);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle(event.getName());
    }

    private void setupPalette(Bitmap bitmap) {
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                collapsingToolbar.setContentScrimColor(palette.getMutedColor(R.attr.colorPrimary));
                collapsingToolbar.setStatusBarScrimColor(palette.getDarkMutedColor(R.color.primaryDark));
                ((Button) findViewById(R.id.rateEventCardOk)).setTextColor(palette.getMutedColor(R.color.primaryDark));
                ((TextView) findViewById(R.id.speakersTitle)).setTextColor(palette.getDarkMutedColor(R.color.primaryDark));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    findViewById(R.id.fab).setBackgroundTintList(ColorStateList.valueOf(palette.getVibrantColor(R.color.accent)));

                    String appName = getString(R.string.app_name);
                    Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    int color = palette.getDarkMutedColor(R.color.primaryDark);
                    ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(appName, icon, color);
                    setTaskDescription(taskDesc);
                }
            }
        });
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
            case TALK: eventIconId = R.drawable.ic_event; break;
            case EDUCATION: eventIconId = R.drawable.ic_education; break;
            case FOOD: eventIconId = R.drawable.ic_food; break;
            default: eventIconId = R.drawable.ic_event; break;
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

        for(int child = 0; child < speakers.size(); child++) {
            final ScheduleSpeaker speaker = speakers.get(child);
            View childView = LayoutInflater.from(speakersContainer.getContext())
                    .inflate(R.layout.event_detail_speaker, speakersContainer, false);

            CircularImageView speakerIcon = (CircularImageView) childView.findViewById(R.id.speakerIcon);
            Picasso.with(this).load(speaker.getImageUrl()).resize(150, 150).centerCrop().into(speakerIcon);
            ((TextView) childView.findViewById(R.id.speakerTitle)).setText(speaker.getName()
                    + " | " + speaker.getOrganization());
            ((TextView) childView.findViewById(R.id.speakerDescription)).setText(speaker.getDescription());

            speakersContainer.addView(childView);
        }
    }
}
