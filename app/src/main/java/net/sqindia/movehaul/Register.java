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

public class Register extends Activity {
    LinearLayout btn_back;
    Button btn_submit;
    TextView txt_register;
    EditText edtxt_name,edtxt_email,edtxt_mobile;
    String str_email,str_mobile,str_name;
    TextInputLayout flt_name,flt_email,flt_mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);
        FontsManager.initFormAssets(this, "fonts/CATAMARAN-REGULAR.TTF");       //initialization
        FontsManager.changeFonts(this);

        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        txt_register = (TextView) findViewById(R.id.text_register);
        edtxt_name = (EditText) findViewById(R.id.editTextName);
        edtxt_email = (EditText) findViewById(R.id.editTextEmail);
        edtxt_mobile = (EditText) findViewById(R.id.editTextMobile);
        flt_email = (TextInputLayout) findViewById(R.id.float_email);
        flt_mobile = (TextInputLayout) findViewById(R.id.float_mobile);
        flt_name = (TextInputLayout) findViewById(R.id.float_name);


        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/CATAMARAN-REGULAR.TTF");
        flt_email.setTypeface(type);
        flt_mobile.setTypeface(type);
        flt_name.setTypeface(type);



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_email = edtxt_email.getText().toString().trim();
                str_mobile = edtxt_mobile.getText().toString().trim();
                str_name = edtxt_name.getText().toString().trim();

                    if (!(str_name.isEmpty())) {
                        if (!(str_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(str_email).matches())) {
                            if (!(str_mobile.isEmpty() || str_mobile.length() < 9)) {
                                Intent i = new Intent(Register.this,LoginActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                edtxt_mobile.setError("Enter valid phone number");
                                edtxt_mobile.requestFocus();
                            }
                        } else {
                            edtxt_email.setError("Enter a valid email address!");
                            edtxt_email.requestFocus();
                        }
                    } else {
                        edtxt_name.setError("Enter a Name!");
                        edtxt_name.requestFocus();
                    }

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this,Splash_screen.class);
                startActivity(i);
                finish();
            }
        });
    }
}
