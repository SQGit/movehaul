package net.sqindia.movehaul;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

/**
 * Created by sqindia on 02-11-2016.
 */

public class Payment_Details extends Activity {
    Button btn_continue;
    LinearLayout btn_back;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String tot_amt;
    TextView tv_payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_details);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);

        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        tv_payment = (TextView) findViewById(R.id.textview_payment);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Payment_Details.this);
        editor = sharedPreferences.edit();

        tot_amt = sharedPreferences.getString("payment_amount","");

        tv_payment.setText(tot_amt);



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Payment_Details.this, DriversList.class);
                startActivity(i);
                finish();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Payment_Details.this,Payment_Card_Details.class);
                startActivity(i);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Payment_Details.this,DashboardNavigation.class);
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
