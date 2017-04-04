package com.movhaul.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


import com.google.firebase.iid.FirebaseInstanceId;
import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Modified by Salman on 24-10-2016.
 */

public class LoginOtpActivity extends Activity implements TextWatcher {
    static EditText et_otp1, et_otp2, et_otp3, et_otp4;
    private static LoginOtpActivity inst;
    LinearLayout btn_back;
    String str_otppin, str_for, str_data,fcm_id;
    Button btn_submit;
    TextView tv_resendotp,tv_snack;
    private View view;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Snackbar snackbar, snack_wifi;
    Config config;
    Typeface tf;
    ProgressDialog mProgressDialog;

    private LoginOtpActivity(View view) {
        this.view = view;
    }

    public LoginOtpActivity() {

    }

    public static LoginOtpActivity instance() {
        return inst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.movhaul.customer.R.layout.login_otp_screen);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);
        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginOtpActivity.this);
        editor = sharedPreferences.edit();

        mProgressDialog = new ProgressDialog(LoginOtpActivity.this, com.movhaul.customer.R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setTitle(com.movhaul.customer.R.string.loading);
        mProgressDialog.setMessage(getString(com.movhaul.customer.R.string.asdwasdf));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        fcm_id = FirebaseInstanceId.getInstance().getToken();

        Intent getIntent = getIntent();

        str_for = getIntent.getStringExtra("for");
        str_data = getIntent.getStringExtra("data");

        Log.e("tag",fcm_id+" dd "+str_data+ str_for);

        btn_back = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_back);

        et_otp1 = (EditText) findViewById(com.movhaul.customer.R.id.editext_otp1);
        et_otp2 = (EditText) findViewById(com.movhaul.customer.R.id.editext_otp2);
        et_otp3 = (EditText) findViewById(com.movhaul.customer.R.id.editext_otp3);
        et_otp4 = (EditText) findViewById(com.movhaul.customer.R.id.editext_otp4);

        btn_submit = (Button) findViewById(com.movhaul.customer.R.id.button_submit);
        tv_resendotp = (TextView) findViewById(com.movhaul.customer.R.id.textview_resendotp);


        snackbar = Snackbar
                .make(findViewById(com.movhaul.customer.R.id.top), com.movhaul.customer.R.string.no_internet, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);

        if (!config.isConnected(LoginOtpActivity.this)) {
            snackbar.show();
            tv_snack.setText(com.movhaul.customer.R.string.please_try_again);
        }




        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginOtpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });


        et_otp1.addTextChangedListener(new LoginOtpActivity(et_otp1));
        et_otp2.addTextChangedListener(new LoginOtpActivity(et_otp2));
        et_otp3.addTextChangedListener(new LoginOtpActivity(et_otp3));
        et_otp4.addTextChangedListener(new LoginOtpActivity(et_otp4));

        et_otp4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode != KeyEvent.KEYCODE_BACK && keyCode != KeyEvent.KEYCODE_DEL ) {
                    if (et_otp4.getText().toString().length() == 1) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput (0, 0);
                    }
                }
                return false;
            }
        });


        tv_resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_resendotp.setVisibility(View.GONE);
                new resend_otp().execute();
            }
        });




        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_otp1.getText().toString().isEmpty()) {
                    et_otp1.requestFocus();
                } else {
                    if (et_otp2.getText().toString().isEmpty()) {
                        et_otp2.requestFocus();
                    } else {
                        if (et_otp3.getText().toString().isEmpty()) {
                            et_otp3.requestFocus();
                        } else {
                            if (et_otp4.getText().toString().isEmpty()) {
                                et_otp4.requestFocus();
                            } else {
                                str_otppin = et_otp1.getText().toString() + et_otp2.getText().toString() + et_otp3.getText().toString() + et_otp4.getText().toString();
                                Log.e("tag", "pin:" + str_otppin);

                                Log.e("tag","for"+str_for+str_data);

                                new otp_verify().execute();
                            }
                        }
                    }
                }
            }
        });


    }



    public void recivedSms(String message) {
        try {
            Log.e("tag", "asd: " + message);

            char[] cArray = message.toCharArray();

            et_otp1.setText(String.valueOf(cArray[cArray.length - 4]));
            et_otp2.setText(String.valueOf(cArray[cArray.length - 3]));
            et_otp3.setText(String.valueOf(cArray[cArray.length - 2]));
            et_otp4.setText(String.valueOf(cArray[cArray.length - 1]));
            //


        } catch (Exception e) {
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {





    }

    @Override
    public void afterTextChanged(Editable editable) {


        switch (view.getId()) {
            case com.movhaul.customer.R.id.editext_otp1:

                if (editable.length() == 0) {
                    et_otp1.requestFocus();
                } else if (editable.length() == 1) {
                    et_otp2.requestFocus();
                }

                break;
            case com.movhaul.customer.R.id.editext_otp2:

                if (editable.length() == 0) {
                    et_otp1.requestFocus();
                } else if (editable.length() == 1) {
                    et_otp3.requestFocus();
                }

                break;
            case com.movhaul.customer.R.id.editext_otp3:

                if (editable.length() == 0) {
                    et_otp2.requestFocus();
                } else if (editable.length() == 1) {
                    et_otp4.requestFocus();
                }
                break;
            case com.movhaul.customer.R.id.editext_otp4:

                if (editable.length() == 0) {
                    et_otp3.requestFocus();
                   /* if(!et_otp3.getText().toString().isEmpty()){
                        et_otp4.requestFocus();
                    }*/
                }
                break;
        }


    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(LoginOtpActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }


    public class otp_verify extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "",url;

            try {
                JSONObject jsonObject = new JSONObject();


                if (str_for.equals("phone")) {

                    jsonObject.accumulate("customer_mobile", "+91"+str_data);
                    jsonObject.accumulate("customer_otp", str_otppin);
                    jsonObject.accumulate("fcm_id", fcm_id);
                    url = "customer/mobilelogin";
                } else {

                    jsonObject.accumulate("customer_email", str_data);
                    jsonObject.accumulate("customer_otp", str_otppin);
                    jsonObject.accumulate("fcm_id", fcm_id);
                    url = "customer/emaillogin";
                }


                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL + url, json);

            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mProgressDialog.dismiss();
            Log.e("tag", "tag" + s);


            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                    String msg = jo.getString("message");
                    Log.d("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {

                        String id = jo.getString("customer_id");
                        String token = jo.getString("token");
                        String mobile = jo.getString("customer_mobile");
                        String email = jo.getString("customer_email");
                        String fake_id = jo.getString("fake_id");
                        String name = jo.getString("customer_name");
                        String image = jo.getString("customer_image");

                        editor.putString("id",id);
                        editor.putString("token",token);
                        editor.putString("customer_mobile",mobile);
                        editor.putString("customer_email",email);
                        editor.putString("customer_name",name);
                        editor.putString("customer_id",fake_id);
                        if(!(image.equals(null)||image == null || image == "null")){
                            editor.putString("customer_image",image);
                        }
                        editor.putString("login","success");
                        editor.commit();

                        Intent i = new Intent(LoginOtpActivity.this, DashboardNavigation.class);
                        startActivity(i);
                        finish();


                    } else if (status.equals("false")) {

                        if(msg.contains("Authentication failed.Wrong Password")){
                            snackbar.show();
                            tv_snack.setText(com.movhaul.customer.R.string.asdunt);
                        }
                        else{
                            snackbar.show();
                            tv_snack.setText(com.movhaul.customer.R.string.please_try_again);
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


    public class resend_otp extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag","reg_preexe");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "",url;

            try {
                JSONObject jsonObject = new JSONObject();



                if (str_for.equals("phone")) {

                    jsonObject.accumulate("customer_mobile", "+91"+str_data);
                    jsonObject.accumulate("customer_otp", str_otppin);
                    url ="customermobileotp";
                } else {

                    jsonObject.accumulate("customer_email", str_data);
                    jsonObject.accumulate("customer_otp", str_otppin);
                    url = "customeremailotp";
                }


                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL + url, json);

            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
                mProgressDialog.dismiss();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag","tag"+s);
            mProgressDialog.dismiss();


            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                    String msg = jo.getString("message");
                    Log.d("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {


                        // String sus_txt = "Thank you for Signing Up MoveHaul.";

                        //Toast.makeText(getApplicationContext(),sus_txt,Toast.LENGTH_LONG).show();
                        tv_snack.setText(getString(com.movhaul.customer.R.string.aoptp) + str_data);
                        snackbar.show();


                    } else if (status.equals("false")) {




                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag","nt"+e.toString());
                    // Toast.makeText(getApplicationContext(),"Network Errror0",Toast.LENGTH_LONG).show();
                    snackbar.show();
                }
            } else {
                // Toast.makeText(getApplicationContext(),"Network Errror1",Toast.LENGTH_LONG).show();
                snackbar.show();
            }

        }

    }


}
