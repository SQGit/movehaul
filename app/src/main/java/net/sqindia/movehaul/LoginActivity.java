package net.sqindia.movehaul;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

/**
 * Created by sqindia on 21-10-2016.
 */

public class LoginActivity extends Activity {
    Button btn_submit;
    TextView tv_forgot_mobile;
    LinearLayout btn_back;
    String str_mobile_no;
    EditText et_mobile_no;
    TextInputLayout flt_mobile_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FontsManager.initFormAssets(this, "fonts/CATAMARAN-REGULAR.TTF");       //initialization
        FontsManager.changeFonts(this);


        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        tv_forgot_mobile = (TextView) findViewById(R.id.text_forgot_no);
        et_mobile_no = (EditText) findViewById(R.id.editTextMobileNo);
        flt_mobile_no = (TextInputLayout) findViewById(R.id.float_mobile);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/CATAMARAN-REGULAR.TTF");
        flt_mobile_no.setTypeface(type);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, Splash_screen.class);
                startActivity(i);
                finish();
            }
        });
        tv_forgot_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, Forgot_Mobile.class);
                startActivity(i);
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_mobile_no = et_mobile_no.getText().toString().trim();

               // if (!(str_mobile_no.isEmpty() || str_mobile_no.length() < 9)) {
                    Intent i = new Intent(LoginActivity.this, LoginOtpActivity.class);
                 //   i.putExtra("phone",str_mobile_no);
                    startActivity(i);
                    finish();
             //   } else {
              //      et_mobile_no.setError("Enter valid phone number");
             //       et_mobile_no.requestFocus();
             //   }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(LoginActivity.this,Splash_screen.class);
        startActivity(i);
        finish();
    }
}
