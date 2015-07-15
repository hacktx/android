package hacktx.hacktx2015.models;

import java.util.ArrayList;

/**
 * Created by Drew on 6/27/15.
 */
public class ScheduleCluster {
    private int id;
    private String name;
    private ArrayList<ScheduleEvent> eventsList;

    public ScheduleCluster(int id, String name, ArrayList<ScheduleEvent> eventsList) {
        this.id = id;
        this.name = name;
        this.eventsList = eventsList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ScheduleEvent> getEventsList() {
        return eventsList;
    }

    public void setEventsList(ArrayList<ScheduleEvent> eventsList) {
        this.eventsList = eventsList;
    }
}
