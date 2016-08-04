package com.example.dell.mypay;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.Space;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class InvoiceAckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_ack);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#068FC4")));
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invoice_ack, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_invoice_ack, container, false);
            ImageView tick = (ImageView)rootView.findViewById(R.id.receipt_success_image);
            ImageView cancel = (ImageView)rootView.findViewById(R.id.cancel_image);
            LinearLayout lin_succ = (LinearLayout)rootView.findViewById(R.id.message_success);
            LinearLayout lin_fail = (LinearLayout)rootView.findViewById(R.id.message_failure);
            Button retry = (Button)rootView.findViewById(R.id.retry_button);
            Button contiue_shop = (Button)rootView.findViewById(R.id.space);

            Intent intent=getActivity().getIntent();
            boolean value = intent.getBooleanExtra("Invoice",false);
            if (value){
                tick.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.GONE);
                lin_succ.setVisibility(View.VISIBLE);
                lin_fail.setVisibility(View.GONE);
                contiue_shop.setVisibility(View.VISIBLE);
                retry.setVisibility(View.GONE);
                contiue_shop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent =new Intent(getActivity(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }
            else{
                tick.setVisibility(View.GONE);
                cancel.setVisibility(View.VISIBLE);
                lin_succ.setVisibility(View.GONE);
                lin_fail.setVisibility(View.VISIBLE);
                contiue_shop.setVisibility(View.GONE);
                retry.setVisibility(View.VISIBLE);
                retry.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v) {
                        Intent intent =new Intent(getActivity(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                });

            }

            return rootView;
        }
    }
}
