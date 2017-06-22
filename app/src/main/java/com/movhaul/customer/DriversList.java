package com.movhaul.customer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;
import com.rey.material.widget.Button;
import com.rey.material.widget.ListView;
import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SQINDIA on 10/28/2016.
 */

public class DriversList extends AppCompatActivity {
    ListView lv_drv_list;
    ImageView iv_filter;
    LinearLayout lt_filter_dialog;
    Button btn_filter;
    com.rey.material.widget.LinearLayout btn_back, btn_refresh;
    ImageView iv_close, iv_refresh;
    int i = 0;
    Snackbar snackbar;
    Typeface tf;
    TextView tv_snack;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DriversListAdapter drv_adapter;
    ArrayList<MV_Datas> ar_driver_data;
    MV_Datas mv_datas;
    String vec_type, service_url;
    int doid ;
    ProgressDialog mProgressDialog;
    private static final int REQUEST_CODE_PAYMENT = 545;
    String bidding_id, booking_id, driver_id, transaction_id,id,token;
    Dialog dialog1;
    Button  btn_ok;
    ImageView btn_close;
    TextView tv_dialog1, tv_dialog2, tv_dialog3, tv_dialog4;

    public static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int height = display.getHeight();
        return height;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.movhaul.customer.R.layout.drivers_list);
        FontsManager.initFormAssets(DriversList.this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(DriversList.this);

        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");

        Intent getdata = getIntent();

        vec_type = getdata.getStringExtra("vec_type");

        if (vec_type.equals("Bus")) {
            service_url = "customer/driverslistbus";
        } else {
            service_url = "customer/driverslist";
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DriversList.this);
        editor = sharedPreferences.edit();

        ar_driver_data = new ArrayList<>();

        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");

        lv_drv_list = (ListView) findViewById(com.movhaul.customer.R.id.listview_driver);
        iv_filter = (ImageView) findViewById(com.movhaul.customer.R.id.imgview_filter);
        lt_filter_dialog = (LinearLayout) findViewById(com.movhaul.customer.R.id.filter_dialog);
        btn_filter = (Button) findViewById(com.movhaul.customer.R.id.btn_filter);
        iv_close = (ImageView) findViewById(com.movhaul.customer.R.id.button_close);
        iv_refresh = (ImageView) findViewById(com.movhaul.customer.R.id.refresh);

        btn_back = (com.rey.material.widget.LinearLayout) findViewById(com.movhaul.customer.R.id.layout_back);
        btn_refresh = (com.rey.material.widget.LinearLayout) findViewById(com.movhaul.customer.R.id.layout_refresh);

        lt_filter_dialog.setVisibility(View.GONE);

        final RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setInterpolator(new LinearInterpolator());


        final int height = getDeviceHeight(DriversList.this);

        mProgressDialog = new ProgressDialog(DriversList.this);
        mProgressDialog.setTitle(getString(R.string.adc));
        mProgressDialog.setMessage(getString(R.string.cwa));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);


        snackbar = Snackbar
                .make(findViewById(com.movhaul.customer.R.id.top), R.string.asee, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);


        if (!com.movhaul.customer.Config.isConnected(DriversList.this)) {
            snackbar.show();
            tv_snack.setText(R.string.wqc);
        } else {
            new get_drivers().execute();

        }


        final ArrayList<String> drv_arlist = new ArrayList<>();


        lv_drv_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lv_drv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                drv_adapter.registerToggle(pos);
                Log.e("tag", "clicked" + pos);


            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DriversList.this, Job_review.class);
                startActivity(i);
                finish();
            }
        });
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DriversList.this, "Refresh", Toast.LENGTH_SHORT).show();
                iv_refresh.startAnimation(rotate);
            }
        });


////*******Showing Filter Options *********/////

        iv_close.setVisibility(View.GONE);

        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialog_filter.show();

                TranslateAnimation anim_btn_b2t = new TranslateAnimation(0, 0, height, 0);
                anim_btn_b2t.setDuration(500);

                TranslateAnimation anim_btn_t2b = new TranslateAnimation(0, 0, 0, height);
                anim_btn_t2b.setDuration(500);


                if (i == 0) {
                    // iv_filter.setImageDrawable(getResources().getDrawable(R.drawable.close_btn));
                    iv_filter.setImageResource(com.movhaul.customer.R.drawable.close_btn);


                    i = 1;

                    lt_filter_dialog.setVisibility(View.VISIBLE);
                    lv_drv_list.setEnabled(false);
                    lt_filter_dialog.setAnimation(anim_btn_b2t);
                    btn_refresh.setVisibility(View.GONE);

                } else if (i == 1) {
                    // iv_filter.setImageDrawable(getResources().getDrawable(R.drawable.filter_ico));
                    iv_filter.setImageResource(com.movhaul.customer.R.mipmap.ic_filter_ico);
                    i = 0;

                    lt_filter_dialog.setVisibility(View.GONE);
                    lv_drv_list.setEnabled(true);
                    lt_filter_dialog.setAnimation(anim_btn_t2b);
                    btn_refresh.setVisibility(View.VISIBLE);
                    iv_filter.setVisibility(View.VISIBLE);
                }

            }
        });

        //********* Filtering Truck, Price& Rating  *********/////////
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lt_filter_dialog.setVisibility(View.GONE);
                lv_drv_list.setEnabled(true);

                TranslateAnimation anim_btn_t2b = new TranslateAnimation(0, 0, 0, height);
                anim_btn_t2b.setDuration(500);

                iv_filter.setImageResource(com.movhaul.customer.R.mipmap.ic_filter_ico);
                i = 0;

                lt_filter_dialog.setAnimation(anim_btn_t2b);
                btn_refresh.setVisibility(View.VISIBLE);
                iv_filter.setVisibility(View.VISIBLE);


            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        dialog1 = new Dialog(DriversList.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setCancelable(false);
        dialog1.setContentView(com.movhaul.customer.R.layout.dialogue_job_posting);
        btn_ok = (Button) dialog1.findViewById(com.movhaul.customer.R.id.button_ok);
        btn_close = (ImageView) dialog1.findViewById(com.movhaul.customer.R.id.button_close);
        tv_dialog1 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_1);
        tv_dialog2 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_2);
        tv_dialog3 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_3);
        tv_dialog4 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_4);
        tv_dialog1.setText(R.string.acw);
        tv_dialog2.setText(R.string.cwx);
        tv_dialog3.setText(R.string.cqa);
        tv_dialog4.setText(R.string.cxz);

        tv_dialog1.setTypeface(tf);
        tv_dialog2.setTypeface(tf);
        tv_dialog3.setTypeface(tf);
        tv_dialog4.setTypeface(tf);
        btn_ok.setTypeface(tf);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();

                editor.putString("job_id","");
                editor.commit();

                Intent i = new Intent(DriversList.this, DashboardNavigation.class);
                startActivity(i);
                finish();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();


                editor.putString("job_id","");
                editor.commit();

                Intent i = new Intent(DriversList.this, DashboardNavigation.class);
                startActivity(i);
                finish();
            }
        });

    }


    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e("tagMyAdapter_class", "onActivityResult"+data.toString());

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PAYMENT) {
            Log.e("tag", "cd:" + resultCode);
            Log.e("tag", "rc:" + requestCode);
            Log.e("tag", "dt:" + data.toString());

            Bundle bundle = data.getExtras();
            String authorizid = bundle.getString("authoriztionId");
            String remitaTransRef= bundle.getString("remitaTransRef");
            String responceCode = bundle.getString("responseCode");

            Log.e("tag","ss: "+authorizid);
            Log.e("tag","ss1: "+remitaTransRef);
            Log.e("tag","ss2: "+responceCode);

            /*final int height = getDeviceHeight(DashboardNavigation.this);
            TranslateAnimation anim_btn_b2t = new TranslateAnimation(0, 0, height, 0);
            anim_btn_b2t.setDuration(500);
            lt_filter_dialog.setAnimation(anim_btn_b2t);
            lt_filter_dialog.setVisibility(View.VISIBLE);
            aet_drop.setText("");*/

            //
            ///customer/emergencypayment

            booking_id = sharedPreferences.getString("booking_id", "");
            driver_id = sharedPreferences.getString("driver_id", "");
            bidding_id = sharedPreferences.getString("bidding_id", "");
            transaction_id = remitaTransRef;

            new book_now_task().execute();
        }


    }



    public class get_drivers extends AsyncTask<String, Void, String> {
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
                jsonObject.accumulate("booking_id", sharedPreferences.getString("job_id", ""));
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + service_url, json, id, token);

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
                    // String msg = jo.getString("message");
                    Log.d("tag", "<-----Status----->" + status);


                    if (status.equals("true")) {


                        JSONArray goods_data = jo.getJSONArray("message");

                        editor.putString("jobsize", String.valueOf(goods_data.length()));
                        editor.commit();

                        if (goods_data.length() > 0) {

                            for (int i = 0; i < goods_data.length(); i++) {


                                JSONObject jos = goods_data.getJSONObject(i);


                                if (jos.getString("driver_job_status").equals("free")) {


                                    if (jos.has("bus_image_front")) {

                                        mv_datas = new MV_Datas();

                                        String driver_name = jos.getString("driver_name");
                                        String driver_id = jos.getString("driver_id");
                                        String driver_image = jos.getString("driver_image");
                                        String truck_front = jos.getString("bus_image_front");
                                        String truck_back = jos.getString("bus_image_back");
                                        String truck_side = jos.getString("bus_image_side");
                                        String truck_type = jos.getString("bus_type");
                                        String bidding = jos.getString("bidding_cost");
                                        String rating = jos.getString("driver_rating");
                                        String damage_control = jos.getString("damage_control");
                                        String booking_id = jos.getString("booking_id");
                                        String bidding_id = jos.getString("bidding_id");


                                        mv_datas.setDriver_id(driver_id);
                                        mv_datas.setName(driver_name);
                                        mv_datas.setDriver_image(driver_image);
                                        mv_datas.setTruck_front(truck_front);
                                        mv_datas.setTruck_back(truck_back);
                                       // mv_datas.setTruck_side(truck_side);
                                        mv_datas.setTruck_type(truck_type);
                                        mv_datas.setBidding(bidding);
                                        mv_datas.setRating(rating);
                                        mv_datas.setDamage_control(damage_control);
                                        mv_datas.setBooking_id(booking_id);
                                        mv_datas.setBidding_id(bidding_id);

                                        doid = 0;

                                        ar_driver_data.add(mv_datas);


                                    } else {
                                        mv_datas = new MV_Datas();

                                        String driver_name = jos.getString("driver_name");
                                        String driver_id = jos.getString("driver_id");
                                        String driver_image = jos.getString("driver_image");
                                        String truck_front = jos.getString("truck_image_front");
                                        String truck_back = jos.getString("truck_image_back");
                                        String truck_side = jos.getString("truck_image_side");
                                        String truck_type = jos.getString("truck_type");
                                        String bidding = jos.getString("bidding_cost");
                                        String rating = jos.getString("driver_rating");
                                        String damage_control = jos.getString("damage_control");
                                        String booking_id = jos.getString("booking_id");
                                        String bidding_id = jos.getString("bidding_id");


                                        mv_datas.setDriver_id(driver_id);
                                        mv_datas.setName(driver_name);
                                        mv_datas.setDriver_image(driver_image);
                                        mv_datas.setTruck_front(truck_front);
                                        mv_datas.setTruck_back(truck_back);
                                        mv_datas.setTruck_side(truck_side);
                                        mv_datas.setTruck_type(truck_type);
                                        mv_datas.setBidding(bidding);
                                        mv_datas.setRating(rating);
                                        mv_datas.setDamage_control(damage_control);
                                        mv_datas.setBooking_id(booking_id);
                                        mv_datas.setBidding_id(bidding_id);
                                        doid = 1;

                                        ar_driver_data.add(mv_datas);

                                    }


                                }
                            }

                            if (ar_driver_data.size() == 0) {
                                finish();
                                Toast.makeText(getApplicationContext(), R.string.azew, Toast.LENGTH_LONG).show();
                            }

                        } else {
                            finish();
                            Toast.makeText(getApplicationContext(), R.string.azew, Toast.LENGTH_LONG).show();

                        }


                        drv_adapter = new DriversListAdapter(DriversList.this, DriversList.this, ar_driver_data,doid);
                        lv_drv_list.setAdapter(drv_adapter);

                    } else {
                        finish();
                        Toast.makeText(getApplicationContext(), R.string.azew, Toast.LENGTH_LONG).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    // Toast.makeText(getApplicationContext(),"Network Errror0",Toast.LENGTH_LONG).show();
                }
            } else {
                // Toast.makeText(getApplicationContext(),"Network Errror1",Toast.LENGTH_LONG).show();
            }

        }

    }


    public class book_now_task extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";


            Log.e("tag","no poto");

            String  s = "";
            JSONObject jsonObject = new JSONObject();
            try {


                jsonObject.put("transaction_id","Trc0Bz39dox");
                jsonObject.put("booking_id", booking_id);
                jsonObject.put("driver_id", driver_id);
                jsonObject.put("bidding_id", bidding_id);


                json = jsonObject.toString();

                return s = HttpUtils.makeRequest1(com.movhaul.customer.Config.WEB_URL + "customer/payment",json,id,token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;



        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            mProgressDialog.dismiss();

            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                    Log.d("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {

                        dialog1.show();
                    } else if (status.equals("false")) {

                        Log.e("tag", "Location not updated");
                        //has to check internet and location...
                        Toast.makeText(getApplicationContext(),R.string.ase,Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    Toast.makeText(getApplicationContext(),R.string.ase,Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(),R.string.ase,Toast.LENGTH_LONG).show();
            }

        }

    }



}
