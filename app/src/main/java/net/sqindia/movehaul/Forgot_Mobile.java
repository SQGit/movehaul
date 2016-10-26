package net.sqindia.movehaul;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

/**
 * Created by sqindia on 22-10-2016.
 */

public class Forgot_Mobile extends Activity {
    LinearLayout btn_back;
    EditText edtxt_email;
    String str_email;
    TextInputLayout flt_email;
    Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_phone);
        FontsManager.initFormAssets(this, "fonts/CATAMARAN-REGULAR.TTF");       //initialization
        FontsManager.changeFonts(this);

        flt_email = (TextInputLayout) findViewById(R.id.float_email);
        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        edtxt_email = (EditText) findViewById(R.id.editTextEmail);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/CATAMARAN-REGULAR.TTF");
        flt_email.setTypeface(type);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_email = edtxt_email.getText().toString().trim();

                if (!(str_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(str_email).matches())) {
                    Intent i = new Intent(Forgot_Mobile.this, LoginOtpActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    edtxt_email.setError("Enter a valid email address!");
                    edtxt_email.requestFocus();
                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Forgot_Mobile.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
