package com.movhaul.customer;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sqindia on 02-11-2016.
 */

public class Payment_Card_Details extends Activity {
    Button btn_paynow, btn_ok;
    Dialog dialog1;
    ImageView btn_close;
    TextView tv_dialog1, tv_dialog2, tv_dialog3, tv_dialog4;
    LinearLayout btn_back;
    TextInputLayout flt_exp_date, flt_cvv, flt_name;
    Typeface type;
    SharedPreferences sharedPreferences;
    String bidding_id, booking_id, driver_id, transaction_id,id,token;
    SharedPreferences.Editor editor;
    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.movhaul.customer.R.layout.payment_carddetails);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);
        btn_paynow = (Button) findViewById(com.movhaul.customer.R.id.button_cardpay);
        btn_back = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_back);
        flt_exp_date = (TextInputLayout) findViewById(com.movhaul.customer.R.id.float_exp_date);
        flt_cvv = (TextInputLayout) findViewById(com.movhaul.customer.R.id.float_cvv);
        flt_name = (TextInputLayout) findViewById(com.movhaul.customer.R.id.float_name);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Payment_Card_Details.this);
        editor = sharedPreferences.edit();

        mProgressDialog = new ProgressDialog(Payment_Card_Details.this);
        mProgressDialog.setTitle("Loading..");
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);


        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");


        ImageView btn_close;
        TextView tv_dialog1, tv_dialog2, tv_dialog3, tv_dialog4;
        flt_cvv.setTypeface(type);
        flt_exp_date.setTypeface(type);
        flt_name.setTypeface(type);

        btn_paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                booking_id = sharedPreferences.getString("booking_id", "");
                driver_id = sharedPreferences.getString("driver_id", "");
                bidding_id = sharedPreferences.getString("bidding_id", "");
                transaction_id = "oid3982asdfeo3";

                Log.e("tag","book "+booking_id);
                Log.e("tag","drik "+driver_id);
                Log.e("tag","bid "+bidding_id);

                new book_now_task().execute();

                //dialog1.show();
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
        dialog1.setContentView(com.movhaul.customer.R.layout.dialogue_job_posting);
        btn_ok = (Button) dialog1.findViewById(com.movhaul.customer.R.id.button_ok);
        btn_close = (ImageView) dialog1.findViewById(com.movhaul.customer.R.id.button_close);
        tv_dialog1 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_1);
        tv_dialog2 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_2);
        tv_dialog3 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_3);
        tv_dialog4 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_4);
        tv_dialog1.setText("Your Trip has Been");
        tv_dialog2.setText("Confirmed!!");
        tv_dialog3.setText("Our Driver will");
        tv_dialog4.setText("Contact you soon..");

        tv_dialog1.setTypeface(type);
        tv_dialog2.setTypeface(type);
        tv_dialog3.setTypeface(type);
        tv_dialog4.setTypeface(type);
        btn_ok.setTypeface(type);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();

                editor.putString("job_id","");
                editor.commit();

                Intent i = new Intent(Payment_Card_Details.this, DashboardNavigation.class);
                startActivity(i);
                finish();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();


                editor.putString("job_id","");
                editor.commit();

                Intent i = new Intent(Payment_Card_Details.this, DashboardNavigation.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Payment_Card_Details.this, Payment_Details.class);
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







    public class book_now_task extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";


                Log.e("tag","no poto");

                String  s = "";
                JSONObject jsonObject = new JSONObject();
                try {


                    jsonObject.put("transaction_id",transaction_id);
                    jsonObject.put("booking_id", booking_id);
                    jsonObject.put("driver_id", driver_id);
                    jsonObject.put("bidding_id", bidding_id);


                    json = jsonObject.toString();

                    return s = HttpUtils.makeRequest1(com.movhaul.customer.Config.WEB_URL + "customer/payment",json,id,token);
                } catch (JSONException e) {
                    e.printStackTrace();
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
                    Log.d("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {

                        dialog1.show();
                    } else if (status.equals("false")) {

                        Log.e("tag", "Location not updated");
                        //has to check internet and location...
                        Toast.makeText(getApplicationContext(),"Network Errror. Please Try Again Later",Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    Toast.makeText(getApplicationContext(),"Network Errror. Please Try Again Later",Toast.LENGTH_LONG).show();
                }
            } else {
                 Toast.makeText(getApplicationContext(),"Network Errror1",Toast.LENGTH_LONG).show();
            }

        }

    }





















}
