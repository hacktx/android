package hacktx.hacktx2015.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import hacktx.hacktx2015.R;
import hacktx.hacktx2015.enums.EventType;
import hacktx.hacktx2015.models.ScheduleCluster;
import hacktx.hacktx2015.models.ScheduleEvent;
import hacktx.hacktx2015.models.ScheduleSpeaker;
import hacktx.hacktx2015.views.adapters.ScheduleClusterRecyclerView;

/**
 * Created by Drew on 6/28/15.
 */
public class ScheduleDayFragment extends Fragment {

    private ArrayList<ScheduleCluster> scheduleList;
    RecyclerView recyclerView;

    public ScheduleDayFragment() {}

    public static ScheduleDayFragment newInstance(String request) {

        Bundle args = new Bundle();
        args.putString("request", request);

        ScheduleDayFragment fragment = new ScheduleDayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_schedule_day, container, false);
        scheduleList = new ArrayList<>();
        getFakeData();

        recyclerView = (RecyclerView) root.findViewById(R.id.scheduleRecyclerView);
        RecyclerView.Adapter scheduleAdapter = new ScheduleClusterRecyclerView(scheduleList);
        recyclerView.setAdapter(scheduleAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        return root;
    }

    private void getFakeData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        ScheduleSpeaker batman = new ScheduleSpeaker(0,
                "Batman",
                "Justice Inc",
                "Hero can be anyone. Even a man knowing something as simple and reassuring as putting a coat around a young boy shoulders to let him know the world hadn't ended.",
                "http://cdn.bgr.com/2015/04/batman-v-superman-trailer.png");
        ScheduleSpeaker superman = new ScheduleSpeaker(1,
                "Superman",
                "Peace Inc",
                "You will travel far, my little Kal-El. But we will never leave you... even in the face of our death. The richness of our lives shall be yours. All that I have, all that I've learned, everything I feel... all this, and more, I... I bequeath you, my son. You will carry me inside you, all the days of your life. You will make my strength your own, and see my life through your eyes, as your life will be seen through mine. The son becomes the father, and the father the son. This is all I... all I can send you, Kal-El.",
                "http://1.bp.blogspot.com/-w9vnmSGyhBU/VRL-ioqskHI/AAAAAAAAATA/WpM7LR8Pg1k/s1600/superman01.png");

        ArrayList<ScheduleSpeaker> batmanList = new ArrayList<>();
        batmanList.add(batman);
        ArrayList<ScheduleSpeaker> supermanList = new ArrayList<>();
        supermanList.add(superman);
        ArrayList<ScheduleSpeaker> superAndBatList = new ArrayList<>();
        batmanList.add(batman);
        supermanList.add(superman);

        try {
            ScheduleEvent batman0 = new ScheduleEvent(0, EventType.TALK, "BatScript: The real dev language",
                    formatter.parse("2015-09-26 08:00:00"),
                    formatter.parse("2015-09-26 09:00:00"),
                    "SAC Mainroom", "We be talkin about stuff", batmanList);
            ScheduleEvent batman1 = new ScheduleEvent(1, EventType.EDUCATION, "BatPython: The gangsta dev language",
                    formatter.parse("2015-09-26 08:00:00"),
                    formatter.parse("2015-09-26 09:00:00"),
                    "Blue Room", "We be talkin about stuff", batmanList);
            ArrayList<ScheduleEvent> firstList = new ArrayList<>();
            firstList.add(batman0);
            firstList.add(batman1);
            ScheduleCluster firstCluster = new ScheduleCluster(0, "8:00 AM", firstList);

            ScheduleEvent superman0 = new ScheduleEvent(2, EventType.FOOD, "SuperJava: The caffinated dev language",
                    formatter.parse("2015-09-26 09:00:00"),
                    formatter.parse("2015-09-26 10:00:00"),
                    "Taco Room", "We be talkin about stuff", supermanList);
            ScheduleEvent superbat0 = new ScheduleEvent(3, EventType.FOOD, "V super much Bat: Food for all",
                    formatter.parse("2015-09-26 09:00:00"),
                    formatter.parse("2015-09-26 13:00:00"),
                    "SAC Mainroom", "We be talkin about stuff", superAndBatList);

            ArrayList<ScheduleEvent> secondList = new ArrayList<>();
            secondList.add(superman0);
            secondList.add(superbat0);
            ScheduleCluster secondCluster = new ScheduleCluster(0, "9:00 AM", secondList);

            scheduleList.add(firstCluster);
            scheduleList.add(secondCluster);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
