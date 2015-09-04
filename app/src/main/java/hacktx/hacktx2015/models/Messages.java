package hacktx.hacktx2015.models;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

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

    public Date getTsDate() {
        SimpleDateFormat formatFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        try {
            return formatFrom.parse(getTs());
        }
        catch (ParseException e) {
            Log.d("MessagesModel", e.getMessage());
            return null;
        }
    }

    public String getFormattedTsString() {
        //format date object to new date
        SimpleDateFormat formatTo = new SimpleDateFormat("MMM dd, hh:mma", Locale.US);
        return formatTo.format(getTsDate());
    }

    public static Comparator<Messages> MessagesComparator
            = new Comparator<Messages>() {

        public int compare(Messages message1, Messages message2) {
            return message2.getTsDate().compareTo(message1.getTsDate());
        }

    };

}
