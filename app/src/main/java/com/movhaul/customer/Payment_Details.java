package com.movhaul.customer;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;
import com.systemspecs.remita.remitapaymentgateway.RemitaMainActivity;

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
    private static final int REQUEST_CODE_PAYMENT = 545;

    private static final int REQUEST_CODE_PAYMENT_ROADSIDE = 123;
    Button btn_continue, btn_cardpay;
    LinearLayout btn_back;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String tot_amt, cus_mail;
    TextView tv_payment;
    String payment;
    android.widget.LinearLayout lt_payment, lt_choose;
    String bidding_id, booking_id, driver_id, transaction_id, id, token;
    String paystack_public_key = "pk_test_7a2b290173743af455823f46d4d1f63f4377cce4";
    String paystack_secrect_key = "sk_test_12704985e95d8959e480ac4eef130e4c6875a801";
    ProgressDialog mProgressDialog;
    Card card;
    Dialog dialog1;
    Button btn_ok;
    ImageView btn_close;
    Typeface tf;
    TextView tv_dialog1, tv_dialog2, tv_dialog3, tv_dialog4;
    Snackbar snackbar;
    EditText et_card1, et_card2, et_card3, et_card4, et_exp, et_cvv, name;
    TextView tv_snack;
    boolean bl_truck;
    Dialog dg_road_confirm;
    EditText et_name, et_phone;
    CheckBox cb_box;
    private RadioGroup radioGroup;
    private RadioButton r_card, bank, verve, wallet;
    private Charge charge;
    private Transaction transaction;
    private String referenc;
    String cus_name,cus_mobile;

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

        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");

        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");


        cus_mail = sharedPreferences.getString("customer_email", "");
        cus_name = sharedPreferences.getString("customer_name", "");
        cus_mobile = sharedPreferences.getString("customer_mobile", "");



        booking_id = sharedPreferences.getString("booking_id", "");
        tot_amt = sharedPreferences.getString("payment_amount", "");

        if (!(sharedPreferences.getString("book_for", "").equals("roadside"))) {
            bl_truck = true;
            driver_id = sharedPreferences.getString("driver_id", "");
            bidding_id = sharedPreferences.getString("bidding_id", "");
        }

        snackbar = Snackbar
                .make(findViewById(com.movhaul.customer.R.id.top), "Network Error! Please Try Again Later.", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);


        Log.e("tag", "bok " + booking_id);
        Log.e("tag", "dr " + driver_id);
        Log.e("tag", "bid " + bidding_id);

        tv_payment.setText(tot_amt);

        PaystackSdk.initialize(getApplicationContext());

        lt_payment = (android.widget.LinearLayout) findViewById(R.id.card_payment);
        lt_choose = (android.widget.LinearLayout) findViewById(R.id.payment);
        lt_payment.setVisibility(View.GONE);
        radioGroup = (RadioGroup) findViewById(com.movhaul.customer.R.id.radioGroup2);
        r_card = (RadioButton) findViewById(R.id.radio1);
        bank = (RadioButton) findViewById(R.id.radio2);
        btn_cardpay = (Button) findViewById(com.movhaul.customer.R.id.button_cardpay);

        et_card1 = (EditText) findViewById(R.id.editext_card1);
        et_card2 = (EditText) findViewById(R.id.editext_card2);
        et_card3 = (EditText) findViewById(R.id.editext_card3);
        et_card4 = (EditText) findViewById(R.id.editext_card4);
        et_exp = (EditText) findViewById(R.id.editTextExpiryDate);
        et_cvv = (EditText) findViewById(R.id.editTextCvv);

        mProgressDialog = new ProgressDialog(Payment_Details.this);
        mProgressDialog.setTitle("Loading..");
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);


        dialog1 = new Dialog(Payment_Details.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setCancelable(false);
        dialog1.setContentView(com.movhaul.customer.R.layout.dialogue_job_confirm);
        btn_ok = (Button) dialog1.findViewById(com.movhaul.customer.R.id.button_ok);
        btn_close = (ImageView) dialog1.findViewById(com.movhaul.customer.R.id.button_close);
        tv_dialog1 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_1);
        tv_dialog2 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_2);
       tv_dialog3 = (TextView) findViewById(com.movhaul.customer.R.id.textView_3);
        //tv_dialog4 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_4);
        et_name = (EditText) findViewById(R.id.name);
        et_phone = (EditText) findViewById(R.id.phone);
        cb_box = (CheckBox) findViewById(R.id.checkbox);
        // tv_dialog1.setText("Your Trip has Been");
        //   tv_dialog2.setText("Confirmed!!");
        //   tv_dialog3.setText("Our Driver will");
        //  tv_dialog4.setText("Contact you soon..");

        et_name.setText(cus_name);
        et_phone.setText(cus_mobile);

        tv_dialog1.setTypeface(tf);
        tv_dialog2.setTypeface(tf);
        tv_dialog3.setTypeface(tf);
       // tv_dialog4.setTypeface(tf);
        et_name.setTypeface(tf);
        et_phone.setTypeface(tf);
        btn_ok.setTypeface(tf);


        cb_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    et_name.setVisibility(View.GONE);
                    et_phone.setVisibility(View.GONE);
                    tv_dialog3.setText("Shipment Received by You ");
                    //et_name.setText(cus_name);
                   // et_phone.setText(cus_mobile);
                }
                else{
                    et_name.setVisibility(View.VISIBLE);
                    et_phone.setVisibility(View.VISIBLE);
                    tv_dialog3.setText("Shipment Received by");
                    et_name.setText("");
                    et_name.requestFocus();
                    et_phone.setText("");
                }
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();

                editor.putString("job_id", "");
                editor.commit();

                Intent i = new Intent(Payment_Details.this, DashboardNavigation.class);
                startActivity(i);
                finish();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog1.dismiss();
                editor.putString("job_id", "");
                editor.commit();
                Intent i = new Intent(Payment_Details.this, DashboardNavigation.class);
                startActivity(i);
                finish();

                /*if (cb_box.isChecked()) {
                    dialog1.dismiss();
                    editor.putString("job_id", "");
                    editor.commit();
                    Intent i = new Intent(Payment_Details.this, DashboardNavigation.class);
                    startActivity(i);
                    finish();
                } else {
                    if (!et_name.getText().toString().trim().isEmpty() && et_name.getText().toString().trim().length() > 4) {
                        if (!et_phone.getText().toString().trim().isEmpty() && et_phone.getText().toString().trim().length() > 9) {
                            dialog1.dismiss();
                            editor.putString("job_id", "");
                            editor.commit();
                            Intent i = new Intent(Payment_Details.this, DashboardNavigation.class);
                            startActivity(i);
                            finish();
                        } else {
                            snackbar.show();
                            tv_snack.setText("Enter Valid Receiver Phone");
                        }
                    } else {
                        snackbar.show();
                        tv_snack.setText("Enter Valid Receiver Name");
                    }
                }*/


            }
        });


        dg_road_confirm = new Dialog(Payment_Details.this);
        dg_road_confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dg_road_confirm.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dg_road_confirm.setCancelable(false);
        dg_road_confirm.setContentView(com.movhaul.customer.R.layout.dialog_road_confirm);

        Button btn_yes = (Button) dg_road_confirm.findViewById(R.id.button_yes);
        TextView tv_txt1 = (TextView) dg_road_confirm.findViewById(R.id.textView_1);
        TextView tv_txt2 = (TextView) dg_road_confirm.findViewById(R.id.textView_2);

        tv_txt1.setTypeface(tf);
        tv_txt2.setTypeface(tf);
        btn_yes.setTypeface(tf);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dg_road_confirm.dismiss();

                editor.putString("job_id", "");
                editor.commit();

                Intent i = new Intent(Payment_Details.this, DashboardNavigation.class);
                startActivity(i);
                finish();
            }
        });


        btn_cardpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_card1.getText().toString().length() >= 4) {
                    if (et_card2.getText().toString().length() >= 4) {
                        if (et_card3.getText().toString().length() >= 4) {
                            if (et_card4.getText().toString().length() >= 4) {
                                if (et_exp.getText().toString().length() >= 6) {
                                    if (et_cvv.getText().toString().length() >= 3) {

                                        String cardNum = et_card1.getText().toString() + et_card2.getText().toString() + et_card3.getText().toString() + et_card4.getText().toString();
                                        card = new Card.Builder(cardNum, 0, 0, "").build();
                                        card.setCvc(et_cvv.getText().toString());
                                        card.setExpiryMonth(11);
                                        card.setExpiryYear(2025);
                                        try {
                                            startAFreshCharge();
                                        } catch (Exception e) {
                                            Log.e("tag", "An error occured hwile charging card: %s %s" + e.getClass().getSimpleName() + e.getMessage());
                                        }

                                    } else {
                                        snackbar.show();
                                        tv_snack.setText("Enter Valid Cvv Number");
                                        et_cvv.requestFocus();

                                    }
                                } else {

                                    snackbar.show();
                                    tv_snack.setText("Enter Valid Expiry Date");
                                    et_exp.requestFocus();

                                }
                            } else {
                                snackbar.show();
                                tv_snack.setText("Enter Valid Card Number");
                                et_card4.requestFocus();
                            }
                        } else {
                            snackbar.show();
                            tv_snack.setText("Enter Valid Card Number");
                            et_card3.requestFocus();
                        }
                    } else {
                        snackbar.show();
                        tv_snack.setText("Enter Valid Card Number");
                        et_card2.requestFocus();

                    }
                } else {
                    snackbar.show();
                    tv_snack.setText("Enter Valid Card Number");
                    et_card1.requestFocus();

                }


              /*  String cardNum = "4123450131001381";
                card = new Card.Builder(cardNum, 0, 0, "").build();
                card.setCvc("883");
                card.setExpiryMonth(11);
                card.setExpiryYear(2025);
                try {
                    startAFreshCharge();
                } catch (Exception e) {
                    Log.e("tag", "An error occured hwile charging card: %s %s" + e.getClass().getSimpleName() + e.getMessage());
                }*/


            }
        });


        et_exp.addTextChangedListener(new AutoAddTextWatcher(et_exp, "/", 2));


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // find which radio button is selected

                if (checkedId == com.movhaul.customer.R.id.radio1) {

                    if(cb_box.isChecked()) {
                        payment = "CARD";
                        Log.e("tag", payment);
                        lt_payment.setVisibility(View.VISIBLE);
                        lt_choose.setVisibility(View.GONE);

                        cus_name = sharedPreferences.getString("customer_name","");
                        cus_mobile = sharedPreferences.getString("customer_mobile","");
                    }
                    else{

                        if (!et_name.getText().toString().trim().isEmpty() && et_name.getText().toString().trim().length() > 4) {
                            if (!et_phone.getText().toString().trim().isEmpty() && et_phone.getText().toString().trim().length() > 9) {

                                payment = "CARD";
                                Log.e("tag", payment);
                                lt_payment.setVisibility(View.VISIBLE);
                                lt_choose.setVisibility(View.GONE);
                                cus_name = et_name.getText().toString().trim();
                                cus_mobile = et_phone.getText().toString().trim();

                            } else {

                                snackbar.show();
                                tv_snack.setText("Enter Valid Receiver Phone");
                                group.clearCheck();
                            }
                        } else {
                            snackbar.show();
                            tv_snack.setText("Enter Valid Receiver Name");
                            group.clearCheck();
                        }
                    }


                } else if (checkedId == com.movhaul.customer.R.id.radio2) {

                    if(cb_box.isChecked()) {
                        payment = "BANKING";
                        Log.e("tag", payment);

                        cus_name = sharedPreferences.getString("customer_name","");
                        cus_mobile = sharedPreferences.getString("customer_mobile","");

                        Intent intent = new Intent(Payment_Details.this, RemitaMainActivity.class);
                        intent.putExtra("amount", tot_amt);
                        intent.putExtra("testMode", true);
                        intent.putExtra("apiKey", "U1lTUC4xNUhPMTIkMTIzLjR8U1lTUA==");
                        intent.putExtra("txnToken", "55316C54554334784E5568504D54496B4D54497A4C6A523855316C5455413D3D7C3932333737633266613035313135306337363534386636376266623131303165383831366464343834666234363064653062343731663538643461323835303537333638653232313135363366383334666337613166333265333336653834626539656566393465396363356131363739353463646239333434363164313732");
                        if (bl_truck)
                            startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                        else
                            startActivityForResult(intent, REQUEST_CODE_PAYMENT_ROADSIDE);

                    }
                    else{

                        if (!et_name.getText().toString().trim().isEmpty() && et_name.getText().toString().trim().length() > 4) {
                            if (!et_phone.getText().toString().trim().isEmpty() && et_phone.getText().toString().trim().length() > 9) {

                                Intent intent = new Intent(Payment_Details.this, RemitaMainActivity.class);
                                intent.putExtra("amount", tot_amt);
                                intent.putExtra("testMode", true);
                                intent.putExtra("apiKey", "U1lTUC4xNUhPMTIkMTIzLjR8U1lTUA==");
                                intent.putExtra("txnToken", "55316C54554334784E5568504D54496B4D54497A4C6A523855316C5455413D3D7C3932333737633266613035313135306337363534386636376266623131303165383831366464343834666234363064653062343731663538643461323835303537333638653232313135363366383334666337613166333265333336653834626539656566393465396363356131363739353463646239333434363164313732");
                                if (bl_truck)
                                    startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                                else
                                    startActivityForResult(intent, REQUEST_CODE_PAYMENT_ROADSIDE);

                                cus_name = et_name.getText().toString().trim();
                                cus_mobile = et_phone.getText().toString().trim();

                            } else {
                                snackbar.show();
                                tv_snack.setText("Enter Valid Receiver Phone");
                                group.clearCheck();
                            }
                        } else {
                            snackbar.show();
                            tv_snack.setText("Enter Valid Receiver Name");
                            group.clearCheck();
                        }

                    }


                }
                else {
                    payment = "Paypal";

                    //Toast.makeText(getApplicationContext(), payment+" Payment Not Supported,Use Card or NetBanking", Toast.LENGTH_LONG).show();

                }

            }


        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(Payment_Details.this, DriversList.class);
                startActivity(i);
                finish();*/
                onBackPressed();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lt_payment.setVisibility(View.VISIBLE);
                lt_choose.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (lt_payment.isShown()) {

            r_card.setChecked(false);
            lt_payment.setVisibility(View.GONE);
            lt_choose.setVisibility(View.VISIBLE);

        } else {
            finish();
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

    private void startAFreshCharge() {
        // initialize the charge
        charge = new Charge();
        mProgressDialog.show();
        new fetchAccessCodeFromServer().execute("https://api.paystack.co/transaction/initialize");
    }

    private void chargeCard() {
        transaction = null;
        PaystackSdk.chargeCard(Payment_Details.this, charge, new co.paystack.android.Paystack.TransactionCallback() {
            // This is called only after transaction is successful
            @Override
            public void onSuccess(Transaction transaction) {

                Payment_Details.this.transaction = transaction;
                // Toast.makeText(Payment_Details.this, transaction.getReference(), Toast.LENGTH_LONG).show();
                Log.e("tag", "trans3: " + transaction);
                new verifyOnServer().execute(transaction.getReference());
            }

            // This is called only before requesting OTP
            // Save reference so you may send to server if
            // error occurs with OTP
            // No need to dismiss dialog
            @Override
            public void beforeValidate(Transaction transaction) {
                Payment_Details.this.transaction = transaction;
                Log.e("tag", "trans2: " + transaction);
                // Toast.makeText(Payment_Details.this, transaction.getReference(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                // If an access code has expired, simply ask your server for a new one
                // and restart the charge instead of displaying error
                Payment_Details.this.transaction = transaction;
                Log.e("tag", "trans0: " + transaction);
                if (error instanceof ExpiredAccessCodeException) {
                    Payment_Details.this.startAFreshCharge();
                    Payment_Details.this.chargeCard();
                    return;
                }


                if (transaction.getReference() != null) {
                    // Toast.makeText(Payment_Details.this, transaction.getReference() + " concluded with error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    new verifyOnServer().execute(transaction.getReference());
                } else {
                    // Toast.makeText(Payment_Details.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("tag", String.format("Error: %s %s", error.getClass().getSimpleName() + error.getMessage()));
                }
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PAYMENT) {

            Log.e("tagMyAdapter_class", "onActivityResult" + data.toString());

            Log.e("tag", "cd:" + resultCode);
            Log.e("tag", "rc:" + requestCode);
            Log.e("tag", "dt:" + data.toString());

            Bundle bundle = data.getExtras();
            String authorizid = bundle.getString("authoriztionId");
            String remitaTransRef = bundle.getString("remitaTransRef");
            String responceCode = bundle.getString("responseCode");

            Log.e("tag", "ss: " + authorizid);
            Log.e("tag", "ss1: " + remitaTransRef);
            Log.e("tag", "ss2: " + responceCode);

            transaction_id = remitaTransRef;

            new book_now_task("Trc0Bz39dox").execute();
        } else if (requestCode == REQUEST_CODE_PAYMENT_ROADSIDE && resultCode == Activity.RESULT_OK) {

            Bundle bundle = data.getExtras();
            String authorizid = bundle.getString("authoriztionId");
            String remitaTransRef = bundle.getString("remitaTransRef");
            String responceCode = bundle.getString("responseCode");

            transaction_id = remitaTransRef;
            Log.e("tag", "ss1: " + remitaTransRef);

            new book_roadside().execute();
        } else {
            bank.setChecked(false);
        }


    }

    private class fetchAccessCodeFromServer extends AsyncTask<String, Void, String> {
        private String error;

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            mProgressDialog.dismiss();
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
                Log.e("tag", String.format("There was a problem getting a new access code form the backend: %s", error));

                snackbar.show();
                tv_snack.setText(String.format("There was a problem getting a new access code form the backend: %s", error));
            }
        }

        @Override
        protected String doInBackground(String... ac_url) {

            String json;
            try {
                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(ac_url[0]);
                httppost.setHeader("Authorization", "Bearer " + paystack_secrect_key);


                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("amount", tot_amt + "00");
                    jsonObject.put("email", cus_mail);

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

    private class verifyOnServer extends AsyncTask<String, Void, String> {

        private String error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... reference) {

            try {
                String responseString = null;

                Log.e("tag", "ref Code : " + referenc);

                String url = "https://api.paystack.co/transaction" + "/verify/" + referenc;

                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(url);

                // add request header
                request.addHeader("Authorization", "Bearer " + paystack_secrect_key);

                HttpResponse response = client.execute(request);

                Log.e("tag", "\nSending 'GET' request to URL : " + url);
                Log.e("tag", "Response Code : " +
                        response.getStatusLine().getStatusCode());

                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

                Log.e("tag", "rs: " + result.toString());

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
            mProgressDialog.dismiss();
            ;
            if (result != null) {
                Log.e("tag", "Gateway response: %s " + result);

                if (result != null) {
                    try {
                        JSONObject jo = new JSONObject(result);
                        String status = jo.getString("status");
                        Log.d("tag", "<-----Status----->" + status);
                        if (status.equals("true")) {
                            String data = jo.getString("data");

                            JSONObject j_data = new JSONObject(data);

                            String payment_sts = j_data.getString("status");
                            if (payment_sts.equals("success")) {
                                String pay_ref = j_data.getString("reference");
                                new book_now_task(pay_ref).execute();

                            } else {
                                //   Toast.makeText(getApplicationContext(), "Payment Failed", Toast.LENGTH_LONG).show();
                                snackbar.show();
                                tv_snack.setText("Payment Failed.,Please Try Again");

                                r_card.setChecked(false);
                                lt_payment.setVisibility(View.GONE);
                                lt_choose.setVisibility(View.VISIBLE);
                                //finish();
                            }


                        } else if (status.equals("false")) {

                            Log.e("tag", "Location not updated");
                            //has to check internet and location...
                            // Toast.makeText(getApplicationContext(), "Network Errror. Please Try Again Later", Toast.LENGTH_LONG).show();
                            snackbar.show();
                            tv_snack.setText("Network Errror. Please Try Again Later");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("tag", "nt" + e.toString());
                        // Toast.makeText(getApplicationContext(), "Network Errror. Please Try Again Later", Toast.LENGTH_LONG).show();
                        snackbar.show();
                        tv_snack.setText("Network Errror. Please Try Again Later");
                    }
                } else {
                    //Toast.makeText(getApplicationContext(), "Network Errror1", Toast.LENGTH_LONG).show();
                    snackbar.show();
                    tv_snack.setText("Network Errror. Please Try Again Later");

                }


            } else {
                Log.e("tag", String.format("There was a problem verifying %s on the backend: %s ", referenc, error));

                snackbar.show();
                tv_snack.setText(String.format("There was a problem verifying %s on the backend: %s ", referenc, error));
            }
        }


    }

    public class book_now_task extends AsyncTask<String, Void, String> {

        String reference;

        public book_now_task(String ref) {
            reference = ref;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";


            Log.e("tag", "no poto");

            String s = "";
            JSONObject jsonObject = new JSONObject();
            try {


                jsonObject.put("transaction_id", reference);
                jsonObject.put("booking_id", booking_id);
                jsonObject.put("driver_id", driver_id);
                jsonObject.put("bidding_id", bidding_id);


                json = jsonObject.toString();

                return s = HttpUtils.makeRequest1(com.movhaul.customer.Config.WEB_URL + "customer/payment", json, id, token);
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
                        // Toast.makeText(getApplicationContext(), "Network Errror. Please Try Again Later", Toast.LENGTH_LONG).show();
                        snackbar.show();
                        tv_snack.setText("Network Errror. Please Try Again Later");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    //  Toast.makeText(getApplicationContext(), "Network Errror. Please Try Again Later", Toast.LENGTH_LONG).show();
                    snackbar.show();
                    tv_snack.setText("Network Errror. Please Try Again Later");
                }
            } else {
                //Toast.makeText(getApplicationContext(), "Network Errror1", Toast.LENGTH_LONG).show();
                snackbar.show();
                tv_snack.setText("Network Errror. Please Try Again Later");
            }

        }

    }


    public class AutoAddTextWatcher implements TextWatcher {
        private CharSequence mBeforeTextChanged;
        private int[] mArray_pos;
        private EditText mEditText;
        private String mAppentText;

        public AutoAddTextWatcher(EditText editText, String appendText, int... position) {
            this.mEditText = editText;
            this.mAppentText = appendText;
            this.mArray_pos = position.clone();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            mBeforeTextChanged = s.toString();


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            for (int i = 0; i < mArray_pos.length; i++) {
                if (((mBeforeTextChanged.length() - mAppentText.length() * i) == (mArray_pos[i] - 1) &&
                        (s.length() - mAppentText.length() * i) == mArray_pos[i])) {
                    mEditText.append(mAppentText);

                    break;
                }

                if (((mBeforeTextChanged.length() - mAppentText.length() * i) == mArray_pos[i] &&
                        (s.length() - mAppentText.length() * i) == (mArray_pos[i] + 1))) {
                    int idx_start = mArray_pos[i] + mAppentText.length() * i;
                    int idx_end = Math.min(idx_start + mAppentText.length(), s.length());

                    String sub = mEditText.getText().toString().substring(idx_start, idx_end);

                    if (!sub.equals(mAppentText)) {
                        mEditText.getText().insert(s.length() - 1, mAppentText);
                    }

                    break;
                }

                if (mAppentText.length() > 1 &&
                        (mBeforeTextChanged.length() - mAppentText.length() * i) == (mArray_pos[i] + mAppentText.length()) &&
                        (s.length() - mAppentText.length() * i) == (mArray_pos[i] + mAppentText.length() - 1)) {
                    int idx_start = mArray_pos[i] + mAppentText.length() * i;
                    int idx_end = Math.min(idx_start + mAppentText.length(), s.length());

                    mEditText.getText().delete(idx_start, idx_end);

                    break;
                }

            }


        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    }


    public class book_roadside extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";


            Log.e("tag", "no poto");

            String s = "";
            JSONObject jsonObject = new JSONObject();
            try {


                jsonObject.put("transaction_id", "Rco32w3Viels3");
                jsonObject.put("booking_id", booking_id);


                json = jsonObject.toString();

                return s = HttpUtils.makeRequest1(com.movhaul.customer.Config.WEB_URL + "customer/emergencypayment", json, id, token);
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

                        dg_road_confirm.show();
                    } else if (status.equals("false")) {

                        Log.e("tag", "Location not updated");
                        //has to check internet and location...
                        Toast.makeText(getApplicationContext(), "Network Errror. Please Try Again Later", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    Toast.makeText(getApplicationContext(), "Network Errror. Please Try Again Later", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Network Errror1", Toast.LENGTH_LONG).show();
            }

        }

    }


}
