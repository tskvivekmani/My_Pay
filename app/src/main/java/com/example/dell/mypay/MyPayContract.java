package com.example.dell.mypay;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dell on 08-07-2016.
 */
public class MyPayContract {
    public static final String CONTENT_AUTHORITY = "com.example.dell.mypay";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SHOPS = "SHOPS";
    public static final String PATH_ITEMS = "ITEMS";
    public static final String PATH_CARD_DETAILS = "CARD_DETAILS";
    public static final String PATH_CART = "CART";

    public static final class ShopsEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_SHOPS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SHOPS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SHOPS;

        public static final String TABLE_NAME = "SHOPS";

        public static final String COLUMN_SHOP_ID = "SHOP_ID";

        public static final String COLUMN_SHOP_NAME = "SHOP_NAME";

        public static final String COLUMN_ADDRESS = "ADDRESS";

        public static final String COLUMN_LATITUDE = "LATITUDE";

        public static final String COLUMN_LONGITUDE = "LONGITUDE";

        public static final String COLUMN_SHOP_API = "SHP_API";

        public static Uri buildShopUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildShopWithShopId(int shopId){
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(shopId))
                    .build();
        }

        public static int getShopIdFromUri(Uri uri){
            return Integer.parseInt(uri.getPathSegments().get(1));
        }

    }

    public static final class ItemsEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_ITEMS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        public static final String TABLE_NAME = "ITEMS";

        public static final String COLUMN_SHOP_ID_REF = "SHOP_ID_REF";

        public static final String COLUMN_ITEM_ID = "ITEM_ID";

        public static final String COLUMN_ITEM_NAME = "ITEM_NAME";

        public static final String COLUMN_ITEM_DESC = "ITEM_DESC";

        public static final String COLUMN_ITEM_PRICE = "ITEM_PRICE";

        public static final String COLUMN_ITEM_PRICE_UNIT = "ITEM_PRICE_UNIT";

        public static final String COLUMN_ITEM_BARCODE = "ITEM_BARCODE";

        public static final String COLUMN_ITEM_LOGO = "ITEM_LOGO";

        public static Uri buildItemUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildItemWithShopId(int shopId){
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(shopId))
                    .build();
        }

        public static Uri buildItemWithShopIdAndBarcode(int sid,String code){
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(sid))
                    .appendPath(code)
                    .build();
        }

        public static String getItemBarcodeFromUri(Uri uri){
            return uri.getPathSegments().get(2);
        }



    }

    public static final class CardDetails implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_CARD_DETAILS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CARD_DETAILS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CARD_DETAILS;

        public static final String TABLE_NAME = "CARD_DETAILS";

        public static final String COLUMN_CARD_NUMBER = "CARD_NUMBER";

        public static final String COLUMN_CARD_TYPE = "CARD_TYPE";

        public static final String COLUMN_CARD_EXPIRY_MM = "CARD_EXPIRY_MM";

        public static final String COLUMN_CARD_EXPIRY_YY = "CARD_EXPIRY_YY";

        public static final String COLUMN_CARD_NAME = "CARD_NAME";

        public static final String COLUMN_CARD_BANK = "CARD_BANK";

        public static Uri buildCardUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildCardWithCardId(int cardId){
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(cardId))
                    .build();
        }

        public static int getCardNumFromUri(Uri uri){
            return Integer.parseInt(uri.getPathSegments().get(1));
        }

    }

    public static final class CartEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_CART).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CART;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CART;

        public static final String TABLE_NAME = "CART";

        public static final String COLUMN_CART_ID = "CART_ID";

        public static final String COLUMN_C_ITEM_NAME = "C_ITEM_NAME";

        public static final String COLUMN_C_ITEM_DESC = "C_ITEM_DESC";

        public static final String COLUMN_C_ITEM_LOGO = "C_ITEM_LOGO";

        public static final String COLUMN_C_ITEM_PRICE = "C_ITEM_PRICE";

        public static final String COLUMN_C_ITEM_PRICE_UNIT = "C_ITEM_PRICE_UNIT";

        public static final String COLUMN_C_QUANTITY = "QUANTITY";

        public static Uri buildCartUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildCartWithCartId(int cartId){
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(cartId))
                    .build();
        }

        public static int getCartIdFromUri(Uri uri){
            return Integer.parseInt(uri.getPathSegments().get(1));
        }
    }


}
