package net.sqindia.movehaul;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

/**
 * Created by sqindia on 27-10-2016.
 */

public class Job_review extends Activity {
    LinearLayout btn_back;
    Button btn_continue;
    TextView tv_timer,tv_da_header;
    public Vibrator vibrator;

    Snackbar snackbar;
    Typeface tf;
    TextView tv_snack,tv_pickup,tv_drop,tv_delivery,tv_date,tv_time,tv_truck;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String id,token;
    String vec_type;
    ImageView iv_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_review);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);

        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Job_review.this);
        editor = sharedPreferences.edit();

        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");

        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        tv_timer = (TextView) findViewById(R.id.text_Timer);

        tv_pickup = (TextView) findViewById(R.id.textview_pickup);
        tv_drop = (TextView) findViewById(R.id.textview_drop);
        tv_delivery = (TextView) findViewById(R.id.textview_delivery);
        tv_date = (TextView) findViewById(R.id.textview_date);
        tv_time = (TextView) findViewById(R.id.textview_time);
        tv_truck = (TextView) findViewById(R.id.textview_truck);
        iv_type  = (ImageView) findViewById(R.id.image_type);

        tv_da_header = (TextView) findViewById(R.id.textview_da_header);


        snackbar = Snackbar
                .make(findViewById(R.id.top), "Network Error! Please Try Again Later.", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);



        if (!net.sqindia.movehaul.Config.isConnected(Job_review.this)) {
            snackbar.show();
            tv_snack.setText("Please Connect Internet and Try again");
        }
        else{
            new get_review().execute();

        }






        btn_continue.setEnabled(false);

        new CountDownTimer(20000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                tv_timer.setText(""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                if((TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)==0) &&
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))==10)
                {

                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    v.vibrate(5000);
                }

            }

            public void onFinish() {
                //onClickVibrate();
                tv_timer.setText("done!");
                btn_continue.setEnabled(true);
            }
        }.start();

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //vibrator.cancel();
                Intent i = new Intent(Job_review.this,DriversList.class);
                i.putExtra("vec_type",vec_type);
                startActivity(i);
                //finish();

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Job_review.this,DashboardNavigation.class);
                startActivity(i);
                finish();
            }
        });

    }
    public void onClickVibrate()
    {
        //Set the pattern, like vibrate for 300 milliseconds and then stop for 200 ms, then
        //vibrate for 300 milliseconds and then stop for 500 ms. You can change the pattern and
        // test the result for better clarity.
        long pattern[]={0,300,200,300,500};

        // Start the vibration
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        //start vibration with repeated count, use -1 if you don't want to repeat the vibration
        vibrator.vibrate(pattern, 0);
    }
    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Intent i = new Intent(Job_review.this,DashboardNavigation.class);
        startActivity(i);
        finish();
    }



    public class get_review extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("booking_id", sharedPreferences.getString("job_id","") );
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + "customer/showcurrentjob", json, id, token);

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



                        JSONArray goods_data = jo.getJSONArray("jobs");

                        if(goods_data.length()>0) {
                            for (int i = 0; i < goods_data.length(); i++) {


                                JSONObject jos = goods_data.getJSONObject(i);


                                String booking_id = jos.getString("booking_id");
                                String customer_id = jos.getString("driver_id");
                                String pickup_location = jos.getString("pickup_location");
                                String drop_location = jos.getString("drop_location");
                                String delivery_address = jos.getString("delivery_address");
                                String goods_type = jos.getString("goods_type");
                                String description = jos.getString("description");
                                String booking_time = jos.getString("booking_time");
                                vec_type = jos.getString("vehicle_type");
                                String vehicle_main_type = jos.getString("vehicle_main_type");
                                String vehicle_sub_type = jos.getString("vehicle_sub_type");


                                String[] parts = booking_time.trim().split("T");
                                String part1 = parts[0]; // 004
                                String part2 = parts[1]; // 034556

                                Log.e("tag","1st"+part1);
                                Log.e("tag","2st"+part2);
                                Log.e("tag","2stasd"+goods_type);

                                if(vec_type.equals("Bus")){
                                    tv_da_header.setText("Nearby Landmark");
                                    iv_type.setImageResource(R.drawable.bus_type);
                                    //iv_type.setBackgroundResource(R.drawable.bus_type);
                                }
                                else{
                                    tv_da_header.setText("Nearby Landmark");
                                    iv_type.setImageResource(R.drawable.select_truck_type);
                                   // iv_type.setBackgroundResource(R.drawable.select_truck_type);
                                }

                                tv_pickup.setText(pickup_location);
                                tv_drop.setText(drop_location);
                                tv_delivery.setText(delivery_address);
                                tv_date.setText(part1);
                                tv_time.setText(part2);
                                tv_truck.setText(vehicle_main_type+" - "+vehicle_sub_type);




                            }
                        }
                        else{
                            finish();
                        }






                    } else if (status.equals("false")) {

                        Log.e("tag", "Location not updated");
                        //has to check internet and location...


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













}
