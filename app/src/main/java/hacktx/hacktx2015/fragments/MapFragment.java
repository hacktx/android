package hacktx.hacktx2015.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import hacktx.hacktx2015.R;

/**
 * Created by Drew on 7/22/2015.
 */
public class MapFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);

        setupMapButtons(root);

        return root;
    }

    private void setupMapButtons(ViewGroup root) {
        final ImageView floorImage = (ImageView) root.findViewById(R.id.mapImage);

        Button floor1 = (Button) root.findViewById(R.id.button1);
        floor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floorImage.setImageResource(R.drawable.sac_first_level);
            }
        });
        Button floor2 = (Button) root.findViewById(R.id.button2);
        floor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floorImage.setImageResource(R.drawable.sac_second_level);
            }
        });
        Button floor3 = (Button) root.findViewById(R.id.button3);
        floor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floorImage.setImageResource(R.drawable.sac_third_level);
            }
        });
    }
}
