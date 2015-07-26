package hacktx.hacktx2015.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import hacktx.hacktx2015.enums.EventType;

public class ScheduleEvent {
    private int id;
    private EventType type;
    private String name;
    private String startDate;
    private String endDate;
    private String location;
    private String description;
    private ArrayList<ScheduleSpeaker> speakerList;

    public ScheduleEvent(int id, EventType type, String name, String startDate, String endDate, String location, String description, ArrayList<ScheduleSpeaker> speakerList) {
        this.id = id;
        this.type = type;
        this.name = name;
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
        if(start.get(Calendar.AM_PM) != end.get(Calendar.AM_PM)) {
            startAmPm = (start.get(Calendar.AM_PM) == 0) ? " AM" : " PM";
            endAmPm = (end.get(Calendar.AM_PM) == 0) ? " AM" : " PM";
        }

        return String.format("%01d:%02d", start.get(Calendar.HOUR), start.get(Calendar.MINUTE))
                + startAmPm + " - "
                + String.format("%01d:%02d", end.get(Calendar.HOUR), end.get(Calendar.MINUTE))
                + endAmPm + " | " + getLocation();
    }
}
