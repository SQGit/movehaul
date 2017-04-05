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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidviewhover.BlurLayout;
import com.nineoldandroids.animation.ObjectAnimator;
import com.rey.material.widget.TextView;
import com.sloop.fonts.FontsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Salman on 04-04-2017.
 */

public class RecycleViewAdapter extends
        RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {


    //public static View hover;

    Context context;
    ArrayList<MV_Datas> ar_job_list;
    Activity activity;
    MV_Datas mv_datas;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Typeface tf;
    int postion_s;
    String booking_id, id, token;
    ProgressDialog mProgressDialog;
    private RecycleViewAdapter adapter;
    public int hover_click = 0;
    TranslateAnimation anim_truck_c2r2,anim_truck_c2r3, anim_truck_c2r1, anim_truck_c2r;

    public RecycleViewAdapter(Context context, Activity acti, ArrayList<MV_Datas> objects) {
        this.activity = acti;
        this.context = context;
        this.ar_job_list = objects;
        adapter = this;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder,  int position) {

        Log.e("tag","s:"+position);
       // postion_s = position;



        mv_datas = ar_job_list.get(position);
        int ko = Integer.valueOf(mv_datas.getDriver_count());
        Log.e("tag", position + " :ss: " + ko);
       /* if (ko != 0) {
            viewHolder.tv_book.setVisibility(View.GONE);
        } else {
            viewHolder.tv_book.setVisibility(View.GONE);
        }*/



        /*hoverLayout.setOnHoverListener(new View.OnHoverListener() {
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



        //viewHolder.tv_book.setVisibility(View.GONE);
       // viewHolder.tv_delete.setVisibility(View.GONE);


        final float width = getDeviceWidth(context);



        anim_truck_c2r = new TranslateAnimation(-500,viewHolder.tv_book.getX(), 0, 0);
        anim_truck_c2r.setDuration(1000);
        anim_truck_c2r.setFillAfter(true);



        anim_truck_c2r1 = new TranslateAnimation(viewHolder.tv_book.getX(),-500, 0, 0);
        anim_truck_c2r1.setDuration(1000);
        anim_truck_c2r1.setFillAfter(true);
        anim_truck_c2r1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.e("tag","2st animation end");

                viewHolder.tv_book.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        anim_truck_c2r2 = new TranslateAnimation(viewHolder.tv_delete.getX()+500,viewHolder.tv_delete.getX(), 0, 0);
        anim_truck_c2r2.setDuration(1000);
        anim_truck_c2r2.setFillAfter(true);



        anim_truck_c2r3 = new TranslateAnimation(viewHolder.tv_delete.getX(),viewHolder.tv_delete.getX()+500, 0, 0);
        anim_truck_c2r3.setDuration(1000);
        anim_truck_c2r3.setFillAfter(true);
        anim_truck_c2r3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.e("tag","2st animation end");

                viewHolder.tv_delete.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        viewHolder.tv_pickup.setText(mv_datas.getPickup());
        viewHolder.tv_drop.setText(mv_datas.getDrop());
        viewHolder.tv_date.setText(mv_datas.getTime());
        viewHolder.tv_bid_count.setText(mv_datas.getDriver_count());


        viewHolder.tv_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Log.e("tag","book"+position);
               /* mv_datas = ar_job_list.get(position);
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
                }*/

            }
        });


        viewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.e("tag","delete"+position);
               /* mv_datas = ar_job_list.get(position);
                booking_id = mv_datas.getBooking_id();
                Log.e("tag","deL_id:"+mv_datas.getBooking_id());

                if (!com.movhaul.customer.Config.isConnected(context)) {
                    Toast.makeText(context,"No network Found,Please Try Again Later.",Toast.LENGTH_SHORT).show();
                }
                else{
                    new delete_job().execute();

                }*/
            }
        });


    }

    private float getDeviceWidth(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int height = display.getHeight();
        return height;
    }


    @Override
    public int getItemCount() {
        return ar_job_list.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_list_adapter, parent, false);

        FontsManager.initFormAssets(activity, "fonts/lato.ttf");
        FontsManager.changeFonts(activity);


        final int poss = viewType;
        postion_s = viewType;




        mProgressDialog = new ProgressDialog(context, com.movhaul.customer.R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setTitle(com.movhaul.customer.R.string.loading);
        mProgressDialog.setMessage(context.getString(com.movhaul.customer.R.string.wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");



         final MyViewHolder holder = new MyViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tag","click v_poss"+poss);
                Log.e("tag","click postion_s"+postion_s);
                if (holder.tv_book.getVisibility() == View.GONE) {
                    holder.tv_book.startAnimation(anim_truck_c2r);
                    holder.tv_delete.startAnimation(anim_truck_c2r2);

                    holder.tv_book.setVisibility(View.VISIBLE);
                    holder.tv_delete.setVisibility(View.VISIBLE);
                    hover_click=1;
                }
                else {
                    holder.tv_book.startAnimation(anim_truck_c2r1);
                    holder.tv_delete.startAnimation(anim_truck_c2r3);
                   // holder.tv_book.setVisibility(View.GONE);
                   // holder.tv_delete.setVisibility(View.GONE);
                    hover_click=0;
                }
            }
        });


        return new MyViewHolder(v);
    }






    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_pickup, tv_drop, tv_date, tv_pick_txt, tv_drop_txt, tv_date_txt, tv_bid_count, tv_bid_count_txt, tv_book, tv_delete;
        //BlurLayout hoverLayout;
       // View hover;

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

           // hoverLayout.enableTouchEvent(false);



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