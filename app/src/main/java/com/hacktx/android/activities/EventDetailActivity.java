/*
 * Copyright 2017 HackTX.
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

package com.hacktx.android.activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hacktx.android.R;
import com.hacktx.android.models.EventFeedback;
import com.hacktx.android.models.ScheduleEvent;
import com.hacktx.android.models.ScheduleSpeaker;
import com.hacktx.android.network.HackTxClient;
import com.hacktx.android.network.UserStateStore;
import com.hacktx.android.network.services.HackTxService;
import com.hacktx.android.utils.AlphaSatColorMatrixEvaluator;
import com.hacktx.android.utils.ConfigParam;
import com.hacktx.android.views.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EventDetailActivity extends BaseActivity {

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
                animator.setDuration(250);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_semi_transparent));
        }

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
            case R.id.action_map:
                Bundle b = new Bundle();
                b.putString(getString(R.string.analytics_param_event_name), event.getName());
                mMetricsManager.logEvent(R.string.analytics_event_map, b);
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("open", "maps");
                startActivity(intent);
                return true;
            case R.id.action_share:
                Bundle b2 = new Bundle();
                b2.putString(getString(R.string.analytics_param_event_name), event.getName());
                mMetricsManager.logEvent(R.string.analytics_event_share, b2);
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
        Log.d("EventDetailActivity", "Passed event data: " + eventData);
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

    private void setupCards() {
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
                            mMetricsManager.logEvent(R.string.analytics_event_feedback_cancel, null);
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
                                    mMetricsManager.logEvent(R.string.analytics_event_feedback_submit, null);
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
                    mMetricsManager.logEvent(R.string.analytics_event_feedback_already_submitted, null);
                    Snackbar.make(findViewById(android.R.id.content), R.string.event_feedback_already_submitted, Snackbar.LENGTH_SHORT).show();
                }
            }
        };

        if(shouldShowFeedbackCard()) {
            findViewById(R.id.rateEventCardOk).setOnClickListener(feedbackOnClickListener);

            findViewById(R.id.rateEventCardNoThanks).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMetricsManager.logEvent(R.string.analytics_event_feedback_no_thanks, null);
                    UserStateStore.setFeedbackIgnored(EventDetailActivity.this, event.getId(), true);
                    rateEventCard.setVisibility(View.GONE);
                }
            });
        } else {
            findViewById(R.id.rateEventCard).setVisibility(View.GONE);
        }

        if (!mConfigManager.getValue(ConfigParam.EVENT_FEEDBACK)) {
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

    /**
     * Returns if the feedback card should be displayed given current date and feedback status.
     * @return true if feedback hasn't been submitted, isn't ignored, and the event has ended
     */
    private boolean shouldShowFeedbackCard() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Calendar now = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        try {
            end.setTime(formatter.parse(event.getEndDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return now.after(end) && !UserStateStore.getFeedbackIgnored(this, event.getId()) && !UserStateStore.getFeedbackSubmitted(this, event.getId());
    }
}
