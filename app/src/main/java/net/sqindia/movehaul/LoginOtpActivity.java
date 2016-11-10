package net.sqindia.movehaul;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    String str_otppin, str_for, str_data;
    Button btn_submit;
    TextView tv_resendotp;
    private View view;

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
        setContentView(R.layout.login_otp_screen);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);

        Intent getIntent = getIntent();

        str_for = getIntent.getStringExtra("for");
        str_data = getIntent.getStringExtra("data");

        Log.e("tag","dd"+str_data+ str_for);

        btn_back = (LinearLayout) findViewById(R.id.layout_back);

        et_otp1 = (EditText) findViewById(R.id.editext_otp1);
        et_otp2 = (EditText) findViewById(R.id.editext_otp2);
        et_otp3 = (EditText) findViewById(R.id.editext_otp3);
        et_otp4 = (EditText) findViewById(R.id.editext_otp4);

        btn_submit = (Button) findViewById(R.id.button_submit);
        tv_resendotp = (TextView) findViewById(R.id.textview_resendotp);


    /*    ReceiveSmsBroadcastReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.e("Text",messageText);

                char[] cArray = messageText.toCharArray();

                et_otp1.setText(String.valueOf(cArray[0]));
                et_otp2.setText(String.valueOf(cArray[1]));
                et_otp3.setText(String.valueOf(cArray[2]));
                et_otp4.setText(String.valueOf(cArray[3]));

                Toast.makeText(LoginOtpActivity.this,"Message: "+messageText,Toast.LENGTH_LONG).show();
            }
        });*/


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

                if (et_otp4.getText().toString().length() == 1) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

                return false;
            }
        });


        tv_resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_resendotp.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Otp Send to " + str_for, Toast.LENGTH_LONG).show();
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

                                new otp_verify().execute();


                            }
                        }
                    }
                }
            }
        });


    }

    public void updateList(final String smsMessage) {

        Log.e("tag", "ss" + smsMessage);

        /*char[] cArray = smsMessage.toCharArray();

        et_otp1.setText(String.valueOf(cArray[0]));
        et_otp2.setText(String.valueOf(cArray[1]));
        et_otp3.setText(String.valueOf(cArray[2]));
        et_otp4.setText(String.valueOf(cArray[3]));

        Toast.makeText(LoginOtpActivity.this,"Message: "+smsMessage,Toast.LENGTH_LONG).show();*/
    }

    public void receiveSms(String message) {
        Log.e("tag", "msgd4" + message);

        Toast.makeText(getApplicationContext(), "toast::" + message, Toast.LENGTH_LONG).show();

        try {


            char[] cArray = message.toCharArray();

            et_otp1.setText(String.valueOf(cArray[0]));
            et_otp2.setText("4");
            et_otp3.setText(cArray[2]);
            et_otp4.setText(cArray[3]);


        } catch (Exception e) {
            Log.e("tag", "asd: " + e.toString());
        }


       /* ComponentName receiver = new ComponentName(this, ReceiveSmsBroadcastReceiver.class);

        PackageManager pm = this.getPackageManager();
        pm.setComponentEnabledSetting(receiver,

                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,

                PackageManager.DONT_KILL_APP);

        Toast.makeText(this, "Disabledbroadcseceiver", Toast.LENGTH_SHORT).show();*/


    }

    public void recivedSms(String message) {
        try {
            Log.e("tag", "asd: " + message);

            char[] cArray = message.toCharArray();

            et_otp1.setText(String.valueOf(cArray[cArray.length - 4]));
            et_otp2.setText(String.valueOf(cArray[cArray.length - 3]));
            et_otp3.setText(String.valueOf(cArray[cArray.length - 2]));
            et_otp4.setText(String.valueOf(cArray[cArray.length - 1]));


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
            case R.id.editext_otp1:

                if (editable.length() == 0) {
                    et_otp1.requestFocus();
                } else if (editable.length() == 1) {
                    et_otp2.requestFocus();
                }

                break;
            case R.id.editext_otp2:

                if (editable.length() == 0) {
                    et_otp1.requestFocus();
                } else if (editable.length() == 1) {
                    et_otp3.requestFocus();
                }

                break;
            case R.id.editext_otp3:

                if (editable.length() == 0) {
                    et_otp2.requestFocus();
                } else if (editable.length() == 1) {
                    et_otp4.requestFocus();
                }
                break;
            case R.id.editext_otp4:

                if (editable.length() == 0) {
                    et_otp3.requestFocus();
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
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();


                if (str_for.equals("mobile")) {

                    jsonObject.accumulate("customer_mobile", str_data);
                    jsonObject.accumulate("customer_otp", str_otppin);
                } else {

                    jsonObject.accumulate("customer_email", str_data);
                    jsonObject.accumulate("customer_otp", str_otppin);
                }


                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL + "customer/mobilelogin", json);

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

                        Intent i = new Intent(LoginOtpActivity.this, DashboardNavigation.class);
                        startActivity(i);
                        finish();


                    } else if (status.equals("false")) {


                        Toast.makeText(getApplicationContext(), "Otp Failed", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    Toast.makeText(getApplicationContext(), "Network Errror0", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Network Errror1", Toast.LENGTH_LONG).show();
            }

        }

    }


}
