package com.movhaul.customer;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by Salman on 24-04-2017.
 */

public class Paystack extends Activity {
    Button btn_submit;
    TextView tv_txt;

    String paystack_public_key = "pk_test_7a2b290173743af455823f46d4d1f63f4377cce4";
    String paystack_secrect_key = "sk_test_12704985e95d8959e480ac4eef130e4c6875a801";

    Card card;
    private Charge charge;
    private Transaction transaction;
    private String referenc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paystack);

        PaystackSdk.initialize(getApplicationContext());

        btn_submit = (Button) findViewById(R.id.button);
        tv_txt = (TextView) findViewById(R.id.textview);

        PaystackSdk.setPublicKey(paystack_public_key);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cardNum = "5066666666666666666";

                card = new Card.Builder(cardNum, 0, 0, "").build();
                card.setCvc("666");
                card.setExpiryMonth(11);
                card.setExpiryYear(2025);

                try {
                    startAFreshCharge();
                } catch (Exception e) {
                    tv_txt.setText(String.format("An error occured hwile charging card: %s %s", e.getClass().getSimpleName(), e.getMessage()));
                }

            }
        });
    }


    private void startAFreshCharge() {
        // initialize the charge
        charge = new Charge();
        // Perform transaction/initialize on our server to get an access code
        // documentation: https://developers.paystack.co/reference#initialize-a-transaction

        //new fetchAccessCodeFromServer().execute(backend_url+"/new-access-code");

          new fetchAccessCodeFromServer().execute("https://api.paystack.co/transaction/initialize");


    }

    private class fetchAccessCodeFromServer extends AsyncTask<String, Void, String> {
        private String error;

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tv_txt.setText(result);
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
                    tv_txt.setText(e.toString());
                }


            } else {
                tv_txt.setText(String.format("There was a problem getting a new access code form the backend: %s", error));
            }
        }

        @Override
        protected String doInBackground(String... ac_url) {

            String json;
            try {
                //driver/driverupdate
                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(ac_url[0]);
                //  HttpPost httppost = new HttpPost("http://104.197.80.225:3010/vagan/post");

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
        PaystackSdk.chargeCard(Paystack.this, charge, new co.paystack.android.Paystack.TransactionCallback() {
            // This is called only after transaction is successful
            @Override
            public void onSuccess(Transaction transaction) {

                Paystack.this.transaction = transaction;
                tv_txt.setText(" ");
                Toast.makeText(Paystack.this, transaction.getReference(), Toast.LENGTH_LONG).show();
                Log.e("tag","trans3: "+transaction);
                new verifyOnServer().execute(transaction.getReference());
            }

            // This is called only before requesting OTP
            // Save reference so you may send to server if
            // error occurs with OTP
            // No need to dismiss dialog
            @Override
            public void beforeValidate(Transaction transaction) {
                Paystack.this.transaction = transaction;
                Log.e("tag","trans2: "+transaction);
                Toast.makeText(Paystack.this, transaction.getReference(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                // If an access code has expired, simply ask your server for a new one
                // and restart the charge instead of displaying error
                Paystack.this.transaction = transaction;
                Log.e("tag","trans0: "+transaction);
                if (error instanceof ExpiredAccessCodeException) {
                    Paystack.this.startAFreshCharge();
                    Paystack.this.chargeCard();
                    return;
                }


                if (transaction.getReference() != null) {
                    Toast.makeText(Paystack.this, transaction.getReference() + " concluded with error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    tv_txt.setText(String.format("%s  concluded with error: %s %s", transaction.getReference(), error.getClass().getSimpleName(), error.getMessage()));
                    new verifyOnServer().execute(transaction.getReference());
                } else {
                    Toast.makeText(Paystack.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    tv_txt.setText(String.format("Error: %s %s", error.getClass().getSimpleName(), error.getMessage()));
                }
            }

        });
    }




    private class verifyOnServer extends AsyncTask<String, Void, String> {

        private String error;

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                tv_txt.setText(String.format("Gateway response: %s", result));

            } else {
                tv_txt.setText(String.format("There was a problem verifying %s on the backend: %s ", referenc, error));
            }
        }

        @Override
        protected String doInBackground(String... reference) {

            try {
                String responseString = null;


               /* HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("https://api.paystack.co/transaction" + "/verify/" + this.reference);
                httpget.setHeader("Authorization", "Bearer "+paystack_secrect_key);
                try {
                    HttpResponse response = httpclient.execute(httpget);
                    HttpEntity r_entity = response.getEntity();
                    int statusCode = response.getStatusLine().getStatusCode();
                    Log.e("tag_code", response.getStatusLine().toString());
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
                }*/

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
    }






}
