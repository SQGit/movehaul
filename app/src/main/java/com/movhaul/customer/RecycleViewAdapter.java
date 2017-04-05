package com.movhaul.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidviewhover.BlurLayout;
import com.rey.material.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Salman on 04-04-2017.
 */

public class RecycleViewAdapter extends
        RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {


    Context context;
    ArrayList<MV_Datas> ar_job_list;
    Activity activity;
    MV_Datas mv_datas;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Typeface tf;
    String booking_id, id, token;
    private RecycleViewAdapter adapter;
    ProgressDialog mProgressDialog;
    //public static View hover;
   // public static BlurLayout hoverLayout;



    public RecycleViewAdapter(Context context, Activity acti, ArrayList<MV_Datas> objects) {
        this.activity = acti;
        this.context = context;
        this.ar_job_list = objects;
        adapter = this;
    }



    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int position) {

        mv_datas = ar_job_list.get(position);
        int ko = Integer.valueOf(mv_datas.getDriver_count());
        Log.e("tag",position+" :ss: "+ko);
        if(ko !=0){
            viewHolder.tv_book.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.tv_book.setVisibility(View.GONE);
        }



        /*hoverLayout.enableBlurBackground(false);
        hoverLayout.addChildAppearAnimator(hover, R.id.text_book, Techniques.SlideInLeft);
        hoverLayout.addChildAppearAnimator(hover, R.id.text_delete, Techniques.SlideInRight);
        hoverLayout.addChildDisappearAnimator(hover, R.id.text_book, Techniques.SlideOutLeft);
        hoverLayout.addChildDisappearAnimator(hover, R.id.text_delete, Techniques.SlideOutRight);
        hoverLayout.setHoverView(hover);
        hoverLayout.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                Log.e("tag","hover _slie");
                return false;
            }
        });
        hoverLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tag","item_click");
            }
        });
        hoverLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("tag","touch_click"+position);
                return false;
            }
        });
        hoverLayout.addAppearListener(new BlurLayout.AppearListener() {
            @Override
            public void onStart() {
                Log.e("tag","appearche"+position);
            }
            @Override
            public void onEnd() {
                Log.e("tag","appear_click"+position);
            }
        });*/


        viewHolder.tv_pickup.setText(mv_datas.getPickup());
        viewHolder.tv_drop.setText(mv_datas.getDrop());
        viewHolder.tv_date.setText(mv_datas.getTime());
        viewHolder.tv_bid_count.setText(mv_datas.getDriver_count());


        viewHolder.tv_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mv_datas = ar_job_list.get(position);

                int ko = Integer.valueOf(mv_datas.getDriver_count());

                Log.e("tag", " postions: " + position);
                Log.e("tag", ko+" id: " + mv_datas.getDriver_count());

                if (ko != 0) {
                    booking_id = mv_datas.getBooking_id();
                    editor.putString("job_id", booking_id);
                    editor.commit();
                    Intent i = new Intent(context, Job_review.class);
                    activity.startActivity(i);
                }
                else{
                    Toast.makeText(context,"No Drivers Bidded",Toast.LENGTH_SHORT).show();
                }

            }
        });

        viewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mv_datas = ar_job_list.get(position);
                booking_id = mv_datas.getBooking_id();
                Log.e("tag","deL_id:"+mv_datas.getBooking_id());

                if (!com.movhaul.customer.Config.isConnected(context)) {
                    Toast.makeText(context,"No network Found,Please Try Again Later.",Toast.LENGTH_SHORT).show();
                }
                else{
                    new delete_job().execute();

                }
            }
        });



    }


    @Override
    public int getItemCount() {
        return ar_job_list.size();
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_list_adapter, parent, false);

        mProgressDialog = new ProgressDialog(context, com.movhaul.customer.R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setTitle(com.movhaul.customer.R.string.loading);
        mProgressDialog.setMessage(context.getString(com.movhaul.customer.R.string.wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");

        //hover = LayoutInflater.from(context).inflate(R.layout.hover_layout, null);
        return new MyViewHolder(v);
    }

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_pickup, tv_drop, tv_date, tv_pick_txt, tv_drop_txt, tv_date_txt, tv_bid_count, tv_bid_count_txt, tv_book, tv_delete;



        public MyViewHolder(View view) {
            super(view);
            tv_pickup = (com.rey.material.widget.TextView) view.findViewById(R.id.textview_title_pickup);
            tv_drop = (com.rey.material.widget.TextView) view.findViewById(R.id.textview_title_drop);
            tv_date = (com.rey.material.widget.TextView) view.findViewById(R.id.textview_title_date);
            tv_pick_txt = (com.rey.material.widget.TextView) view.findViewById(R.id.textview_title_date);
            tv_drop_txt = (com.rey.material.widget.TextView) view.findViewById(R.id.textview_book_to_txt);
            tv_date_txt = (com.rey.material.widget.TextView) view.findViewById(R.id.textview_title_date);
            tv_bid_count = (com.rey.material.widget.TextView) view.findViewById(R.id.textview_bidding_count);
            tv_bid_count_txt = (com.rey.material.widget.TextView) view.findViewById(R.id.textview_bidding_count_txt);
            tv_book = (com.rey.material.widget.TextView) view.findViewById(R.id.text_book);
            tv_delete = (com.rey.material.widget.TextView) view.findViewById(R.id.text_delete);

            //hoverLayout = (BlurLayout) view.findViewById(R.id.blur_layout);
            //hoverLayout.enableTouchEvent(true);



        }
    }

    public class delete_job extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("booking_id", booking_id);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + "customer/deletebooking", json, id, token);

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

                        Intent insd = activity.getIntent();
                        context.startActivity(insd);

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