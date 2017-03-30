package com.movhaul.customer;

//customer\nearbydrivers
      //  latitude,longitude,radius


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;
import com.sloop.fonts.FontsManager;
import com.systemspecs.remita.remitapaymentgateway.RemitaMainActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class DashboardNavigation extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, com.google.android.gms.location.LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, MapWrapperLayout.OnDragListener {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int REQUEST_AC_PICKUP = 1;
    private static final int REQUEST_AC_DROP = 2;
    private static final int REQUEST_CODE_PAYMENT = 545;
    private static final int REQUEST_PROFILE = 5;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    public static boolean mMapIsTouched = false;
    private static String TAG = "tag_MAP LOCATION";
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStreetOutput;
    protected GoogleApiClient mGoogleApiClient;
    Context mContext;
    ArrayList<Uri> image_uris;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    Button btn_book_now, btn_book_later;
    TextView tv_name, tv_email, tv_myTrips, tv_jobReview, tv_payments, tv_tracking, tv_offers, tv_emergencyContacts;
    AutoCompleteTextView aet_pickup, aet_drop;
    TextInputLayout flt_pickup, flt_drop_location;
    ImageView btn_menu, rightmenu;
    android.widget.LinearLayout lt_drop, lt_pickup;
    Dialog dialog1, dialog2, dg_road_confirm;
    Button btn_yes, btn_no, btn_update;
    int exit_status;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Typeface tf;
    Snackbar snackbar, snackbar_loc;
    android.widget.TextView tv_snack, tv_snack_loc, tv_snack_loc_act;
    android.widget.TextView tv_txt1, tv_txt2, tv_txt3;
    String current_time, service_id, service_token, str_lati, str_longi, str_locality, str_address, customer_mobile, customer_email, customer_name, str_profile_img;
    Geocoder geocoder;
    List<Address> addresses;
    LinearLayout lt_first, lt_last;
    FrameLayout lt_second, lt_frame;
    // SupportMapFragment mapFragment;
    CustomMapFragment customMapFragment;
    ImageView btn_editProfile, btn_close, btn_editProfile_img;
    EditText et_username, et_email;
    public static String id;
    public static String token;
    public String mPickup_lat;
    public String mPickup_long;
    public String mDrop_lat;
    public String mDrop_long;
    public String vec_type;
    TextInputLayout flt_uname, flt_email;
    ArrayList<String> selectedPhotos = new ArrayList<>();
    ImageView iv_location, iv_zoomin, iv_zoomout;
    int diff;
    ProgressDialog mProgressDialog;
    ImageView iv_truck, iv_bus, iv_road_assit;
    LinearLayout lt_filter_dialog;
    Button btn_book_roadside;
    LinearLayout lt_road_side;
    String str_time, str_pickup, str_drop, str_pickup_lati, str_pickup_longi, str_drop_lati, str_drop_longi;
    android.widget.RelativeLayout bottomSheetViewgroup;
    BottomSheetBehavior bottomSheetBehavior;
    LinearLayout lt_bt_veh_type, lt_bt_tow_type;
    LinearLayout lt_bt_veh_car, lt_bt_veh_truck, lt_bt_veh_bus, lt_bt_veh_tow, lt_bt_veh_flatbed;
    String cu_vec_type, dr_vec_type;
    android.widget.RelativeLayout fl_bottom_frame;
    LinearLayout lt_top;
    boolean p_loc_comp;
    public double dl_pick_lati, dl_pick_longi, dl_drop_lati, dl_drop_longi;
    ImageView iv_map_point;
    private GoogleMap mMap;
    private LatLng mCenterLatLong;
    private AddressResultReceiver mResultReceiver;

    public double dl_dr_lati,dl_dr_longi;

    public static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int height = display.getHeight();
        return height;
    }

    public static int getDeviceWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        return width;
    }

    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<>();
        if (addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Read Contacts");
        if (addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write Contacts");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = getString(com.movhaul.customer.R.string.ees) + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                }


                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        }

    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                if (!shouldShowRequestPermissionRationale(permission))
                    return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();

                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);


                } else {
                    insertDummyContactWrapper();
                    Toast.makeText(DashboardNavigation.this, com.movhaul.customer.R.string.permis_si, Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.movhaul.customer.R.layout.activity_dashboard);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");
        FontsManager.changeFonts(this);
        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DashboardNavigation.this);
        editor = sharedPreferences.edit();
        mContext = this;

        insertDummyContactWrapper();

        drawer = (DrawerLayout) findViewById(com.movhaul.customer.R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(com.movhaul.customer.R.id.nav_view);
        btn_book_now = (Button) findViewById(com.movhaul.customer.R.id.btn_book_now);
        btn_book_later = (Button) findViewById(com.movhaul.customer.R.id.btn_book_later);
        tv_name = (TextView) findViewById(com.movhaul.customer.R.id.textView_name);
        tv_email = (TextView) findViewById(com.movhaul.customer.R.id.textView_email);
        tv_myTrips = (TextView) findViewById(com.movhaul.customer.R.id.textView_mytrips);
        tv_jobReview = (TextView) findViewById(com.movhaul.customer.R.id.textView_jobreview);
        tv_payments = (TextView) findViewById(com.movhaul.customer.R.id.textView_payments);
        tv_tracking = (TextView) findViewById(com.movhaul.customer.R.id.textView_tracking);
        tv_offers = (TextView) findViewById(com.movhaul.customer.R.id.textView_offers);
        tv_emergencyContacts = (TextView) findViewById(com.movhaul.customer.R.id.textView_emergencycontacts);
        aet_pickup = (AutoCompleteTextView) findViewById(com.movhaul.customer.R.id.editText_pickUp);
        aet_drop = (AutoCompleteTextView) findViewById(com.movhaul.customer.R.id.editText_dropLocation);
        flt_pickup = (TextInputLayout) findViewById(com.movhaul.customer.R.id.float_pickup);
        flt_drop_location = (TextInputLayout) findViewById(com.movhaul.customer.R.id.float_drop);
        lt_drop = (android.widget.LinearLayout) findViewById(com.movhaul.customer.R.id.layout_drop);
        lt_pickup = (android.widget.LinearLayout) findViewById(com.movhaul.customer.R.id.layout_pickuptype);
        btn_menu = (ImageView) findViewById(com.movhaul.customer.R.id.img_menu);
        rightmenu = (ImageView) findViewById(com.movhaul.customer.R.id.right_menu);
        toolbar = (Toolbar) findViewById(com.movhaul.customer.R.id.toolbar);
        lt_first = (LinearLayout) findViewById(com.movhaul.customer.R.id.first);
        lt_second = (FrameLayout) findViewById(com.movhaul.customer.R.id.second);
        lt_last = (LinearLayout) findViewById(com.movhaul.customer.R.id.bottom_layout);
        lt_frame = (FrameLayout) findViewById(com.movhaul.customer.R.id.frame);
        btn_editProfile = (ImageView) findViewById(com.movhaul.customer.R.id.imageView2);
        btn_editProfile_img = (ImageView) findViewById(com.movhaul.customer.R.id.imageView);

        iv_location = (ImageView) findViewById(com.movhaul.customer.R.id.imageview_location);

        fl_bottom_frame = (android.widget.RelativeLayout) findViewById(com.movhaul.customer.R.id.bottom_frame_layout);
        lt_top = (LinearLayout) findViewById(com.movhaul.customer.R.id.top_layout);
        iv_map_point = (ImageView) findViewById(com.movhaul.customer.R.id.map_point);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        flt_pickup.setTypeface(type);
        flt_drop_location.setTypeface(type);
        service_id = sharedPreferences.getString("id", "");
        service_token = sharedPreferences.getString("token", "");
        customer_mobile = sharedPreferences.getString("customer_mobile", "");
        customer_email = sharedPreferences.getString("customer_email", "");
        customer_name = sharedPreferences.getString("customer_name", "");

        bottomSheetViewgroup
                = (android.widget.RelativeLayout) findViewById(com.movhaul.customer.R.id.bottom_sheet);

        bottomSheetBehavior =
                BottomSheetBehavior.from(bottomSheetViewgroup);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        lt_bt_veh_type = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_vehicle_type);
        lt_bt_tow_type = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_tow_type);

        lt_bt_tow_type.setVisibility(View.GONE);

        lt_bt_veh_car = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_bt_car);
        lt_bt_veh_truck = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_bt_truck);
        lt_bt_veh_bus = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_bt_bus);

        lt_bt_veh_tow = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_bt_tow);
        lt_bt_veh_flatbed = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_bt_flatbed);


        Log.e("tag", "ss:" + bottomSheetBehavior.getState());

        btn_book_roadside = (Button) findViewById(com.movhaul.customer.R.id.btn_book_assistance);

        btn_book_roadside.setVisibility(View.GONE);

        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");

        // mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        // Google map init block
        customMapFragment = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(com.movhaul.customer.R.id.map));
        customMapFragment.setOnDragListener(DashboardNavigation.this);

        customMapFragment.getMapAsync(this);

        // GoogleMap map = customMapFragment.getMap();

        lt_filter_dialog = (LinearLayout) findViewById(com.movhaul.customer.R.id.filter_dialog);
        lt_filter_dialog.setVisibility(View.VISIBLE);

        btn_book_now.setEnabled(false);
        btn_book_later.setEnabled(false);
        lt_pickup.setEnabled(false);
        lt_drop.setEnabled(false);

        iv_truck = (ImageView) findViewById(com.movhaul.customer.R.id.image_truck);
        iv_bus = (ImageView) findViewById(com.movhaul.customer.R.id.image_bus);
        iv_road_assit = (ImageView) findViewById(com.movhaul.customer.R.id.image_roadside_assistance);

        mProgressDialog = new ProgressDialog(DashboardNavigation.this, com.movhaul.customer.R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setTitle(com.movhaul.customer.R.string.loading);
        mProgressDialog.setMessage(getString(com.movhaul.customer.R.string.wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        String[] parts = formattedDate.split(" ");
        String part1 = parts[0];
        String part2 = parts[1];

        current_time = parts[1];
        str_time = part1 + " T " + part2;

        final int height = getDeviceHeight(DashboardNavigation.this);
        final int width = getDeviceWidth(DashboardNavigation.this);

        iv_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TranslateAnimation anim_btn_t2b = new TranslateAnimation(0, 0, 0, height);
                anim_btn_t2b.setDuration(500);
                vec_type = "Bus";
                lt_filter_dialog.setAnimation(anim_btn_t2b);
                lt_filter_dialog.setVisibility(View.GONE);

                btn_book_roadside.setVisibility(View.GONE);
                btn_book_now.setEnabled(true);
                btn_book_later.setEnabled(true);
                lt_pickup.setEnabled(true);
                lt_drop.setEnabled(true);
            }
        });


        iv_truck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TranslateAnimation anim_btn_t2b = new TranslateAnimation(0, 0, 0, height);
                anim_btn_t2b.setDuration(500);
                vec_type = "Truck";
                lt_filter_dialog.setAnimation(anim_btn_t2b);
                lt_filter_dialog.setVisibility(View.GONE);

                btn_book_roadside.setVisibility(View.GONE);
                btn_book_now.setEnabled(true);
                btn_book_later.setEnabled(true);
                lt_pickup.setEnabled(true);
                lt_drop.setEnabled(true);


              //  startService(new Intent(DashboardNavigation.this, DriverService.class));

              //  new get_drivers().execute();


            }
        });


        iv_road_assit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TranslateAnimation anim_btn_t2b = new TranslateAnimation(0, 0, 0, height);
                anim_btn_t2b.setDuration(500);
                vec_type = "Road";
                lt_filter_dialog.setAnimation(anim_btn_t2b);
                lt_filter_dialog.setVisibility(View.GONE);

                btn_book_roadside.setVisibility(View.VISIBLE);
                btn_book_now.setEnabled(false);
                btn_book_later.setEnabled(false);
                lt_pickup.setEnabled(true);
                lt_drop.setEnabled(true);
            }
        });


        // mapFragment.getMapAsync(this);

        image_uris = new ArrayList<>();


        iv_zoomin = (ImageView) findViewById(com.movhaul.customer.R.id.zoomin);
        iv_zoomout = (ImageView) findViewById(com.movhaul.customer.R.id.zoomout);


        iv_zoomin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        iv_zoomout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });


        snackbar = Snackbar
                .make(findViewById(com.movhaul.customer.R.id.top), com.movhaul.customer.R.string.no_internet, Snackbar.LENGTH_SHORT);


        snackbar.setActionTextColor(Color.RED);

        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);


        snackbar_loc = Snackbar
                .make(findViewById(com.movhaul.customer.R.id.cd_layout), com.movhaul.customer.R.string.loc, Snackbar.LENGTH_INDEFINITE)
                .setAction(com.movhaul.customer.R.string.open_settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar_loc.dismiss();
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });


        View sbView_loc = snackbar_loc.getView();
        tv_snack_loc = (android.widget.TextView) sbView_loc.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack_loc_act = (android.widget.TextView) sbView_loc.findViewById(android.support.design.R.id.snackbar_action);
        tv_snack_loc.setTextColor(Color.WHITE);
        tv_snack_loc.setTypeface(tf);
        tv_snack_loc_act.setTypeface(tf);
        tv_snack_loc_act.setTextColor(Color.RED);


        mResultReceiver = new AddressResultReceiver(new Handler());
        if (checkPlayServices()) {
            if (!AppUtils.isLocationEnabled(mContext)) {
                snackbar_loc.show();
            }
            buildGoogleApiClient();
        } else {
            snackbar.show();
            tv_snack.setText(com.movhaul.customer.R.string.alocl);
        }







        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, com.movhaul.customer.R.string.app_name, com.movhaul.customer.R.string.app_name)

        {
            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_SETTLING ) {
                    Log.e("tag_Drawer", "Drawer opened by dragging");

                    if(fl_bottom_frame.getVisibility() == View.GONE) {
                        fl_bottom_frame.setVisibility(View.VISIBLE);
                        lt_top.setVisibility(View.VISIBLE);

                        int valueInPixels = (int) getResources().getDimension(com.movhaul.customer.R.dimen._70sdp);
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) lt_frame.getLayoutParams();
                        params.setMargins(0, valueInPixels, 0, 0);
                        lt_frame.setLayoutParams(params);
                    }
                }
            }
        };



        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if (!(sharedPreferences.getString("customer_image", "").equals(""))) {
            Glide.with(DashboardNavigation.this).load(Config.WEB_URL_IMG + "customer_details/" + sharedPreferences.getString("customer_image", "")).into(btn_editProfile_img);
        }


        dialog2 = new Dialog(DashboardNavigation.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(com.movhaul.customer.R.layout.dialogue_profile);
        btn_update = (Button) dialog2.findViewById(com.movhaul.customer.R.id.button_update);
        btn_close = (ImageView) dialog2.findViewById(com.movhaul.customer.R.id.button_close);
        et_username = (EditText) dialog2.findViewById(com.movhaul.customer.R.id.editTextName);
        et_email = (EditText) dialog2.findViewById(com.movhaul.customer.R.id.editTextEmail);
        flt_uname = (TextInputLayout) dialog2.findViewById(com.movhaul.customer.R.id.float_name);
        flt_email = (TextInputLayout) dialog2.findViewById(com.movhaul.customer.R.id.float_email);
        tv_name.setText(customer_name);
        tv_email.setText(customer_email);

        et_username.setTypeface(tf);
        et_email.setTypeface(tf);
        flt_uname.setTypeface(tf);
        flt_email.setTypeface(tf);
        btn_update.setTypeface(tf);


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(et_username.getText().toString().isEmpty())) {
                    if (!(et_email.getText().toString().isEmpty())) {
                        customer_name = et_username.getText().toString();
                        customer_email = et_email.getText().toString();
                        new profile_update().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), com.movhaul.customer.R.string.aec, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), com.movhaul.customer.R.string.aes, Toast.LENGTH_SHORT).show();
                }
                dialog2.dismiss();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();

            }
        });


        dialog1 = new Dialog(DashboardNavigation.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setCancelable(false);
        dialog1.setContentView(com.movhaul.customer.R.layout.dialog_yes_no);
        btn_yes = (Button) dialog1.findViewById(com.movhaul.customer.R.id.button_yes);
        btn_no = (Button) dialog1.findViewById(com.movhaul.customer.R.id.button_no);

        tv_txt1 = (android.widget.TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_1);
        tv_txt2 = (android.widget.TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_2);
        tv_txt3 = (android.widget.TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_3);

        tv_txt1.setTypeface(tf);
        tv_txt2.setTypeface(tf);
        tv_txt3.setTypeface(tf);
        btn_yes.setTypeface(tf);
        btn_no.setTypeface(tf);

        lt_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity();
            }
        });
        lt_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity1();
            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (exit_status == 0) {
                    editor.clear();
                    editor.commit();
                    dialog1.dismiss();
                    Intent i = new Intent(DashboardNavigation.this, LoginActivity.class);
                    startActivity(i);
                    finishAffinity();
                } else if (exit_status == 1) {
                    finishAffinity();
                    dialog1.dismiss();
                }

            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        btn_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.show();
                drawer.closeDrawer(Gravity.LEFT);
                et_username.setText(customer_name);
                et_email.setText(customer_email);
            }
        });
        btn_editProfile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* PhotoPickerIntent intent = new PhotoPickerIntent(DashboardNavigation.this);
                intent.setPhotoCount(1);
                intent.setColumn(3);
                intent.setShowCamera(true);
                startActivityForResult(intent, REQUEST_PROFILE);*/


                com.gun0912.tedpicker.Config config = new com.gun0912.tedpicker.Config();
                config.setSelectionMin(1);
                config.setSelectionLimit(1);
                config.setCameraHeight(com.movhaul.customer.R.dimen.app_camera_height);

                config.setCameraBtnBackground(com.movhaul.customer.R.drawable.round_dr_red);

                config.setToolbarTitleRes(com.movhaul.customer.R.string.custom_title);
                config.setSelectedBottomHeight(com.movhaul.customer.R.dimen.bottom_height);

                ImagePickerActivity.setConfig(config);
                Intent intent = new Intent(DashboardNavigation.this, com.gun0912.tedpicker.ImagePickerActivity.class);
                startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);

            }
        });


        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        rightmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(DashboardNavigation.this, rightmenu);
                popup.getMenuInflater().inflate(com.movhaul.customer.R.menu.menu, popup.getMenu());
                Menu m = popup.getMenu();
                for (int i = 0; i < m.size(); i++) {
                    MenuItem mi = m.getItem(i);
                    SubMenu subMenu = mi.getSubMenu();
                    if (subMenu != null && subMenu.size() > 0) {
                        for (int j = 0; j < subMenu.size(); j++) {
                            MenuItem subMenuItem = subMenu.getItem(j);
                            applyFontToMenuItem(subMenuItem);
                        }
                    }
                    applyFontToMenuItem(mi);
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case com.movhaul.customer.R.id.support: {

                                Intent i = new Intent(DashboardNavigation.this, WebViewAct.class);
                                startActivity(i);

                                return true;
                            }
                            case com.movhaul.customer.R.id.feedback: {
                                return true;
                            }
                            case com.movhaul.customer.R.id.logout: {
                                dialog1.show();
                                exit_status = 0;
                                tv_txt3.setText(com.movhaul.customer.R.string.asdew);
                                return true;
                            }
                            default: {
                                return true;
                            }
                        }
                    }
                });
                popup.show();
            }

        });


        tv_jobReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    Intent i = new Intent(DashboardNavigation.this, MyJobs.class);
                    startActivity(i);
                    drawer.closeDrawer(Gravity.LEFT);


            }
        });
        tv_myTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardNavigation.this, MyTrips.class);
                startActivity(i);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        tv_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardNavigation.this, Tracking.class);
                startActivity(i);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });


        tv_payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardNavigation.this, Payment.class);
                startActivity(i);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        tv_emergencyContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goIntsd = new Intent(getApplicationContext(), EmergencyContacts.class);
                startActivity(goIntsd);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


              getMyLocation();


                // Location loc = mMap.getMyLocation();


               /* if (loc != null) {
                    LatLng latLang = new LatLng(loc.getLatitude(), loc
                            .getLongitude());
                *//*    cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLang, 17);
                mMap.animateCamera(cameraUpdate);
*//*
                    mMap.clear();
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLang).zoom(15f).build();
                    mMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                    //mMap.addMarker(new MarkerOptions().position(latLang));

                }*/


            }
        });


        if (!(sharedPreferences.getString("book_time", "").equals(""))) {


            String dateStart = current_time;
            String dateStop = sharedPreferences.getString("book_time", "");
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date d1 = null;
            Date d2 = null;
            try {
                d1 = format.parse(dateStart);
                d2 = format.parse(dateStop);
                long diff = d2.getTime() - d1.getTime();
                long diffHours = diff / (60 * 60 * 1000) % 24;
                diff = diffHours;
                Log.e("test", diffHours + " hours, " + diff);
            } catch (Exception e) {// TODO: handle exception        }

            }
        } else {
            diff = 7;
        }


        btn_book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (diff >= 0) {

                    if (aet_drop.getText().toString().isEmpty()) {
                        // Toast.makeText(getApplicationContext(), "Choose Drop Location", Toast.LENGTH_LONG).show();
                        snackbar.show();
                        tv_snack.setText(com.movhaul.customer.R.string.adsfb);
                    } else {
                        editor.putString("pickup", mStreetOutput + ", " + mCityOutput);
                        editor.putString("drop", aet_drop.getText().toString());
                        editor.putString("pickup_lati", mPickup_lat);
                        editor.putString("pickup_long", mPickup_long);
                        editor.putString("drop_lati", mDrop_lat);
                        editor.putString("drop_long", mDrop_long);
                        editor.commit();
                        Intent i = new Intent(DashboardNavigation.this, Book_now.class);
                        i.putExtra("vec_type", vec_type);
                        startActivity(i);
                    }
                } else {
                    snackbar.show();
                    tv_snack.setText(com.movhaul.customer.R.string.asdf);
                }


            }
        });

        btn_book_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Intent i = new Intent(DashboardNavigation.this, Book_later.class);
                startActivity(i);*/
                if (aet_drop.getText().toString().isEmpty()) {
                    // Toast.makeText(getApplicationContext(), "Choose Drop Location", Toast.LENGTH_LONG).show();
                    snackbar.show();
                    tv_snack.setText(com.movhaul.customer.R.string.aewas);
                } else {
                    editor.putString("pickup", mStreetOutput + ", " + mCityOutput);
                    editor.putString("drop", aet_drop.getText().toString());
                    editor.putString("pickup_lati", mPickup_lat);
                    editor.putString("pickup_long", mPickup_long);
                    editor.putString("drop_lati", mDrop_lat);
                    editor.putString("drop_long", mDrop_long);
                    editor.commit();
                    Intent i = new Intent(DashboardNavigation.this, Book_later.class);
                    i.putExtra("vec_type", vec_type);
                    startActivity(i);
                }

            }
        });


        dg_road_confirm = new Dialog(DashboardNavigation.this);
        dg_road_confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dg_road_confirm.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dg_road_confirm.setCancelable(false);
        dg_road_confirm.setContentView(com.movhaul.customer.R.layout.dialog_road_confirm);

        btn_yes = (Button) dg_road_confirm.findViewById(com.movhaul.customer.R.id.button_yes);
        tv_txt1 = (android.widget.TextView) dg_road_confirm.findViewById(com.movhaul.customer.R.id.textView_1);
        tv_txt2 = (android.widget.TextView) dg_road_confirm.findViewById(com.movhaul.customer.R.id.textView_2);

        tv_txt1.setTypeface(tf);
        tv_txt2.setTypeface(tf);
        btn_yes.setTypeface(tf);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dg_road_confirm.dismiss();
                TranslateAnimation anim_btn_b2t = new TranslateAnimation(0, 0, height, 0);
                anim_btn_b2t.setDuration(500);
                lt_filter_dialog.setAnimation(anim_btn_b2t);
                lt_filter_dialog.setVisibility(View.VISIBLE);
                aet_drop.setText("");
            }
        });


        btn_book_roadside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //intent.putExtra("apiKey", "U1lTUC4xNUhPMTIkMTIzLjR8U1lTUA==");
                // intent.putExtra("txnToken", "d3hyVTMxSmxpUXNxNjh6MTBqRXF4Wms3Vll1OTN4S1c2dkMrL2VjWkFpQm9BUzRNb2VGWFpRPT0=");

            /*    Intent intent = new Intent(DashboardNavigation.this, RemitaMainActivity.class);
                intent.putExtra("amount", "250");
                intent.putExtra("testMode", false);
                intent.putExtra("apiKey", "U1lTUC4xNUhPMTIkMTIzLjR8U1lTUA==");
                intent.putExtra("txnToken", "55316C54554334784E5568504D54496B4D54497A4C6A523855316C5455413D3D7C3932333737633266613035313135306337363534386636376266623131303165383831366464343834666234363064653062343731663538643461323835303537333638653232313135363366383334666337613166333265333336653834626539656566393465396363356131363739353463646239333434363164313732");
                startActivityForResult(intent, 102);*/


                if (aet_drop.getText().toString().isEmpty()) {
                    snackbar.show();
                    tv_snack.setText(com.movhaul.customer.R.string.drpa);
                } else {

                    str_pickup = mStreetOutput + ", " + mCityOutput;
                    str_drop = aet_drop.getText().toString();
                    str_pickup_lati = mPickup_lat;
                    str_pickup_longi = mPickup_long;
                    str_drop_lati = mDrop_lat;
                    str_drop_longi = mDrop_long;

                    // new book_roadside().execute();

                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });


        lt_bt_veh_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lt_bt_tow_type.setVisibility(View.VISIBLE);
                lt_bt_veh_type.setVisibility(View.GONE);
                cu_vec_type = "car";
            }
        });
        lt_bt_veh_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lt_bt_tow_type.setVisibility(View.VISIBLE);
                lt_bt_veh_type.setVisibility(View.GONE);
                cu_vec_type = "bus";
            }
        });
        lt_bt_veh_truck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lt_bt_tow_type.setVisibility(View.VISIBLE);
                lt_bt_veh_type.setVisibility(View.GONE);
                cu_vec_type = "truck";
            }
        });

        lt_bt_veh_flatbed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dr_vec_type = "flatbed";
                lt_bt_tow_type.setVisibility(View.GONE);
                lt_bt_veh_type.setVisibility(View.VISIBLE);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                new book_roadside().execute();

                String amt = "322";
                String amt1 = "arefadf";
                String amt2 = "334aadf32";
                //new payment_token(amt,amt1,amt2).execute();

            }
        });
        lt_bt_veh_tow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dr_vec_type = "tow";
                lt_bt_tow_type.setVisibility(View.GONE);
                lt_bt_veh_type.setVisibility(View.VISIBLE);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                new book_roadside().execute();

                String amt = "322";
                String amt1 = "arefadf";
                String amt2 = "334aadf32";
                //new payment_token(amt,amt1,amt2).execute();

            }
        });


    }

    private void getMyLocation() {

        if (mMap != null) {
            Location myLocation = mMap.getMyLocation();

            if (myLocation != null) {
                LatLng myLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

                CameraPosition myPosition = new CameraPosition.Builder().target(myLatLng).zoom(17).bearing(90).tilt(30).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
            }
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null)


                mPickup_lat = String.valueOf(location.getLatitude());
            mPickup_long = String.valueOf(location.getLongitude());

            changeMap(location);
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            mGoogleApiClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        insertDummyContactWrapper();

        if (checkPlayServices()) {
            if (!AppUtils.isLocationEnabled(mContext)) {
                snackbar_loc.show();
            }
            buildGoogleApiClient();
        } else {
            snackbar.show();
            tv_snack.setText(com.movhaul.customer.R.string.asdloc);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void show_disable() {

        //Activity context = DashboardNavigation.this;
//

        //lt_bottom = (LinearLayout) findViewById(R.id.bottom_layout);

        if (mMapIsTouched) {
            Log.e("tagmap", "istouched");
            // lt_top = (LinearLayout) findViewById(R.id.top_layout);
            ////  lt_bottom = (LinearLayout) findViewById(R.id.bottom_layout);

            //   lt_top.setVisibility(View.GONE);
            //   lt_bottom.setVisibility(View.GONE);
        } else {
            Log.e("tagmap", "nottouched");
            //  lt_top = (LinearLayout) findViewById(R.id.top_layout);
            // lt_bottom = (LinearLayout) findViewById(R.id.bottom_layout);

            //  lt_top.setVisibility(View.VISIBLE);
            //  lt_bottom.setVisibility(View.VISIBLE);
        }

    }


    /////////////////////////

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // DashboardNavigation.this.unregisterReceiver(appendChatScreenMsgReceiver);
    }

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.

         //   AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setTypeFilter(Place.TYPE_COUNTRY).setCountry("US").build();
            //.setBoundsBias(new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466)))
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent, REQUEST_AC_PICKUP);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = getString(com.movhaul.customer.R.string.goglso) +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void openAutocompleteActivity1() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
           // AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setTypeFilter(Place.TYPE_COUNTRY).setCountry("US").build();
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent, REQUEST_AC_DROP);

          //  lt_pickup.setEnabled(false);
            p_loc_comp = true;


        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = getString(com.movhaul.customer.R.string.goglso) +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_AC_PICKUP) {
            Log.e("tag", "request");
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(mContext, data);
                // TODO call location based filter
                LatLng latLong;

                aet_pickup.setText("");
                aet_pickup.append(place.getAddress());
                Log.e("tagplace", " place " + place.getAddress() + " attrib " + place.getAttributions() + " name " + place.getName() + " phone " + place.getPhoneNumber() + " latlon " + place.getLatLng().toString());


                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (mMap != null) {
                    mMap.getUiSettings().setZoomControlsEnabled(false);

                   // mMap.clear();


                    latLong = place.getLatLng();
                    dl_pick_lati = latLong.latitude;
                    dl_pick_longi = latLong.longitude;

                    Log.e("tag","d: :"+dl_drop_longi);

                    if(p_loc_comp)
                    draw_line(dl_pick_lati,dl_pick_longi,dl_drop_lati,dl_drop_longi);

                    //latLong = new LatLng(location.getLatitude(), location.getLongitude());

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLong).zoom(15f).build();

                    //  mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    mMap.getUiSettings().setZoomControlsEnabled(false);
                    mMap.getUiSettings().setZoomGesturesEnabled(true);
                    mMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                    //mMap.addMarker(new MarkerOptions().position(latLong));

                }


            }


        } else if (requestCode == REQUEST_AC_DROP) {
            Log.e("tag", "request_drop");

            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place1 = PlaceAutocomplete.getPlace(mContext, data);
                // TODO call location based filter
                LatLng latLong;
                latLong = place1.getLatLng();
                aet_drop.setText("");

                aet_drop.append(place1.getAddress());
                Log.e("tag", "place111" + place1.getAddress());
                Log.e("tag", "la00: " + place1.getLatLng());
                mDrop_lat = String.valueOf(latLong.latitude);
                mDrop_long = String.valueOf(latLong.longitude);

                dl_drop_lati = latLong.latitude;
                dl_drop_longi = latLong.longitude;


                draw_line(dl_pick_lati,dl_pick_longi,dl_drop_lati,dl_drop_longi);


            }


        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(mContext, data);
        } else if (resultCode == RESULT_CANCELED) {
            // Indicates that the activity closed before a selection was made. For example if
            // the user pressed the back button.
        }

 /*       List<String> photos = null;
        if (resultCode == RESULT_OK && requestCode == REQUEST_PROFILE) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            Log.d("tag", "img: " + selectedPhotos.get(0));
            str_profile_img = selectedPhotos.get(0);
            //Picasso.with(ProfileActivity.this).load(new File(str_profile_img)).into(iv_profile);
            new profile_update().execute();
            // Glide.with(DashboardNavigation.this).load(new File(str_profile_img)).into(btn_editProfile_img);
        }*/

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resultCode == Activity.RESULT_OK) {

            image_uris = data.getParcelableArrayListExtra(com.gun0912.tedpicker.ImagePickerActivity.EXTRA_IMAGE_URIS);
            Log.e("tag", "12345" + image_uris);
            selectedPhotos.clear();
            if (image_uris != null) {
                str_profile_img = image_uris.get(0).toString();
                selectedPhotos.add(str_profile_img);
                new profile_update().execute();
            }
        }

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PAYMENT) {
            Log.e("tag", "cd:" + resultCode);
            Log.e("tag", "rc:" + requestCode);
            Log.e("tag", "dt:" + data.toString());

            Bundle bundle = data.getExtras();
            String authorizid = bundle.getString("authoriztionId");
            String remitaTransRef= bundle.getString("remitaTransRef");
            String responceCode = bundle.getString("responseCode");

            Log.e("tag","ss: "+authorizid);
            Log.e("tag","ss1: "+remitaTransRef);
            Log.e("tag","ss2: "+responceCode);


            /*final int height = getDeviceHeight(DashboardNavigation.this);
            TranslateAnimation anim_btn_b2t = new TranslateAnimation(0, 0, height, 0);
            anim_btn_b2t.setDuration(500);
            lt_filter_dialog.setAnimation(anim_btn_b2t);
            lt_filter_dialog.setVisibility(View.VISIBLE);
            aet_drop.setText("");*/


            //

            ///customer/emergencypayment


            new book_now_task().execute();


        }


    }

    private void draw_line(double dl_pick_lati,double dl_pick_longi,double dl_drop_lati,double dl_drop_longi) {


       // mMap.clear();

        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .position(new LatLng(dl_pick_lati, dl_pick_longi)));

        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(new LatLng(dl_drop_lati, dl_drop_longi)));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(dl_drop_lati, dl_drop_longi)).zoom(15f).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        iv_map_point.setVisibility(View.GONE);
        /*Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(dl_pick_lati, dl_pick_longi), new LatLng(dl_drop_lati, dl_drop_longi))
                .width(5)
                .color(Color.RED));*/


        // Getting URL to the Google Directions API

        String str_origin = "origin=" + dl_pick_lati + "," + dl_pick_longi;
        String str_dest = "destination=" + dl_drop_lati + "," + dl_drop_longi;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);

    }

    /**
     * A method to download json data from url
     */
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

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            changeMap(mLastLocation);
            Log.d(TAG, "ON connected");

        } else
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    protected void displayAddressOutput() {

        // Log.e("tag00", "output11111" + mAddressOutput);
        //aet_pickup.setText(mAddressOutput);
        try {
            if (mAreaOutput != null)
                // aet_pickup.setText(mAreaOutput+ "");
                Log.e("tag11", "output" + mAddressOutput);
            aet_pickup.setText("");

            aet_pickup.setText(mAddressOutput);
            //aet_pickup.setText(mAreaOutput);
            //   Log.e("tag22", "output" + mAreaOutput);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.e("tagmap", "OnMapReady");
        mMap = googleMap;


        // mMap.getUiSettings().setMapToolbarEnabled(false);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        } else {
            mMap.setMyLocationEnabled(true);
        }
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    protected void startIntentService(Location mLocation) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //finish();
                snackbar.show();
                tv_snack.setText(com.movhaul.customer.R.string.google_play);
            }
            return false;
        }
        return true;
    }

    private void changeMap(Location location) {

        //  Log.e(TAG, "Reaching map" + mMap);

        Log.e("tagmap", "change_map_started");


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // check if map is created successfully or not
        if((aet_pickup.getText().toString().isEmpty())) {
            if (mMap != null) {
                LatLng latLong;
                Log.e("tagmap", "change_map_map_not_null");
                //mMap.clear();
                dl_pick_lati = location.getLatitude();
                dl_pick_longi = location.getLongitude();
                latLong = new LatLng(dl_pick_lati, dl_pick_longi);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLong).zoom(15f).build();
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
                startIntentService(location);
            } else {
                Toast.makeText(getApplicationContext(),
                        com.movhaul.customer.R.string.unabs, Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (lt_filter_dialog.getVisibility() == View.VISIBLE) {
            dialog1.show();
            exit_status = 1;
            tv_txt3.setText(com.movhaul.customer.R.string.ext);
        } else {
            final int height = getDeviceHeight(DashboardNavigation.this);
            TranslateAnimation anim_btn_b2t = new TranslateAnimation(0, 0, height, 0);
            anim_btn_b2t.setDuration(500);
            lt_filter_dialog.setAnimation(anim_btn_b2t);
            lt_filter_dialog.setVisibility(View.VISIBLE);
            aet_drop.setText("");
            p_loc_comp = false;
           // mMap.clear();
            getMyLocation();
            iv_map_point.setVisibility(View.VISIBLE);
            mResultReceiver = new AddressResultReceiver(new Handler());


        }
    }



    @Override
    public void onDrag(MotionEvent motionEvent) {

        Log.e("tag", "motion:" + motionEvent);

        if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
            Log.e("tag", "motion_events" + "  motion_changes");

            if (lt_filter_dialog.getVisibility() != View.VISIBLE) {
                if (!p_loc_comp) {
                    fl_bottom_frame.setVisibility(View.GONE);
                    lt_top.setVisibility(View.GONE);
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) lt_frame.getLayoutParams();
                    params.setMargins(0, 5, 0, 0);
                    lt_frame.setLayoutParams(params);
                }
            }

        } else {
            Log.e("tag", "motion_events" + "  not_changes");

            if(!p_loc_comp) {
                fl_bottom_frame.setVisibility(View.VISIBLE);
                lt_top.setVisibility(View.VISIBLE);
                int valueInPixels = (int) getResources().getDimension(com.movhaul.customer.R.dimen._70sdp);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) lt_frame.getLayoutParams();
                params.setMargins(0, valueInPixels, 0, 0);
                lt_frame.setLayoutParams(params);
                if (!p_loc_comp) {
                    Log.e("tag", "after_drop");
                    change_map_camera();
                } else {
                    Log.e("tag", "changen nothing");
                }
            }
        }


    }

    public void change_map_camera() {

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.e("tagmap", "Camera postion change" + cameraPosition + "");
                mCenterLatLong = cameraPosition.target;
                dl_pick_lati = mCenterLatLong.latitude;
                dl_pick_longi = mCenterLatLong.longitude;


                if (!p_loc_comp) {

                    try {

                        Location mLocation = new Location("");
                        mLocation.setLatitude(dl_pick_lati);
                        mLocation.setLongitude(dl_pick_longi);
                       // mMap.clear();
                        str_lati = String.valueOf(mCenterLatLong.latitude);
                        str_longi = String.valueOf(mCenterLatLong.longitude);
                        startIntentService(mLocation);
                        geocoder = new Geocoder(DashboardNavigation.this, Locale.getDefault());
                        try {
                            addresses = geocoder.getFromLocation(mCenterLatLong.latitude, mCenterLatLong.longitude, 1);
                            str_locality = addresses.get(0).getLocality();
                            str_address = addresses.get(0).getAddressLine(0);
                            Log.e("tagplace0", "lati: " + str_lati + "longi: " + str_longi + "\nlocality: " + str_locality + "\taddr0: " + str_address +
                                    "\naddr1: " + addresses.get(0).getAddressLine(1) + "\n addr2: " + addresses.get(0).getAddressLine(2) + "\n adminarea: "
                                    + addresses.get(0).getAdminArea() + "\n feature name: " + addresses.get(0).getFeatureName() + "\n Sub loca: "
                                    + addresses.get(0).getSubLocality() + "\n subadmin: " + addresses.get(0).getSubAdminArea()
                                    + "\n premisis: " + addresses.get(0).getPremises() + "\n postal " + addresses.get(0).getPostalCode());
                            mStreetOutput = str_address;
                            mCityOutput = str_locality;
                            mPickup_lat = str_lati;
                            mPickup_long = str_longi;
                        } catch (Exception e) {
                            Log.e("tag", "er_geocoder: " + e.toString());
                        }
                        // new updateLocation().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("tag", "eroo:" + e.toString());
                    } finally {
                        lt_first.setVisibility(View.VISIBLE);
                        lt_second.setVisibility(View.VISIBLE);
                        lt_last.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("tag", "mapnotworks:");
                }


            }
        });


    }

    // Fetches data from url passed
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
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
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
                    lineOptions.color(getResources().getColor(com.movhaul.customer.R.color.redColor));

                }

                // Drawing polyline in the Google Map for the i-th route
                mMap.addPolyline(lineOptions);
            }
        }
    }

    public class profile_update extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
        }

        @Override
        protected String doInBackground(String... strings) {

            if (selectedPhotos.size() > 0) {
                String json = "", jsonStr = "";
                try {
                    //driver/driverupdate
                    String responseString = null;
                    HttpClient httpclient = new DefaultHttpClient();
                    //HttpPost httppost = new HttpPost("https://demotest007.000webhostapp.com/webapi/Api/candidate_reg_image");
                    HttpPost httppost = new HttpPost(Config.WEB_URL + "customer/customerupdate");
                    httppost.setHeader("id", id);
                    httppost.setHeader("sessiontoken", token);
                    httppost.setHeader("customer_email", customer_email);
                    httppost.setHeader("customer_name", customer_name);

                /*    httppost.setHeader("candidate_name", "jj");
                    httppost.setHeader("fathers_name", "dgfh");
                    httppost.setHeader("mobile_no", "67867967676");
                    httppost.setHeader("date_of_birth", "9-2-92");
                    httppost.setHeader("gender", "hghhv");
                    httppost.setHeader("address", "jhvgjv");
                    httppost.setHeader("voter_id", "hvhvhj");
                    httppost.setHeader("aadhar_id", "hvhv");
                    httppost.setHeader("party_name", "hvhv");*/

                    try {

                        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                        entity.addPart("customerimage", new FileBody(new File(str_profile_img), "image/jpeg"));
                        //entity.addPart("candidate_profile", new FileBody(new File(str_profile_img), "image/jpeg"));
                        Log.e("tag", "img: if ");
                        httppost.setEntity(entity);
                        HttpResponse response = httpclient.execute(httppost);
                        HttpEntity r_entity = response.getEntity();
                        int statusCode = response.getStatusLine().getStatusCode();
                        Log.e("tag", response.getStatusLine().toString());
                        if (statusCode == 200) {
                            responseString = EntityUtils.toString(r_entity);

                            Log.e("InputStream00", responseString);
                        } else {
                            responseString = "Error occurred! Http Status Code: "
                                    + statusCode;
                        }
                    } catch (ClientProtocolException e) {
                        responseString = e.toString();
                    } catch (IOException e) {
                        responseString = e.toString();
                    }
                    return responseString;
                } catch (Exception e) {
                    Log.e("InputStream0", e.getLocalizedMessage());
                }
                return null;
            } else {

                Log.e("tag", "no poto");
                String s = "";
                JSONObject jsonObject = new JSONObject();
                try {
                    Log.e("tag", customer_name);
                    Log.e("tag", customer_email);
                    jsonObject.put("customer_name", customer_name);
                    jsonObject.put("customer_email", customer_email);
                    String json = jsonObject.toString();
                    return s = HttpUtils.makeRequest1(com.movhaul.customer.Config.WEB_URL + "customer/customerupdate", json, id, token);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", e.toString());
                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);


            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");

                    Log.d("tag", "<-----Status----->" + status);

                    if (status.equals("true")) {

                        String img = jo.getString("customer_image");
                        String name = jo.getString("customer_name");
                        String email = jo.getString("customer_email");


                        selectedPhotos.clear();
                        Glide.with(DashboardNavigation.this).load(Config.WEB_URL_IMG + "customer_details/" + img).into(btn_editProfile_img);

                        editor.putString("customer_image", img);
                        editor.putString("customer_name", name);
                        editor.putString("customer_email", email);
                        editor.commit();

                        Log.e("tag", "img: " + Config.WEB_URL_IMG + "customer_details/" + img);
                        Log.e("tag", "img: " + name);
                        Log.e("tag", "img: " + email);

                        tv_name.setText(name);
                        tv_email.setText(email);

                        //Glide.with(DashboardNavigation.this).load(new File(str_profile_img)).into(btn_editProfile_img);

                    } else {
                        Toast.makeText(getApplicationContext(), com.movhaul.customer.R.string.asder, Toast.LENGTH_LONG).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    //Toast.makeText(getApplicationContext(), "Network Errror0", Toast.LENGTH_LONG).show();

                }
            } else {
                //Toast.makeText(getApplicationContext(), "Network Errror1", Toast.LENGTH_LONG).show();
            }

        }

    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY);
            mAreaOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_AREA);
            mCityOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_CITY);
            mStreetOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_STREET);

            displayAddressOutput();
            // Show a toast message if an address was found.
            if (resultCode == AppUtils.LocationConstants.SUCCESS_RESULT) {
                //  showToast(getString(R.string.address_found));
            }
        }
    }

    public class payment_token extends AsyncTask<String, Void, String> {

        String amount, txn_ref, narrat, status;

        payment_token(String amt, String ref, String narration) {
            this.amount = amt;
            this.txn_ref = ref;
            this.narrat = narration;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String amt = URLEncoder.encode(amount, "UTF-8");
                String ref = URLEncoder.encode(txn_ref, "UTF-8");
                String narration = URLEncoder.encode(narrat, "UTF-8");
                String URL = "http://104.197.80.225:8080/remitaserver/?amount=" + amt + "&transRef=" + ref + "&narration=" + narration;
                return status = HttpUtils.makeRequest0(URL);
            } catch (Exception e) {
                e.printStackTrace();
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
                    String Txn_Ref = jo.getString("txn_token");
                    Log.e("tag", "ref:" + Txn_Ref);

                    Intent intent = new Intent(DashboardNavigation.this, RemitaMainActivity.class);
                    intent.putExtra("amount", "250");
                    intent.putExtra("testMode", true);
                    intent.putExtra("apiKey", "U1lTUC4xNUhPMTIkMTIzLjR8U1lTUA==");
                    intent.putExtra("txnToken", "55316C54554334784E5568504D54496B4D54497A4C6A523855316C5455413D3D7C3932333737633266613035313135306337363534386636376266623131303165383831366464343834666234363064653062343731663538643461323835303537333638653232313135363366383334666337613166333265333336653834626539656566393465396363356131363739353463646239333434363164313732");
                    startActivityForResult(intent, 102);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    Toast.makeText(getApplicationContext(), com.movhaul.customer.R.string.ase, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), com.movhaul.customer.R.string.aese, Toast.LENGTH_LONG).show();
            }

        }

    }

    public class book_roadside extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "booking_task");
            mProgressDialog.show();


        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";

            Log.e("tag", "no poto");

            String s = "";
            JSONObject jsonObject = new JSONObject();
            try {


                jsonObject.put("pickup_location", str_pickup);
                jsonObject.put("drop_location", str_drop);
                jsonObject.put("goods_type", "road");
                jsonObject.put("vehicle_type", dr_vec_type);//DRIVER ROAD ASSISTANCE TYPE
                jsonObject.put("vehicle_sub_type", cu_vec_type);//CUSTOMER VEHICLE TYPE
                jsonObject.put("booking_time", str_time);
                jsonObject.put("pickup_latitude", str_pickup_lati);
                jsonObject.put("pickup_longitude", str_pickup_longi);
                jsonObject.put("drop_latitude", str_drop_lati);
                jsonObject.put("drop_longitude", str_drop_longi);


                json = jsonObject.toString();

                return s = HttpUtils.makeRequest1(com.movhaul.customer.Config.WEB_URL + "customer/emergencybooking", json, id, token);
            } catch (JSONException e) {
                e.printStackTrace();
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
                    String msg = jo.getString("message");

                    Log.d("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {
                        Log.e("tag", msg);
                        String bookingid = jo.getString("booking_id");
                        String cost = jo.getString("price");
                        editor.putString("job_id", bookingid);
                        editor.commit();

                        Intent intent = new Intent(DashboardNavigation.this, RemitaMainActivity.class);
                        intent.putExtra("amount", cost);
                        intent.putExtra("testMode", true);
                        intent.putExtra("apiKey", "U1lTUC4xNUhPMTIkMTIzLjR8U1lTUA==");
                        intent.putExtra("txnToken", "55316C54554334784E5568504D54496B4D54497A4C6A523855316C5455413D3D7C3932333737633266613035313135306337363534386636376266623131303165383831366464343834666234363064653062343731663538643461323835303537333638653232313135363366383334666337613166333265333336653834626539656566393465396363356131363739353463646239333434363164313732");
                        startActivityForResult(intent, REQUEST_CODE_PAYMENT);


                    } else if (status.equals("false")) {

                        Log.e("tag", "Location not updated");
                        //has to check internet and location...
                        Toast.makeText(getApplicationContext(), com.movhaul.customer.R.string.eer, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    Toast.makeText(getApplicationContext(), com.movhaul.customer.R.string.era, Toast.LENGTH_LONG).show();
                }
            } else {
                // Toast.makeText(getApplicationContext(),"Network Errror1",Toast.LENGTH_LONG).show();
            }

        }

    }

    public class book_now_task extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";


            Log.e("tag", "no poto");

            String s = "";
            JSONObject jsonObject = new JSONObject();
            try {


                jsonObject.put("transaction_id", "Sha30sdwelsd");
                jsonObject.put("booking_id", sharedPreferences.getString("job_id", ""));


                json = jsonObject.toString();

                return s = HttpUtils.makeRequest1(com.movhaul.customer.Config.WEB_URL + "customer/emergencypayment", json, id, token);
            } catch (JSONException e) {
                e.printStackTrace();
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

                        dg_road_confirm.show();
                    } else if (status.equals("false")) {

                        Log.e("tag", "Location not updated");
                        //has to check internet and location...
                        Toast.makeText(getApplicationContext(), "Network Errror. Please Try Again Later", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    Toast.makeText(getApplicationContext(), "Network Errror. Please Try Again Later", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Network Errror1", Toast.LENGTH_LONG).show();
            }

        }

    }




    public class FirstService extends Service {

        private  String TAG = "tagss";

        @Override
        public IBinder onBind(Intent arg0) {
            // TODO Auto-generated method stub
            return null;
        }



        @Override
        public void onStart(Intent intent, int startId) {
            // TODO Auto-generated method stub
            super.onStart(intent, startId);
            Log.e(TAG, "FirstService started");
            //this.stopSelf();
        }

        @Override
        public void onDestroy() {
            // TODO Auto-generated method stub
            super.onDestroy();
            Log.e(TAG, "FirstService destroyed");
        }

    }


    public static void geee(){

        //if(dl_pick_lati != 0.0)
        //new get_drivers().execute();

    }
    public  void getDrv(){

    }


    public  class get_drivers extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            String s = "";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("latitude", String.valueOf(dl_pick_lati));
                jsonObject.put("longitude",  String.valueOf(dl_pick_longi));
                json = jsonObject.toString();

                return s = HttpUtils.makeRequest1(com.movhaul.customer.Config.WEB_URL + "customer/finddrivers", json, id, token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
         //   Log.e("tag", "tag" + s);

            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                   // Log.d("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {

                        Log.d("tag", "<-----true----->" + s);

                        JSONArray jsoi = jo.getJSONArray("message");

                        JSONObject jos = jsoi.getJSONObject(0);

                        double latitude = Double.valueOf(jos.getString("driver_latitude"));
                        double longitude =  Double.valueOf(jos.getString("driver_longitude"));

                        new get_drivers().execute();

                        if(dl_dr_lati != latitude){
                            Log.e("tag","0la:"+latitude);
                            Log.e("tag","1la:"+dl_dr_lati);
                            dl_dr_lati = latitude;
                            dl_dr_longi = longitude;


                            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.trk_img))
                                    .position(new LatLng(dl_dr_lati, dl_dr_longi)));

                        }
                        else{
                            Log.e("tag","qw_la0:"+latitude);
                            Log.e("tag","qw_la1:"+dl_dr_lati);
                        }

                      //  new get_drivers().execute();10
                    } else if (status.equals("false")) {

                        Log.e("tag", "Location not updated");
                        //has to check internet and location...

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                }
            } else {
            }

        }

    }










}



