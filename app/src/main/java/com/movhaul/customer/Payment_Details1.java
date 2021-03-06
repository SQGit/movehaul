package com.movhaul.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sqindia on 02-11-2016.
 * not used
 */

public class Payment_Details1 extends Activity {
    Button btn_continue;
    LinearLayout btn_back;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String tot_amt;
    TextView tv_payment;
    private RadioGroup radioGroup;
    private RadioButton visa,verve,wallet;
    String payment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.movhaul.customer.R.layout.payment_details1);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);

        btn_continue = (Button) findViewById(com.movhaul.customer.R.id.btn_continue);
        btn_back = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_back);
        tv_payment = (TextView) findViewById(com.movhaul.customer.R.id.textview_payment);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Payment_Details1.this);
        editor = sharedPreferences.edit();

        tot_amt = sharedPreferences.getString("payment_amount","");

        //tv_payment.setText(tot_amt);

        radioGroup = (RadioGroup) findViewById(com.movhaul.customer.R.id.radioGroup2);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // find which radio button is selected

                if(checkedId == com.movhaul.customer.R.id.r_visa) {

                  payment = "VISA";

                } else if(checkedId == com.movhaul.customer.R.id.r_verve) {

                    payment = "VERVE";

                } else if(checkedId == com.movhaul.customer.R.id.r_wallet) {

                    payment = "BANK_INTERNET";

                }
                else{
                    payment = "MASTERCARD";
                }

            }



        });




        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Payment_Details1.this, DriversList.class);
                startActivity(i);
                finish();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent i = new Intent(Payment_Details1.this, WebViewAct.class);
                startActivity(i);


            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Payment_Details1.this,DashboardNavigation.class);
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


    public class login_customer extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("amt", "91");
                jsonObject.accumulate("payerName ", "salman");
                jsonObject.accumulate("payerEmail ", "salman@sqindia.net");
                jsonObject.accumulate("paymenttype  ", "VISA");
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest("http://www.passafaila.com/remita/processpayment.php", json);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);

            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                    String msg = jo.getString("message");
                    Log.d("tag", "<-----Status----->" + status);

                    if (status.equals("true")) {

                    } else if (status.equals("false")) {

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
