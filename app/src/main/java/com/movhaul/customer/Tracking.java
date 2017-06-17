package com.movhaul.customer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
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

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rey.material.widget.ImageView;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sqindia on 02-11-2016.
 */

public class Tracking extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
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
    Location glocation;
    int iko;
    private GoogleMap mMap;
    double curr_lati,curr_longi,drop_lati,drop_longi,mid_lati,mid_longi,driver_latitude,driver_longitude;

    private boolean mPermissionDenied = false;

    Firebase reference1, reference2;


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


        //Firebase.setAndroidContext(this);
        //reference1 = new Firebase("https://movehaul-147509.firebaseio.com/driver_track" + "driver" + "_" + "customer");
        //reference2 = new Firebase("https://movehaul-147509.firebaseio.com/driver_track" + "customer" + "_" + "driver");



        /*reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                double latitude = Double.valueOf(map.get("latitude").toString());
                double longitude = Double.valueOf(map.get("longitude").toString());


                Log.e("tag","fir_Vl"+latitude+longitude);

                if(sv_tracking.getVisibility() == View.VISIBLE){
                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_truck)));

                }

                Toast.makeText(getApplicationContext(),"values updating",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/



        tv_hint.setVisibility(View.VISIBLE);
        sv_tracking.setVisibility(View.GONE);

        act_tracking.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                btn_search.setVisibility(View.GONE);
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

                 drop_lati = Double.valueOf(mv_datas.drop_latitude);
                 drop_longi = Double.valueOf(mv_datas.drop_longitude);

                driver_latitude = Double.valueOf(mv_datas.driver_latitude);
                driver_longitude = Double.valueOf(mv_datas.driver_longitude);

                Log.e("tag","lat:"+drop_lati);
                Log.e("tag","lon:"+drop_longi);

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(new LatLng(drop_lati,drop_longi)).icon(BitmapDescriptorFactory.fromResource(R.drawable.delivery_addr_tracking)));
                mMap.addMarker(new MarkerOptions().position(new LatLng(driver_latitude,driver_longitude)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_truck)));

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                midPoint(curr_lati,curr_longi,drop_lati,drop_longi);




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
                    btn_search.setVisibility(View.VISIBLE);
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
                String getId = act_tracking.getText().toString().trim();
                if (!act_tracking.getText().toString().trim().isEmpty()) {

                    Log.e("tag", "getIdsts:" + getID(getId));

                    if (getID(getId)) {
                        snackbar.show();
                        tv_snack.setText("correct Booking ID");
                    } else {
                        snackbar.show();
                        tv_snack.setText("Invalid Booking ID");
                    }
                } else {
                    snackbar.show();
                    tv_snack.setText("Enter Booking ID");

                }
            }
        });

    }

    public boolean getID(String asf) {

        boolean id_sts = false;
        int i = 0;

        do {
            if (i < ar_ids.size()) {
                Log.e("tag", "asf:" + asf);
                Log.e("tag", "arids:" + ar_ids.get(i));

                if (ar_ids.get(i).equals(asf)) {
                    id_sts = true;
                }
                Log.e("tag", "dists:" + id_sts);
            }
            i++;
        } while (!id_sts);

       /* for (String string : ar_ids) {
            if (string.equals(asf)) {
                return true;
            }

        }*/
        return id_sts;
    }

    public  void midPoint(double lat1,double lon1,double lat2,double lon2){

        double dLon = Math.toRadians(lon2 - lon1);

        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        //print out in degrees
        System.out.println(Math.toDegrees(lat) + " " + Math.toDegrees(lon));
        Toast.makeText(getApplicationContext(),"mid POint"+lat+lon,Toast.LENGTH_LONG).show();
        mid_lati = Math.toDegrees(lat);
        mid_longi = Math.toDegrees(lon);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mid_lati, mid_longi), 10.5f));
      //  mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(mid_lati,mid_longi)));
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

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(9.0f));
        mMap.getUiSettings().setZoomControlsEnabled(true);


        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));
            if (!success) {
                Log.e("TAG", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("TAG", "Can't find style. Error: ", e);
        }


        mMap.setOnMyLocationChangeListener(myLocationChangeListener);



    }

    GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if (iko == 0) {
            Log.e("tag", "locationchanged");
                curr_lati = location.getLatitude();
                curr_longi = location.getLongitude();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10.5f));

                iko = 1;
            } else {
            }

        }
    };



    @Override
    public boolean onMyLocationButtonClick() {

        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
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

                                String drop_latitude = jos.getString("drop_latitude");
                                String drop_longitude = jos.getString("drop_longitude");
                                String driver_latitude = jos.getString("driver_latitude");
                                String driver_longitude = jos.getString("driver_longitude");

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
                                mv_datas.setDrop_latitude(drop_latitude);
                                mv_datas.setDrop_longitude(drop_longitude);
                                mv_datas.setDriver_latitude(driver_latitude);
                                mv_datas.setDriver_longitude(driver_longitude);

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

                            id_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.text, ar_ids);
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
