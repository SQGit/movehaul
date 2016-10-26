package net.sqindia.movehaul;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;


import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;
import com.sloop.fonts.FontsManager;

/**
 * Created by SQINDIA on 10/26/2016.
 */

public class DashboardNavigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    Button btn_book_now, btn_book_later;
    TextView tv_name, tv_email, tv_myTrips, tv_jobReview, tv_payments, tv_tracking, tv_offers, tv_emergencyContacts;
    EditText et_pickup, et_droplocation;
    TextInputLayout flt_pickup, flt_droplocation;

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

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/CATAMARAN-REGULAR.TTF");
        flt_pickup.setTypeface(type);
        flt_droplocation.setTypeface(type);

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

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
