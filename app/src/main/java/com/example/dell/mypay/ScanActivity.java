package com.example.dell.mypay;

import android.content.ContentValues;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.content.Intent;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;



public class ScanActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int DATA_LOADER = 10;
    private static final int ITEM_LOADER = 11;
    private int shop_id;
    private static final String[] SHOPS_COLUMN_SCAN = {

            MyPayContract.ShopsEntry.TABLE_NAME+"."+MyPayContract.ShopsEntry._ID,
            MyPayContract.ShopsEntry.COLUMN_SHOP_ID,
            MyPayContract.ShopsEntry.COLUMN_SHOP_NAME,
            MyPayContract.ShopsEntry.COLUMN_ADDRESS,
            MyPayContract.ShopsEntry.COLUMN_SHOP_API
    };
    private static final String[] ITEMS_COLUMN_SCAN = {
            MyPayContract.ItemsEntry.TABLE_NAME+"."+MyPayContract.ItemsEntry._ID,
            MyPayContract.ItemsEntry.COLUMN_ITEM_ID,
            MyPayContract.ItemsEntry.COLUMN_SHOP_ID_REF,
            MyPayContract.ItemsEntry.COLUMN_ITEM_NAME,
            MyPayContract.ItemsEntry.COLUMN_ITEM_DESC,
            MyPayContract.ItemsEntry.COLUMN_ITEM_LOGO,
            MyPayContract.ItemsEntry.COLUMN_ITEM_PRICE,
            MyPayContract.ItemsEntry.COLUMN_ITEM_PRICE_UNIT,
            MyPayContract.ItemsEntry.COLUMN_ITEM_BARCODE
    };

    private int item_id;
    private String item_name,item_desc,item_logo,price,price_unit;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#068FC4")));
        getSupportLoaderManager().initLoader(DATA_LOADER, savedInstanceState,
                this);
        //getSupportLoaderManager().initLoader(ITEM_LOADER, savedInstanceState,
        //        this);

    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                new CheckBarcode().execute(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            new CartAccess(this,shop_id).execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        switch (i){
            case DATA_LOADER:
                Intent intent=getIntent();
                if (intent==null)return null;
                Log.d("ScanActivity","oncreateLoader:data loader:"+intent.getData()+"loader id:"+i);
                Loader<Cursor> cursorLoader=new CursorLoader(
                        this,
                        intent.getData(),
                        SHOPS_COLUMN_SCAN,
                        null,null,null
                );
                return cursorLoader;
            /**case ITEM_LOADER:
                Uri getUri=getIntent().getData();
                if (getUri ==null)return null;
                Log.d("ScanActivity","oncreateLoader:item loader loader id"+i);
                int shp_id = MyPayContract.ShopsEntry.getShopIdFromUri(getUri);
                return new CursorLoader(this,
                        MyPayContract.ItemsEntry.buildItemWithShopId(shp_id),
                        ITEMS_COLUMN_SCAN,
                        null,
                        new String[]{Integer.toString(shp_id)}, null);**/
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, final Cursor cursor) {
        switch (cursorLoader.getId()){
            case DATA_LOADER:
                if(!cursor.moveToFirst())return;
                Log.d("ScanActivity","onloadfinished:data loader");
                ImageButton scan = (ImageButton)findViewById(R.id.button_scan);
                scan.setImageResource(R.drawable.scan);

                final IntentIntegrator integrator = new IntentIntegrator(this);

                shop_id = cursor.getInt(cursor.getColumnIndex(
                        MyPayContract.ShopsEntry.COLUMN_SHOP_ID
                ));
                final String inv_api = cursor.getString(cursor.getColumnIndex(
                        MyPayContract.ShopsEntry.COLUMN_SHOP_API
                ));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Cursor c = getContentResolver().query(
                                MyPayContract.ItemsEntry.buildItemWithShopId(shop_id),
                                null,null,new String[]{Integer.toString(shop_id)},null
                        );
                        if(!c.moveToFirst()){
                            Log.d("ScanActivity","checking item");
                            Intent i = new Intent(ScanActivity.this,ItemService.class);
                            i.putExtra(ItemService.ITEM_EXTRA,inv_api);
                            startService(i);
                        }
                    }
                }).start();
                scan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                        integrator.setPrompt("Scan a barcode");
                        integrator.setCameraId(0);  // Use a specific camera of the device
                        integrator.setBeepEnabled(true);
                        integrator.setOrientationLocked(false);
                        //integrator.setBarcodeImageEnabled(true);
                        integrator.initiateScan();
                    }
                });
                break;
            /**case ITEM_LOADER:
                if(!cursor.moveToFirst())return;
                Log.d("ScanActivity","onloadfinished:item loader");
                item_id = cursor.getInt(cursor.getColumnIndex(
                        MyPayContract.ItemsEntry.COLUMN_ITEM_ID));**/



        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    public class CheckBarcode extends AsyncTask<String,Void,Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            String code = strings[0];
            Cursor cur = getContentResolver().query(
                    MyPayContract.ItemsEntry
                            .buildItemWithShopIdAndBarcode(shop_id,
                                    code),
                    ITEMS_COLUMN_SCAN,
                    null,
                    new String[]{Integer.toString(shop_id),code},
                    null
            );
            if(cur.moveToFirst()){
                Log.d("ScanActivity","checking barcode");
                item_id = cur.getInt(cur.getColumnIndex(
                        MyPayContract.ItemsEntry.COLUMN_ITEM_ID));
                Cursor cartCur = getContentResolver().query(
                        MyPayContract.CartEntry.buildCartWithCartId(item_id),
                        new String[]{MyPayContract.CartEntry.COLUMN_C_QUANTITY},
                        null,null,null
                );
                if(cartCur.moveToFirst()){
                    int q = cartCur.getInt(cartCur.getColumnIndex(
                            MyPayContract.CartEntry.COLUMN_C_QUANTITY));
                    ContentValues values=new ContentValues();
                    values.put(MyPayContract.CartEntry.COLUMN_C_QUANTITY,(q+1));
                    int updaterows = getContentResolver().update(
                            MyPayContract.CartEntry.buildCartWithCartId(item_id),
                            values,
                            null,null
                    );

                }
                else {
                    item_name = cur.getString(cur.getColumnIndex(
                            MyPayContract.ItemsEntry.COLUMN_ITEM_NAME));
                    item_desc = cur.getString(cur.getColumnIndex(
                            MyPayContract.ItemsEntry.COLUMN_ITEM_DESC));
                    item_logo = cur.getString(cur.getColumnIndex(
                            MyPayContract.ItemsEntry.COLUMN_ITEM_LOGO));
                    price = cur.getString(cur.getColumnIndex(
                            MyPayContract.ItemsEntry.COLUMN_ITEM_PRICE));
                    price_unit = cur.getString(cur.getColumnIndex(
                            MyPayContract.ItemsEntry.COLUMN_ITEM_PRICE_UNIT));

                    ContentValues values = new ContentValues();
                    values.put(MyPayContract.CartEntry.COLUMN_CART_ID, item_id);
                    values.put(MyPayContract.CartEntry.COLUMN_C_ITEM_NAME, item_name);
                    values.put(MyPayContract.CartEntry.COLUMN_C_ITEM_DESC, item_desc);
                    values.put(MyPayContract.CartEntry.COLUMN_C_ITEM_LOGO, item_logo);
                    values.put(MyPayContract.CartEntry.COLUMN_C_ITEM_PRICE, price);
                    values.put(MyPayContract.CartEntry.COLUMN_C_ITEM_PRICE_UNIT, price_unit);
                    values.put(MyPayContract.CartEntry.COLUMN_C_QUANTITY,1);
                    Uri inert_uri = getApplicationContext().getContentResolver().insert(
                            MyPayContract.CartEntry.CONTENT_URI, values
                    );
                }
                return true;
            }
            else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean){
                Button payment = (Button)findViewById(R.id.button_payment);
                payment.setVisibility(View.VISIBLE);
                payment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(
                                ScanActivity.this,CartActivity.class)
                                .setData(MyPayContract.ShopsEntry.buildShopWithShopId(
                                                shop_id)
                                );
                        startActivity(intent);
                    }
                });
            }
            else
                Toast.makeText(ScanActivity.this,"Item not validated.Please scan again",
                        Toast.LENGTH_LONG).show();
        }
    }
}
