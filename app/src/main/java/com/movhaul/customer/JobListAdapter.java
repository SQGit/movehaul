package com.movhaul.customer;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.androidviewhover.BlurLayout;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
public class JobListAdapter extends ArrayAdapter<MV_Datas> {

    Context context;
    ArrayList<MV_Datas> ar_job_list;
    Activity activity;
    MV_Datas mv_datas;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Typeface tf;
    String booking_id;



    public JobListAdapter(Context context, Activity acti, ArrayList<MV_Datas> objects) {
        super(context, 0, objects);
        this.activity = acti;
        this.context = context;
        this.ar_job_list = objects;


    }

    @Override
    public int getCount() {
        return ar_job_list.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder;
        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/lato.ttf");


        mv_datas = ar_job_list.get(position);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.job_list_adapter, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.tv_pickup = (com.rey.material.widget.TextView) row.findViewById(R.id.textview_title_pickup);
            viewHolder.tv_drop = (com.rey.material.widget.TextView) row.findViewById(R.id.textview_title_drop);
            viewHolder.tv_date = (com.rey.material.widget.TextView) row.findViewById(R.id.textview_title_date);
            viewHolder.tv_pick_txt = (com.rey.material.widget.TextView) row.findViewById(R.id.textview_title_date);
            viewHolder.tv_drop_txt = (com.rey.material.widget.TextView) row.findViewById(R.id.textview_book_to_txt);
            viewHolder.tv_date_txt = (com.rey.material.widget.TextView) row.findViewById(R.id.textview_title_date);
          //  viewHolder.tv_book = (com.rey.material.widget.TextView) row.findViewById(R.id.textview_book_driver);
           // viewHolder.tv_delete = (com.rey.material.widget.TextView) row.findViewById(R.id.textview_delete);
            viewHolder.tv_bid_count = (com.rey.material.widget.TextView) row.findViewById(R.id.textview_bidding_count);
            viewHolder.tv_bid_count_txt = (com.rey.material.widget.TextView) row.findViewById(R.id.textview_bidding_count_txt);

            viewHolder.mSampleLayout = (BlurLayout)row.findViewById(R.id.blur_layout);



            viewHolder.tv_pickup.setTypeface(tf);
            viewHolder.tv_drop.setTypeface(tf);
            viewHolder.tv_date.setTypeface(tf);
            viewHolder.tv_date_txt.setTypeface(tf);
            viewHolder.tv_drop_txt.setTypeface(tf);
            viewHolder.tv_pick_txt.setTypeface(tf);
           // viewHolder.tv_book.setTypeface(tf);
            //viewHolder.tv_delete.setTypeface(tf);
            viewHolder.tv_bid_count.setTypeface(tf);
            viewHolder.tv_bid_count_txt.setTypeface(tf);

            row.setTag(viewHolder);


        } else {
            row = convertView;
            viewHolder = (ViewHolder) row.getTag();
        }


        viewHolder.hover = LayoutInflater.from(context).inflate(R.layout.hover_layout, null);

        viewHolder.hover.findViewById(R.id.txtos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Swing)
                        .duration(550)
                        .playOn(v);
            }
        });
        viewHolder.hover.findViewById(R.id.txtbs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Wobble)
                        .duration(550)
                        .playOn(v);
            }
        });



        viewHolder.mSampleLayout.setHoverView(viewHolder.hover);
        //viewHolder.mSampleLayout.setBlurDuration(5);
       // viewHolder.mSampleLayout.setBlurRadius(0);
        viewHolder.mSampleLayout.enableBlurBackground(false);
        viewHolder.mSampleLayout.addChildAppearAnimator(viewHolder.hover, R.id.txtos, Techniques.SlideInLeft);
        viewHolder.mSampleLayout.addChildAppearAnimator(viewHolder.hover, R.id.txtbs, Techniques.SlideInRight);

        viewHolder.mSampleLayout.addChildDisappearAnimator(viewHolder.hover, R.id.txtos, Techniques.SlideOutLeft);
        viewHolder.mSampleLayout.addChildDisappearAnimator(viewHolder.hover, R.id.txtbs, Techniques.SlideOutRight);

        //viewHolder.mSampleLayout.addChildAppearAnimator(viewHolder.hover, R.id.description, Techniques.FadeInUp);
        //viewHolder.mSampleLayout.addChildDisappearAnimator(viewHolder.hover, R.id.description, Techniques.FadeOutDown);


       // viewHolder.mSampleLayout.addChildAppearAnimator(viewHolder.hover, R.id.txtos, Techniques.Landing);
       // viewHolder.mSampleLayout.addChildDisappearAnimator(viewHolder.hover, R.id.txtos, Techniques.TakingOff);
        //viewHolder.mSampleLayout.enableZoomBackground(true);
        //viewHolder.mSampleLayout.setBlurDuration(1200);


        viewHolder.tv_pickup.setText(mv_datas.getPickup());
        viewHolder.tv_drop.setText(mv_datas.getDrop());
        viewHolder.tv_date.setText(mv_datas.getTime());
        viewHolder.tv_bid_count.setText(mv_datas.getDriver_count());


      /*  viewHolder.tv_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mv_datas = ar_job_list.get(position);
                Log.e("tag","id:"+mv_datas.getBooking_id());
                booking_id = mv_datas.getBooking_id();

                editor.putString("job_id", booking_id);
                editor.commit();

                Intent i = new Intent(context, Job_review.class);
                activity.startActivity(i);

            }
        });

        viewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mv_datas = ar_job_list.get(position);
                Log.e("tag","deL_id:"+mv_datas.getBooking_id());
            }
        });*/


        return row;


    }

    class ViewHolder {

        TextView tv_pickup,tv_drop,tv_date,tv_pick_txt,tv_drop_txt,tv_date_txt,tv_book,tv_delete,tv_bid_count,tv_bid_count_txt;

        private BlurLayout mSampleLayout;
        View hover;

    }






    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    class MyDragListener implements View.OnDragListener {
        Drawable enterShape = context.getResources().getDrawable(
                R.drawable.shape_droptarget);
        Drawable normalShape = context.getResources().getDrawable(R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundDrawable(normalShape);
                default:
                    break;
            }
            return true;
        }
    }



}