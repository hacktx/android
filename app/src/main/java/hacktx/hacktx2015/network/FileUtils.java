package hacktx.hacktx2015.network;

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

import hacktx.hacktx2015.models.ScheduleCluster;

/**
 * Created by Drew on 7/25/15.
 */
public class FileUtils {

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

    public static ArrayList<ScheduleCluster> getScheduleCache(Context context, int day) {
        ArrayList<ScheduleCluster> result = new ArrayList<>();
        String cache = "";

        // Get Schedule Cache
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

        // Put Cache into POJO
        if(!cache.isEmpty()) {
            result = new Gson().fromJson(cache, new TypeToken<ArrayList<ScheduleCluster>>(){}.getType());
        }

        return result;
    }
}
