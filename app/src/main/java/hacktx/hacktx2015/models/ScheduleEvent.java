package hacktx.hacktx2015.models;

import java.util.ArrayList;

/**
 * Created by Drew on 6/27/15.
 */
public class ScheduleEvent {
    private int id;
    private String type;
    private String name;
    private String startTime;
    private String endTime;
    private String location;
    private String description;
    private ArrayList<ScheduleSpeaker> speakerList;

    public ScheduleEvent(int id, String type, String name, String startTime, String endTime, String location, String description, ArrayList<ScheduleSpeaker> speakerList) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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
}
