package net.sqindia.movehaul;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rey.material.widget.ImageView;
import com.sloop.fonts.FontsManager;

/**
 * Created by sqindia on 02-11-2016.
 */

public class Tracking extends FragmentActivity {
    ImageView btn_search;
    TextView tv_hint;
    ScrollView sv_tracking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracking);
        FontsManager.initFormAssets(this, "fonts/CATAMARAN-REGULAR.TTF");       //initialization
        FontsManager.changeFonts(this);

        btn_search = (ImageView) findViewById(R.id.btn_search);
        tv_hint = (TextView) findViewById(R.id.textView_hint);
        sv_tracking = (ScrollView) findViewById(R.id.scrollView_tracking);

        tv_hint.setVisibility(View.VISIBLE);
        sv_tracking.setVisibility(View.GONE);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv_hint.setVisibility(View.GONE);
                sv_tracking.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Tracking.this,DashboardNavigation.class);
        startActivity(i);
        finish();
    }
}
