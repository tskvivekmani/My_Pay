package com.example.dell.mypay;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.dell.mypay.MyPayContract.CartEntry;

/**
 * Created by dell on 17-07-2016.
 */
public class CartAdapter extends CursorAdapter {

    CustomButtonListener customListener;
    private Context con;

    public CartAdapter(Context context, Cursor c, int flags) {

        super(context, c, flags);
        con=context;
    }

    public interface CustomButtonListener {
        public void onButtonClickListner(int cid);
    }

    public void setCustomButtonListner(CustomButtonListener listener) {
        this.customListener = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.cart_items_detail,viewGroup,false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        ImageView logo = (ImageView)view.findViewById(R.id.item_logo_image);
        TextView name = (TextView)view.findViewById(R.id.item_name_text);
        TextView desc = (TextView)view.findViewById(R.id.item_desc_text);
        TextView price = (TextView)view.findViewById(R.id.item_price_text);
        TextView quan = (TextView)view.findViewById(R.id.item_quantity);
        ImageButton remove = (ImageButton)view.findViewById(R.id.item_remove_icon);
        String item_logo = cursor.getString(cursor.getColumnIndex(CartEntry.COLUMN_C_ITEM_LOGO));
        String item_name = cursor.getString(cursor.getColumnIndex(CartEntry.COLUMN_C_ITEM_NAME));
        String item_desc = cursor.getString(cursor.getColumnIndex(CartEntry.COLUMN_C_ITEM_DESC));
        Double item_price = cursor.getDouble(cursor.getColumnIndex(CartEntry.COLUMN_C_ITEM_PRICE));
        String item_p_unit = cursor.getString(cursor.getColumnIndex(
                CartEntry.COLUMN_C_ITEM_PRICE_UNIT));
        int quantity = cursor.getInt(cursor.getColumnIndex(CartEntry.COLUMN_C_QUANTITY));
        final int cid = cursor.getInt(cursor.getColumnIndex(CartEntry.COLUMN_CART_ID));
        name.setText(item_name);
        desc.setText(item_desc);
        price.setText(item_p_unit+item_price);
        remove.setImageResource(R.drawable.delte);
        logo.setImageResource(R.drawable.jeans);
        quan.setText("Quantity : "+quantity);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customListener != null) {
                    customListener.onButtonClickListner(cid);
                }
            }
        });
    }
}
