package hacktx.hacktx2015.network;

import hacktx.hacktx2015.network.services.HackTxService;
import retrofit.RestAdapter;

/**
 * Created by Drew on 7/25/15.
 */
public class HackTxClient {
    private static final String HACKTX_BASE_URL = "http://hacktx.getsandbox.com/";
    private HackTxService mHackTxService;

    private static HackTxClient instance = null;

    public static HackTxClient getInstance() {
        if(instance == null) {
            instance = new HackTxClient();
        }
        return instance;
    }

    protected HackTxClient() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(HACKTX_BASE_URL)
                .build();

        mHackTxService = restAdapter.create(HackTxService.class);
    }

    public HackTxService getApiService() {
        return mHackTxService;
    }
}
