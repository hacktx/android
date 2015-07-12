package hacktx.hacktx2015.models;

/**
 * Created by britne on 7/11/15.
 */
public class Messages {

    private String user;
    private String text;
    private String ts;

    public Messages(String user, String text, String ts) {
        this.user = user;
        this.text = text;
        this.ts = ts;
    }

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public String getTs() {
        return ts;
    }
}
