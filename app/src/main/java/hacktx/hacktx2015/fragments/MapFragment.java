package hacktx.hacktx2015.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import hacktx.hacktx2015.R;

/**
 * Created by Drew on 7/22/2015.
 */
public class MapFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);

        setupToolbar((Toolbar) root.findViewById(R.id.toolbar));
        setupMapButtons(root);

        return root;
    }

    private void setupMapButtons(ViewGroup root) {
        final ImageView floorImage = (ImageView) root.findViewById(R.id.mapImage);

        final Button floor1 = (Button) root.findViewById(R.id.button1);
        final Button floor2 = (Button) root.findViewById(R.id.button2);
        final Button floor3 = (Button) root.findViewById(R.id.button3);

        floor2.setBackgroundColor(getResources().getColor(R.color.tw__medium_gray));

        floor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floorImage.setImageResource(R.drawable.sac_first_level);
                floor1.setBackgroundColor(getResources().getColor(R.color.tw__medium_gray));
                floor2.setBackgroundColor(getResources().getColor(R.color.tw__light_gray));
                floor3.setBackgroundColor(getResources().getColor(R.color.tw__light_gray));
            }
        });
        floor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floorImage.setImageResource(R.drawable.sac_second_level);
                floor1.setBackgroundColor(getResources().getColor(R.color.tw__light_gray));
                floor2.setBackgroundColor(getResources().getColor(R.color.tw__medium_gray));
                floor3.setBackgroundColor(getResources().getColor(R.color.tw__light_gray));
            }
        });
        floor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floorImage.setImageResource(R.drawable.sac_third_level);
                floor1.setBackgroundColor(getResources().getColor(R.color.tw__light_gray));
                floor2.setBackgroundColor(getResources().getColor(R.color.tw__light_gray));
                floor3.setBackgroundColor(getResources().getColor(R.color.tw__medium_gray));
            }
        });
    }
}
