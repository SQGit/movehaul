package com.movhaul.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sqindia on 21-10-2016.
 */

public class LoginActivity extends Activity {
    Button btn_submit;
    TextView tv_forgot_mobile, tv_snack;
    LinearLayout btn_back;
    String str_mobile,str_mobile_prefix;
    EditText et_mobile_no;
    TextInputLayout flt_mobile_no;
    Snackbar snackbar, snack_wifi;
    Config config;
    Typeface tf;
    ProgressDialog mProgressDialog;
    CountryCodePicker ccp;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.movhaul.customer.R.layout.activity_login);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);
        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        config = new Config();

        btn_submit = (Button) findViewById(com.movhaul.customer.R.id.btn_submit);
        btn_back = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_back);
        tv_forgot_mobile = (TextView) findViewById(com.movhaul.customer.R.id.text_forgot_no);
        et_mobile_no = (EditText) findViewById(com.movhaul.customer.R.id.editTextMobileNo);
        flt_mobile_no = (TextInputLayout) findViewById(com.movhaul.customer.R.id.float_mobile);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        et_mobile_no.setTypeface(tf);
        flt_mobile_no.setTypeface(tf);
        ccp.setTypeFace(tf);
        try {
            TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            String countryCodeValue = tm.getNetworkCountryIso();
            ccp.setCountryForNameCode(countryCodeValue);
        } catch (Exception ex) {
            Log.e("tag", "er:" + ex.toString());
        } finally {
            Log.e("tag", "flg" + ccp.getSelectedCountryCodeWithPlus());
            str_mobile_prefix = ccp.getSelectedCountryCodeWithPlus();
        }
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                str_mobile_prefix = ccp.getSelectedCountryCodeWithPlus();
                Log.e("tag", "flg_ccp" + ccp.getSelectedCountryCodeWithPlus());
            }
        });



        // show_wifi_not_connected();


        mProgressDialog = new ProgressDialog(LoginActivity.this, com.movhaul.customer.R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setTitle(getString(com.movhaul.customer.R.string.lo));
        mProgressDialog.setMessage(getString(com.movhaul.customer.R.string.los));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        snack_wifi = config.snackbar(snack_wifi, LoginActivity.this, tv_snack, tf);


        snackbar = Snackbar
                .make(findViewById(com.movhaul.customer.R.id.top), com.movhaul.customer.R.string.no_internet, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);

        if (!config.isConnected(LoginActivity.this)) {
            snackbar.show();
            tv_snack.setText(com.movhaul.customer.R.string.please_try_again);
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        tv_forgot_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, Forgot_Mobile.class);
                startActivity(i);
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_mobile = et_mobile_no.getText().toString().trim();

                if (!(str_mobile.isEmpty() || str_mobile.length() < 10)) {
                    if (config.isConnected(LoginActivity.this)) {
                        new login_customer().execute();
                    } else {
                        snackbar.show();
                        tv_snack.setText(com.movhaul.customer.R.string.please_try_again);
                    }

                } else {
                   // et_mobile_no.setError("Enter valid phone number");
                    snackbar.show();
                    tv_snack.setText(com.movhaul.customer.R.string.wa);
                    et_mobile_no.requestFocus();
                }

            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void show_wifi_not_connected() {

        if (!config.isConnected(LoginActivity.this)) {

            snack_wifi = Snackbar
                    .make(findViewById(com.movhaul.customer.R.id.top), com.movhaul.customer.R.string.a, Snackbar.LENGTH_INDEFINITE)
                    .setAction(com.movhaul.customer.R.string.ae, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });
            snack_wifi.setActionTextColor(Color.RED);
            View sbView = snack_wifi.getView();
            tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            android.widget.TextView textView = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_action);
            tv_snack.setTextColor(Color.WHITE);
            tv_snack.setTypeface(tf);
            textView.setTypeface(tf);
            snack_wifi.show();


        }
    }

    public class login_customer extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
            mProgressDialog.show();
            Log.e("tag","m:"+str_mobile_prefix+str_mobile);
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("customer_mobile", str_mobile_prefix + str_mobile);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL + "customermobileotp", json);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            mProgressDialog.dismiss();
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                    String msg = jo.getString("message");
                    Log.d("tag", "<-----Status----->" + status);

                    if (status.equals("true")) {
                        Intent i = new Intent(LoginActivity.this, LoginOtpActivity.class);
                        i.putExtra("for", "phone");
                        i.putExtra("data", str_mobile);
                        startActivity(i);
                        finish();
                    } else if (status.equals("false")) {
                        if (msg.contains("Register with MoveHaul first to Generate OTP")) {

                            tv_snack.setText(com.movhaul.customer.R.string.asdw);
                            snackbar.show();
                        } else if (msg.contains("Error Occured[object Object]")) {

                            Intent i = new Intent(LoginActivity.this, LoginOtpActivity.class);
                            i.putExtra("for", "phone");
                            i.putExtra("data", str_mobile);
                            startActivity(i);
                            finish();
                        }
                        else {
                            snackbar.show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    snackbar.show();
                }
            } else {
                snackbar.show();
            }

        }

    }

}
