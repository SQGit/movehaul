package net.sqindia.movehaul;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;
import com.sloop.fonts.FontsManager;

/**
 * Created by SQINDIA on 11/1/2016.
 */

public class EmergencyContacts extends Activity {

    Button btn_submit;
    TextView tv_hint;
    LinearLayout lt_edit,lt_show;
    TextInputLayout til_name,til_phone,til_relation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_contacts);

        FontsManager.initFormAssets(EmergencyContacts.this, "fonts/CATAMARAN-REGULAR.TTF");       //initialization
        FontsManager.changeFonts(EmergencyContacts.this);


        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/CATAMARAN-REGULAR.TTF");



        btn_submit = (Button) findViewById(R.id.button_submit);
        tv_hint = (TextView) findViewById(R.id.textview_hint);
        lt_edit = (LinearLayout) findViewById(R.id.linear_edit);
        lt_show = (LinearLayout) findViewById(R.id.linear_show);

        til_name = (TextInputLayout) findViewById(R.id.til_name);
        til_phone = (TextInputLayout) findViewById(R.id.til_phone);
        til_relation = (TextInputLayout) findViewById(R.id.til_relation);

        til_name.setTypeface(tf);
        til_phone.setTypeface(tf);
        til_relation.setTypeface(tf);

        tv_hint.setVisibility(View.VISIBLE);
        lt_edit.setVisibility(View.GONE);
        lt_show.setVisibility(View.GONE);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tv_hint.getVisibility() == View.VISIBLE){
                    Log.e("tag","0");
                    tv_hint.setVisibility(View.GONE);
                    lt_edit.setVisibility(View.VISIBLE);
                    lt_show.setVisibility(View.GONE);
                }
                else if(lt_edit.getVisibility() == View.VISIBLE){
                    Log.e("tag","1");
                    tv_hint.setVisibility(View.GONE);
                    lt_edit.setVisibility(View.GONE);
                    lt_show.setVisibility(View.VISIBLE);
                }
                else if(lt_show.getVisibility() == View.VISIBLE){
                    Log.e("tag","2");
                    tv_hint.setVisibility(View.GONE);
                    lt_edit.setVisibility(View.VISIBLE);
                    lt_show.setVisibility(View.GONE);
                }

            }
        });




    }
}
