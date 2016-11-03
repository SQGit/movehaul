package net.sqindia.movehaul;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;

import com.sloop.fonts.FontsManager;

import static java.security.AccessController.getContext;

/**
 * Modified by Salman on 24-10-2016.
 */

public class LoginOtpActivity extends Activity implements TextWatcher {
    LinearLayout btn_back;
    static EditText et_otp1, et_otp2, et_otp3, et_otp4;
    private View view;
    String str_otppin,str_phone;
    Button btn_submit;
    TextView tv_resendotp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_otp_screen);
        FontsManager.initFormAssets(this, "fonts/CATAMARAN-REGULAR.TTF");       //initialization
        FontsManager.changeFonts(this);

        Intent getIntent = getIntent();

        str_phone = getIntent.getStringExtra("phone");



        btn_back = (LinearLayout) findViewById(R.id.layout_back);

        et_otp1 = (EditText) findViewById(R.id.editext_otp1);
        et_otp2 = (EditText) findViewById(R.id.editext_otp2);
        et_otp3 = (EditText) findViewById(R.id.editext_otp3);
        et_otp4 = (EditText) findViewById(R.id.editext_otp4);

        btn_submit = (Button) findViewById(R.id.button_submit);
        tv_resendotp = (TextView) findViewById(R.id.textview_resendotp);


        ReceiveSmsBroadcastReceiver.bindListener(new SmsListener() {
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
        });



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
                Toast.makeText(getApplicationContext(),"Otp Send to "+str_phone,Toast.LENGTH_LONG).show();
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LoginOtpActivity.this,DashboardNavigation.class);
                startActivity(i);
                finish();


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
                                str_otppin = et_otp1.getText().toString()+et_otp2.getText().toString()+et_otp3.getText().toString()+et_otp4.getText().toString();
                                Log.e("tag","pin:"+str_otppin);
                            }
                        }
                    }
                }
            }
        });


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
            Log.e("tag","asd: "+e.toString());
        }


       /* ComponentName receiver = new ComponentName(this, ReceiveSmsBroadcastReceiver.class);

        PackageManager pm = this.getPackageManager();
        pm.setComponentEnabledSetting(receiver,

                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,

                PackageManager.DONT_KILL_APP);

        Toast.makeText(this, "Disabledbroadcseceiver", Toast.LENGTH_SHORT).show();*/


    }


    private LoginOtpActivity(View view) {
        this.view = view;
    }


    public LoginOtpActivity() {

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
        Intent i = new Intent(LoginOtpActivity.this,LoginActivity.class);
        startActivity(i);
        finish();
    }
}
