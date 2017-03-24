package com.movhaul.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sqindia on 22-10-2016.
 */

public class Forgot_Mobile extends Activity {
    LinearLayout btn_back;
    EditText edtxt_email;
    String str_email;
    TextInputLayout flt_email;
    Button btn_submit;
    Typeface tf;
    Snackbar snackbar;
    Config config;
    TextView tv_snack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.movhaul.customer.R.layout.forgot_phone);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);

        flt_email = (TextInputLayout) findViewById(com.movhaul.customer.R.id.float_email);
        btn_back = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_back);
        edtxt_email = (EditText) findViewById(com.movhaul.customer.R.id.editTextEmail);
        btn_submit = (Button) findViewById(com.movhaul.customer.R.id.btn_submit);

         tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        flt_email.setTypeface(tf);


        snackbar = Snackbar
                .make(findViewById(com.movhaul.customer.R.id.top), "Network Error! Please Try Again Later.", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);

        if (!config.isConnected(Forgot_Mobile.this)) {
            snackbar.show();
            tv_snack.setText("Please Connect Internet and Try again");
        }


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_email = edtxt_email.getText().toString().trim();

                if (!(str_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(str_email).matches())) {


                    str_email = edtxt_email.getText().toString().trim();

                    new forgot_mobile().execute();


                   /* Intent i = new Intent(Forgot_Mobile.this, LoginOtpActivity.class);
                    startActivity(i);
                    finish();*/
                } else {
                    edtxt_email.setError("Enter a valid email address!");
                    edtxt_email.requestFocus();
                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Forgot_Mobile.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Forgot_Mobile.this, LoginActivity.class);
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


    public class forgot_mobile extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag","reg_preexe");
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("customer_email", str_email);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL + "customeremailotp", json);

            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag","tag"+s);


            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                    String msg = jo.getString("message");
                    Log.d("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {


                        // String sus_txt = "Thank you for Signing Up MoveHaul.";

                        //Toast.makeText(getApplicationContext(),sus_txt,Toast.LENGTH_LONG).show();

                        Intent i = new Intent(Forgot_Mobile.this, LoginOtpActivity.class);
                        i.putExtra("for","email");
                        i.putExtra("data",str_email);
                        startActivity(i);
                        finish();


                    } else if (status.equals("false")) {


                        if (msg.contains("Register with Movehaul first to Generate OTP")) {

                           // Toast.makeText(getApplicationContext(),"Mobile Number Not Registered",Toast.LENGTH_LONG).show();

                            tv_snack.setText("No Number Associated with this Email !");
                            snackbar.show();

                        }
                        else if (msg.contains("Error Occured[object Object]")) {

                            Intent i = new Intent(Forgot_Mobile.this, LoginOtpActivity.class);
                            i.putExtra("for","email");
                            i.putExtra("data",str_email);
                            startActivity(i);
                            finish();

                        }
                        else  {

                            //Toast.makeText(getApplicationContext(),"Please Try Again Later",Toast.LENGTH_LONG).show();
                            tv_snack.setText("Please Try Again Later !");
                            snackbar.show();
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag","nt"+e.toString());
                   // Toast.makeText(getApplicationContext(),"Network Errror0",Toast.LENGTH_LONG).show();
                    tv_snack.setText("Network Error, Please Try Again Later !");
                    snackbar.show();
                }
            } else {
               // Toast.makeText(getApplicationContext(),"Network Errror1",Toast.LENGTH_LONG).show();
                tv_snack.setText("Network Error, Please Try Again Later !");
                snackbar.show();
            }

        }

    }



}
