package hacktx.hacktx2015.models;

import java.util.ArrayList;
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
    private Date startTime;
    private Date endTime;
    private String location;
    private String description;
    private ArrayList<ScheduleSpeaker> speakerList;

    public ScheduleEvent(int id, EventType type, String name, Date startTime, Date endTime, String location, String description, ArrayList<ScheduleSpeaker> speakerList) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
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
        return "" + (getStartTime().getHours()) + ":" + (getStartTime().getMinutes()) +  " - " + (getEndTime().getHours()) + ":" + (getEndTime().getMinutes()) +  " | " + getLocation();
    }
}
