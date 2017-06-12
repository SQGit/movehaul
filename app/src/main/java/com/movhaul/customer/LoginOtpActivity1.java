package com.movhaul.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
 * Modified by Salman on 24-10-2016.
 */

public class LoginOtpActivity1 extends Activity implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {
    //static EditText et_otp1, et_otp2, et_otp3, et_otp4;
    //private static LoginOtpActivity1 inst;
    LinearLayout btn_back;
    String str_otppin, str_for, str_data;
    Button btn_submit;
    TextView tv_resendotp, tv_snack;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Snackbar snackbar, snack_wifi;
    Config config;
    Typeface tf;
    ProgressDialog mProgressDialog;
    private View view;
    private EditText mPinFirstDigitEditText, mPinSecondDigitEditText, mPinThirdDigitEditText, mPinForthDigitEditText, mPinHiddenEditText;
    String aa,bb,cc,dd;

    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.login_otp_screen1);
        setContentView(new MainLayout(this, null));
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);
        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginOtpActivity1.this);
        editor = sharedPreferences.edit();

        mProgressDialog = new ProgressDialog(LoginOtpActivity1.this, com.movhaul.customer.R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setTitle("Loading..");
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);


        Intent getIntent = getIntent();

        str_for = getIntent.getStringExtra("for");
        str_data = getIntent.getStringExtra("data");

        Log.e("tag", "dd" + str_data + str_for);

        btn_back = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_back);

        mPinFirstDigitEditText = (EditText) findViewById(com.movhaul.customer.R.id.pin_first_edittext);
        mPinSecondDigitEditText = (EditText) findViewById(com.movhaul.customer.R.id.pin_second_edittext);
        mPinThirdDigitEditText = (EditText) findViewById(com.movhaul.customer.R.id.pin_third_edittext);
        mPinForthDigitEditText = (EditText) findViewById(com.movhaul.customer.R.id.pin_forth_edittext);
        mPinHiddenEditText = (EditText) findViewById(com.movhaul.customer.R.id.pin_hidden_edittext);

        /*et_otp1 = (EditText) findViewById(R.id.editext_otp1);
        et_otp2 = (EditText) findViewById(R.id.editext_otp2);
        et_otp3 = (EditText) findViewById(R.id.editext_otp3);
        et_otp4 = (EditText) findViewById(R.id.editext_otp4);*/

        btn_submit = (Button) findViewById(com.movhaul.customer.R.id.button_submit);
        tv_resendotp = (TextView) findViewById(com.movhaul.customer.R.id.textview_resendotp);


        snackbar = Snackbar
                .make(findViewById(com.movhaul.customer.R.id.top), "Network Error! Please Try Again Later.", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);

        if (!config.isConnected(LoginOtpActivity1.this)) {
            snackbar.show();
            tv_snack.setText("Please Connect Internet and Try again");
        }


        setPINListeners();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginOtpActivity1.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });




      /*  et_otp4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (et_otp4.getText().toString().length() == 1) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

                return false;
            }
        });*/


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
  /*              if (et_otp1.getText().toString().isEmpty()) {
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
                            } else {*/
                // str_otppin = et_otp1.getText().toString() + et_otp2.getText().toString() + et_otp3.getText().toString() + et_otp4.getText().toString();
                Log.e("tag", "pin1:" + mPinFirstDigitEditText.getText().toString());
                Log.e("tag", "pin2:" + mPinSecondDigitEditText.getText().toString());
                Log.e("tag", "pin3:" + mPinThirdDigitEditText.getText().toString());
                Log.e("tag", "pin4:" + mPinForthDigitEditText.getText().toString());

                Log.e("tag","pin: "+mPinHiddenEditText.getText().toString());


                Log.e("tag","dup: "+aa+bb+cc+dd);


                // Log.e("tag","for"+str_for+str_data);

                // new otp_verify().execute();


                //}
                //  }
                // }
                //   }
            }
        });

    }

    private void setPINListeners() {
        mPinHiddenEditText.addTextChangedListener(this);

        mPinFirstDigitEditText.setOnFocusChangeListener(this);
        mPinSecondDigitEditText.setOnFocusChangeListener(this);
        mPinThirdDigitEditText.setOnFocusChangeListener(this);
        mPinForthDigitEditText.setOnFocusChangeListener(this);

        mPinFirstDigitEditText.setOnKeyListener(this);
        mPinSecondDigitEditText.setOnKeyListener(this);
        mPinThirdDigitEditText.setOnKeyListener(this);
        mPinForthDigitEditText.setOnKeyListener(this);
        mPinHiddenEditText.setOnKeyListener(this);
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
        /*Intent i = new Intent(LoginOtpActivity1.this, LoginActivity.class);
        startActivity(i);*/
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {

        setDefaultPinBackground(mPinFirstDigitEditText);
        setDefaultPinBackground(mPinSecondDigitEditText);
        setDefaultPinBackground(mPinThirdDigitEditText);
        setDefaultPinBackground(mPinForthDigitEditText);

        Log.e("tag","ds:"+s);
        aa = mPinFirstDigitEditText.getText().toString();
        bb = mPinSecondDigitEditText.getText().toString();
        cc = mPinThirdDigitEditText.getText().toString();
        dd = mPinForthDigitEditText.getText().toString();

        if (s.length() == 0) {
            setFocusedPinBackground(mPinFirstDigitEditText);
            mPinFirstDigitEditText.setText("");
        } else if (s.length() == 1) {
            setFocusedPinBackground(mPinSecondDigitEditText);
            mPinFirstDigitEditText.setText(s.charAt(0) + "");
            mPinSecondDigitEditText.setText("");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 2) {
            setFocusedPinBackground(mPinThirdDigitEditText);
            mPinSecondDigitEditText.setText(s.charAt(1) + "");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 3) {
            setFocusedPinBackground(mPinForthDigitEditText);
            mPinThirdDigitEditText.setText(s.charAt(2) + "");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 4) {
            setDefaultPinBackground(mPinForthDigitEditText);
            mPinForthDigitEditText.setText(s.charAt(3) + "");
            hideSoftKeyboard(mPinForthDigitEditText);
        }

    }
    @Override
    public void afterTextChanged(Editable editable) {

        Log.e("tag","ed:"+editable.toString());


    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        final int id = v.getId();
        switch (id) {
            case com.movhaul.customer.R.id.pin_first_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case com.movhaul.customer.R.id.pin_second_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case com.movhaul.customer.R.id.pin_third_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case com.movhaul.customer.R.id.pin_forth_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            default:
                break;
        }


    }
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = v.getId();
            switch (id) {
                case com.movhaul.customer.R.id.pin_hidden_edittext:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (mPinHiddenEditText.getText().length() == 4)
                        {
                            mPinForthDigitEditText.setText("");
                            Log.e("tag","3key3:"+mPinThirdDigitEditText.getText().toString());
                            Log.e("tag","3key2:"+mPinSecondDigitEditText.getText().toString());
                            Log.e("tag","3key1:"+mPinFirstDigitEditText.getText().toString());
                        }

                        else if (mPinHiddenEditText.getText().length() == 3)
                        {
                            mPinThirdDigitEditText.setText("");
                            Log.e("tag","2key2:"+mPinSecondDigitEditText.getText().toString());
                            Log.e("tag","2key1:"+mPinFirstDigitEditText.getText().toString());
                        }
                        else if (mPinHiddenEditText.getText().length() == 2)
                        {
                            mPinSecondDigitEditText.setText("");
                            Log.e("tag","1key1:"+mPinFirstDigitEditText.getText().toString());
                        }
                        else if (mPinHiddenEditText.getText().length() == 1)
                            mPinFirstDigitEditText.setText("");

                        if (mPinHiddenEditText.length() > 0)
                            mPinHiddenEditText.setText(mPinHiddenEditText.getText().subSequence(0, mPinHiddenEditText.length() - 1));

                        return true;
                    }

                    break;

                default:
                    return false;
            }
        }


        return false;
    }

    public void hideSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    @SuppressWarnings("deprecation")
    public void setViewBackground(View view, Drawable background) {
        if (view == null || background == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    private void setFocusedPinBackground(EditText editText) {
        setViewBackground(editText, getResources().getDrawable(com.movhaul.customer.R.drawable.btns));
    }

    private void setDefaultPinBackground(EditText editText) {
        setViewBackground(editText, getResources().getDrawable(com.movhaul.customer.R.drawable.btns1));
    }

    public class MainLayout extends LinearLayout {

        public MainLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(com.movhaul.customer.R.layout.login_otp_screen1, this);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            final int proposedHeight = MeasureSpec.getSize(heightMeasureSpec);
            final int actualHeight = getHeight();

            Log.d("TAG", "proposed: " + proposedHeight + ", actual: " + actualHeight);

            if (actualHeight >= proposedHeight) {
                // Keyboard is shown
                if (mPinHiddenEditText.length() == 0)
                    setFocusedPinBackground(mPinFirstDigitEditText);
                else
                    setDefaultPinBackground(mPinFirstDigitEditText);
            }

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public class otp_verify extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "", url;

            try {
                JSONObject jsonObject = new JSONObject();


                if (str_for.equals("phone")) {

                    jsonObject.accumulate("customer_mobile", "+91" + str_data);
                    jsonObject.accumulate("customer_otp", str_otppin);
                    url = "customer/mobilelogin";
                } else {

                    jsonObject.accumulate("customer_email", str_data);
                    jsonObject.accumulate("customer_otp", str_otppin);
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

                        editor.putString("id", id);
                        editor.putString("token", token);
                        editor.putString("customer_mobile", mobile);
                        editor.putString("customer_email", email);
                        editor.putString("customer_name", name);
                        editor.putString("customer_id", fake_id);
                        if (!(image.equals(null) || image == null || image == "null")) {
                            editor.putString("customer_image", image);
                        }
                        editor.putString("login", "success");
                        editor.commit();

                        Intent i = new Intent(LoginOtpActivity1.this, DashboardNavigation.class);
                        startActivity(i);
                        finish();


                    } else if (status.equals("false")) {

                        if (msg.contains("Authentication failed.Wrong Password")) {
                            snackbar.show();
                            tv_snack.setText("Authentication Failed, Enter Corrent OTP");
                        } else {
                            snackbar.show();
                            tv_snack.setText("Please Connect Internet and Try again");
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
            Log.e("tag", "reg_preexe");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "", url;

            try {
                JSONObject jsonObject = new JSONObject();


                if (str_for.equals("phone")) {

                    jsonObject.accumulate("customer_mobile", "+91" + str_data);
                    jsonObject.accumulate("customer_otp", str_otppin);
                    url = "customermobileotp";
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
            Log.e("tag", "tag" + s);
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
                        tv_snack.setText("Otp Send to " + str_data);
                        snackbar.show();


                    } else if (status.equals("false")) {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
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
