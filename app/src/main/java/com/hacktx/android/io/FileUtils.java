/*
 * Copyright 2017 HackTX.
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

package com.hacktx.android.io;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.hacktx.android.models.ScheduleCluster;

public class FileUtils {

    /**
     * Saves schedule cache to disk.
     *
     * @param context Context by which to save cache.
     * @param day Unique identifier for cache.
     * @param data Data to save to cache.
     */
    public static void setScheduleCache(Context context, int day, ArrayList<ScheduleCluster> data) {
        String dataStr = new Gson().toJson(data);

        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput("schedule-" + day + ".json", Context.MODE_PRIVATE);
            outputStream.write(dataStr.getBytes());
            outputStream.close();

            UserStateStore.setScheduleLastUpdated(context, day, System.currentTimeMillis());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads schedule cache from disk.
     *
     * @param context Context by which to read cache.
     * @param day Unique identifier for cache.
     * @return List of <code>ScheduleCluster</code> read from cache.
     */
    public static ArrayList<ScheduleCluster> getScheduleCache(Context context, int day) {
        ArrayList<ScheduleCluster> result = new ArrayList<>();
        String cache = "";

        try {
            InputStream inputStream = context.openFileInput("schedule-" + day + ".json");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                cache = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!cache.isEmpty()) {
            result = new Gson().fromJson(cache, new TypeToken<ArrayList<ScheduleCluster>>(){}.getType());
        }

        return result;
    }
}
