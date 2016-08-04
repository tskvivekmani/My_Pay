package com.example.dell.mypay;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.dell.mypay.MyPayContract.ShopsEntry;
import com.example.dell.mypay.MyPayContract.ItemsEntry;
import com.example.dell.mypay.MyPayContract.CardDetails;
import com.example.dell.mypay.MyPayContract.CartEntry;

/**
 * Created by dell on 09-07-2016.
 */
public class MyPayDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "mypay.db";

    final String SQL_CREATE_SHOPS_TABLE = "CREATE TABLE " + ShopsEntry.TABLE_NAME + " (" +
            ShopsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ShopsEntry.COLUMN_SHOP_ID + " INTEGER NOT NULL, " +
            ShopsEntry.COLUMN_SHOP_NAME + " TEXT NOT NULL, " +
            ShopsEntry.COLUMN_ADDRESS + " TEXT NOT NULL, " +
            ShopsEntry.COLUMN_LATITUDE + " REAL NOT NULL, " +
            ShopsEntry.COLUMN_LONGITUDE + " REAL NOT NULL, " +
            ShopsEntry.COLUMN_SHOP_API + " TEXT NOT NULL " +
            " );";

    final String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + ItemsEntry.TABLE_NAME + " (" +
            ItemsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ItemsEntry.COLUMN_ITEM_ID + " INTEGER NOT NULL, " +
            ItemsEntry.COLUMN_SHOP_ID_REF + " INTEGER NOT NULL, " +
            ItemsEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, " +
            ItemsEntry.COLUMN_ITEM_DESC + " TEXT NOT NULL, " +
            ItemsEntry.COLUMN_ITEM_LOGO + " TEXT NOT NULL, " +
            ItemsEntry.COLUMN_ITEM_PRICE + " REAL NOT NULL, " +
            ItemsEntry.COLUMN_ITEM_PRICE_UNIT + " TEXT NOT NULL, " +
            ItemsEntry.COLUMN_ITEM_BARCODE + " TEXT NOT NULL, " +
            " FOREIGN KEY (" + ItemsEntry.COLUMN_SHOP_ID_REF + ") REFERENCES " +
            ShopsEntry.TABLE_NAME + " (" + ShopsEntry.COLUMN_SHOP_ID + ") " +
            " );";

    final String SQL_CREATE_CARD_DETAILS_TABLE = "CREATE TABLE " + CardDetails.TABLE_NAME + " (" +
            CardDetails._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CardDetails.COLUMN_CARD_NUMBER + " LONG NOT NULL, " +
            CardDetails.COLUMN_CARD_NAME + " TEXT NOT NULL, " +
            CardDetails.COLUMN_CARD_TYPE + " TEXT NOT NULL, " +
            CardDetails.COLUMN_CARD_BANK + " TEXT NOT NULL, " +
            CardDetails.COLUMN_CARD_EXPIRY_MM + " TEXT NOT NULL, " +
            CardDetails.COLUMN_CARD_EXPIRY_YY + " TEXT NOT NULL " +
            " );";

    final String SQL_CREATE_CART_TABLE = "CREATE TABLE " + CartEntry.TABLE_NAME + " (" +
            CartEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CartEntry.COLUMN_CART_ID + " INTEGER NOT NULL, " +
            CartEntry.COLUMN_C_ITEM_NAME + " TEXT NOT NULL, " +
            CartEntry.COLUMN_C_ITEM_DESC + " TEXT NOT NULL, " +
            CartEntry.COLUMN_C_ITEM_LOGO + " TEXT NOT NULL, " +
            CartEntry.COLUMN_C_ITEM_PRICE + " REAL NOT NULL, " +
            CartEntry.COLUMN_C_ITEM_PRICE_UNIT + " TEXT NOT NULL, " +
            CartEntry.COLUMN_C_QUANTITY+ " INTEGER NOT NULL, "+
            " FOREIGN KEY (" + CartEntry.COLUMN_CART_ID + ") REFERENCES " +
            ItemsEntry.TABLE_NAME + " (" + ItemsEntry.COLUMN_ITEM_ID + ") " +
            " );";

    public MyPayDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_SHOPS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ITEMS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CARD_DETAILS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ShopsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ItemsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CardDetails.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CartEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
