package net.sqindia.movehaul;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
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
    FloatingActionButton flt_icon;
    com.rey.material.widget.ImageView img_ico;

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

        flt_icon = (FloatingActionButton) findViewById(R.id.float_icon);

     //   img_ico = (com.rey.material.widget.ImageView) findViewById(R.id.img_ico);


        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/CATAMARAN-REGULAR.TTF");
        flt_pickup.setTypeface(type);
        flt_droplocation.setTypeface(type);



        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(DashboardNavigation.this);
        ImageView rlIcon1 = new ImageView(DashboardNavigation.this);
        ImageView rlIcon2 = new ImageView(DashboardNavigation.this);
        ImageView rlIcon3 = new ImageView(DashboardNavigation.this);
        ImageView rlIcon4 = new ImageView(DashboardNavigation.this);
        ImageView rlIcon5 = new ImageView(DashboardNavigation.this);
        ImageView rlIcon6 = new ImageView(DashboardNavigation.this);

        rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.truck_1));
        rlIcon2.setImageDrawable(getResources().getDrawable(R.drawable.truck_icon));
        rlIcon3.setImageDrawable(getResources().getDrawable(R.drawable.truck_icon));
        rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.truck_icon));
        rlIcon5.setImageDrawable(getResources().getDrawable(R.drawable.truck_icon));
        rlIcon6.setImageDrawable(getResources().getDrawable(R.drawable.truck_icon));


        // Build the menu with default options: light theme, 90 degrees, 72dp radius.
        // Set 4 default SubActionButtons
        final FloatingActionMenu rightLowerMenu = new FloatingActionMenu.Builder(DashboardNavigation.this)
                .addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon2).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon3).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon4).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon5).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon6).build())
                .attachTo(flt_icon)
                .build();










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
}
