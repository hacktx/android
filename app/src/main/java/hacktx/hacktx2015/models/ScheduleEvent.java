package hacktx.hacktx2015.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hacktx.hacktx2015.enums.EventType;

/**
 * Created by Drew on 6/27/15.
 */
public class ScheduleEvent {
    private int id;
    private EventType type;
    private String name;
    //2001-07-04 12:08:56
    private Calendar startTime;
    private Calendar endTime;
    private String location;
    private String description;
    private ArrayList<ScheduleSpeaker> speakerList;

    public ScheduleEvent(int id, EventType type, String name, Date startTime, Date endTime, String location, String description, ArrayList<ScheduleSpeaker> speakerList) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.startTime = Calendar.getInstance();
        this.startTime.setTime(startTime);
        this.endTime = Calendar.getInstance();
        this.endTime.setTime(endTime);
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

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
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
        String startAmPm = "", endAmPm = "";
        if(getStartTime().get(Calendar.AM_PM) != getEndTime().get(Calendar.AM_PM)) {
            startAmPm = (getStartTime().get(Calendar.AM_PM) == 0) ? " AM" : " PM";
            endAmPm = (getEndTime().get(Calendar.AM_PM) == 0) ? " AM" : " PM";
        }

        return String.format("%01d:%02d", getStartTime().get(Calendar.HOUR), getStartTime().get(Calendar.MINUTE))
                + startAmPm + " - "
                + String.format("%01d:%02d", getEndTime().get(Calendar.HOUR), getEndTime().get(Calendar.MINUTE))
                + endAmPm + " | " + getLocation();
    }
}
