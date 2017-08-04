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
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ramotion.foldingcell.FoldingCell;
import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.rey.material.widget.ListView;
import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/**
 * Created by SQINDIA on 10/26/2016.
 * my trips show current ,upcoming and finished jobs
 * in history of jobs cancelled and finished jobs will show.
 *
 */
@SuppressWarnings("ConstantConditions")
public class MyTrips extends AppCompatActivity {
    ListView ht_lview;
    LinearLayout btn_back;
    ArrayList<String> ht_arlist;
    MV_Datas mv_datas;
    Snackbar snackbar;
    Typeface tf;
    android.widget.TextView tv_snack;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String id, token;
    ArrayList<MV_Datas> ar_job_upcoming;
    ArrayList<MV_Datas> ar_job_history;
    ProgressDialog mProgressDialog;
    android.widget.TextView tv_cr_date, tv_cr_time, tv_cr_pickup, tv_cr_drop, tv_cr_tr_type, tv_cr_dr_name, tv_cr_dr_phone, tv_cr_job_cost, tv_cr_job_id;
    Button btn_cancel_job;
    ImageView iv_type;
    ImageView iv_content_prof;
    TabLayout tl_indicator;
    Dialog dg_show_cancel;
    TextView tv_dg_txt, tv_dg_txt2;
    Button btn_dg_cancel;
    android.widget.LinearLayout lt_top;
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                FontsManager.initFormAssets(MyTrips.this, "fonts/lato.ttf");       //initialization
                FontsManager.changeFonts(MyTrips.this);
                //tl_indicator.setupWithViewPager(viewPager);
               // tl_indicator.getTabAt(1).select();
            } else if (position == 1) {
                FontsManager.initFormAssets(MyTrips.this, "fonts/lato.ttf");       //initialization
                FontsManager.changeFonts(MyTrips.this);
            } else {
                FontsManager.initFormAssets(MyTrips.this, "fonts/lato.ttf");       //initialization
                FontsManager.changeFonts(MyTrips.this);
            }
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };
    private ViewPager viewPager;
    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.movhaul.customer.R.layout.mytrips);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");
        FontsManager.changeFonts(this);
        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyTrips.this);
        editor = sharedPreferences.edit();
        btn_back = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_back);
        viewPager = (ViewPager) findViewById(com.movhaul.customer.R.id.view_pager);
        tl_indicator = (TabLayout) findViewById(R.id.tabs);
        layouts = new int[]{
                com.movhaul.customer.R.layout.current_trips1,
                com.movhaul.customer.R.layout.history_trips,
                com.movhaul.customer.R.layout.upcoming_trips,};
        mProgressDialog = new ProgressDialog(MyTrips.this);
        mProgressDialog.setTitle(com.movhaul.customer.R.string.loading);
        mProgressDialog.setMessage(getString(com.movhaul.customer.R.string.wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        snackbar = Snackbar
                .make(findViewById(com.movhaul.customer.R.id.top), com.movhaul.customer.R.string.no_internet, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);
        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");
        ar_job_upcoming = new ArrayList<>();
        ar_job_history = new ArrayList<>();
        myViewPagerAdapter = new MyViewPagerAdapter();
        Log.e("tag_id", id);
        Log.e("tag_token", token);
        if (!com.movhaul.customer.Config.isConnected(MyTrips.this)) {
            snackbar.show();
            tv_snack.setText(com.movhaul.customer.R.string.please_try_again);
        } else {
            new get_history().execute();
        }
        dg_show_cancel = new Dialog(MyTrips.this);
        dg_show_cancel.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dg_show_cancel.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dg_show_cancel.setCancelable(true);
        dg_show_cancel.setContentView(com.movhaul.customer.R.layout.dialog_road_confirm);
        btn_dg_cancel = (Button) dg_show_cancel.findViewById(com.movhaul.customer.R.id.button_yes);
        tv_dg_txt = (android.widget.TextView) dg_show_cancel.findViewById(com.movhaul.customer.R.id.textView_1);
        tv_dg_txt2 = (android.widget.TextView) dg_show_cancel.findViewById(com.movhaul.customer.R.id.textView_2);
        tv_dg_txt.setText(R.string.ca);
        tv_dg_txt2.setText(R.string.axcanc);
        btn_dg_cancel.setText("OK");
        tv_dg_txt.setTypeface(tf);
        tv_dg_txt2.setTypeface(tf);
        btn_dg_cancel.setTypeface(tf);
        tl_indicator.setupWithViewPager(viewPager);
        btn_dg_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dg_show_cancel.dismiss();
                if (!com.movhaul.customer.Config.isConnected(MyTrips.this)) {
                    snackbar.show();
                    tv_snack.setText(com.movhaul.customer.R.string.please_try_again);
                } else {
                    new cancel_job(mv_datas.getBooking_id(), mv_datas.getDriver_id()).execute();
                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyTrips.this, DashboardNavigation.class);
                startActivity(i);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MyTrips.this, DashboardNavigation.class);
        startActivity(i);
        finish();
    }
    private class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        MyViewPagerAdapter() {
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
           /*FontsManager.initFormAssets(getApplicationContext(), "fonts/lato.ttf");       //initialization
            FontsManager.changeFonts((Activity) getApplicationContext());*/
            if (position == 0) {
                lt_top = (android.widget.LinearLayout) view.findViewById(R.id.layout_top);
                if (ar_job_upcoming.size() > 0) {
                    lt_top.setVisibility(View.VISIBLE);
                    mv_datas = ar_job_upcoming.get(0);
                    Log.e("tag", "pickup: " + mv_datas.getPickup() + mv_datas.getJob_cost());
                    Log.e("tag", "33size " + ar_job_upcoming.size());
                    tv_cr_date = (android.widget.TextView) view.findViewById(com.movhaul.customer.R.id.cr_date);
                    tv_cr_time = (android.widget.TextView) view.findViewById(com.movhaul.customer.R.id.cr_time);
                    tv_cr_pickup = (android.widget.TextView) view.findViewById(com.movhaul.customer.R.id.cr_pickup);
                    tv_cr_drop = (android.widget.TextView) view.findViewById(com.movhaul.customer.R.id.cr_drop);
                    tv_cr_tr_type = (android.widget.TextView) view.findViewById(com.movhaul.customer.R.id.cr_truck_type);
                    tv_cr_dr_name = (android.widget.TextView) view.findViewById(com.movhaul.customer.R.id.cr_dr_name);
                    tv_cr_dr_phone = (android.widget.TextView) view.findViewById(com.movhaul.customer.R.id.cr_dr_phone);
                    tv_cr_job_cost = (android.widget.TextView) view.findViewById(com.movhaul.customer.R.id.cr_job_cost);
                    tv_cr_job_id = (android.widget.TextView) view.findViewById(com.movhaul.customer.R.id.cr_job_id);
                    iv_content_prof = (ImageView) view.findViewById(com.movhaul.customer.R.id.imageview_content_profile);
                    iv_type = (ImageView) view.findViewById(com.movhaul.customer.R.id.imageView5);
                    btn_cancel_job = (Button) view.findViewById(R.id.btn_canceljob);
                    tv_cr_date.setText(mv_datas.getDate());
                    tv_cr_time.setText(mv_datas.getTime());
                    tv_cr_pickup.setText(mv_datas.getPickup());
                    tv_cr_drop.setText(mv_datas.getDrop());
                    tv_cr_tr_type.setText(mv_datas.getTruck_type());
                    tv_cr_dr_name.setText(mv_datas.getName());
                    tv_cr_dr_phone.setText(mv_datas.getDriver_number());
                    tv_cr_job_cost.setText(mv_datas.getJob_cost());
                    tv_cr_job_id.setText(mv_datas.getBooking_id());
                    Glide.with(MyTrips.this).load(Config.WEB_URL_IMG + "driver_details/" + mv_datas.getDriver_image()).into(iv_content_prof);
                    if (mv_datas.getVec_type().equals("Bus")) {
                        iv_type.setImageResource(com.movhaul.customer.R.drawable.bus_type);
                    } else {
                        iv_type.setImageResource(com.movhaul.customer.R.drawable.select_truck_type);
                    }
                    btn_cancel_job.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("tag", mv_datas.getDriver_id() + "cancel_Job" + mv_datas.getBooking_id());
                            dg_show_cancel.show();
                        }
                    });
                } else {
                /*    tl_indicator.setupWithViewPager(viewPager);
                    tl_indicator.getTabAt(0).select();

                    snackbar.show();
                    tv_snack.setText("You don't have any jobs to show.");
                    viewPager.setCurrentItem(1);*/
                    lt_top.setVisibility(View.GONE);
                }
            } else if (position == 1) {
                ht_lview = (ListView) view.findViewById(com.movhaul.customer.R.id.lview);
                ht_arlist = new ArrayList<>();
                HistoryAdapter adapter = new HistoryAdapter(MyTrips.this, ar_job_history);
                ht_lview.setAdapter(adapter);
            } else {
                android.widget.ListView up_lview;
                up_lview = (android.widget.ListView) view.findViewById(com.movhaul.customer.R.id.lview);
                final UpcomingAdapter up_adapter = new UpcomingAdapter(MyTrips.this, MyTrips.this, ar_job_upcoming);
                up_lview.setAdapter(up_adapter);
                up_lview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                up_lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                        // toggle clicked cell state
                        ((FoldingCell) view).toggle(false);
                        // register in adapter that state for selected cell is toggled
                        up_adapter.registerToggle(pos);
                        Log.e("tag", "clicked" + pos);
                    }
                });
            }
            return view;
        }
        @Override
        public int getCount() {
            return layouts.length;
        }
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            String title;
            if (position == 0) {
                title = getString(com.movhaul.customer.R.string.curr);
            } else if (position == 1) {
                title = getString(com.movhaul.customer.R.string.hist);
            } else {
                title = getString(com.movhaul.customer.R.string.upco);
            }
            return title;
        }
    }
    @SuppressWarnings({"unused", "UnusedAssignment"})
    private class get_history extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + "customer/jobhistory", json, id, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
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
                        JSONArray goods_data = jo.getJSONArray("message");
                        if (goods_data.length() > 0) {
                            for (int i = 0; i < goods_data.length(); i++) {
                                JSONObject jos = goods_data.getJSONObject(i);
                                mv_datas = new MV_Datas();
                                String pickup_location = jos.getString("pickup_location");
                                String drop_location = jos.getString("drop_location");
                                String delivery_address = jos.getString("delivery_address");
                                String driver_name = jos.getString("driver_name");
                                String driver_phone = jos.getString("driver_mobile_pri");
                                String booking_time = jos.getString("booking_time");
                                String truck_type = jos.getString("vehicle_sub_type");
                                String vehicle_type = jos.getString("vehicle_type");
                                String job_cost = jos.getString("job_cost");
                                String driver_image = jos.getString("driver_image");
                                String booking_id = jos.getString("booking_id");
                                String job_status = jos.getString("job_status");
                                String driver_id = jos.getString("driver_id");
                                //2016\/12\/08 T 18:12
                                String[] parts = booking_time.trim().split("T");
                                String part1 = parts[0]; // 004
                                String part2 = parts[1]; // 034556
                                Log.e("tag", "1st" + part1);
                                Log.e("tag", "2st" + part2);

                                mv_datas.setName(driver_name);
                                mv_datas.setDriver_image(driver_image);
                                mv_datas.setDriver_number(driver_phone);
                                mv_datas.setDate(part1);
                                mv_datas.setTime(part2);
                                mv_datas.setTruck_type(truck_type);
                                mv_datas.setPickup(pickup_location);
                                mv_datas.setDrop(drop_location);
                                mv_datas.setJob_cost(job_cost);
                                mv_datas.setBooking_id(booking_id);
                                mv_datas.setVec_type(vehicle_type);
                                mv_datas.setJob_status(job_status);
                                mv_datas.setDriver_id(driver_id);

                                if (job_status.equals("confirmed")) {
                                    ar_job_upcoming.add(mv_datas);
                                } else {
                                    ar_job_history.add(mv_datas);
                                }
                            }
                            Log.e("tag", "size " + ar_job_upcoming.size());
                            viewPager.setAdapter(myViewPagerAdapter);
                            viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

                            // tiv.setTabIndicatorFactory(new TabIndicatorView.ViewPagerIndicatorFactory(viewPager));

                        } else {
                            finish();
                            Toast.makeText(getApplicationContext(), R.string.cade, Toast.LENGTH_LONG).show();
                        }

                    } else if (status.equals("false")) {

                        Log.e("tag", "status false");
                        //has to check internet and location...
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    // Toast.makeText(getApplicationContext(),"Network Errror0",Toast.LENGTH_LONG).show();
                }
            }

        }

    }
    @SuppressWarnings({"unused", "UnusedAssignment"})
    private class cancel_job extends AsyncTask<String, Void, String> {
        String booking_id, driver_id;
        cancel_job(String booking, String driver) {
            booking_id = booking;
            driver_id = driver;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("booking_id", booking_id);
                jsonObject.accumulate("driver_id", driver_id);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + "customer/canceljob", json, id, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
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
                    // String msg = jo.getString("message");
                    Log.e("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {
                        Log.e("tag", "<-----true----->" + status);
                        // adapter.notifyDataSetChanged();
                        Intent insd = getIntent();
                        startActivity(insd);
                    } else if (status.equals("false")) {
                        Log.e("tag", "Location not updated");
                        //has to check internet and location...
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    // Toast.makeText(getApplicationContext(),"Network Errror0",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
