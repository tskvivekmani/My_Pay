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
public class ItemService extends IntentService {

    public static final String ITEM_EXTRA = "ITEMS";


    public ItemService() {
        super("ItemService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("ItemService","inside service");

    }

}
