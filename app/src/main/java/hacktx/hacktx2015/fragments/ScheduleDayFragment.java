package hacktx.hacktx2015.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hacktx.hacktx2015.R;

/**
 * Created by Drew on 6/28/15.
 */
public class ScheduleDayFragment extends Fragment {

    public ScheduleDayFragment() {}

    public static ScheduleDayFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);

        ScheduleDayFragment fragment = new ScheduleDayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_schedule_day, container, false);
        Log.v("Frag", getArguments().getString("title"));
        TextView textV = (TextView) root.findViewById(R.id.textView);
        textV.setText(getArguments().getString("title"));
        return root;
    }
}
