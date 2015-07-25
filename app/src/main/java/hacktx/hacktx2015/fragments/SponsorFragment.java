package hacktx.hacktx2015.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hacktx.hacktx2015.R;

/**
 * Created by Drew on 7/22/2015.
 */
public class SponsorFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_sponsors, container, false);

        setupToolbar((Toolbar) root.findViewById(R.id.toolbar));

        return root;
    }
}
