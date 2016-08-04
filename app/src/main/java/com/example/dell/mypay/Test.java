package com.example.dell.mypay;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import com.example.dell.mypay.MyPayContract.ShopsEntry;
import com.example.dell.mypay.MyPayContract.ItemsEntry;
import com.example.dell.mypay.MyPayContract.CardDetails;
import com.example.dell.mypay.MyPayContract.CartEntry;

/**
 * Created by dell on 17-07-2016.
 */
public class Test {

    private Context context;

    public Test(Context context){
        this.context=context;
    }
    public void testExecute() {
        ContentValues values = new ContentValues();
        values.put(ShopsEntry.COLUMN_SHOP_ID,100);
        values.put(ShopsEntry.COLUMN_SHOP_NAME,"Turtle");
        values.put(ShopsEntry.COLUMN_ADDRESS,"22,Central Park,Kolkata");
        values.put(ShopsEntry.COLUMN_LATITUDE,"22.31");
        values.put(ShopsEntry.COLUMN_LONGITUDE,"23,67");
        values.put(ShopsEntry.COLUMN_SHOP_API,"www.turtle.com/inventory");
        Uri iuri=context.getContentResolver().insert(ShopsEntry.CONTENT_URI,values);

        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemsEntry.COLUMN_ITEM_ID,10);
        contentValues.put(ItemsEntry.COLUMN_SHOP_ID_REF,100);
        contentValues.put(ItemsEntry.COLUMN_ITEM_NAME,"Jeans");
        contentValues.put(ItemsEntry.COLUMN_ITEM_DESC,"Slim fit jeans");
        contentValues.put(ItemsEntry.COLUMN_ITEM_LOGO,"logo");
        contentValues.put(ItemsEntry.COLUMN_ITEM_PRICE,1500.00);
        contentValues.put(ItemsEntry.COLUMN_ITEM_PRICE_UNIT,"Rs");
        contentValues.put(ItemsEntry.COLUMN_ITEM_BARCODE,"036000291452");
        Uri u = context.getContentResolver().insert(ItemsEntry.CONTENT_URI,contentValues);

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(ItemsEntry.COLUMN_ITEM_ID,11);
        contentValues1.put(ItemsEntry.COLUMN_SHOP_ID_REF,100);
        contentValues1.put(ItemsEntry.COLUMN_ITEM_NAME,"Trousers");
        contentValues1.put(ItemsEntry.COLUMN_ITEM_DESC,"Slim fit trousers");
        contentValues1.put(ItemsEntry.COLUMN_ITEM_LOGO,"logo");
        contentValues1.put(ItemsEntry.COLUMN_ITEM_PRICE,2500.00);
        contentValues1.put(ItemsEntry.COLUMN_ITEM_PRICE_UNIT,"Rs");
        contentValues1.put(ItemsEntry.COLUMN_ITEM_BARCODE,"3245456345344");
        Uri u1 = context.getContentResolver().insert(ItemsEntry.CONTENT_URI,contentValues1);

        Cursor c = context.getContentResolver().query(ShopsEntry.CONTENT_URI,null,null,null,null);
        Cursor c1 = context.getContentResolver().query(ItemsEntry.CONTENT_URI,null,null,null,null);
        if (c.moveToFirst() && c1.moveToFirst())
            Toast.makeText(context,"Values inserted",Toast.LENGTH_SHORT).show();




    }


}
