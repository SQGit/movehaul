package com.movhaul.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.androidviewhover.BlurLayout;
import com.rey.material.widget.TextView;
import com.sloop.fonts.FontsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
public class JobListAdapter extends ArrayAdapter<MV_Datas> {

    private static final int ITEM_VIEW_TYPE_ITEM = 0;
    private static final int ITEM_VIEW_TYPE_SEPARATOR = 1;
    Context context;
    ArrayList<MV_Datas> ar_job_list;
    Activity activity;
    MV_Datas mv_datas;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Typeface tf;
    String booking_id, id, token;
    Integer selected_position = -1;
    ProgressDialog mProgressDialog;
    //private JobListAdapter adapter;



    public JobListAdapter(Context context, Activity acti, ArrayList<MV_Datas> objects) {
        super(context, R.layout.job_list_adapter, objects);
        this.activity = acti;
        this.context = context;
        this.ar_job_list = objects;
       // adapter = this;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /*@Override
    public int getItemViewType(int position) {
        return this.getItem(position).isSection() ? ITEM_VIEW_TYPE_SEPARATOR : ITEM_VIEW_TYPE_ITEM;
    }*/

    @Override
    public int getCount() {
        return ar_job_list.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {




        ViewHolder viewHolder;

        FontsManager.initFormAssets(activity, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(activity);

        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/lato.ttf");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");

        mProgressDialog = new ProgressDialog(context, com.movhaul.customer.R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setTitle(com.movhaul.customer.R.string.loading);
        mProgressDialog.setMessage(context.getString(com.movhaul.customer.R.string.wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.job_list_adapter, parent, false);

            viewHolder.tv_pickup = (com.rey.material.widget.TextView) convertView.findViewById(R.id.textview_title_pickup);
            viewHolder.tv_drop = (com.rey.material.widget.TextView) convertView.findViewById(R.id.textview_title_drop);
            viewHolder.tv_date = (com.rey.material.widget.TextView) convertView.findViewById(R.id.textview_title_date);
            viewHolder.tv_pick_txt = (com.rey.material.widget.TextView) convertView.findViewById(R.id.textview_title_date);

            viewHolder.tv_date_txt = (com.rey.material.widget.TextView) convertView.findViewById(R.id.textview_title_date);
            viewHolder.tv_bid_count = (com.rey.material.widget.TextView) convertView.findViewById(R.id.textview_bidding_count);
            viewHolder.tv_bid_count_txt = (com.rey.material.widget.TextView) convertView.findViewById(R.id.textview_bidding_count_txt);



            viewHolder.tv_book = (com.rey.material.widget.TextView) convertView.findViewById(R.id.text_book);
            viewHolder.tv_delete = (com.rey.material.widget.TextView) convertView.findViewById(R.id.text_delete);

            viewHolder.tv_pickup.setTypeface(tf);
            viewHolder.tv_drop.setTypeface(tf);
            viewHolder.tv_date.setTypeface(tf);
            viewHolder.tv_date_txt.setTypeface(tf);
            viewHolder.tv_drop_txt.setTypeface(tf);
            viewHolder.tv_pick_txt.setTypeface(tf);
            viewHolder.tv_bid_count.setTypeface(tf);
            viewHolder.tv_bid_count_txt.setTypeface(tf);
            viewHolder.tv_book.setTypeface(tf);

            //viewHolder.mSampleLayout = (BlurLayout) convertView.findViewById(R.id.blur_layout);
            //viewHolder.hover = LayoutInflater.from(context).inflate(R.layout.hover_layout, null);

            //viewHolder.tv_book= (com.rey.material.widget.TextView)  viewHolder.hover.findViewById(R.id.text_book);
            //viewHolder.tv_delete = (com.rey.material.widget.TextView)  viewHolder.hover.findViewById(R.id.text_delete);

         /*    viewHolder.mSampleLayout.enableBlurBackground(false);
            viewHolder.mSampleLayout.addChildAppearAnimator(viewHolder.hover, R.id.text_book, Techniques.SlideInLeft);
            viewHolder.mSampleLayout.addChildAppearAnimator(viewHolder.hover, R.id.text_delete, Techniques.SlideInRight);
            viewHolder.mSampleLayout.addChildDisappearAnimator(viewHolder.hover, R.id.text_book, Techniques.SlideOutLeft);
            viewHolder.mSampleLayout.addChildDisappearAnimator(viewHolder.hover, R.id.text_delete, Techniques.SlideOutRight);*/


            // viewHolder.mSampleLayout.enableTouchEvent(true);

            // viewHolder.mSampleLayout.setTag(position);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        mv_datas = ar_job_list.get(position);
        //Log.e("tag", position + " position " + mv_datas.getDriver_count());
        int ko = Integer.valueOf(mv_datas.getDriver_count());
        if(ko !=0){
            viewHolder.tv_book.setVisibility(View.VISIBLE);
            viewHolder.tv_book.setTag(position);
        }




     //   viewHolder.mSampleLayout.setHoverView(viewHolder.hover);


      /*  viewHolder.hover.findViewById(R.id.text_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.FlipInX)
                        .duration(550)
                        .playOn(v);

                Log.e("tag","hover:"+position+" txt:"+mv_datas.getBooking_id());
            }
        });*/


        //Log.e("tag", "hover " +viewHolder.mSampleLayout.getHoverStatus());

        // viewHolder.mSampleLayout.addChildAppearAnimator(viewHolder.hover, R.id.text_book, Techniques.SlideInLeft);
        // viewHolder.mSampleLayout.addChildAppearAnimator(viewHolder.hover, R.id.text_delete, Techniques.SlideInRight);
        // viewHolder.mSampleLayout.addChildDisappearAnimator(viewHolder.hover, R.id.text_book, Techniques.SlideOutLeft);
        // viewHolder.mSampleLayout.addChildDisappearAnimator(viewHolder.hover, R.id.text_delete, Techniques.SlideOutRight);


       /* viewHolder.mSampleLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("tag","touch"+position);

                viewHolder.mSampleLayout.dismissHover();
                viewHolder.mSampleLayout.setHovered(false);
                viewHolder.mSampleLayout.clearFocus();
                viewHolder.mSampleLayout.dispatchSetSelected(true);
                viewHolder.mSampleLayout.setHoverView(viewHolder.hover);


                selected_position = position;


                return false;
            }
        });*/

       /* if(position != selected_position){
            viewHolder.mSampleLayout.dismissHover();
            viewHolder.mSampleLayout.setHovered(false);
        }*/



       /*if(Integer.valueOf(mv_datas.getDriver_count()) <1){
           viewHolder.tv_book.setVisibility(View.GONE);
       }*/

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


        viewHolder.tv_pickup.setText(mv_datas.getPickup());
        viewHolder.tv_drop.setText(mv_datas.getDrop());
        viewHolder.tv_date.setText(mv_datas.getTime());
        viewHolder.tv_bid_count.setText(mv_datas.getDriver_count());

        return convertView;

    }


    private static class ViewHolder {
        TextView tv_pickup, tv_drop, tv_date, tv_pick_txt, tv_drop_txt, tv_date_txt, tv_bid_count, tv_bid_count_txt, tv_book, tv_delete;
         //View hover;
         //BlurLayout mSampleLayout;
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