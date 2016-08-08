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

package com.hacktx.android.network;

import android.net.Uri;
import android.util.Log;

import com.hacktx.android.HackTXApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class MockClient implements Client {

    private final String TAG = getClass().getSimpleName();

    @Override
    public Response execute(Request request) throws IOException {
        Uri uri = Uri.parse(request.getUrl());

        Log.d(TAG, "Request URI: " + uri.toString());

        String fileName = "";

        if(uri.getPath().contains("/schedule")) {
            fileName = "schedule.json";
        } else if(uri.getPath().contains("/sponsors")) {
            fileName = "partners.json";
        } else if(uri.getPath().contains("/announcements")) {
            fileName = "announcements.json";
        } else if (request.getMethod().equals("GET")) {
            Log.d(TAG, "No mock found for " + uri.getPath());
            return new Response(request.getUrl(), 404, "Mock not found.", Collections.EMPTY_LIST, null);
        } else {
            Log.d(TAG, "Mocked 200 OK for " + uri.getPath());
            return new Response(request.getUrl(), 200, "OK", Collections.EMPTY_LIST, null);
        }

        Log.d(TAG, "Response mock: " + fileName);

        InputStream is = HackTXApplication.getInstance().getAssets().open("mocks/" + fileName.toLowerCase());
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String responseString = new String(buffer);

        return new Response(request.getUrl(), 200, "nothing", Collections.EMPTY_LIST, new TypedByteArray("application/json", responseString.getBytes()));
    }
}