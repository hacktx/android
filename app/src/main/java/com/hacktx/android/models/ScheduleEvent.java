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

package com.hacktx.android.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.hacktx.android.enums.EventType;

public class ScheduleEvent {

    private int id;
    private EventType type;
    private String name;
    private String imageUrl;
    private String startDate;
    private String endDate;
    private EventLocation location;
    private String description;
    private ArrayList<ScheduleSpeaker> speakerList;

    public ScheduleEvent(int id, EventType type, String name, String imageUrl,
                         String startDate, String endDate, EventLocation location,
                         String description, ArrayList<ScheduleSpeaker> speakerList) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.description = description;
        this.speakerList = speakerList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public EventLocation getLocation() {
        return location;
    }

    public void setLocation(EventLocation location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<ScheduleSpeaker> getSpeakerList() {
        return speakerList;
    }

    public void setSpeakerList(ArrayList<ScheduleSpeaker> speakerList) {
        this.speakerList = speakerList;
    }

    public String getEventDetails() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        try {
            start.setTime(formatter.parse(startDate));
            end.setTime(formatter.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return getEventTimes() + " | " + getLocation().getLocationDetails();
    }

    public String getEventTimes() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        try {
            start.setTime(formatter.parse(startDate));
            end.setTime(formatter.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String startAmPm = "", endAmPm = "";
        if (start.get(Calendar.AM_PM) != end.get(Calendar.AM_PM)) {
            startAmPm = (start.get(Calendar.AM_PM) == 0) ? " AM" : " PM";
            endAmPm = (end.get(Calendar.AM_PM) == 0) ? " AM" : " PM";
        }

        int startHourOfDay = (start.get(Calendar.HOUR) == 0) ? 12 : start.get(Calendar.HOUR);
        int endHourOfDay = (end.get(Calendar.HOUR) == 0) ? 12 : end.get(Calendar.HOUR);

        return String.format("%01d:%02d", startHourOfDay, start.get(Calendar.MINUTE))
                + startAmPm + " - "
                + String.format("%01d:%02d", endHourOfDay, end.get(Calendar.MINUTE))
                + endAmPm;
    }
}
