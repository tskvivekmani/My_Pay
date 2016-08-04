package com.example.dell.mypay;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.dell.mypay.MyPayContract.ShopsEntry;
import com.example.dell.mypay.MyPayContract.ItemsEntry;
import com.example.dell.mypay.MyPayContract.CardDetails;
import com.example.dell.mypay.MyPayContract.CartEntry;

/**
 * Created by dell on 09-07-2016.
 */
public class MyPayProvider extends ContentProvider {

    private MyPayDbHelper myPayDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final int SHOPS = 100;
    static final int ITEMS = 101;
    static final int CARD_DETAILS = 102;
    static final int CART = 103;
    static final int SHOPS_WITH_DISTANCE = 200;
    static final int ITEMS_WITH_SHOP_ID_BARCODE = 201;
    static final int CARD_WITH_NUMBER = 202;
    static final int SHOPS_WITH_ID = 203;
    static final int ITEMS_WITH_SHOP_ID = 204;
    static final int CART_WITH_ID = 205;

    private static final String sShopsIdSelection = ShopsEntry.TABLE_NAME+"."+
            ShopsEntry.COLUMN_SHOP_ID+" = ? ";
    private static final String sItemShopIdBarSelection = ItemsEntry.TABLE_NAME+"."+
            ItemsEntry.COLUMN_SHOP_ID_REF+" = ? AND "+
            ItemsEntry.COLUMN_ITEM_BARCODE+" = ? ";
    private static final String sCardNumSelection = CardDetails.TABLE_NAME+"."+
            CardDetails.COLUMN_CARD_NUMBER+" = ? ";
    private static final String sItemWithShopIdSelection = ItemsEntry.TABLE_NAME+"."+
            ItemsEntry.COLUMN_SHOP_ID_REF+" = ? ";
    private static final String sCartWithIdSelection = CartEntry.TABLE_NAME+"."+
            CartEntry.COLUMN_CART_ID+" = ? ";

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MyPayContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,MyPayContract.PATH_SHOPS,SHOPS);
        matcher.addURI(authority,MyPayContract.PATH_ITEMS,ITEMS);
        matcher.addURI(authority,MyPayContract.PATH_CARD_DETAILS,CARD_DETAILS);
        matcher.addURI(authority,MyPayContract.PATH_CART,CART);
        //matcher.addURI(authority,MyPayContract.PATH_SHOPS+"/*",SHOPS_WITH_DISTANCE);
        matcher.addURI(authority,MyPayContract.PATH_ITEMS+"/#/*",ITEMS_WITH_SHOP_ID_BARCODE);
        matcher.addURI(authority,MyPayContract.PATH_CARD_DETAILS+"/#",CARD_WITH_NUMBER);
        matcher.addURI(authority,MyPayContract.PATH_SHOPS+"/#",SHOPS_WITH_ID);
        matcher.addURI(authority,MyPayContract.PATH_ITEMS+"/#",ITEMS_WITH_SHOP_ID);
        matcher.addURI(authority,MyPayContract.PATH_CART+"/#",CART_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        myPayDbHelper= new MyPayDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d("MyPayProvider","inside query");
        Log.d("MyPayProvider","incoming Uri:"+uri);
        Log.d("MyPayProvider"," "+sUriMatcher.match(uri));
        Cursor retCursor=null;
        switch (sUriMatcher.match(uri)){
            case SHOPS:
                Log.d("MyPayProvider","Shops");
                retCursor = myPayDbHelper.getReadableDatabase().query(
                        ShopsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                Log.d("MyPayProvider","retcursor SHOPS :"+retCursor);
                break;
            case ITEMS:
                Log.d("MyPayProvider","Items");
                retCursor = myPayDbHelper.getReadableDatabase().query(
                        ItemsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                Log.d("MyPayProvider","retcursor ITEMS"+retCursor);
                break;
            case CARD_DETAILS:
                retCursor = myPayDbHelper.getReadableDatabase().query(
                        CardDetails.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CART:
                retCursor = myPayDbHelper.getReadableDatabase().query(
                        CartEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case SHOPS_WITH_ID:
                Log.d("MyPayProvider","Shops with Id");
                retCursor = myPayDbHelper.getReadableDatabase().query(
                        ShopsEntry.TABLE_NAME,
                        projection,
                        sShopsIdSelection,
                        new String[]{Integer.toString(ShopsEntry.getShopIdFromUri(uri))},
                        null,
                        null,
                        sortOrder
                );
                Log.d("MyPayProvider","retcursor SHOPS_WITH_ID"+retCursor);
                break;
            case ITEMS_WITH_SHOP_ID:
                Log.d("MyPayProvider","Items with Shop Id");
                retCursor = myPayDbHelper.getReadableDatabase().query(
                        ItemsEntry.TABLE_NAME,
                        projection,
                        sItemWithShopIdSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                Log.d("MyPayProvider","retcursor ITEMS_WITH_SHOP_ID"+retCursor);
                break;
            case ITEMS_WITH_SHOP_ID_BARCODE:
                retCursor = myPayDbHelper.getReadableDatabase().query(
                        ItemsEntry.TABLE_NAME,
                        projection,
                        sItemShopIdBarSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CARD_WITH_NUMBER:
                retCursor = myPayDbHelper.getReadableDatabase().query(
                        CardDetails.TABLE_NAME,
                        projection,
                        sCardNumSelection,
                        new String[]{Long.toString(CardDetails.getCardNumFromUri(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            case CART_WITH_ID:
                retCursor = myPayDbHelper.getReadableDatabase().query(
                        CartEntry.TABLE_NAME,
                        projection,
                        sCartWithIdSelection,
                        new String[]{Integer.toString(CartEntry.getCartIdFromUri(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        Log.d("MyPayProvider","ret cursor:"+retCursor);
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);//to notify contentObserver
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case SHOPS:
                return ShopsEntry.CONTENT_TYPE;
            case SHOPS_WITH_DISTANCE:
                return null;
            case ITEMS:
                return ItemsEntry.CONTENT_TYPE;
            case ITEMS_WITH_SHOP_ID_BARCODE:
                return ItemsEntry.CONTENT_ITEM_TYPE;
            case CARD_DETAILS:
                return CardDetails.CONTENT_TYPE;
            case CARD_WITH_NUMBER:
                return CardDetails.CONTENT_ITEM_TYPE;
            case SHOPS_WITH_ID:
                return ShopsEntry.CONTENT_ITEM_TYPE;
            case ITEMS_WITH_SHOP_ID:
                return ItemsEntry.CONTENT_TYPE;
            case CART:
                return CartEntry.CONTENT_TYPE;
            case CART_WITH_ID:
                return CartEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db=myPayDbHelper.getWritableDatabase();
        Uri retUri = null;
        switch(sUriMatcher.match(uri)) {
            case SHOPS: {
                long id = db.insert(ShopsEntry.TABLE_NAME, null, contentValues);
                if (id > 0)
                    retUri = ShopsEntry.buildShopUri(id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case ITEMS: {
                long id = db.insert(ItemsEntry.TABLE_NAME, null, contentValues);
                if (id > 0)
                    retUri = ItemsEntry.buildItemUri(id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;

            }
            case CARD_DETAILS: {
                long id = db.insert(CardDetails.TABLE_NAME, null, contentValues);
                if (id > 0)
                    retUri = CardDetails.buildCardUri(id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CART:{
                long id = db.insert(CartEntry.TABLE_NAME, null, contentValues);
                if (id > 0)
                    retUri = CartEntry.buildCartUri(id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db=myPayDbHelper.getWritableDatabase();
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (selection ==null)selection="1";
        Log.e("MyPayProvider","uri matcher:"+sUriMatcher.match(uri));
        Log.e("MyPayProvider","incoming Uri:"+uri);
        switch(sUriMatcher.match(uri)) {
            case SHOPS:
                rowsDeleted = db.delete(ShopsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEMS:
                rowsDeleted=db.delete(ItemsEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case CARD_DETAILS:
                rowsDeleted=db.delete(CardDetails.TABLE_NAME,selection,selectionArgs);
                break;
            case CART:
                rowsDeleted=db.delete(CartEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case CART_WITH_ID:
                rowsDeleted=db.delete(
                        CartEntry.TABLE_NAME,
                        sCartWithIdSelection,
                        new String[]{Integer.toString(CartEntry.getCartIdFromUri(uri))});
                Log.e("MyPayProvider","rowsdeleted:"+rowsDeleted);
                Log.e("MyPayProvider","cart id:"+CartEntry.getCartIdFromUri(uri));
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db=myPayDbHelper.getWritableDatabase();
        int rowsUpdated;
        switch(sUriMatcher.match(uri)) {
            case SHOPS:
                rowsUpdated=db.update(ShopsEntry.TABLE_NAME,contentValues,selection,selectionArgs);
                break;
            case ITEMS:
                rowsUpdated=db.update(ItemsEntry.TABLE_NAME,contentValues,selection,selectionArgs);
                break;
            case CARD_DETAILS:
                rowsUpdated=db.update(CardDetails.TABLE_NAME,contentValues,selection,selectionArgs);
                break;
            case CART:
                rowsUpdated=db.update(CartEntry.TABLE_NAME,contentValues,selection,selectionArgs);
                break;
            case CART_WITH_ID:
                rowsUpdated=db.update(CartEntry.TABLE_NAME,
                        contentValues,
                        sCartWithIdSelection,
                        new String[]{Integer.toString(CartEntry.getCartIdFromUri(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
