package hacktx.hacktx2015.network;

import java.util.List;

import hacktx.hacktx2015.models.Channels;
import hacktx.hacktx2015.models.Messages;
import hacktx.hacktx2015.models.SlackChannelResponse;
import hacktx.hacktx2015.models.SlackMessageResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by britne on 7/10/15.
 */
public interface RetrofitApi {

    @GET("/channels.list")
    void getChannelsList(@Query("token") String apiToken, Callback<SlackChannelResponse> callback);

    @GET("/channels.history")
    void getMessages(@Query("token") String apiToken, @Query("channel") String channel,
                     Callback<SlackMessageResponse> callback);
}
