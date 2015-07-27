package hacktx.hacktx2015.network.services;

import java.util.ArrayList;

import hacktx.hacktx2015.models.AnnouncementResponse;
import hacktx.hacktx2015.models.Messages;
import hacktx.hacktx2015.models.ScheduleCluster;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Drew on 7/25/15.
 */
public interface HackTxService {

    /**
     *   SCHEDULE
     */
    @GET("/schedule/{day}")
    ArrayList<ScheduleCluster> getScheduleDayData(@Path("day") int day);

    @GET("/announcements")
    ArrayList<Messages> getMessages();


}
