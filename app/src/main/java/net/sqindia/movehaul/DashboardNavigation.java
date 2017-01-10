package net.sqindia.movehaul;


import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;
import com.sloop.fonts.FontsManager;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.Locale;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;


interface BooleanChangeDispatcher {

    public void addBooleanChangeListener(BooleanChangeListener listener);

    public boolean getFlag();

    public void setFlag(boolean flag);

}

interface BooleanChangeListener extends EventListener {

    public void stateChanged(BooleanChangeEvent event);

}

public class DashboardNavigation extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, com.google.android.gms.location.LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, BooleanChangeDispatcher {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final int REQUEST_CODE_AUTOCOMPLETE1 = 2;
    private static final int REQUEST_PROFILE = 5;
    public static boolean mMapIsTouched = false;
    private static String TAG = "tag_MAP LOCATION";
    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStreetOutput;
    protected GoogleApiClient mGoogleApiClient;
    Context mContext;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    Button btn_book_now, btn_book_later;
    TextView tv_name, tv_email, tv_myTrips, tv_jobReview, tv_payments, tv_tracking, tv_offers, tv_emergencyContacts;
    AutoCompleteTextView starting, destination;
    TextInputLayout flt_pickup, flt_droplocation;
    ImageView btn_menu, rightmenu;
    android.widget.LinearLayout droplv, pickuplv;
    Dialog dialog1, dialog2;
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
    SupportMapFragment mapFragment;
    // TouchableMapFragment mapFragment;
    ImageView btn_editProfile, btn_close, btn_editProfile_img;
    EditText et_username, et_email;
    String id, token, mPickup_lat, mPickup_long, mDrop_lat, mDrop_long;
    TextInputLayout flt_uname, flt_email;
    ArrayList<String> selectedPhotos = new ArrayList<>();
    int diff;
    LinearLayout lt_top, lt_bottom;
    BroadcastReceiver appendChatScreenMsgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            if (mMapIsTouched) {
                Log.e("tagmap", "istouched");
                lt_top.setVisibility(View.GONE);
                lt_bottom.setVisibility(View.GONE);
            } else {
                Log.e("tagmap", "nottouched");
                lt_top.setVisibility(View.VISIBLE);
                lt_bottom.setVisibility(View.VISIBLE);
            }


        }
    };
    private GoogleMap mMap;
    private LatLng mCenterLatLong;
    private AddressResultReceiver mResultReceiver;
    private boolean flag;
    private List<BooleanChangeListener> listeners;


    public DashboardNavigation(){

    }

    public DashboardNavigation(boolean initialFlagState) {
        flag = initialFlagState;
        listeners = new ArrayList<BooleanChangeListener>();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");
        FontsManager.changeFonts(this);
        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DashboardNavigation.this);
        editor = sharedPreferences.edit();
        mContext = this;

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        btn_book_now = (Button) findViewById(R.id.btn_book_now);
        btn_book_later = (Button) findViewById(R.id.btn_book_later);
        tv_name = (TextView) findViewById(R.id.textView_name);
        tv_email = (TextView) findViewById(R.id.textView_email);
        tv_myTrips = (TextView) findViewById(R.id.textView_mytrips);
        tv_jobReview = (TextView) findViewById(R.id.textView_jobreview);
        tv_payments = (TextView) findViewById(R.id.textView_payments);
        tv_tracking = (TextView) findViewById(R.id.textView_tracking);
        tv_offers = (TextView) findViewById(R.id.textView_offers);
        tv_emergencyContacts = (TextView) findViewById(R.id.textView_emergencycontacts);
        starting = (AutoCompleteTextView) findViewById(R.id.editText_pickUp);
        destination = (AutoCompleteTextView) findViewById(R.id.editText_dropLocation);
        flt_pickup = (TextInputLayout) findViewById(R.id.float_pickup);
        flt_droplocation = (TextInputLayout) findViewById(R.id.float_drop);
        droplv = (android.widget.LinearLayout) findViewById(R.id.layout_drop);
        pickuplv = (android.widget.LinearLayout) findViewById(R.id.layout_pickuptype);
        btn_menu = (ImageView) findViewById(R.id.img_menu);
        rightmenu = (ImageView) findViewById(R.id.right_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lt_first = (LinearLayout) findViewById(R.id.first);
        lt_second = (FrameLayout) findViewById(R.id.second);
        lt_last = (LinearLayout) findViewById(R.id.bottom_layout);
        lt_frame = (FrameLayout) findViewById(R.id.frame);
        btn_editProfile = (ImageView) findViewById(R.id.imageView2);
        btn_editProfile_img = (ImageView) findViewById(R.id.imageView);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        flt_pickup.setTypeface(type);
        flt_droplocation.setTypeface(type);
        service_id = sharedPreferences.getString("id", "");
        service_token = sharedPreferences.getString("token", "");
        customer_mobile = sharedPreferences.getString("customer_mobile", "");
        customer_email = sharedPreferences.getString("customer_email", "");
        customer_name = sharedPreferences.getString("customer_name", "");

        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // mapFragment = (TouchableMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);


        DashboardNavigation.this.registerReceiver(this.appendChatScreenMsgReceiver, new IntentFilter("appendChatScreenMsg"));

        lt_top = (LinearLayout) findViewById(R.id.top_layout);
        lt_bottom = (LinearLayout) findViewById(R.id.bottom_layout);


        BooleanChangeListener listener = new BooleanChangeListener() {
            @Override
            public void stateChanged(BooleanChangeEvent event) {
                Log.e("tagmap","Detected change to: "
                        + " -- event: " + event);
            }
        };

        DashboardNavigation test = new DashboardNavigation(mMapIsTouched);
        test.addBooleanChangeListener(listener);

        test.setFlag(mMapIsTouched); // no change, no event dispatch


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String[] parts = formattedDate.split(" ");
        current_time = parts[1];


        snackbar = Snackbar
                .make(findViewById(R.id.top), "Network Error! Please Try Again Later.", Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.RED);

        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);


        snackbar_loc = Snackbar
                .make(findViewById(R.id.drawer_layout), "Location Not Enabled", Snackbar.LENGTH_INDEFINITE)
                .setAction("Open Settings", new View.OnClickListener() {
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
            tv_snack.setText("Location services not supporting in this device");
        }


        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if (!(sharedPreferences.getString("customer_image", "").equals(""))) {
            Glide.with(DashboardNavigation.this).load(Config.WEB_URL + "customer_details/" + sharedPreferences.getString("customer_image", "")).into(btn_editProfile_img);
        }


        dialog2 = new Dialog(DashboardNavigation.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.dialogue_profile);
        btn_update = (Button) dialog2.findViewById(R.id.button_update);
        btn_close = (ImageView) dialog2.findViewById(R.id.button_close);
        et_username = (EditText) dialog2.findViewById(R.id.editTextName);
        et_email = (EditText) dialog2.findViewById(R.id.editTextEmail);
        flt_uname = (TextInputLayout) dialog2.findViewById(R.id.float_name);
        flt_email = (TextInputLayout) dialog2.findViewById(R.id.float_email);
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
                        Toast.makeText(getApplicationContext(), "Enter User Email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter User Name", Toast.LENGTH_SHORT).show();
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
        dialog1.setContentView(R.layout.dialog_yes_no);
        btn_yes = (Button) dialog1.findViewById(R.id.button_yes);
        btn_no = (Button) dialog1.findViewById(R.id.button_no);

        tv_txt1 = (android.widget.TextView) dialog1.findViewById(R.id.textView_1);
        tv_txt2 = (android.widget.TextView) dialog1.findViewById(R.id.textView_2);
        tv_txt3 = (android.widget.TextView) dialog1.findViewById(R.id.textView_3);

        tv_txt1.setTypeface(tf);
        tv_txt2.setTypeface(tf);
        tv_txt3.setTypeface(tf);
        btn_yes.setTypeface(tf);
        btn_no.setTypeface(tf);

        pickuplv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity();
            }
        });
        droplv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity1();
            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (exit_status == 0) {
                    editor.putString("login", "");
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
                et_username.setText(customer_name);
                et_email.setText(customer_email);
            }
        });
        btn_editProfile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoPickerIntent intent = new PhotoPickerIntent(DashboardNavigation.this);
                intent.setPhotoCount(1);
                intent.setColumn(3);
                intent.setShowCamera(true);
                startActivityForResult(intent, REQUEST_PROFILE);
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
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
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
                            case R.id.support: {
                                return true;
                            }
                            case R.id.feedback: {
                                return true;
                            }
                            case R.id.logout: {
                                dialog1.show();
                                exit_status = 0;
                                tv_txt3.setText("Logout");
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
                if (!(sharedPreferences.getString("job_id", "").equals(""))) {

                    Intent i = new Intent(DashboardNavigation.this, Job_review.class);
                    startActivity(i);
                    drawer.closeDrawer(Gravity.LEFT);
                }

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

                    if (destination.getText().toString().isEmpty()) {
                        // Toast.makeText(getApplicationContext(), "Choose Drop Location", Toast.LENGTH_LONG).show();
                        snackbar.show();
                        tv_snack.setText("Choose Drop Location");
                    } else {
                        editor.putString("pickup", mStreetOutput + ", " + mCityOutput);
                        editor.putString("drop", destination.getText().toString());
                        editor.putString("pickup_lati", mPickup_lat);
                        editor.putString("pickup_long", mPickup_long);
                        editor.putString("drop_lati", mDrop_lat);
                        editor.putString("drop_long", mDrop_long);
                        editor.commit();
                        Intent i = new Intent(DashboardNavigation.this, Book_now.class);
                        startActivity(i);
                    }
                } else {
                    snackbar.show();
                    tv_snack.setText("You already have current job. Please choose book later!");
                }
            }
        });

        btn_book_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardNavigation.this, Book_later.class);
                startActivity(i);
            }
        });


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

        if (checkPlayServices()) {
            if (!AppUtils.isLocationEnabled(mContext)) {
                snackbar_loc.show();
            }
            buildGoogleApiClient();
        } else {
            snackbar.show();
            tv_snack.setText("Location services not supporting in this device");
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DashboardNavigation.this.unregisterReceiver(appendChatScreenMsgReceiver);
    }


    /////////////////////////


    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void openAutocompleteActivity1() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE1);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
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
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            Log.e("tag", "request");
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(mContext, data);
                // TODO call location based filter
                LatLng latLong;

                starting.setText("");
                starting.append(place.getAddress());
                Log.e("tagplace", " place " + place.getAddress() + " attrib " + place.getAttributions() + " name " + place.getName() + " phone " + place.getPhoneNumber() + " latlon " + place.getLatLng().toString()
                );


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

                    mMap.clear();
                    latLong = place.getLatLng();

                    //latLong = new LatLng(location.getLatitude(), location.getLongitude());

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLong).zoom(15f).build();

                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                    mMap.addMarker(new MarkerOptions().position(latLong).icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_point)));

                }


            }


        } else if (requestCode == REQUEST_CODE_AUTOCOMPLETE1) {
            Log.e("tag", "request_drop");

            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place1 = PlaceAutocomplete.getPlace(mContext, data);
                // TODO call location based filter
                LatLng latLong;
                latLong = place1.getLatLng();
                destination.setText("");

                destination.append(place1.getAddress());
                Log.e("tag", "place111" + place1.getAddress());
                Log.e("tag", "la00: " + place1.getLatLng());
                mDrop_lat = String.valueOf(latLong.latitude);
                mDrop_long = String.valueOf(latLong.longitude);

            }


        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(mContext, data);
        } else if (resultCode == RESULT_CANCELED) {
            // Indicates that the activity closed before a selection was made. For example if
            // the user pressed the back button.
        }

        List<String> photos = null;
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
        }
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
        //starting.setText(mAddressOutput);
        try {
            if (mAreaOutput != null)
                // starting.setText(mAreaOutput+ "");
                Log.e("tag11", "output" + mAddressOutput);
            starting.setText("");

            starting.setText(mAddressOutput);
            //starting.setText(mAreaOutput);
            //   Log.e("tag22", "output" + mAreaOutput);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.e("tagmap", "OnMapReady");
        mMap = googleMap;


        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.e("tagmap", "Camera postion change" + cameraPosition + "");
                mCenterLatLong = cameraPosition.target;
                // mMap.clear();

                lt_first.setVisibility(View.GONE);
                lt_second.setVisibility(View.GONE);
                lt_last.setVisibility(View.GONE);

                try {

                    Location mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);
                    LatLng latLng = new LatLng(mCenterLatLong.latitude, mCenterLatLong.longitude);
                    mMap.clear();

                    mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_point)));
                    str_lati = String.valueOf(mCenterLatLong.latitude);
                    str_longi = String.valueOf(mCenterLatLong.longitude);


                    startIntentService(mLocation);
                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {

                            Log.e("tagmap", "Camera postion change map click");

                            mMap.clear();
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(latLng).zoom(15f).build();
                            mMap.animateCamera(CameraUpdateFactory
                                    .newCameraPosition(cameraPosition));
                            mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_point)));
                            str_lati = String.valueOf(mCenterLatLong.latitude);
                            str_longi = String.valueOf(mCenterLatLong.longitude);

                        }
                    });


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
            }
        });


        mMap.setOnGroundOverlayClickListener(new GoogleMap.OnGroundOverlayClickListener() {
            @Override
            public void onGroundOverlayClick(GroundOverlay groundOverlay) {
                Log.e("tagmap", "worked");

            }
        });


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
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(false);
            LatLng latLong;

            Log.e("tagmap", "change_map_map_not_null");
            mMap.clear();


            latLong = new LatLng(location.getLatitude(), location.getLongitude());

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(15f).build();

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            mMap.addMarker(new MarkerOptions().position(latLong).icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_point)));

            // mLocationMarkerText.setText("Lat : " + location.getLatitude() + "," + "Long : " + location.getLongitude());
            startIntentService(location);

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                    Log.e("tagmap", "change_map_map_click");

                    mMap.clear();
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng).zoom(15f).build();
                    mMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                    mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_point)));

                }
            });


        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        dialog1.show();
        exit_status = 1;
        tv_txt3.setText("Exit");
    }


    @Override
    public void addBooleanChangeListener(BooleanChangeListener listener) {
        listeners.add(listener);
    }


    @Override
    public boolean getFlag() {
        return flag;
    }

    @Override
    public void setFlag(boolean flag) {

        Log.e("tagmap", "flagvalue" + flag);

        if (this.flag != flag) {
            this.flag = flag;
            // dispatchEvent();
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
                    HttpPost httppost = new HttpPost(Config.WEB_URL + "customer/customerupdate");
                    httppost.setHeader("id", id);
                    httppost.setHeader("sessiontoken", token);
                    httppost.setHeader("customer_email", customer_email);
                    httppost.setHeader("customer_name", customer_name);
                    try {

                        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                        entity.addPart("customerimage", new FileBody(new File(str_profile_img), "image/jpeg"));
                        Log.e("tag", "img: if ");
                        httppost.setEntity(entity);
                        HttpResponse response = httpclient.execute(httppost);
                        HttpEntity r_entity = response.getEntity();
                        int statusCode = response.getStatusLine().getStatusCode();
                        Log.e("tag", response.getStatusLine().toString());
                        if (statusCode == 200) {
                            responseString = EntityUtils.toString(r_entity);
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
                    return s = HttpUtils.makeRequest1(net.sqindia.movehaul.Config.WEB_URL + "customer/customerupdate", json, id, token);
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


                        Glide.with(DashboardNavigation.this).load(Config.WEB_URL + "customer_details/" + img).into(btn_editProfile_img);

                        editor.putString("customer_image", img);
                        editor.putString("customer_name", name);
                        editor.putString("customer_email", email);
                        editor.commit();

                        Log.e("tag", "img: " + Config.WEB_URL + "customer_details/" + img);
                        Log.e("tag", "img: " + name);
                        Log.e("tag", "img: " + email);

                        tv_name.setText(name);
                        tv_email.setText(email);

                        //Glide.with(DashboardNavigation.this).load(new File(str_profile_img)).into(btn_editProfile_img);

                    } else {
                        Toast.makeText(getApplicationContext(), "Network Errror", Toast.LENGTH_LONG).show();

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


}

class BooleanChangeEvent extends EventObject {

    private final BooleanChangeDispatcher dispatcher;

    public BooleanChangeEvent(BooleanChangeDispatcher dispatcher) {
        super(dispatcher);
        this.dispatcher = dispatcher;
    }


}

