package hacktx.hacktx2015.activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.estimote.sdk.cloud.internal.User;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import hacktx.hacktx2015.BuildConfig;
import hacktx.hacktx2015.HackTXApplication;
import hacktx.hacktx2015.R;
import hacktx.hacktx2015.fragments.MapFragment;
import hacktx.hacktx2015.models.EventFeedback;
import hacktx.hacktx2015.models.ScheduleEvent;
import hacktx.hacktx2015.models.ScheduleSpeaker;
import hacktx.hacktx2015.network.HackTxClient;
import hacktx.hacktx2015.network.UserStateStore;
import hacktx.hacktx2015.network.services.HackTxService;
import hacktx.hacktx2015.utils.AlphaSatColorMatrixEvaluator;
import hacktx.hacktx2015.views.CircularImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EventDetailActivity extends AppCompatActivity {

    private Tracker mTracker;
    private CollapsingToolbarLayout collapsingToolbar;
    private ScheduleEvent event;
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            if(from == Picasso.LoadedFrom.MEMORY) {
                ((ImageView) findViewById(R.id.header)).setImageBitmap(bitmap);
            } else {
                ImageView imageView = (ImageView) findViewById(R.id.header);
                final BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
                imageView.setImageDrawable(drawable);
                AlphaSatColorMatrixEvaluator evaluator = new AlphaSatColorMatrixEvaluator();
                final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(evaluator.getColorMatrix());
                drawable.setColorFilter(filter);

                ObjectAnimator animator = ObjectAnimator.ofObject(filter, "colorMatrix", evaluator, evaluator.getColorMatrix());

                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        drawable.setColorFilter(filter);
                    }
                });
                animator.setDuration(1500);
                animator.start();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        setupGoogleAnalyticsTracker();

        setupEventData(getIntent().getExtras().getString("eventData"));
        setupToolbar((Toolbar) findViewById(R.id.toolbar));
        setupTaskActivityInfo();
        setupCards();
        setupEventDetails();
        setupEventDescription();
        setupSpeakers();
    }

    private void setupGoogleAnalyticsTracker() {
        // Obtain the shared Tracker instance.
        HackTXApplication application = (HackTXApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Screen~" + "EventDetail-id:" + event.getId());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
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
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("open", "maps");
                intent.putExtra("building", (event.getLocation().getBuilding().equalsIgnoreCase("CLA")) ? MapFragment.CLA : MapFragment.SAC);
                intent.putExtra("level", Integer.parseInt(event.getLocation().getLevel()));
                startActivity(intent);
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
            event = new Gson().fromJson(eventData, new TypeToken<ScheduleEvent>() {}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupToolbar(Toolbar toolbar) {
        Picasso.with(this).load(event.getImageUrl()).placeholder(R.color.primary).into(target);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle(event.getName());
    }

    private void setupTaskActivityInfo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String appName = getString(R.string.app_name);
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            int color = ContextCompat.getColor(this, R.color.taskbarColor);
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(appName, icon, color);
            setTaskDescription(taskDesc);
        }
    }

    private void setupCards() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Calendar now = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        final CardView rateEventCard = (CardView) findViewById(R.id.rateEventCard);

        View.OnClickListener feedbackOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!UserStateStore.getFeedbackSubmitted(EventDetailActivity.this, event.getId())) {
                    final Dialog dialog = new Dialog(EventDetailActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_feedback);
                    WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                    dialog.getWindow().setAttributes(params);
                    dialog.show();

                    final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.feedbackDialogRatingBar);
                    dialog.findViewById(R.id.feedbackDialogCancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.findViewById(R.id.feedbackDialogSubmit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HackTxService hackTxService = HackTxClient.getInstance().getApiService();
                            hackTxService.sendFeedback(event.getId(), (int) ratingBar.getRating(), new Callback<EventFeedback>() {
                                @Override
                                public void success(EventFeedback feedback, Response response) {
                                    UserStateStore.setFeedbackSubmitted(EventDetailActivity.this, event.getId(), true);
                                    findViewById(R.id.rateEventCard).setVisibility(View.GONE);
                                    dialog.dismiss();
                                    Snackbar.make(findViewById(android.R.id.content), R.string.event_feedback_submitted, Snackbar.LENGTH_SHORT).show();
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.e("EventDetailActivity", "Error when submitting feedback: " + error.getMessage());
                                    dialog.dismiss();
                                    Snackbar.make(findViewById(android.R.id.content), R.string.event_feedback_failed, Snackbar.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else {
                    Snackbar.make(findViewById(android.R.id.content), R.string.event_feedback_already_submitted, Snackbar.LENGTH_SHORT).show();
                }
            }
        };

        try {
            end.setTime(formatter.parse(event.getEndDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(!end.before(now) && !shouldShowFeedbackCard()) {
            findViewById(R.id.rateEventCard).setVisibility(View.GONE);
        } else {
            findViewById(R.id.rateEventCardOk).setOnClickListener(feedbackOnClickListener);

            findViewById(R.id.rateEventCardNoThanks).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserStateStore.setFeedbackIgnored(EventDetailActivity.this, event.getId(), true);
                    rateEventCard.setVisibility(View.GONE);
                }
            });
        }

        if(!BuildConfig.IN_APP_FEEDBACK) {
            findViewById(R.id.rateEventCard).setVisibility(View.GONE);
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

        ((TextView) findViewById(R.id.eventLocation)).setText(event.getLocation().getLocationDetails());
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

    private boolean shouldShowFeedbackCard() {
        return !UserStateStore.getFeedbackIgnored(this, event.getId()) && !UserStateStore.getFeedbackSubmitted(this, event.getId());
    }
}
