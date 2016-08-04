package com.example.dell.mypay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.PaymentRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import cz.msebera.android.httpclient.Header;

public class CartFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,CartAdapter.CustomButtonListener{

    private static final int DATA_LOADER = 90;
    private static final int CART_LOADER = 91;
    private CartAdapter mCartAdapter;
    private int shop_id;
    private TextView t_price;
    private String price_unit;
    private static final String[] SHOPS_COLUMN_CART = {

            MyPayContract.ShopsEntry.TABLE_NAME+"."+MyPayContract.ShopsEntry._ID,
            MyPayContract.ShopsEntry.COLUMN_SHOP_ID,
            MyPayContract.ShopsEntry.COLUMN_SHOP_NAME,
            MyPayContract.ShopsEntry.COLUMN_ADDRESS,
            MyPayContract.ShopsEntry.COLUMN_SHOP_API
    };
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

    public CartFragment() {
    }

    private static final int REQUEST_CODE = 0x1234;
    private static final String SERVER_BASE_URL = "https://helloworld-8c984.appspot.com";
    //            "http://my-pay-1376.appspot.com";
    private final String paypalClientID = "eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiI0YmRiYTJiZGNkOGU2MjA5NjlkYzE1OTg0MDk4NjZkMzc1MGU5NDZhZTJkZjMzZmQzMDIwZWQyOGZkYzI4ZDFlfGNyZWF0ZWRfYXQ9MjAxNi0wNy0xNFQxODowNDo0MC4yNzEyMzUyMDUrMDAwMFx1MDAyNm1lcmNoYW50X2lkPTM0OHBrOWNnZjNiZ3l3MmJcdTAwMjZwdWJsaWNfa2V5PTJuMjQ3ZHY4OWJxOXZtcHIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvMzQ4cGs5Y2dmM2JneXcyYi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzLzM0OHBrOWNnZjNiZ3l3MmIvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tLzM0OHBrOWNnZjNiZ3l3MmIifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiQWNtZSBXaWRnZXRzLCBMdGQuIChTYW5kYm94KSIsImNsaWVudElkIjpudWxsLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJlbnZpcm9ubWVudCI6Im9mZmxpbmUiLCJ1bnZldHRlZE1lcmNoYW50IjpmYWxzZSwiYnJhaW50cmVlQ2xpZW50SWQiOiJtYXN0ZXJjbGllbnQzIiwiYmlsbGluZ0FncmVlbWVudHNFbmFibGVkIjp0cnVlLCJtZXJjaGFudEFjY291bnRJZCI6ImFjbWV3aWRnZXRzbHRkc2FuZGJveCIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJjb2luYmFzZUVuYWJsZWQiOmZhbHNlLCJtZXJjaGFudElkIjoiMzQ4cGs5Y2dmM2JneXcyYiIsInZlbm1vIjoib2ZmIn0=";
    final SyncHttpClient client = new SyncHttpClient();
    double tot_price;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        ListView cart_list = (ListView)rootView.findViewById(R.id.cart_list);
        mCartAdapter = new CartAdapter(getActivity(),null,0);
        mCartAdapter.setCustomButtonListner(CartFragment.this);
        cart_list.setAdapter(mCartAdapter);

        Button p_paypal = (Button)rootView.findViewById(R.id.paypal_payment);
//        Button p_card = (Button)rootView.findViewById(R.id.card_payment);
        t_price = (TextView)rootView.findViewById(R.id.total_price);
        new FindTotalAmount().execute();

        p_paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               startActivity(new Intent(getActivity(),PayPalActivity.class));
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        client.get(
                                SERVER_BASE_URL+"/rest/hello/clientID",
                                new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        onBraintreeSubmit(new String(responseBody));
                                    }
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        Log.d("My Log","Error occurred while connecting");
                                    }
                                });
                    }
                });
                t.start();
            }
        });

        return rootView;
    }

    public void onBraintreeSubmit(String ID) {
        PaymentRequest paymentRequest = new PaymentRequest()
                .clientToken(ID)
                .amount("Rs." + " " +tot_price)
                .primaryDescription("Total Bill: Rs."+tot_price)
                .submitButtonText("Pay")
                .disablePayPal();
        startActivityForResult(paymentRequest.getIntent(getActivity()), REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(
                            BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                    );
                    final String nonce = paymentMethodNonce.getNonce();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            client.get(SERVER_BASE_URL + "/rest/hello/clientNonce?nonce=" + nonce, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    startActivity(new Intent(getActivity(),TransactionActivity.class)
                                            .setData(MyPayContract.ShopsEntry.buildShopWithShopId(shop_id)));
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    Toast.makeText(getContext().getApplicationContext(),"Payment Failed due to issue",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    thread.start();
//                  client.post(SERVER_BASE_URL+"/rest/hello/nonce",)

                    break;
                case BraintreePaymentActivity.BRAINTREE_RESULT_DEVELOPER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_UNAVAILABLE:
                    // handle errors here, a throwable may be available in
                    data.getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DATA_LOADER,savedInstanceState,this);
        getLoaderManager().initLoader(CART_LOADER,savedInstanceState,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        switch (i){
            case CART_LOADER:
                return new CursorLoader(getActivity(),
                        MyPayContract.CartEntry.CONTENT_URI,
                        CART_COLUMNS,
                        null,null,null);
            case DATA_LOADER:
                Intent intent=getActivity().getIntent();
                if (intent==null)return null;
                Loader<Cursor> cursorLoader=new CursorLoader(
                        getActivity(),
                        intent.getData(),
                        SHOPS_COLUMN_CART,
                        null,null,null
                );
                return cursorLoader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        switch (cursorLoader.getId()){
            case CART_LOADER:
                mCartAdapter.swapCursor(cursor);
                break;
            case DATA_LOADER:
                if(cursor.moveToFirst())
                    shop_id=cursor.getInt(cursor.getColumnIndex(
                            MyPayContract.ShopsEntry.COLUMN_SHOP_ID));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mCartAdapter.swapCursor(null);

    }

    @Override
    public void onButtonClickListner(int cid) {
        int del = getActivity().getContentResolver().delete(
                MyPayContract.CartEntry.buildCartWithCartId(cid),
                null,null
        );
        new FindTotalAmount().execute();
        Log.e("CartFragment", "rows deleted:" + del);
        Log.e("CartFragment", "cart id" + cid);
        mCartAdapter.notifyDataSetChanged();
    }

    public class FindTotalAmount extends AsyncTask<Void,Void,Cursor>{

        @Override
        protected Cursor doInBackground(Void... voids) {
            Cursor c = getActivity().getContentResolver().query(
                    MyPayContract.CartEntry.CONTENT_URI,
                    new String[]{"SUM("+MyPayContract.CartEntry.COLUMN_C_ITEM_PRICE+
                            "*"+
                            MyPayContract.CartEntry.COLUMN_C_QUANTITY+") AS TOTAL"},
                    null,null,null
            );
            Cursor cur = getActivity().getContentResolver().query(
                    MyPayContract.CartEntry.CONTENT_URI,
                    new String[]{"DISTINCT "+
                            MyPayContract.CartEntry.COLUMN_C_ITEM_PRICE_UNIT+" AS P_UNIT"},
                    null,null,null
            );
            if(cur.moveToFirst())
                price_unit = cur.getString(cur.getColumnIndex("P_UNIT"));
            return c;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            if(cursor.moveToFirst() && t_price!=null && price_unit!=null) {
                tot_price = cursor.getDouble(cursor.getColumnIndex("TOTAL"));
                t_price.setText(price_unit + " " +
                        Double.toString(cursor.getDouble(cursor.getColumnIndex("TOTAL"))));
            }
            SharedPreferences.Editor editor=getActivity()
                    .getSharedPreferences("T_price", Context.MODE_PRIVATE).edit();
            editor.putString("t_price", t_price.getText().toString());
            editor.apply();
        }
    }
}