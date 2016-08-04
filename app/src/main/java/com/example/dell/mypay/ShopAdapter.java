package com.example.dell.mypay;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by dell on 15-07-2016.
 */
public class ShopAdapter extends CursorAdapter {
    public ShopAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.listview_detail,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String shop_name = cursor.getString(cursor.getColumnIndex(
                MyPayContract.ShopsEntry.COLUMN_SHOP_NAME));
        String shop_address = cursor.getString(cursor.getColumnIndex(
                MyPayContract.ShopsEntry.COLUMN_ADDRESS));
        TextView shop = (TextView)view.findViewById(R.id.shop_name_textView);
        TextView address = (TextView)view.findViewById(R.id.shop_address_textView);
        shop.setText(shop_name);
        address.setText(shop_address);

    }
}
