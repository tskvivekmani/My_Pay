package com.example.dell.mypay;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by dell on 21-07-2016.
 */
public class CartAccess extends AsyncTask<Void,Void,Boolean> {

    private Context context;
    private int shopId;
    private static final String[] CART_COLUMNS = {
            MyPayContract.CartEntry.TABLE_NAME+"."+MyPayContract.CartEntry._ID,
            MyPayContract.CartEntry.COLUMN_CART_ID,
            MyPayContract.CartEntry.COLUMN_C_ITEM_NAME,
            MyPayContract.CartEntry.COLUMN_C_ITEM_DESC,
            MyPayContract.CartEntry.COLUMN_C_ITEM_LOGO,
            MyPayContract.CartEntry.COLUMN_C_ITEM_PRICE,
            MyPayContract.CartEntry.COLUMN_C_ITEM_PRICE_UNIT,
            MyPayContract.CartEntry.COLUMN_C_QUANTITY
    };

    public CartAccess(Context context,int sid) {
        super();
        this.context=context;
        shopId=sid;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Cursor cursor=context.getContentResolver().query(
                MyPayContract.CartEntry.CONTENT_URI,
                CART_COLUMNS,
                null,null,null);
        if(cursor.moveToFirst())
            return true;
        else return false;

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean) {
            Intent intent = new Intent(context, CartActivity.class)
                    .setData(MyPayContract.ShopsEntry.buildShopWithShopId(shopId));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        else
            Toast.makeText(context, "Cart is empty", Toast.LENGTH_LONG).show();

    }
}
