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

package com.hacktx.android.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hacktx.android.Constants;
import com.hacktx.android.R;
import com.hacktx.android.utils.ConfigParam;

public class GoogleMapFragment extends BaseFragment implements OnMapReadyCallback {

    private SupportMapFragment mMapFragment;
    private GoogleMap mGoogleMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_google_map, container, false);

        setHasOptionsMenu(true);

        setupToolbar((Toolbar) root.findViewById(R.id.toolbar), R.string.fragment_maps_title);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(30.268915, -97.740378))
                .zoom(19)
                .build();

        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .camera(cameraPosition);

        mMapFragment = SupportMapFragment.newInstance(options);
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, mMapFragment);
        fragmentTransaction.commit();

        mMapFragment.getMapAsync(this);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_google_maps, menu);

        if (!mConfigManager.getValue(ConfigParam.REMOTE_MAP)) {
            menu.removeItem(R.id.remote_map);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset_map:
                mMetricsManager.logEvent(R.string.analytics_event_reset_map, null);
                if (mGoogleMap != null) {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(30.268915, -97.740378))
                            .zoom(19)
                            .build();
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                return true;
            case R.id.remote_map:
                mMetricsManager.logEvent(R.string.analytics_event_remote_map, null);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.REMOTE_MAP_URL));
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        mGoogleMap = map;

        mGoogleMap.setOnIndoorStateChangeListener(new GoogleMap.OnIndoorStateChangeListener() {
            @Override
            public void onIndoorBuildingFocused() {

            }

            @Override
            public void onIndoorLevelActivated(IndoorBuilding indoorBuilding) {
                Bundle b = new Bundle();
                b.putInt(getString(R.string.analytics_param_floor), indoorBuilding.getActiveLevelIndex());
                mMetricsManager.logEvent(R.string.analytics_event_floor_change, b);
                applyMarkers(indoorBuilding.getActiveLevelIndex());
            }
        });

        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(30.268915, -97.740378))
                .title(getString(R.string.event_name)));
    }

    private void applyMarkers(int floor) {
        if (mGoogleMap == null) {
            return;
        }

        switch (floor) {
            case 2: // First floor
                mGoogleMap.clear();
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(30.268915, -97.740378))
                        .title(getString(R.string.event_name)));
                return;
            case 1: // Second floor
                mGoogleMap.clear();
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(30.26864, -97.74022))
                        .title(getString(R.string.fragment_map_capital_ballroom)));

                mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(30.2688, -97.74064))
                        .title(getString(R.string.fragment_map_lone_star)));

                mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(30.268915, -97.74075))
                        .title(getString(R.string.fragment_map_austin)));
                return;
            case 0: // Third floor
                mGoogleMap.clear();
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(30.2688, -97.74064))
                        .title(getString(R.string.fragment_map_longhorn)));
                return;
            default: mGoogleMap.clear();
        }
    }
}
