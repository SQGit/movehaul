package com.vineture.movhaul;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rey.material.widget.ImageView;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

/**
 * Created by sqindia on 02-11-2016.
 */

public class Tracking extends FragmentActivity {
    ImageView btn_search;
    TextView tv_hint;
    ScrollView sv_tracking;
    LinearLayout btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.vineture.movhaul.R.layout.tracking);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");
        FontsManager.changeFonts(this);

        btn_search = (ImageView) findViewById(com.vineture.movhaul.R.id.btn_search);
        tv_hint = (TextView) findViewById(com.vineture.movhaul.R.id.textView_hint);
        sv_tracking = (ScrollView) findViewById(com.vineture.movhaul.R.id.scrollView_tracking);
        btn_back = (LinearLayout) findViewById(com.vineture.movhaul.R.id.layout_back);

        tv_hint.setVisibility(View.VISIBLE);
        sv_tracking.setVisibility(View.GONE);

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}
