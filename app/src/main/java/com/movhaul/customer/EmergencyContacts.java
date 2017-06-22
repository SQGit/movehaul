package com.movhaul.customer;

import android.app.Activity;
import android.content.Context;
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
import com.rey.material.widget.ImageView;
import com.rey.material.widget.TextView;
import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SQINDIA on 11/1/2016.
 */

public class EmergencyContacts extends Activity {

    TextView tv_emergency_name1, tv_emergency_name2, tv_emergency_no1, tv_emergency_no2, tv_emergency_relation1, tv_emergency_relation2;
    String id, token;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button btn_submit;
    LinearLayout lt_edit, lt_contact_1;
    CardView card_2;
    TextInputLayout tl_name, tl_number, tl_relation;
    android.widget.EditText et_name, et_number, et_relation;
    Snackbar snackbar;
    android.widget.TextView tv_snack;
    ImageView iv_edit1, iv_edit2;
    boolean bl_edit_1, bl_edit_2;
    com.rey.material.widget.LinearLayout btn_back;


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
        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");
        btn_submit = (Button) findViewById(R.id.button_submit);
        lt_edit = (LinearLayout) findViewById(R.id.linear_edit_1);
        lt_contact_1 = (LinearLayout) findViewById(R.id.linear_success_1);
        card_2 = (CardView) findViewById(R.id.card_view2);
        et_name = (android.widget.EditText) findViewById(R.id.edittext_name);
        et_number = (android.widget.EditText) findViewById(R.id.edittext_phone);
        et_relation = (android.widget.EditText) findViewById(R.id.edittext_relation);
        tl_name = (TextInputLayout) findViewById(com.movhaul.customer.R.id.til_name);
        tl_number = (TextInputLayout) findViewById(com.movhaul.customer.R.id.til_phone);
        tl_relation = (TextInputLayout) findViewById(com.movhaul.customer.R.id.til_relation);
        tv_emergency_name1 = (TextView) findViewById(com.movhaul.customer.R.id.textview_emergency_name1);
        tv_emergency_name2 = (TextView) findViewById(com.movhaul.customer.R.id.textview_emergency_name2);
        tv_emergency_no1 = (TextView) findViewById(com.movhaul.customer.R.id.textview_emergency_number1);
        tv_emergency_no2 = (TextView) findViewById(com.movhaul.customer.R.id.textview_emergency_number2);
        tv_emergency_relation1 = (TextView) findViewById(com.movhaul.customer.R.id.textview_emergency_relation1);
        tv_emergency_relation2 = (TextView) findViewById(com.movhaul.customer.R.id.textview_emergency_relation2);
        btn_back = (com.rey.material.widget.LinearLayout) findViewById(com.movhaul.customer.R.id.layout_back);
        iv_edit1 = (ImageView) findViewById(R.id.button_editContact1);
        iv_edit2 = (ImageView) findViewById(R.id.button_editContact2);

       // tv_emergency_relation1.setInp
        tl_name.setTypeface(tf);
        tl_number.setTypeface(tf);
        tl_relation.setTypeface(tf);
        bl_edit_1 = false;
        bl_edit_2 = false;

       // new get_emg_contact().execute();
        /*///////////////////////////////////////////
        editor.putString("emergency_1", "");     ////
        editor.putString("emergency_2", "");     ////
        editor.apply();                          ////
        btn_submit.setVisibility(View.VISIBLE);  ////
        ////////*////////////////////////////////////
        if (sharedPreferences.getString("emergency_1", "").equals("")) {
            if (!com.movhaul.customer.Config.isConnected(EmergencyContacts.this)) {
                snackbar.show();
                tv_snack.setText(R.string.ase);
            } else {
                new get_emg_contact().execute();
            }
        } else {
            check_from_db();
        }


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bl_edit_1) {
                    Log.e("tag", "edit_emergency_1");
                    if (!et_name.getText().toString().trim().isEmpty()) {
                        if (!et_number.getText().toString().trim().isEmpty()) {
                            if (!et_relation.getText().toString().trim().isEmpty()) {
                                String name = et_name.getText().toString().trim();
                                String number = et_number.getText().toString().trim();
                                String relation = et_relation.getText().toString().trim();
                                lt_edit.setVisibility(View.GONE);
                                lt_contact_1.setVisibility(View.VISIBLE);
                                if (sharedPreferences.getString("emergency_2", "").equals("success")) {
                                    card_2.setVisibility(View.VISIBLE);
                                    btn_submit.setVisibility(View.GONE);
                                } else {
                                    btn_submit.setVisibility(View.VISIBLE);
                                    btn_submit.setText("Add Contact");
                                }
                                editor.putString("emergency_1", "success");
                                editor.putString("emg1_name", name);
                                editor.putString("emg1_number", number);
                                editor.putString("emg1_relation", relation);
                                editor.apply();
                                tv_emergency_name1.setText(name.toUpperCase());
                                tv_emergency_no1.setText(number);
                                tv_emergency_relation1.setText(relation);
                                bl_edit_1 = false;
                                new insert_emg_contact(name, number, relation, 1).execute();
                            } else {
                                snackbar.show();
                                et_relation.requestFocus();
                                tv_snack.setText(R.string.asdz);
                            }
                        } else {
                            snackbar.show();
                            et_number.requestFocus();
                            tv_snack.setText(R.string.zcwe);
                        }
                    } else {
                        snackbar.show();
                        et_name.requestFocus();
                        tv_snack.setText(R.string.azce);
                    }
                } else if (bl_edit_2) {
                    Log.e("tag", "edit_emergency_2");
                    if (!et_name.getText().toString().trim().isEmpty()) {
                        if (!et_number.getText().toString().trim().isEmpty()) {
                            if (!et_relation.getText().toString().trim().isEmpty()) {
                                String name = et_name.getText().toString().trim();
                                String number = et_number.getText().toString().trim();
                                String relation = et_relation.getText().toString().trim();
                                lt_edit.setVisibility(View.GONE);
                                lt_contact_1.setVisibility(View.VISIBLE);
                                btn_submit.setVisibility(View.GONE);
                                card_2.setVisibility(View.VISIBLE);
                                editor.putString("emergency_2", "success");
                                editor.putString("emg2_name", name);
                                editor.putString("emg2_number", number);
                                editor.putString("emg2_relation", relation);
                                editor.apply();

                                new insert_emg_contact(name, number, relation, 3).execute();

                                tv_emergency_name2.setText(name.toUpperCase());
                                tv_emergency_no2.setText(number);
                                tv_emergency_relation2.setText(relation);
                                bl_edit_2 = false;
                            } else {
                                snackbar.show();
                                et_relation.requestFocus();
                                tv_snack.setText(R.string.asdz);
                            }
                        } else {
                            snackbar.show();
                            et_number.requestFocus();
                            tv_snack.setText(R.string.zcwe);
                        }
                    } else {
                        snackbar.show();
                        et_name.requestFocus();
                        tv_snack.setText(R.string.azce);
                    }
                } else {
                    if (sharedPreferences.getString("emergency_1", "").equals("success")) {
                        if (btn_submit.getText().equals("Add Contact")) {
                            lt_edit.setVisibility(View.VISIBLE);
                            lt_contact_1.setVisibility(View.GONE);
                            btn_submit.setText("Submit");
                            et_name.setText("");
                            et_number.setText("");
                            et_relation.setText("");
                            et_name.requestFocus();
                        } else if (btn_submit.getText().equals("Submit")) {
                            if (!et_name.getText().toString().trim().isEmpty()) {
                                if (!et_number.getText().toString().trim().isEmpty()) {
                                    if (!et_relation.getText().toString().trim().isEmpty()) {
                                        String name = et_name.getText().toString().trim();
                                        String number = et_number.getText().toString().trim();
                                        String relation = et_relation.getText().toString().trim();
                                        card_2.setVisibility(View.VISIBLE);
                                        btn_submit.setVisibility(View.GONE);
                                        lt_edit.setVisibility(View.GONE);
                                        lt_contact_1.setVisibility(View.VISIBLE);
                                        editor.putString("emergency_2", "success");
                                        editor.putString("emg2_name", name);
                                        editor.putString("emg2_number", number);
                                        editor.putString("emg2_relation", relation);
                                        editor.apply();

                                        new insert_emg_contact(name, number, relation, 2).execute();
                                        tv_emergency_name2.setText(name.toUpperCase());
                                        tv_emergency_no2.setText(number);
                                        tv_emergency_relation2.setText(relation);
                                    } else {
                                        //relation
                                        snackbar.show();
                                        et_relation.requestFocus();
                                        tv_snack.setText(R.string.asdz);
                                    }
                                } else {
                                    //phone
                                    snackbar.show();
                                    et_number.requestFocus();
                                    tv_snack.setText(R.string.zcwe);
                                }
                            } else {
                                //name
                                snackbar.show();
                                et_name.requestFocus();
                                tv_snack.setText(R.string.azce);
                            }
                        }
                    } else {
                        if (!et_name.getText().toString().trim().isEmpty()) {
                            if (!et_number.getText().toString().trim().isEmpty()) {
                                if (!et_relation.getText().toString().trim().isEmpty()) {
                                    String name = et_name.getText().toString().trim();
                                    String number = et_number.getText().toString().trim();
                                    String relation = et_relation.getText().toString().trim();
                                    lt_edit.setVisibility(View.GONE);
                                    lt_contact_1.setVisibility(View.VISIBLE);
                                    btn_submit.setText("Add Contact");
                                    editor.putString("emergency_1", "success");
                                    editor.putString("emg1_name", name);
                                    editor.putString("emg1_number", number);
                                    editor.putString("emg1_relation", relation);
                                    editor.apply();
                                    new insert_emg_contact(name, number, relation, 0).execute();
                                    tv_emergency_name1.setText(name.toUpperCase());
                                    tv_emergency_no1.setText(number);
                                    tv_emergency_relation1.setText(relation);
                                } else {
                                    //relation
                                    snackbar.show();
                                    et_relation.requestFocus();
                                    tv_snack.setText(R.string.asdz);
                                }
                            } else {
                                //phone
                                snackbar.show();
                                et_number.requestFocus();
                                tv_snack.setText(R.string.zcwe);
                            }
                        } else {
                            //name
                            snackbar.show();
                            et_name.requestFocus();
                            tv_snack.setText(R.string.azce);
                        }
                    }
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lt_contact_1.setVisibility(View.GONE);
                lt_edit.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                card_2.setVisibility(View.GONE);
                btn_submit.setText("Submit");
                et_name.setText(sharedPreferences.getString("emg1_name", ""));
                et_number.setText(sharedPreferences.getString("emg1_number", ""));
                et_relation.setText(sharedPreferences.getString("emg1_relation", ""));
                bl_edit_1 = true;
                bl_edit_2 = false;
            }
        });
        iv_edit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lt_contact_1.setVisibility(View.GONE);
                lt_edit.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                card_2.setVisibility(View.GONE);
                et_name.setText(sharedPreferences.getString("emg2_name", ""));
                et_number.setText(sharedPreferences.getString("emg2_number", ""));
                et_relation.setText(sharedPreferences.getString("emg2_relation", ""));
                bl_edit_2 = true;
                bl_edit_1 = false;
            }
        });
    }

    private void check_from_db() {
        if (sharedPreferences.getString("emergency_1", "").equals("success")) {
            lt_edit.setVisibility(View.GONE);
            lt_contact_1.setVisibility(View.VISIBLE);
            tv_emergency_name1.setText(sharedPreferences.getString("emg1_name", "").toUpperCase());
            tv_emergency_no1.setText(sharedPreferences.getString("emg1_number", ""));
            tv_emergency_relation1.setText(sharedPreferences.getString("emg1_relation", ""));
            if (sharedPreferences.getString("emergency_2", "").equals("success")) {
                card_2.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.GONE);
                tv_emergency_name2.setText(sharedPreferences.getString("emg2_name", "").toUpperCase());
                tv_emergency_no2.setText(sharedPreferences.getString("emg2_number", ""));
                tv_emergency_relation2.setText(sharedPreferences.getString("emg2_relation", ""));
            } else {
                btn_submit.setVisibility(View.VISIBLE);
                btn_submit.setText(R.string.addc);
                card_2.setVisibility(View.GONE);
            }
        } else {
            lt_edit.setVisibility(View.VISIBLE);
            lt_contact_1.setVisibility(View.GONE);
            card_2.setVisibility(View.GONE);
            btn_submit.setText("Submit");
        }
    }

    @Override
    public void onBackPressed() {
        if (bl_edit_1) {
            lt_edit.setVisibility(View.GONE);
            lt_contact_1.setVisibility(View.VISIBLE);
            if (sharedPreferences.getString("emergency_2", "").equals("success")) {
                card_2.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.GONE);
            } else {
                btn_submit.setVisibility(View.VISIBLE);
                btn_submit.setText(R.string.addc);
            }
        } else if (bl_edit_2) {
            lt_edit.setVisibility(View.GONE);
            lt_contact_1.setVisibility(View.VISIBLE);
            card_2.setVisibility(View.VISIBLE);
            btn_submit.setVisibility(View.GONE);
        } else {
            finish();
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

    public class insert_emg_contact extends AsyncTask<String, Void, String> {
        String name, number, relation, url;
        int emg_id;

        public insert_emg_contact(String name, String number, String relation, int id) {
            this.name = name;
            this.number = number;
            this.relation = relation;
            this.emg_id = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (emg_id == 0 || emg_id == 2) {
                url = "customer/insertemergency";
            } else if (emg_id == 1 || emg_id == 3) {
                url = "customer/updateemergency";
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("emergency_name", name);
                jsonObject.accumulate("emergency_mobile", number);
                jsonObject.accumulate("emergency_relation", relation);
                if (emg_id == 1)
                    jsonObject.accumulate("emergency_id", sharedPreferences.getString("emg1_id", ""));
                if (emg_id == 3)
                    jsonObject.accumulate("emergency_id", sharedPreferences.getString("emg2_id", ""));

                    json = jsonObject.toString();
                Log.e("tag", "<----service_token--->" + token);
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + url, json, id, token);
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

    public class get_emg_contact extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + "customer/getemergency", json, id, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                if (status.equals("true")) {
                    JSONArray jobs_data = jo.getJSONArray("message");
                    if (jobs_data.length() > 0) {
                        for (int i = 0; i < jobs_data.length(); i++) {
                            JSONObject jos = jobs_data.getJSONObject(i);
                            String name = jos.getString("emergency_name");
                            String number = jos.getString("emergency_mobile");
                            String relation = jos.getString("emergency_relation");
                            String emg_id = jos.getString("emergency_id");

                            if (i == 0) {
                                editor.putString("emergency_1", "success");
                                editor.putString("emg1_name", name);
                                editor.putString("emg1_number", number);
                                editor.putString("emg1_relation", relation);
                                editor.putString("emg1_id", emg_id);
                                editor.apply();
                                check_from_db();
                                Log.e("tag", "working" + i);
                            }
                            if (i == 1) {
                                editor.putString("emergency_2", "success");
                                editor.putString("emg2_name", name);
                                editor.putString("emg2_number", number);
                                editor.putString("emg2_relation", relation);
                                editor.putString("emg2_id", emg_id);
                                editor.apply();
                                check_from_db();
                                Log.e("tag", "working" + i);
                            }


                        }
                    } else {

                        Log.e("tag", "no data found");
                        check_from_db();

                    }
                } else {
                    Log.e("tag", "false");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("tag", "nt" + e.toString());
            }
        }
    }


}
