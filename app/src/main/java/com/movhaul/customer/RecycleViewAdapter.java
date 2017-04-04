package com.movhaul.customer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidviewhover.BlurLayout;
import com.rey.material.widget.TextView;

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
    public static View hover;
    public static BlurLayout hoverLayout;



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
        if(ko !=0){
            viewHolder.tv_book.setVisibility(View.VISIBLE);
            viewHolder.tv_book.setTag(position);
        }



        hoverLayout.enableBlurBackground(false);
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
        });


        viewHolder.tv_pickup.setText(mv_datas.getPickup());
        viewHolder.tv_drop.setText(mv_datas.getDrop());
        viewHolder.tv_date.setText(mv_datas.getTime());
        viewHolder.tv_bid_count.setText(mv_datas.getDriver_count());



    }


    @Override
    public int getItemCount() {
        return ar_job_list.size();
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_list_adapter, parent, false);
        hover = LayoutInflater.from(context).inflate(R.layout.hover_layout, null);
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

            hoverLayout = (BlurLayout) view.findViewById(R.id.blur_layout);
            hoverLayout.enableTouchEvent(true);



        }
    }
}