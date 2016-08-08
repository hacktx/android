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

import android.util.Log;

import com.hacktx.android.HackTXApplication;
import com.hacktx.android.network.services.HackTxService;
import retrofit.RestAdapter;

public class HackTxClient {

    private static final String TAG = getInstance().getClass().getSimpleName();
    private static final String HACKTX_BASE_URL = "https://my.hacktx.com/api/";
    private HackTxService mHackTxService;

    private static HackTxClient instance = null;

    public static HackTxClient getInstance() {
        if(instance == null) {
            instance = new HackTxClient();
        }
        return instance;
    }

    public static void rebuildInstance() {
        Log.d(TAG, "Rebuilding HackTXClient");
        instance = new HackTxClient();
    }

    private HackTxClient() {
        RestAdapter.Builder restAdapterBuilder = new RestAdapter.Builder()
                .setEndpoint(HACKTX_BASE_URL);

        if (UserStateStore.getMockServerEnabled(HackTXApplication.getInstance())) {
            Log.d(TAG, "Mock server enabled, using MockClient.");
            restAdapterBuilder.setClient(new MockClient());
        }

        RestAdapter restAdapter = restAdapterBuilder.build();

        mHackTxService = restAdapter.create(HackTxService.class);
    }

    public HackTxService getApiService() {
        return mHackTxService;
    }
}
