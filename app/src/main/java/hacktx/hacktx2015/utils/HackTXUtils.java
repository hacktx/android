package hacktx.hacktx2015.utils;

import java.util.Calendar;

public class HackTXUtils {

    public static boolean hasHackTxStarted() {
        Calendar now = Calendar.getInstance();
        Calendar hackTx = Calendar.getInstance();
        hackTx.set(Calendar.MONTH, Calendar.SEPTEMBER);
        hackTx.set(Calendar.DAY_OF_MONTH, 25);

        return now.after(hackTx);
    }
}
