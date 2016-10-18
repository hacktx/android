/*
 * Copyright 2016 HackTX.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hacktx.android.network.services;

import java.util.ArrayList;

import com.hacktx.android.models.EventFeedback;
import com.hacktx.android.models.Announcement;
import com.hacktx.android.models.ScheduleCluster;
import com.hacktx.android.models.Sponsor;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface HackTxService {

    @GET("/schedule/{day}")
    ArrayList<ScheduleCluster> getScheduleDayData(@Path("day") int day);

    @GET("/announcements")
    void getAnnouncements(Callback<ArrayList<Announcement>> messagesList);

    @GET("/partners")
    void getSponsors(Callback<ArrayList<Sponsor>> sponsorsList);

    @FormUrlEncoded
    @POST("/feedback")
    void sendFeedback(@Field("id") int id, @Field("rating") int rating, Callback<EventFeedback> cb);

}
