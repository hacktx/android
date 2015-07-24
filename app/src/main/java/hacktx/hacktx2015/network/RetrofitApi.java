package hacktx.hacktx2015.network;

import hacktx.hacktx2015.models.AnnouncementResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by britne on 7/10/15.
 */
public interface RetrofitApi {

    @GET("/hacktx message something")
    void getMessages(@Query("token") String apiToken, Callback<AnnouncementResponse> callback);
}
