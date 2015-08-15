package hacktx.hacktx2015.models;

import java.util.Date;

/**
 * Created by britne on 7/11/15.
 */
public class Messages {

    private String text;
    private String ts;

    public Messages(String text, String ts) {
        this.text = text;
        this.ts = ts;
    }

    public String getText() {
        return text;
    }

    public String getTs() {
        return ts;
    }
}
