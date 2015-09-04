package hacktx.hacktx2015.models;

public class EventLocation {

    private String building;
    private String level;
    private String room;

    public EventLocation(String building, String level, String room) {
        this.building = building;
        this.level = level;
        this.room = room;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getLocationDetails() {
        return building + " " + room;
    }

}
