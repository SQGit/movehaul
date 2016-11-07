package net.sqindia.movehaul;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.widget.Button;

import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

/**
 * Created by sqindia on 02-11-2016.
 */

public class Payment_Card_Details extends Activity {
    Button btn_paynow,btn_ok;
    Dialog dialog1;
    ImageView btn_close;
    TextView tv_dialog1,tv_dialog2,tv_dialog3,tv_dialog4;
    LinearLayout btn_back;
    TextInputLayout flt_exp_date,flt_cvv,flt_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_carddetails);
        FontsManager.initFormAssets(this,"fonts/CATAMARAN-REGULAR.TTF");       //initialization
        FontsManager.changeFonts(this);
        btn_paynow = (Button) findViewById(R.id.btn_paynow);
        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        flt_exp_date = (TextInputLayout) findViewById(R.id.float_exp_date);
        flt_cvv = (TextInputLayout) findViewById(R.id.float_cvv);
        flt_name = (TextInputLayout) findViewById(R.id.float_name);
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/CATAMARAN-REGULAR.TTF");
        flt_cvv.setTypeface(type);
        flt_exp_date.setTypeface(type);
        flt_name.setTypeface(type);

        btn_paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.show();
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Payment_Card_Details.this, Payment_Details.class);
                startActivity(i);
                finish();
            }
        });
        dialog1 = new Dialog(Payment_Card_Details.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.dialogue_job_posting);
        btn_ok = (Button) dialog1.findViewById(R.id.button_ok);
        btn_close = (ImageView) dialog1.findViewById(R.id.button_close);
        tv_dialog1 = (TextView) dialog1.findViewById(R.id.textView_1);
        tv_dialog2 = (TextView) dialog1.findViewById(R.id.textView_2);
        tv_dialog3 = (TextView) dialog1.findViewById(R.id.textView_3);
        tv_dialog4 = (TextView) dialog1.findViewById(R.id.textView_4);
        tv_dialog1.setText("Your Trip has Been");
        tv_dialog2.setText("Confirmed!!");
        tv_dialog3.setText("Our Driver will");
        tv_dialog4.setText("Contact you soon..");

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                Intent i = new Intent(Payment_Card_Details.this, MyTrips.class);
                startActivity(i);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Payment_Card_Details.this,Payment_Details.class);
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
