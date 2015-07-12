package hacktx.hacktx2015.models;

/**
 * Created by britne on 7/12/15.
 */
public class Channels {

    private String id;
    private String name;

    public Channels(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
