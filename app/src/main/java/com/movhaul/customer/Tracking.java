package com.movhaul.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.rey.material.widget.ImageView;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sqindia on 02-11-2016.
 */

public class Tracking extends FragmentActivity implements OnMapReadyCallback {
    ImageView btn_search;
    TextView tv_hint;
    ScrollView sv_tracking;
    LinearLayout btn_back;
    Snackbar snackbar;
    Typeface tf;
    ProgressDialog mProgressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView tv_snack;
    String id, token;
    MV_Datas mv_datas;
    ArrayList<MV_Datas> ar_job_upcoming;
    AutoCompleteTextView act_tracking;
    ArrayAdapter<String> id_adapter;
    ArrayList<String> ar_ids;
    HashMap<String, MV_Datas> hs_datas;
    TextView tv_date, tv_time, tv_drop, tv_driver_name, tv_driver_phone;
    CustomMapFragment customMapFragment;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.movhaul.customer.R.layout.tracking);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");
        FontsManager.changeFonts(this);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Tracking.this);
        editor = sharedPreferences.edit();


        btn_search = (ImageView) findViewById(com.movhaul.customer.R.id.btn_search);
        tv_hint = (TextView) findViewById(com.movhaul.customer.R.id.textView_hint);
        sv_tracking = (ScrollView) findViewById(com.movhaul.customer.R.id.scrollView_tracking);
        btn_back = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_back);

        act_tracking = (AutoCompleteTextView) findViewById(R.id.act_textview);

        tv_date = (TextView) findViewById(R.id.textview_date);
        tv_time = (TextView) findViewById(R.id.textview_time);
        tv_drop = (TextView) findViewById(R.id.textview_address);
        tv_driver_name = (TextView) findViewById(R.id.textview_driver_name);
        tv_driver_phone = (TextView) findViewById(R.id.textview_driver_phone);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mProgressDialog = new ProgressDialog(Tracking.this);
        mProgressDialog.setTitle(com.movhaul.customer.R.string.loading);
        mProgressDialog.setMessage(getString(com.movhaul.customer.R.string.wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        snackbar = Snackbar
                .make(findViewById(com.movhaul.customer.R.id.top), com.movhaul.customer.R.string.no_internet, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);

        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");


        ar_ids = new ArrayList<>();
        ar_job_upcoming = new ArrayList<>();
        hs_datas = new HashMap<>();

        if (!com.movhaul.customer.Config.isConnected(Tracking.this)) {
            snackbar.show();
            tv_snack.setText(com.movhaul.customer.R.string.please_try_again);
        } else {
            new get_jobs().execute();

        }


        tv_hint.setVisibility(View.VISIBLE);
        sv_tracking.setVisibility(View.GONE);

        act_tracking.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_hint.setVisibility(View.GONE);
                sv_tracking.setVisibility(View.VISIBLE);
                String d_id = act_tracking.getText().toString();

                mv_datas = hs_datas.get(d_id);

                Log.e("tag", mv_datas.getTime());
                Log.e("tag", mv_datas.getName());

                tv_date.setText(mv_datas.getDate());
                tv_time.setText(mv_datas.getTime());
                tv_drop.setText(mv_datas.getDrop());
                tv_driver_name.setText(mv_datas.getName());
                tv_driver_phone.setText(mv_datas.getDriver_number());
            }
        });

        act_tracking.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count <= 0) {
                    tv_hint.setVisibility(View.VISIBLE);
                    sv_tracking.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Tracking.this, DashboardNavigation.class);
                startActivity(i);
                finish();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // tv_hint.setVisibility(View.GONE);
                //sv_tracking.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Tracking.this, DashboardNavigation.class);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


    public class get_jobs extends AsyncTask<String, Void, String> {
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
                                String pickup_location = jos.getString("pickup_location");
                                String drop_location = jos.getString("drop_location");
                                String delivery_address = jos.getString("delivery_address");
                                String driver_name = jos.getString("driver_name");
                                String driver_phone = jos.getString("driver_mobile_pri");
                                String booking_time = jos.getString("booking_time");
                                String truck_type = jos.getString("vehicle_sub_type");
                                String vehicle_type = jos.getString("vehicle_type");
                                String job_cost = jos.getString("job_cost");
                                String driver_image = jos.getString("driver_image");
                                String booking_id = jos.getString("booking_id");
                                String job_status = jos.getString("job_status");
                                String driver_id = jos.getString("driver_id");
                                //2016\/12\/08 T 18:12
                                String[] parts = booking_time.trim().split("T");
                                String part1 = parts[0]; // 004
                                String part2 = parts[1]; // 034556
                                Log.e("tag", "1st" + part1);
                                Log.e("tag", "2st" + part2);

                                mv_datas.setName(driver_name);
                                mv_datas.setDriver_image(driver_image);
                                mv_datas.setDriver_number(driver_phone);
                                mv_datas.setDate(part1);
                                mv_datas.setTime(part2);
                                mv_datas.setTruck_type(truck_type);
                                mv_datas.setPickup(pickup_location);
                                mv_datas.setDrop(drop_location);
                                mv_datas.setJob_cost(job_cost);
                                mv_datas.setBooking_id(booking_id);
                                mv_datas.setVec_type(vehicle_type);
                                mv_datas.setJob_status(job_status);
                                mv_datas.setDriver_id(driver_id);

                                if (job_status.equals("confirmed")) {
                                    ar_job_upcoming.add(mv_datas);
                                    ar_ids.add(booking_id);
                                    hs_datas.put(booking_id, mv_datas);
                                }

                            }
                            Log.e("tag", "size " + ar_job_upcoming.size());
                            Log.e("tag", "ids " + ar_ids.size());
                            for (int i = 0; i < ar_ids.size(); i++) {
                                Log.e("tag", "ar: " + ar_ids.get(i));
                            }

                            id_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item,R.id.text, ar_ids);
                            act_tracking.setAdapter(id_adapter);

                            //    viewPager.setAdapter(myViewPagerAdapter);
                            //   viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

                            // tiv.setTabIndicatorFactory(new TabIndicatorView.ViewPagerIndicatorFactory(viewPager));

                        } else {
                            finish();
                            Toast.makeText(getApplicationContext(), "Currently you don't have any Jobs to Show...", Toast.LENGTH_LONG).show();
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
