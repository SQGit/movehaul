package net.sqindia.movehaul;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.rey.material.widget.Button;

import com.rey.material.widget.TextView;
import com.sloop.fonts.FontsManager;

/**
 * Created by SQINDIA on 10/26/2016.
 */

public class DashboardNavigation extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    Button btn_book_now, btn_book_later;
    TextView tv_name, tv_email, tv_myTrips, tv_jobReview, tv_payments, tv_tracking, tv_offers, tv_emergencyContacts;
    EditText et_pickup, et_droplocation;
    TextInputLayout flt_pickup, flt_droplocation;
    FloatingActionButton fab_truck;

    private boolean serviceWillBeDismissed;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        FontsManager.initFormAssets(this, "fonts/CATAMARAN-REGULAR.TTF");       //initialization
        FontsManager.changeFonts(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        et_pickup = (EditText) findViewById(R.id.editText_pickUp);
        et_droplocation = (EditText) findViewById(R.id.editText_dropLocation);
        flt_pickup = (TextInputLayout) findViewById(R.id.float_pickup);
        flt_droplocation = (TextInputLayout) findViewById(R.id.float_drop);
        fab_truck = (FloatingActionButton) findViewById(R.id.float_icon);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/CATAMARAN-REGULAR.TTF");
        flt_pickup.setTypeface(type);
        flt_droplocation.setTypeface(type);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(DashboardNavigation.this);
        rLSubBuilder.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final FrameLayout.LayoutParams tvParams = new FrameLayout.LayoutParams(90,90);

        final ImageView rlIcon1 = new ImageView(DashboardNavigation.this);
        final ImageView rlIcon2 = new ImageView(DashboardNavigation.this);
        final ImageView rlIcon3 = new ImageView(DashboardNavigation.this);
        final ImageView rlIcon4 = new ImageView(DashboardNavigation.this);
        final ImageView rlIcon5 = new ImageView(DashboardNavigation.this);
        final ImageView rlIcon6 = new ImageView(DashboardNavigation.this);

        rlIcon1.setBackground(getResources().getDrawable(R.drawable.truck_1));
        rlIcon1.setLayoutParams(tvParams);
        rlIcon2.setBackground(getResources().getDrawable(R.drawable.truck_1));
        rlIcon2.setLayoutParams(tvParams);
        rlIcon3.setBackground(getResources().getDrawable(R.drawable.truck_1));
        rlIcon3.setLayoutParams(tvParams);
        rlIcon4.setBackground(getResources().getDrawable(R.drawable.truck_1));
        rlIcon4.setLayoutParams(tvParams);
        rlIcon5.setBackground(getResources().getDrawable(R.drawable.truck_1));
        rlIcon5.setLayoutParams(tvParams);
        rlIcon6.setBackground(getResources().getDrawable(R.drawable.truck_1));
        rlIcon6.setLayoutParams(tvParams);


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
                //.setStartAngle(150)
                .addSubActionView(rlIcon1)
                .addSubActionView(rlIcon2)
                .addSubActionView(rlIcon3)
                //.addSubActionView(tcSub1, tcSub1.getLayoutParams().width, tcSub1.getLayoutParams().height)
                .addSubActionView(rlIcon4)
                .addSubActionView(rlIcon5)
                .addSubActionView(rlIcon6)
                .attachTo(fab_truck)
                .build();



        fam_truck_choose.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {

            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                if(serviceWillBeDismissed) {
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
                rlIcon1.setBackground(getResources().getDrawable(R.drawable.truck_1_hover));
                rlIcon2.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon3.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon4.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon5.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon6.setBackground(getResources().getDrawable(R.drawable.truck_1));
            }
        });

        rlIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceWillBeDismissed = true; // the order is important
                //  fam_truck_choose.close(true);
                rlIcon2.setBackground(getResources().getDrawable(R.drawable.truck_1_hover));
                rlIcon1.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon3.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon4.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon5.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon6.setBackground(getResources().getDrawable(R.drawable.truck_1));
            }
        });

        rlIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceWillBeDismissed = true; // the order is important
                //  fam_truck_choose.close(true);
                rlIcon3.setBackground(getResources().getDrawable(R.drawable.truck_1_hover));
                rlIcon2.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon1.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon4.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon5.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon6.setBackground(getResources().getDrawable(R.drawable.truck_1));
            }
        });

        rlIcon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceWillBeDismissed = true; // the order is important
                //  fam_truck_choose.close(true);
                rlIcon4.setBackground(getResources().getDrawable(R.drawable.truck_1_hover));
                rlIcon2.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon3.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon1.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon5.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon6.setBackground(getResources().getDrawable(R.drawable.truck_1));
            }
        });

        rlIcon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceWillBeDismissed = true; // the order is important
                //  fam_truck_choose.close(true);
                rlIcon5.setBackground(getResources().getDrawable(R.drawable.truck_1_hover));
                rlIcon2.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon3.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon4.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon1.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon6.setBackground(getResources().getDrawable(R.drawable.truck_1));
            }
        });

        rlIcon6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceWillBeDismissed = true; // the order is important
                //  fam_truck_choose.close(true);
                rlIcon6.setBackground(getResources().getDrawable(R.drawable.truck_1_hover));
                rlIcon2.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon3.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon4.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon5.setBackground(getResources().getDrawable(R.drawable.truck_1));
                rlIcon1.setBackground(getResources().getDrawable(R.drawable.truck_1));
            }
        });

       /* rlIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlIcon1.setBackground(getResources().getDrawable(R.drawable.truck_1_hover));
                rlIcon1.setLayoutParams(tvParams);
            }
        });*/






        tv_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardNavigation.this,Tracking.class);
                startActivity(i);
                finish();
            }
        });


        tv_payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardNavigation.this,Payment.class);
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
                Intent goIntsd = new Intent(getApplicationContext(),EmergencyContacts.class);
                startActivity(goIntsd);
            }
        });



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
