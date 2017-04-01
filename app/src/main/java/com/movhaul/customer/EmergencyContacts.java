package com.movhaul.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;
import com.sloop.fonts.FontsManager;

import org.json.JSONObject;

/**
 * Created by SQINDIA on 11/1/2016.
 */

public class EmergencyContacts extends Activity {

    //Button btn_submit;
    TextView tv_hint;
    TextView tv_emergency_name1, tv_emergency_name2, tv_emergency_no1, tv_emergency_no2, tv_emergency_relation1, tv_emergency_relation2;
    //LinearLayout lt_edit,lt_show;
    com.rey.material.widget.LinearLayout btn_back;


    String emergencyName1, emergencyName2, emergencyNumber1, emergencyNumber2, emergencyRelation1, emergencyRelation2, service_token, service_id;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Button btn_submit;
    LinearLayout lt_edit1, lt_success1, lt_edit2, lt_success2;
    CardView card2;
    TextInputLayout tl_name, tl_phone, tl_relation;
    android.widget.EditText et_name,et_number,et_relation;
    Snackbar snackbar;
    android.widget.TextView tv_snack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.movhaul.customer.R.layout.emergency);

        FontsManager.initFormAssets(EmergencyContacts.this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(EmergencyContacts.this);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(EmergencyContacts.this);
        editor = sharedPreferences.edit();

        snackbar = Snackbar
                .make(findViewById(com.movhaul.customer.R.id.top), "Enter Name", Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);



        btn_submit = (Button) findViewById(R.id.button_submit);
        lt_edit1 = (LinearLayout) findViewById(R.id.linear_edit_1);
        lt_success1 = (LinearLayout) findViewById(R.id.linear_success_1);
        lt_edit2 = (LinearLayout) findViewById(R.id.linear_edit_2);
        lt_success2 = (LinearLayout) findViewById(R.id.linear_success_2);
        card2 = (CardView) findViewById(R.id.card_view2);

        et_name = (android.widget.EditText) findViewById(R.id.edittext_name);
        et_number = (android.widget.EditText) findViewById(R.id.edittext_phone);
        et_relation = (android.widget.EditText) findViewById(R.id.edittext_relation);

        tl_name = (TextInputLayout) findViewById(com.movhaul.customer.R.id.til_name);
        tl_phone = (TextInputLayout) findViewById(com.movhaul.customer.R.id.til_phone);
        tl_relation = (TextInputLayout) findViewById(com.movhaul.customer.R.id.til_relation);

        tv_emergency_name1 = (TextView) findViewById(com.movhaul.customer.R.id.textview_emergency_name1);
        tv_emergency_name2 = (TextView) findViewById(com.movhaul.customer.R.id.textview_emergency_name2);
        tv_emergency_no1 = (TextView) findViewById(com.movhaul.customer.R.id.textview_emergency_number1);
        tv_emergency_no2 = (TextView) findViewById(com.movhaul.customer.R.id.textview_emergency_number2);
        tv_emergency_relation1 = (TextView) findViewById(com.movhaul.customer.R.id.textview_emergency_relation1);
        tv_emergency_relation2 = (TextView) findViewById(com.movhaul.customer.R.id.textview_emergency_relation2);

        tl_name.setTypeface(tf);
        tl_phone.setTypeface(tf);
        tl_relation.setTypeface(tf);



        editor.putString("emergency1","");
        editor.putString("emergency2","");
        editor.commit();
        btn_submit.setVisibility(View.VISIBLE);

        if (sharedPreferences.getString("emergency1", "").equals("success")) {


            lt_edit1.setVisibility(View.GONE);
            lt_success1.setVisibility(View.VISIBLE);
            btn_submit.setText("Add Contact");


            if (!sharedPreferences.getString("emergency2", "").equals("success")) {
                lt_edit1.setVisibility(View.VISIBLE);
                lt_success1.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
            }


        } else {

            lt_edit1.setVisibility(View.VISIBLE);
            lt_success1.setVisibility(View.GONE);
            card2.setVisibility(View.GONE);
            btn_submit.setText("Submit");

        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sharedPreferences.getString("emergency1", "").equals("success")) {

                    if(btn_submit.getText().equals("Add Contact")){

                        lt_edit1.setVisibility(View.VISIBLE);
                        lt_success1.setVisibility(View.GONE);
                        btn_submit.setText("Submit");
                        et_name.setText("");
                        et_number.setText("");
                        et_relation.setText("");
                        et_name.requestFocus();

                    }
                    else if(btn_submit.getText().equals("Submit")){





                        if(!et_name.getText().toString().trim().isEmpty()){
                            if(!et_number.getText().toString().trim().isEmpty()){
                                if(!et_relation.getText().toString().trim().isEmpty()){

                                    String name = et_name.getText().toString().trim();
                                    String number = et_number.getText().toString().trim();
                                    String relation = et_relation.getText().toString().trim();






                                    card2.setVisibility(View.VISIBLE);
                                    lt_edit2.setVisibility(View.GONE);
                                    lt_success2.setVisibility(View.VISIBLE);
                                    btn_submit.setVisibility(View.GONE);

                                    lt_edit1.setVisibility(View.GONE);
                                    lt_success1.setVisibility(View.VISIBLE);



                                    editor.putString("emergency2","success");
                                    editor.putString("emg2_name",name);
                                    editor.putString("emg2_number",number);
                                    editor.putString("emg2_relation",relation);
                                    editor.apply();

                                    tv_emergency_name2.setText(name);
                                    tv_emergency_no2.setText(number);
                                    tv_emergency_relation2.setText(relation);

                                }
                                else{
                                    //relation
                                    snackbar.show();
                                    et_relation.requestFocus();
                                    tv_snack.setText("Enter Relation");
                                }

                            }
                            else{
                                //phone
                                snackbar.show();
                                et_number.requestFocus();
                                tv_snack.setText("Enter Valid Phone Number");
                            }

                        }
                        else{
                            //name
                            snackbar.show();
                            et_name.requestFocus();
                            tv_snack.setText("Enter Name");
                        }







                    }



                } else {


                    if(!et_name.getText().toString().trim().isEmpty()){
                        if(!et_number.getText().toString().trim().isEmpty()){
                            if(!et_relation.getText().toString().trim().isEmpty()){

                                String name = et_name.getText().toString().trim();
                                String number = et_number.getText().toString().trim();
                                String relation = et_relation.getText().toString().trim();



                                lt_edit1.setVisibility(View.GONE);
                                lt_success1.setVisibility(View.VISIBLE);
                                btn_submit.setText("Add Contact");
                                editor.putString("emergency1","success");
                                editor.putString("emg1_name",name);
                                editor.putString("emg1_number",number);
                                editor.putString("emg1_relation",relation);
                                editor.apply();

                                tv_emergency_name1.setText(name);
                                tv_emergency_no1.setText(number);
                                tv_emergency_relation1.setText(relation);

                            }
                            else{
                                //relation
                                snackbar.show();
                                et_relation.requestFocus();
                                tv_snack.setText("Enter Relation");
                            }

                        }
                        else{
                            //phone
                            snackbar.show();
                            et_number.requestFocus();
                            tv_snack.setText("Enter Valid Phone Number");
                        }

                    }
                    else{
                        //name
                        snackbar.show();
                        et_name.requestFocus();
                        tv_snack.setText("Enter Name");
                    }



                }

            }
        });









   /*     btn_submit = (Button) findViewById(com.movhaul.customer.R.id.button_submit);
        tv_hint = (TextView) findViewById(com.movhaul.customer.R.id.textview_hint);
        tv_emergency_name1 = (TextView) findViewById(com.movhaul.customer.R.id.textview_emergency_name1);
        tv_emergency_name2 = (TextView) findViewById(com.movhaul.customer.R.id.textview_emergency_name2);
        tv_emergency_no1 = (TextView) findViewById(com.movhaul.customer.R.id.textview_emergency_number1);
        tv_emergency_no2 = (TextView) findViewById(com.movhaul.customer.R.id.textview_emergency_number2);
        tv_emergency_relation1 = (TextView) findViewById(com.movhaul.customer.R.id.textview_emergency_relation1);
        tv_emergency_relation2 = (TextView) findViewById(com.movhaul.customer.R.id.textview_emergency_relation2);




        lt_edit = (LinearLayout) findViewById(com.movhaul.customer.R.id.linear_edit);
        lt_show = (LinearLayout) findViewById(com.movhaul.customer.R.id.linear_show);

        btn_back = (com.rey.material.widget.LinearLayout) findViewById(com.movhaul.customer.R.id.layout_back);

        til_name = (TextInputLayout) findViewById(com.movhaul.customer.R.id.til_name);
        til_phone = (TextInputLayout) findViewById(com.movhaul.customer.R.id.til_phone);
        til_relation = (TextInputLayout) findViewById(com.movhaul.customer.R.id.til_relation);

        til_name.setTypeface(tf);
        til_phone.setTypeface(tf);
        til_relation.setTypeface(tf);

        tv_hint.setVisibility(View.VISIBLE);
        lt_edit.setVisibility(View.GONE);
        lt_show.setVisibility(View.GONE);


        service_token = sharedPreferences.getString("token","");
        service_id = sharedPreferences.getString("id","");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EmergencyContacts.this, DashboardNavigation.class);
                startActivity(i);
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emergencyName1=tv_emergency_name1.getText().toString();
                emergencyName2=tv_emergency_name2.getText().toString();
                emergencyNumber1=tv_emergency_no1.getText().toString();
                emergencyNumber2=tv_emergency_no2.getText().toString();
                emergencyRelation1=tv_emergency_relation1.getText().toString();
                emergencyRelation2=tv_emergency_relation2.getText().toString();

                if(tv_hint.getVisibility() == View.VISIBLE){
                    Log.e("tag","0"+emergencyRelation1+emergencyName1+emergencyNumber1);

                    tv_hint.setVisibility(View.GONE);
                    lt_edit.setVisibility(View.VISIBLE);
                    lt_show.setVisibility(View.GONE);

                }
                else if(lt_edit.getVisibility() == View.VISIBLE){
                    Log.e("tag","1");
                    tv_hint.setVisibility(View.GONE);
                    lt_edit.setVisibility(View.GONE);
                    lt_show.setVisibility(View.VISIBLE);
                    new emergency_contacts().execute();
                }
                else if(lt_show.getVisibility() == View.VISIBLE){
                    Log.e("tag","2");
                    tv_hint.setVisibility(View.GONE);
                    lt_edit.setVisibility(View.VISIBLE);
                    lt_show.setVisibility(View.GONE);
                }

            }
        });

        */


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(EmergencyContacts.this, DashboardNavigation.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }


    public class emergency_contacts extends AsyncTask<String, Void, String> {


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

                //{"emergency_name":"test1","emergency_mobile":"+91668736826",
                // "emergency_relation":"brother"}
                jsonObject.accumulate("emergency_name", emergencyName1);
                jsonObject.accumulate("emergency_mobile", emergencyNumber1);
                jsonObject.accumulate("emergency_relation", emergencyRelation1);

                json = jsonObject.toString();
                Log.e("tag", "<----service_token--->" + service_token);
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + "customer/insertemergency", json, service_id, service_token);

            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);


        }

    }

}
