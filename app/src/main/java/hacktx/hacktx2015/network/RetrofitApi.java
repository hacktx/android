package hacktx.hacktx2015.network;

import java.util.List;

import hacktx.hacktx2015.models.Channels;
import hacktx.hacktx2015.models.Messages;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by britne on 7/10/15.
 */
public interface RetrofitApi {

    @GET("/channels.list")
    List<Channels> getChannelsList(@Query("token") String apiToken, Callback<Channels> callback);

    @GET("/channels.history")
    List<Messages> getMessages(@Query("token") String apiToken, @Query("channel") String channel,
                     Callback<Messages> callback);
}
