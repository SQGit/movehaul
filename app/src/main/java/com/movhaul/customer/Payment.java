package com.movhaul.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.rey.material.widget.LinearLayout;
import com.rey.material.widget.ListView;
import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sqindia on 01-11-2016.
 */

public class Payment extends Activity {
    ListView lv_payment_list;
    LinearLayout btn_back;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String id, token;
    ArrayList<MV_Datas> ar_job_finished;
    ProgressDialog mProgressDialog;
    MV_Datas mv_datas;
    Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.movhaul.customer.R.layout.payment);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);

        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Payment.this);
        editor = sharedPreferences.edit();

        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");

        lv_payment_list = (ListView) findViewById(com.movhaul.customer.R.id.listview_payment);
        btn_back = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_back);

        mProgressDialog = new ProgressDialog(Payment.this);
        mProgressDialog.setTitle(com.movhaul.customer.R.string.loading);
        mProgressDialog.setMessage(getString(com.movhaul.customer.R.string.wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        ar_job_finished = new ArrayList<>();

        if (!com.movhaul.customer.Config.isConnected(Payment.this)) {

            Toast.makeText(getApplicationContext(), "Network Failed,Please Try Again Later", Toast.LENGTH_LONG).show();
            finish();
        } else {
            new get_history().execute();

        }



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Payment.this,DashboardNavigation.class);
                startActivity(i);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Payment.this,DashboardNavigation.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }


    public class get_history extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + "customer/jobhistory", json, id, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            mProgressDialog.dismiss();
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                    Log.d("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {
                        JSONArray goods_data = jo.getJSONArray("message");
                        if (goods_data.length() > 0) {
                            for (int i = 0; i < goods_data.length(); i++) {
                                JSONObject jos = goods_data.getJSONObject(i);
                                mv_datas = new MV_Datas();


                                String booking_time = jos.getString("booking_time");
                                String job_cost = jos.getString("job_cost");
                                String booking_id = jos.getString("booking_id");
                                String job_status = jos.getString("job_status");
                                String[] parts = booking_time.trim().split("T");
                                String part1 = parts[0]; // 004
                                String part2 = parts[1]; // 034556
                                Log.e("tag", "1st" + part1);
                                Log.e("tag", "2st" + part2);

                                mv_datas.setDate(part1);
                                mv_datas.setTime(part2);
                                mv_datas.setJob_cost(job_cost);
                                mv_datas.setBooking_id(booking_id);
                                mv_datas.setJob_status(job_status);

                                if (job_status.equals("finished")) {
                                    ar_job_finished.add(mv_datas);
                                }
                            }
                            Log.e("tag", "size " + ar_job_finished.size());


                            Payment_Adapter adapter = new Payment_Adapter(Payment.this, ar_job_finished);
                            lv_payment_list.setAdapter(adapter);


                        } else {
                            finish();
                            Toast.makeText(getApplicationContext(), R.string.cade, Toast.LENGTH_LONG).show();
                        }

                    } else if (status.equals("false")) {

                        Log.e("tag", "status false");
                        //has to check internet and location...
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    // Toast.makeText(getApplicationContext(),"Network Errror0",Toast.LENGTH_LONG).show();
                }
            } else {
                // Toast.makeText(getApplicationContext(),"Network Errror1",Toast.LENGTH_LONG).show();
            }

        }

    }

}
