package net.sqindia.movehaul;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rey.material.widget.Button;

import com.rey.material.widget.TextView;
import com.sloop.fonts.FontsManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SQINDIA on 11/1/2016.
 */

public class EmergencyContacts extends Activity {

    Button btn_submit;
    TextView tv_hint;
    TextView tv_emergency_name1,tv_emergency_name2,tv_emergency_no1,tv_emergency_no2,tv_emergency_relation1,tv_emergency_relation2;
    LinearLayout lt_edit,lt_show;
    com.rey.material.widget.LinearLayout btn_back;
    TextInputLayout til_name,til_phone,til_relation;

    String emergencyName1,emergencyName2,emergencyNumber1,emergencyNumber2,emergencyRelation1,emergencyRelation2,service_token,service_id;
    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_contacts);

        FontsManager.initFormAssets(EmergencyContacts.this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(EmergencyContacts.this);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");

        btn_submit = (Button) findViewById(R.id.button_submit);
        tv_hint = (TextView) findViewById(R.id.textview_hint);
        tv_emergency_name1 = (TextView) findViewById(R.id.textview_emergency_name1);
        tv_emergency_name2 = (TextView) findViewById(R.id.textview_emergency_name2);
        tv_emergency_no1 = (TextView) findViewById(R.id.textview_emergency_number1);
        tv_emergency_no2 = (TextView) findViewById(R.id.textview_emergency_number2);
        tv_emergency_relation1 = (TextView) findViewById(R.id.textview_emergency_relation1);
        tv_emergency_relation2 = (TextView) findViewById(R.id.textview_emergency_relation2);




        lt_edit = (LinearLayout) findViewById(R.id.linear_edit);
        lt_show = (LinearLayout) findViewById(R.id.linear_show);

        btn_back = (com.rey.material.widget.LinearLayout) findViewById(R.id.layout_back);

        til_name = (TextInputLayout) findViewById(R.id.til_name);
        til_phone = (TextInputLayout) findViewById(R.id.til_phone);
        til_relation = (TextInputLayout) findViewById(R.id.til_relation);

        til_name.setTypeface(tf);
        til_phone.setTypeface(tf);
        til_relation.setTypeface(tf);

        tv_hint.setVisibility(View.VISIBLE);
        lt_edit.setVisibility(View.GONE);
        lt_show.setVisibility(View.GONE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(EmergencyContacts.this);

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
                    new emergency_contacts().execute();
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(EmergencyContacts.this,DashboardNavigation.class);
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
                jsonObject.accumulate("emergency_mobile",  emergencyNumber1);
                jsonObject.accumulate("emergency_relation", emergencyRelation1);

                json = jsonObject.toString();
                Log.e("tag","<----service_token--->"+service_token);
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + "customer/insertemergency", json,service_id,service_token);

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
