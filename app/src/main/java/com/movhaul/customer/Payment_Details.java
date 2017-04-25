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
import android.widget.Toast;

import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.ExpiredAccessCodeException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

/**
 * Created by sqindia on 02-11-2016.
 */

public class Payment_Details extends Activity {
    Button btn_continue,btn_cardpay;
    LinearLayout btn_back;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String tot_amt;
    TextView tv_payment;
    String payment;
    private RadioGroup radioGroup;
    private RadioButton r_card,bank,verve,wallet;
    android.widget.LinearLayout lt_payment,lt_choose;


    String paystack_public_key = "pk_test_7a2b290173743af455823f46d4d1f63f4377cce4";
    String paystack_secrect_key = "sk_test_12704985e95d8959e480ac4eef130e4c6875a801";

    Card card;
    private Charge charge;
    private Transaction transaction;
    private String referenc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.movhaul.customer.R.layout.payment_details);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);

        btn_continue = (Button) findViewById(com.movhaul.customer.R.id.btn_continue);
        btn_back = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_back);
        tv_payment = (TextView) findViewById(com.movhaul.customer.R.id.textview_payment);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Payment_Details.this);
        editor = sharedPreferences.edit();

        tot_amt = sharedPreferences.getString("payment_amount","");

        //tv_payment.setText(tot_amt);

        PaystackSdk.initialize(getApplicationContext());

        lt_payment = (android.widget.LinearLayout) findViewById(R.id.card_payment);
        lt_choose = (android.widget.LinearLayout) findViewById(R.id.payment);
        lt_payment.setVisibility(View.GONE);
        radioGroup = (RadioGroup) findViewById(com.movhaul.customer.R.id.radioGroup2);
        r_card  = (RadioButton) findViewById(R.id.radio1);


        btn_cardpay = (Button) findViewById(com.movhaul.customer.R.id.button_cardpay);


        btn_cardpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String cardNum = "4123450131001381";

                card = new Card.Builder(cardNum, 0, 0, "").build();
                card.setCvc("883");
                card.setExpiryMonth(11);
                card.setExpiryYear(2025);

                try {
                    startAFreshCharge();
                } catch (Exception e) {
                    Log.e("tag","An error occured hwile charging card: %s %s"+ e.getClass().getSimpleName()+ e.getMessage());
                }



            }
        });







        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // find which radio button is selected

                if(checkedId == com.movhaul.customer.R.id.radio1) {

                    payment = "CARD";
                    Log.e("tag",payment);
                    lt_payment.setVisibility(View.VISIBLE);
                    lt_choose.setVisibility(View.GONE);

                } else if(checkedId == com.movhaul.customer.R.id.radio2) {

                    payment = "BANKING";
                    Log.e("tag",payment);

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
                Intent i = new Intent(Payment_Details.this, DriversList.class);
                startActivity(i);
                finish();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lt_payment.setVisibility(View.VISIBLE);
                lt_choose.setVisibility(View.GONE);

                /*Intent i = new Intent(Payment_Details.this, WebViewAct.class);
                startActivity(i);*/

                //new login_customer().execute();
            }
        });

    }
    @Override
    public void onBackPressed() {

        if(lt_payment.isShown()){

            r_card.setChecked(false);
            lt_payment.setVisibility(View.GONE);
            lt_choose.setVisibility(View.VISIBLE);



        }


        //super.onBackPressed();
        /*Intent i = new Intent(Payment_Details.this,DashboardNavigation.class);
        startActivity(i);
        finish();*/
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }



    private void startAFreshCharge() {
        // initialize the charge
        charge = new Charge();
        new fetchAccessCodeFromServer().execute("https://api.paystack.co/transaction/initialize");
    }

    private class fetchAccessCodeFromServer extends AsyncTask<String, Void, String> {
        private String error;

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {


                try {
                    JSONObject jo = new JSONObject(result);
                    String status = jo.getString("status");
                    Log.d("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {


                        String data = jo.getString("data");

                        JSONObject jos = new JSONObject(data);

                        String ref = jos.getString("reference");
                        String access = jos.getString("access_code");

                        Log.e("ref: ", ref);
                        Log.e("access: ", access);

                        referenc = ref;

                        charge.setAccessCode(access);
                        charge.setCard(card);
                        chargeCard();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                }


            } else {
                Log.e("tag",String.format("There was a problem getting a new access code form the backend: %s", error));
            }
        }

        @Override
        protected String doInBackground(String... ac_url) {

            String json;
            try {
                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(ac_url[0]);
                httppost.setHeader("Authorization", "Bearer "+paystack_secrect_key);


                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("amount", "38900");
                    jsonObject.put("email", "ramya@sqindia.net");

                    json = jsonObject.toString();
                    httppost.setEntity(new StringEntity(json));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();
                    int statusCode = response.getStatusLine().getStatusCode();
                    Log.e("tag", response.getStatusLine().toString());
                    if (statusCode == 200) {
                        responseString = EntityUtils.toString(r_entity);

                        Log.e("InputStream00", responseString);
                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                    }
                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                } catch (IOException e) {
                    responseString = e.toString();
                }
                return responseString;
            } catch (Exception e) {
                Log.e("InputStream0", e.getLocalizedMessage());
            }
            return null;
        }
    }




    private void chargeCard() {
        transaction = null;
        PaystackSdk.chargeCard(Payment_Details.this, charge, new co.paystack.android.Paystack.TransactionCallback() {
            // This is called only after transaction is successful
            @Override
            public void onSuccess(Transaction transaction) {

                Payment_Details.this.transaction = transaction;
                Toast.makeText(Payment_Details.this, transaction.getReference(), Toast.LENGTH_LONG).show();
                Log.e("tag","trans3: "+transaction);
                new verifyOnServer().execute(transaction.getReference());
            }

            // This is called only before requesting OTP
            // Save reference so you may send to server if
            // error occurs with OTP
            // No need to dismiss dialog
            @Override
            public void beforeValidate(Transaction transaction) {
                Payment_Details.this.transaction = transaction;
                Log.e("tag","trans2: "+transaction);
                Toast.makeText(Payment_Details.this, transaction.getReference(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                // If an access code has expired, simply ask your server for a new one
                // and restart the charge instead of displaying error
                Payment_Details.this.transaction = transaction;
                Log.e("tag","trans0: "+transaction);
                if (error instanceof ExpiredAccessCodeException) {
                    Payment_Details.this.startAFreshCharge();
                    Payment_Details.this.chargeCard();
                    return;
                }


                if (transaction.getReference() != null) {
                    Toast.makeText(Payment_Details.this, transaction.getReference() + " concluded with error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    new verifyOnServer().execute(transaction.getReference());
                } else {
                    Toast.makeText(Payment_Details.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("tag",String.format("Error: %s %s", error.getClass().getSimpleName()+ error.getMessage()));
                }
            }

        });
    }


    private class verifyOnServer extends AsyncTask<String, Void, String> {

        private String error;

        @Override
        protected String doInBackground(String... reference) {

            try {
                String responseString = null;

                Log.e("tag","ref Code : "+referenc);

                String url = "https://api.paystack.co/transaction" + "/verify/" + referenc;

                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(url);

                // add request header
                request.addHeader("Authorization", "Bearer "+paystack_secrect_key);

                HttpResponse response = client.execute(request);

                Log.e("tag","\nSending 'GET' request to URL : " + url);
                Log.e("tag","Response Code : " +
                        response.getStatusLine().getStatusCode());

                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

                Log.e("tag","rs: "+result.toString());

                responseString = result.toString();


                return responseString;
            } catch (Exception e) {
                Log.e("InputStream0", e.getLocalizedMessage());
            }
            return null;
        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                Log.e("tag","Gateway response: %s "+ result);

            } else {
                Log.e("tag",String.format("There was a problem verifying %s on the backend: %s ", referenc, error));
            }
        }



    }



}
