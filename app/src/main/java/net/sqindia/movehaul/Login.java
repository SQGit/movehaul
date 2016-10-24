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

public class Login extends Activity {
    Button button_submit;
    TextView txt_forgot_no;
    LinearLayout btn_back;
    String str_mobiles_no;
    String salman;
    EditText edtxt_mobile;
    TextInputLayout flt_mobile_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        FontsManager.initFormAssets(this, "fonts/CATAMARAN-REGULAR.TTF");       //initialization
        FontsManager.changeFonts(this);


        button_submit = (Button) findViewById(R.id.btn_submit);
        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        txt_forgot_no = (TextView) findViewById(R.id.text_forgot_no);
        edtxt_mobile = (EditText) findViewById(R.id.editTextMobileNo);
        flt_mobile_no = (TextInputLayout) findViewById(R.id.float_mobile);

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/CATAMARAN-REGULAR.TTF");
        flt_mobile_no.setTypeface(type);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this,Splash_screen.class);
                startActivity(i);
                finish();
            }
        });
        txt_forgot_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this,Forgot_Mobile.class);
                startActivity(i);
                finish();
            }
        });
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_mobiles_no = edtxt_mobile.getText().toString().trim();

                if (!(str_mobiles_no.isEmpty() || str_mobiles_no.length() < 9)) {
                    Intent i = new Intent(Login.this,Login_Otp.class);
                    startActivity(i);
                    finish();
                } else {
                    edtxt_mobile.setError("Enter valid phone number");
                    edtxt_mobile.requestFocus();
                }

            }
        });
    }
}
