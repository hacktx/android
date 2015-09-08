package hacktx.hacktx2015.network.services;

import java.util.ArrayList;

import hacktx.hacktx2015.models.EventFeedback;
import hacktx.hacktx2015.models.Messages;
import hacktx.hacktx2015.models.ScheduleCluster;
import hacktx.hacktx2015.models.Sponsors;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Drew on 7/25/15.
 */
public interface HackTxService {

    @GET("/schedule/{day}")
    ArrayList<ScheduleCluster> getScheduleDayData(@Path("day") int day);

    @GET("/announcements")
    void getMessages(Callback<ArrayList<Messages>> messagesList);

    @GET("/sponsors")
    void getSponsors(Callback<ArrayList<Sponsors>> sponsorsList);

    @FormUrlEncoded
    @POST("/feedback")
    void sendFeedback(@Field("id") int id, @Field("rating") int rating, Callback<EventFeedback> cb);

}
