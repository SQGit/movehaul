package com.movhaul.customer;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.ImageView;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by sqindia on 02-11-2016.
 * trcking of driver and shipment
 * in tracking list of shipments will shown
 * customer wiil see his shipments where it located
 */
@SuppressWarnings("ALL")
public class Tracking extends FragmentActivity implements OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {
    //private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
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
    //CustomMapFragment customMapFragment;
    //Location glocation;
    int iko;
    double curr_lati, curr_longi, drop_lati, drop_longi, mid_lati, mid_longi, driver_latitude, driver_longitude;
    String driver_name;
    Firebase reference1;
    float dist_Between;
    Location currentLocation, dropLocation;
    private GoogleMap mMap;
    public int frm_width;

    private DatabaseReference mDatabase;
    private DatabaseReference mPostReference;

    GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if (iko == 0) {
                Log.e("tag", "locationchanged");
                curr_lati = location.getLatitude();
                curr_longi = location.getLongitude();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10.5f));
                iko = 1;
            }
        }
    };
    private boolean mPermissionDenied = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.movhaul.customer.R.layout.tracking);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");
        FontsManager.changeFonts(this);
       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
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
        frm_width = mapFragment.getView().getMeasuredWidth();
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

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Firebase.setAndroidContext(this);
        //reference2 = new Firebase("https://movehaul-147509.firebaseio.com/driver_track" + "customer" + "_" + "driver");
        tv_hint.setVisibility(View.VISIBLE);
        sv_tracking.setVisibility(View.GONE);
        act_tracking.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getData();
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
                ///   Log.e("tag", "getIdsts:" + getID(getId));
                    if (getID(getId)) {
                        getData();
                        //     snackbar.show();
                        //    tv_snack.setText("correct Booking ID");
                    } else {
                        snackbar.show();
                        tv_snack.setText(R.string.acwa);
                    }
                } else {
                    snackbar.show();
                    tv_snack.setText(R.string.zexc);
                }
            }
        });
    }
    public void getData() {
        driver_name = mv_datas.getName();
       // reference1 = new Firebase("https://movehaul-147509.firebaseio.com/driver_track/" + driver_name);
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
        Log.e("tag", "lat:" + drop_lati);
        Log.e("tag", "lon:" + drop_longi);
        mMap.clear();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        midPoint(curr_lati, curr_longi, drop_lati, drop_longi);
        String str_origin = "origin=" + curr_lati + "," + curr_longi;
        String str_dest = "destination=" + drop_lati + "," + drop_longi;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
        mMap.addMarker(new MarkerOptions().position(new LatLng(drop_lati, drop_longi)).icon(BitmapDescriptorFactory.fromResource(R.drawable.delivery_addr_tracking)));
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_truck))
                .position(new LatLng(driver_latitude, driver_longitude))
                .flat(true)
                .rotation(-50));

        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("driver_track");

        mPostReference.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                Log.e("tag","sna;"+dataSnapshot+" s "+s);
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                Log.e("tag","snae;"+dataSnapshot+" s "+s);
            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {
                Log.e("tag","snas;"+dataSnapshot+" s ");
            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                Log.e("tag","snaa;"+dataSnapshot+" s "+s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.e("tag","madda"+mDatabase);
        Log.e("tag","madda"+mPostReference.getRef().child(driver_name));

        mPostReference.child(driver_name).addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                Log.e("tag","daaasdfa ;"+dataSnapshot+" s "+s);
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                Log.e("tag","fewf ;"+dataSnapshot+" s "+s);
            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {
                Log.e("tag","sdfxc ;"+dataSnapshot+" s ");
            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                Log.e("tag","werew ;"+dataSnapshot+" s "+s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mPostReference.child("salman").addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                Log.e("tag","asdfasdf:"+dataSnapshot);
                Log.e("tag","werwe:"+dataSnapshot.getChildren());
                Log.e("tag","asdfzcx:"+dataSnapshot.getValue());
               // Map map = dataSnapshot.getValue(Map.class);
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                double latitude = Double.valueOf(map.get("latitude").toString());
                double longitude = Double.valueOf(map.get("longitude").toString());
                if (sv_tracking.getVisibility() == View.VISIBLE) {
                    Log.e("tag", "fir_Vl" + latitude + longitude);
                    dropLocation = new Location(LocationManager.GPS_PROVIDER);
                    currentLocation = new Location(LocationManager.GPS_PROVIDER);
                    dropLocation.setLatitude(drop_lati);
                    dropLocation.setLongitude(drop_longi);
                    currentLocation.setLatitude(latitude);
                    currentLocation.setLongitude(longitude);
                    dist_Between = currentLocation.distanceTo(dropLocation);
                    mMap.clear();
                    Log.e("tag", "distance is: " + dist_Between / 1000 + " km");
                    String str_origin = "origin=" + latitude + "," + longitude;
                    String str_dest = "destination=" + drop_lati + "," + drop_longi;
                    String sensor = "sensor=false";
                    String parameters = str_origin + "&" + str_dest + "&" + sensor;
                    String output = "json";
                    String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
                    DownloadTask downloadTask = new DownloadTask();
                    downloadTask.execute(url);
                    Marker pickup_marker, drop_marker;
                    pickup_marker =  mMap.addMarker(new MarkerOptions().position(new LatLng(drop_lati, drop_longi)).icon(BitmapDescriptorFactory.fromResource(R.drawable.delivery_addr_tracking)));
                    LatLng mapCenter = new LatLng(latitude, longitude);
                    // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, 10.5f));
                    drop_marker =  mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_truck))
                            .position(mapCenter)
                            .flat(true)
                            .rotation(-50));
                    LatLng mapCenter11 = new LatLng(mid_lati, mid_longi);
                    CameraPosition cameraPosition1 = CameraPosition.builder()
                            .target(mapCenter11)
                            .build();
                    // Animate the change in camera view over 2 seconds
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition1),
                            2000, null);
                    midPoint(latitude, longitude, drop_lati, drop_longi);
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    Marker[] markers = {pickup_marker, drop_marker};
                    for (Marker m : markers) {
                        builder.include(m.getPosition());
                    }
                    LatLngBounds bounds = builder.build();
                    int padding = ((frm_width * 10) / 100); // offset from edges of the map
                    // in pixels
                    Log.e("tag", "ss:" + padding);
                    //final LatLngBounds bounds = new LatLngBounds.Builder().include(new LatLng(lat1, lon1)).include(new LatLng(lat2, lon2)).build();
                    // mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 120));
                    //  final LatLngBounds boundss = new LatLngBounds.Builder().include(new LatLng(new_lat1, new_long1)).include(new LatLng(new_lat2, new_long2)).build();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,
                            padding+100);
                    mMap.animateCamera(cu);
                }
                //Toast.makeText(getApplicationContext(), "values updating", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

      /*  reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                double latitude = Double.valueOf(map.get("latitude").toString());
                double longitude = Double.valueOf(map.get("longitude").toString());
                if (sv_tracking.getVisibility() == View.VISIBLE) {
                    Log.e("tag", "fir_Vl" + latitude + longitude);
                    dropLocation = new Location(LocationManager.GPS_PROVIDER);
                    currentLocation = new Location(LocationManager.GPS_PROVIDER);
                    dropLocation.setLatitude(drop_lati);
                    dropLocation.setLongitude(drop_longi);
                    currentLocation.setLatitude(latitude);
                    currentLocation.setLongitude(longitude);
                    dist_Between = currentLocation.distanceTo(dropLocation);
                    mMap.clear();
                    Log.e("tag", "distance is: " + dist_Between / 1000 + " km");
                    String str_origin = "origin=" + latitude + "," + longitude;
                    String str_dest = "destination=" + drop_lati + "," + drop_longi;
                    String sensor = "sensor=false";
                    String parameters = str_origin + "&" + str_dest + "&" + sensor;
                    String output = "json";
                    String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
                    DownloadTask downloadTask = new DownloadTask();
                    downloadTask.execute(url);
                    Marker pickup_marker, drop_marker;
                    pickup_marker =  mMap.addMarker(new MarkerOptions().position(new LatLng(drop_lati, drop_longi)).icon(BitmapDescriptorFactory.fromResource(R.drawable.delivery_addr_tracking)));
                    LatLng mapCenter = new LatLng(latitude, longitude);
                   // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, 10.5f));
                   drop_marker =  mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_truck))
                            .position(mapCenter)
                            .flat(true)
                            .rotation(-50));
                    LatLng mapCenter11 = new LatLng(mid_lati, mid_longi);
                    CameraPosition cameraPosition1 = CameraPosition.builder()
                            .target(mapCenter11)
                            .build();
                    // Animate the change in camera view over 2 seconds
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition1),
                            2000, null);
                    midPoint(latitude, longitude, drop_lati, drop_longi);
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    Marker[] markers = {pickup_marker, drop_marker};
                    for (Marker m : markers) {
                        builder.include(m.getPosition());
                    }
                    LatLngBounds bounds = builder.build();
                    int padding = ((frm_width * 10) / 100); // offset from edges of the map
                    // in pixels
                    Log.e("tag", "ss:" + padding);
                    //final LatLngBounds bounds = new LatLngBounds.Builder().include(new LatLng(lat1, lon1)).include(new LatLng(lat2, lon2)).build();
                    // mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 120));
                    //  final LatLngBounds boundss = new LatLngBounds.Builder().include(new LatLng(new_lat1, new_long1)).include(new LatLng(new_lat2, new_long2)).build();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,
                            padding+100);
                    mMap.animateCamera(cu);
                }
                //Toast.makeText(getApplicationContext(), "values updating", Toast.LENGTH_LONG).show();
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
    }
   /* public static double[] midd (double lat1, double long1, double lat2, double long2){
    }*/
    public boolean getID(String asf) {
        boolean id_sts = false;
        int i = 0;
        if(ar_ids.size()>0) {
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
        }
       /* for (String string : ar_ids) {
            if (string.equals(asf)) {
                return true;
            }
        }*/
        return id_sts;
    }
    public void midPoint(double lat1, double lon1, double lat2, double lon2) {
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
      //  System.out.println(Math.toDegrees(lat) + " " + Math.toDegrees(lon));
      //  Toast.makeText(getApplicationContext(), "mid POint" + lat + lon, Toast.LENGTH_LONG).show();
        mid_lati = Math.toDegrees(lat);
        mid_longi = Math.toDegrees(lon);
       /* LatLng mapCenter = new LatLng(mid_lati, mid_longi);
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(mapCenter)
                .zoom(10.5f)
                .build();
        // Animate the change in camera view over 2 seconds
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000, null);*/
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mid_lati, mid_longi), 10.5f));
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
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception sd sd url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
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
                                if (job_status.equals("confirmed") || job_status.equals("started")) {
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
                            Toast.makeText(getApplicationContext(), R.string.acwzaz, Toast.LENGTH_LONG).show();
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
    private class DownloadTask extends AsyncTask<String, Void, String> {
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }
        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            Polyline polyline;
            MarkerOptions markerOptions = new MarkerOptions();
            // Traversing through all the routes
            if (result != null) {
                if (!result.isEmpty()) {
                    for (int i = 0; i < result.size(); i++) {
                        points = new ArrayList<LatLng>();
                        lineOptions = new PolylineOptions();
                        // Fetching i-th route
                        List<HashMap<String, String>> path = result.get(i);
                        // Fetching all the points in i-th route
                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);
                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);
                            points.add(position);
                        }
                        // Adding all the points in the route to LineOptions
                        lineOptions.addAll(points);
                        lineOptions.width(6);
                        lineOptions.color(getResources().getColor(R.color.redColor));
                    }
                    // Drawing polyline in the Google Map for the i-th route
                    polyline = mMap.addPolyline(lineOptions);
                    polyline.remove();
                    polyline = mMap.addPolyline(lineOptions);
                }
            }
        }
    }


}
