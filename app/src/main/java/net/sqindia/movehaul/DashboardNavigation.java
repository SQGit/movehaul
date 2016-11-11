package net.sqindia.movehaul;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

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
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.rey.material.widget.Button;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.SupportMapFragment;
import com.rey.material.widget.LinearLayout;
import com.rey.material.widget.TextView;
import com.sloop.fonts.FontsManager;

/**
 * Created by SQINDIA on 10/26/2016.
 */

public class DashboardNavigation extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, com.google.android.gms.location.LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private GoogleMap mMap;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static String TAG = "MAP LOCATION";
    Context mContext;
    private LatLng mCenterLatLong;
    private AddressResultReceiver mResultReceiver;
    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;
    EditText mLocationAddress;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final int REQUEST_CODE_AUTOCOMPLETE1 = 2;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    Button btn_book_now, btn_book_later;
    TextView tv_name, tv_email, tv_myTrips, tv_jobReview, tv_payments, tv_tracking, tv_offers, tv_emergencyContacts;
    AutoCompleteTextView starting, destination;
    TextInputLayout flt_pickup, flt_droplocation;
    FloatingActionButton fab_truck;
    private boolean serviceWillBeDismissed;
    protected GoogleApiClient mGoogleApiClient;
    ImageView pickup_close,btn_menu,rightmenu;
    android.widget.LinearLayout droplv,pickuplv;
    Dialog dialog1;
    Button btn_yes,btn_no;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");
        FontsManager.changeFonts(this);
        mContext = this;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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
       // fab_truck = (FloatingActionButton) findViewById(R.id.float_icon);
        droplv=(android.widget.LinearLayout) findViewById(R.id.layout_drop);
        pickuplv=(android.widget.LinearLayout) findViewById(R.id.layout_pickuptype);
        btn_menu = (ImageView) findViewById(R.id.img_menu);
        rightmenu = (ImageView) findViewById(R.id.right_menu);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        flt_pickup.setTypeface(type);
        flt_droplocation.setTypeface(type);


        mapFragment.getMapAsync(this);
        mResultReceiver = new AddressResultReceiver(new Handler());
        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            if (!AppUtils.isLocationEnabled(mContext)) {
                // notify user
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setMessage("Location not enabled!");
                dialog.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub

                    }
                });
                dialog.show();
            }
            buildGoogleApiClient();
        } else {
            Toast.makeText(mContext, "Location not supported in this device", Toast.LENGTH_SHORT).show();
        }

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


        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);




        ///////**************** Arc Menu ****************//////////////


      /*  final SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(DashboardNavigation.this);
        rLSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_dr));
        final FrameLayout.LayoutParams tvParams = new FrameLayout.LayoutParams(90, 90);
      //  rLSubBuilder.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        final ImageView rlIcon1 = new ImageView(DashboardNavigation.this);
        final ImageView rlIcon2 = new ImageView(DashboardNavigation.this);
        final ImageView rlIcon3 = new ImageView(DashboardNavigation.this);
        final ImageView rlIcon4 = new ImageView(DashboardNavigation.this);
        final ImageView rlIcon5 = new ImageView(DashboardNavigation.this);
        final ImageView rlIcon6 = new ImageView(DashboardNavigation.this);
        rlIcon1.setBackground(getResources().getDrawable(R.drawable.filter_truck_1));
      //  rlIcon1.setLayoutParams(tvParams);
        rlIcon2.setBackground(getResources().getDrawable(R.drawable.filter_truck_2));
       // rlIcon2.setLayoutParams(tvParams);
        rlIcon3.setBackground(getResources().getDrawable(R.drawable.filter_truck_3));
       // rlIcon3.setLayoutParams(tvParams);
        rlIcon4.setBackground(getResources().getDrawable(R.drawable.filter_truck_4));
       // rlIcon4.setLayoutParams(tvParams);
        rlIcon5.setBackground(getResources().getDrawable(R.drawable.filter_truck_5));
       // rlIcon5.setLayoutParams(tvParams);
        rlIcon6.setBackground(getResources().getDrawable(R.drawable.filter_truck_6));
      //  rlIcon6.setLayoutParams(tvParams);
        SubActionButton.Builder tCSubBuilder = new SubActionButton.Builder(this);
        int blueSubActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);
        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        blueContentParams.setMargins(blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin);
        ImageView rlIcons = new ImageView(DashboardNavigation.this);
        rlIcons.setImageDrawable(getResources().getDrawable(R.drawable.truck_1));
        SubActionButton tcSub1 = tCSubBuilder.setContentView(rlIcons, blueContentParams).build();
        final FloatingActionMenu fam_truck_choose = new FloatingActionMenu.Builder(DashboardNavigation.this)
                //.setRadius(150)
                .setStartAngle(150)
                .addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon2).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon3).build())
                //.addSubActionView(tcSub1, tcSub1.getLayoutParams().width, tcSub1.getLayoutParams().height)
                .addSubActionView(rLSubBuilder.setContentView(rlIcon4).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon5).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon6).build())
                .attachTo(fab_truck)
                .build();

        fam_truck_choose.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {

            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                if (serviceWillBeDismissed) {
                    serviceWillBeDismissed = false;
                }
            }
        });

        // make the red button terminate the service
        rlIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceWillBeDismissed = true; // the order is important
                //  fam_truck_choose.close(true);
                rlIcon1.setBackground(getResources().getDrawable(R.drawable.filter_truck_1));
                rLSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_dr_red));
                Log.e("tag","clicke1");
                rlIcon2.setBackground(getResources().getDrawable(R.drawable.filter_truck_2));
                rlIcon3.setBackground(getResources().getDrawable(R.drawable.filter_truck_3));
                rlIcon4.setBackground(getResources().getDrawable(R.drawable.filter_truck_4));
                rlIcon5.setBackground(getResources().getDrawable(R.drawable.filter_truck_5));
                rlIcon6.setBackground(getResources().getDrawable(R.drawable.filter_truck_6));
            }
        });

        rlIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceWillBeDismissed = true; // the order is important
                //  fam_truck_choose.close(true);
                rlIcon1.setBackground(getResources().getDrawable(R.drawable.filter_truck_1));
                rlIcon2.setBackground(getResources().getDrawable(R.drawable.filter_truck_2));
                rlIcon3.setBackground(getResources().getDrawable(R.drawable.filter_truck_3));
                rlIcon4.setBackground(getResources().getDrawable(R.drawable.filter_truck_4));
                rlIcon5.setBackground(getResources().getDrawable(R.drawable.filter_truck_5));
                rlIcon6.setBackground(getResources().getDrawable(R.drawable.filter_truck_6));
            }
        });

        rlIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceWillBeDismissed = true; // the order is important
                //  fam_truck_choose.close(true);
                rlIcon1.setBackground(getResources().getDrawable(R.drawable.filter_truck_1));
                rlIcon2.setBackground(getResources().getDrawable(R.drawable.filter_truck_2));
                rlIcon3.setBackground(getResources().getDrawable(R.drawable.filter_truck_3));
                rlIcon4.setBackground(getResources().getDrawable(R.drawable.filter_truck_4));
                rlIcon5.setBackground(getResources().getDrawable(R.drawable.filter_truck_5));
                rlIcon6.setBackground(getResources().getDrawable(R.drawable.filter_truck_6));
            }
        });

        rlIcon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceWillBeDismissed = true; // the order is important
                //  fam_truck_choose.close(true);
                rlIcon1.setBackground(getResources().getDrawable(R.drawable.filter_truck_1));
                rlIcon2.setBackground(getResources().getDrawable(R.drawable.filter_truck_2));
                rlIcon3.setBackground(getResources().getDrawable(R.drawable.filter_truck_3));
                rlIcon4.setBackground(getResources().getDrawable(R.drawable.filter_truck_4));
                rlIcon5.setBackground(getResources().getDrawable(R.drawable.filter_truck_5));
                rlIcon6.setBackground(getResources().getDrawable(R.drawable.filter_truck_6));
            }
        });

        rlIcon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceWillBeDismissed = true; // the order is important
                //  fam_truck_choose.close(true);
                rlIcon1.setBackground(getResources().getDrawable(R.drawable.filter_truck_1));
                rlIcon2.setBackground(getResources().getDrawable(R.drawable.filter_truck_2));
                rlIcon3.setBackground(getResources().getDrawable(R.drawable.filter_truck_3));
                rlIcon4.setBackground(getResources().getDrawable(R.drawable.filter_truck_4));
                rlIcon5.setBackground(getResources().getDrawable(R.drawable.filter_truck_5));
                rlIcon6.setBackground(getResources().getDrawable(R.drawable.filter_truck_6));
            }
        });

        rlIcon6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceWillBeDismissed = true; // the order is important
                //  fam_truck_choose.close(true);
                rlIcon1.setBackground(getResources().getDrawable(R.drawable.filter_truck_1));
                rlIcon2.setBackground(getResources().getDrawable(R.drawable.filter_truck_2));
                rlIcon3.setBackground(getResources().getDrawable(R.drawable.filter_truck_3));
                rlIcon4.setBackground(getResources().getDrawable(R.drawable.filter_truck_4));
                rlIcon5.setBackground(getResources().getDrawable(R.drawable.filter_truck_5));
                rlIcon6.setBackground(getResources().getDrawable(R.drawable.filter_truck_6));
            }
        });*/



        dialog1 = new Dialog(DashboardNavigation.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.dialog_yes_no);
        btn_yes = (Button) dialog1.findViewById(R.id.button_yes);
        btn_no = (Button) dialog1.findViewById(R.id.button_no);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
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
                openContextMenu(view);



                PopupMenu popup = new PopupMenu(DashboardNavigation.this, rightmenu);

                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());




                Menu m = popup.getMenu();
                for (int i=0;i<m.size();i++) {
                    MenuItem mi = m.getItem(i);

                    //for aapplying a font to subMenu ...
                    SubMenu subMenu = mi.getSubMenu();
                    if (subMenu != null && subMenu.size() > 0) {
                        for (int j = 0; j < subMenu.size(); j++) {
                            MenuItem subMenuItem = subMenu.getItem(j);
                            applyFontToMenuItem(subMenuItem);
                        }
                    }


                    //the method we have create in activity
                    applyFontToMenuItem(mi);

                }









                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.item1: {

                                return true;
                            }
                            case R.id.item2: {

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
                Intent i = new Intent(DashboardNavigation.this, Job_review.class);
                startActivity(i);
                finish();
            }
        });
        tv_myTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardNavigation.this, MyTrips.class);
                startActivity(i);
                finish();
            }
        });

        tv_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardNavigation.this, Tracking.class);
                startActivity(i);
                finish();
            }
        });


        tv_payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardNavigation.this, Payment.class);
                startActivity(i);
                finish();
            }
        });
        btn_book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(DashboardNavigation.this, Book_now.class);
                startActivity(i);
            }
        });

        btn_book_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardNavigation.this, Book_later.class);
                startActivity(i);
            }
        });


        tv_emergencyContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goIntsd = new Intent(getApplicationContext(), EmergencyContacts.class);
                startActivity(goIntsd);
            }
        });


    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);





    }




    private void applyFontToMenuItem(MenuItem mi)
    {
            Typeface font = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
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
                Log.e("tag", "1234447" + location);
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
                Log.e("tag", "place" + place.getAddress());


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

                    // mLocationMarkerText.setText("Lat : " + location.getLatitude() + "," + "Long : " + location.getLongitude());
                   // startIntentService(location);




                }






            }


        } else if (requestCode == REQUEST_CODE_AUTOCOMPLETE1) {
            Log.e("tag", "request111");

            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place1 = PlaceAutocomplete.getPlace(mContext, data);
                // TODO call location based filter
                LatLng latLong;
                latLong = place1.getLatLng();
                destination.setText("");

                destination.append(place1.getAddress());
                Log.e("tag", "place111" + place1.getAddress());

               /* CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLong).zoom(10f).tilt(50).build();

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
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));*/


            }


        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(mContext, data);
        } else if (resultCode == RESULT_CANCELED) {
            // Indicates that the activity closed before a selection was made. For example if
            // the user pressed the back button.
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
            Log.e("tag", "0000000" + mAddressOutput);
            mAreaOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_AREA);
            Log.e("tag", "11111111" + mAreaOutput);

            mCityOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_CITY);
            Log.e("tag", "22222222" + mCityOutput);

            mStateOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_STREET);

            Log.e("tag", "33333333333" + mStateOutput);


            displayAddressOutput();
            Log.e("tag", "4444444444");

            // Show a toast message if an address was found.
            if (resultCode == AppUtils.LocationConstants.SUCCESS_RESULT) {
                //  showToast(getString(R.string.address_found));


            }


        }

    }


    protected void displayAddressOutput() {

        Log.e("tag", "output11111" + mAddressOutput);
        //starting.setText(mAddressOutput);
        try {
            if (mAreaOutput != null)
                // starting.setText(mAreaOutput+ "");
                Log.e("tag", "output" + mAddressOutput);
            starting.setText("");

            starting.setText(mAddressOutput);
            //starting.setText(mAreaOutput);
            Log.e("tag", "output" + mAreaOutput);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        mMap = googleMap;

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Camera postion change" + "", cameraPosition + "");
                mCenterLatLong = cameraPosition.target;
               // mMap.clear();
                try {

                    Location mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);

                    startIntentService(mLocation);
                    // mLocationMarkerText.setText("Lat : " + mCenterLatLong.latitude + "," + "Long : " + mCenterLatLong.longitude);

                } catch (Exception e) {
                    e.printStackTrace();
                }
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

        Log.d(TAG, "Reaching map" + mMap);


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

            mMap.clear();


            latLong = new LatLng(location.getLatitude(), location.getLongitude());

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(19f).build();

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            mMap.addMarker(new MarkerOptions().position(latLong).icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_point)));

            // mLocationMarkerText.setText("Lat : " + location.getLatitude() + "," + "Long : " + location.getLongitude());
            startIntentService(location);




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
    }




}
