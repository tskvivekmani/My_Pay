package com.example.dell.mypay;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class ShopService extends IntentService {

    public static final String SHOP_FILTER = "fetch_shops";
    public static final String SHOP_EXTRA = "shop";


    public ShopService() {
        super("ShopService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("ShopService","inside service");

    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */

}
