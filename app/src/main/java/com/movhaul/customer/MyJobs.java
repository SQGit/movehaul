package com.movhaul.customer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.LinearLayout;
import com.rey.material.widget.ListView;
import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Salman on 29-03-2017.
 */

public class MyJobs extends Activity {

    ListView lv_jobs_list;
    Snackbar snackbar;
    TextView tv_snack;
    Typeface tf;
    String id, token;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList ar_job_lists;
    MV_Datas mv_datas;
    JobListAdapter job_list_adapter;
    LinearLayout btn_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myjobs);
        FontsManager.initFormAssets(MyJobs.this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(MyJobs.this);
        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyJobs.this);
        editor = sharedPreferences.edit();

        ar_job_lists = new ArrayList<>();

        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");


        lv_jobs_list = (ListView) findViewById(com.movhaul.customer.R.id.listview_jobs);
        btn_back = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_back);

        snackbar = Snackbar
                .make(findViewById(com.movhaul.customer.R.id.top), "Network Error! Please Try Again Later.", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);

        if (!com.movhaul.customer.Config.isConnected(MyJobs.this)) {
            snackbar.show();
            tv_snack.setText("Please Connect Internet and Try again");
        } else {
            new get_job_lists().execute();

        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyJobs.this,DashboardNavigation.class);
                startActivity(i);
                finish();
            }
        });
    }


    public class get_job_lists extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                // jsonObject.accumulate("booking_id", sharedPreferences.getString("job_id", ""));
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + "customer/waitingjobs", json, id, token);

            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);


            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                    // String msg = jo.getString("message");
                    Log.d("tag", "<-----Status----->" + status);


                    if (status.equals("true")) {

                        JSONArray jobs_data = jo.getJSONArray("message");

                        if (jobs_data.length() > 0) {

                            for (int i = 0; i < jobs_data.length(); i++) {


                                JSONObject jos = jobs_data.getJSONObject(i);

                                mv_datas = new MV_Datas();


                                String booking_id = jos.getString("booking_id");
                                String pickup = jos.getString("pickup_location");
                                String drop = jos.getString("drop_location");
                                String booking_time = jos.getString("booking_time");
                                String driver_count = jos.getString("count");

                                String[] parts = booking_time.trim().split("T");
                                String part1 = parts[0]; // 004
                                String part2 = parts[1]; // 034556

                                mv_datas.setBooking_id(booking_id);
                                mv_datas.setPickup(pickup);
                                mv_datas.setDrop(drop);
                                mv_datas.setTime(part1);
                                  mv_datas.setDriver_count(driver_count);

                                ar_job_lists.add(mv_datas);


                            }


                        } else {
                            finish();
                            Toast.makeText(getApplicationContext(), "No Drivers Bidded,Please Wait for some more time.", Toast.LENGTH_LONG).show();

                        }


                        job_list_adapter = new JobListAdapter(MyJobs.this, MyJobs.this, ar_job_lists);
                        lv_jobs_list.setAdapter(job_list_adapter);

                    } else {
                        finish();
                        Toast.makeText(getApplicationContext(), "No Drivers Bidded,Please Wait for some more time.", Toast.LENGTH_LONG).show();

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
