package net.sqindia.movehaul;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by sqindia on 21-10-2016.
 */

public class RegisterActivity extends Activity {


    LinearLayout btn_back;
    Button btn_submit;
    TextView tv_register,tv_snack;
    EditText et_name, et_email, et_mobile;
    String str_email, str_mobile, str_name;
    TextInputLayout til_name, til_email, til_mobile;
    Snackbar snackbar;
    Config config;
    Typeface tf;
    ProgressDialog mProgressDialog;



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);
        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        config = new Config();


        mProgressDialog = new ProgressDialog(RegisterActivity.this);
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);




        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        tv_register = (TextView) findViewById(R.id.text_register);
        et_name = (EditText) findViewById(R.id.editTextName);
        et_email = (EditText) findViewById(R.id.editTextEmail);
        et_mobile = (EditText) findViewById(R.id.editTextMobile);
        til_email = (TextInputLayout) findViewById(R.id.float_email);
        til_mobile = (TextInputLayout) findViewById(R.id.float_mobile);
        til_name = (TextInputLayout) findViewById(R.id.float_name);

        til_email.setTypeface(tf);
        til_mobile.setTypeface(tf);
        til_name.setTypeface(tf);



        snackbar = Snackbar
                .make(findViewById(R.id.top), R.string.no_internet, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);


        if (!config.isConnected(RegisterActivity.this)) {
            snackbar.show();
            tv_snack.setText(R.string.please_try_again);
        }


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_email = et_email.getText().toString().trim();
                str_mobile = et_mobile.getText().toString().trim();
                str_name = et_name.getText().toString().trim();

                if (!(str_name.isEmpty())) {
                    if (!(str_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(str_email).matches())) {
                        if (!(str_mobile.isEmpty() || str_mobile.length() < 10)) {

                            /*Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();*/

                            if (config.isConnected(RegisterActivity.this)) {

                                str_email = et_email.getText().toString().trim();
                                str_mobile = et_mobile.getText().toString().trim();
                                str_name = et_name.getText().toString().trim();

                                new register_customer().execute();

                            } else {
                                snackbar.show();
                                tv_snack.setText(R.string.please_try_again);
                            }


                        } else {
                            //et_mobile.setError("Enter valid phone number");
                            snackbar.show();
                            tv_snack.setText(R.string.ent);
                            et_mobile.requestFocus();
                        }
                    } else {
                        //et_email.setError("Enter a valid email address!");
                        snackbar.show();
                        tv_snack.setText(R.string.entr);
                        et_email.requestFocus();
                    }
                } else {
                   // et_name.setError("Enter a Name!");
                    snackbar.show();
                    tv_snack.setText(R.string.uies);
                    et_name.requestFocus();
                }



               /* *//*Creating for testing screen*//*
                Intent i = new Intent(RegisterActivity.this,DashboardNavigation.class);
                startActivity(i);
                finish();*/

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(RegisterActivity.this, SplashActivity.class);
                startActivity(i);
                finish();*/
                onBackPressed();
                // finish();
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

    public class register_customer extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
            mProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("customer_name", str_name);
                jsonObject.accumulate("customer_mobile", "+91" + str_mobile);
                jsonObject.accumulate("customer_email", str_email);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL + "customersignup", json);

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


                        String sus_txt = getString(R.string.s);

                        Toast.makeText(getApplicationContext(), sus_txt, Toast.LENGTH_LONG).show();

                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();


                    } else if (status.equals("false")) {

                        if (msg.contains("customer_mobile_UNIQUE")) {

                            et_mobile.requestFocus();
                          //  Toast.makeText(getApplicationContext(), "Mobile Number Already Registered", Toast.LENGTH_LONG).show();

                            snackbar.show();
                            tv_snack.setText(R.string.asd);

                        } else if (msg.contains("customer_email_UNIQUE")) {
                            et_email.requestFocus();
                           // Toast.makeText(getApplicationContext(), "Email Already Registered", Toast.LENGTH_LONG).show();

                            snackbar.show();
                            tv_snack.setText(R.string.aew);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    // Toast.makeText(getApplicationContext(), "Network Errror0", Toast.LENGTH_LONG).show();
                    snackbar.show();
                    tv_snack.setText(R.string.no_internet);
                }
            } else {
                //Toast.makeText(getApplicationContext(), "Network Errror1", Toast.LENGTH_LONG).show();
                snackbar.show();
                tv_snack.setText(R.string.no_internet);
            }

        }

    }


}
