package com.movhaul.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final View row;
        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/lato.ttf");
        final ViewHolder viewHolder;

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
            viewHolder.tv_book = (com.rey.material.widget.TextView) row.findViewById(R.id.textview_book_driver);
            viewHolder.tv_delete = (com.rey.material.widget.TextView) row.findViewById(R.id.textview_delete);


            viewHolder.tv_pickup.setTypeface(tf);
            viewHolder.tv_drop.setTypeface(tf);
            viewHolder.tv_date.setTypeface(tf);
            viewHolder.tv_date_txt.setTypeface(tf);
            viewHolder.tv_drop_txt.setTypeface(tf);
            viewHolder.tv_pick_txt.setTypeface(tf);
            viewHolder.tv_book.setTypeface(tf);
            viewHolder.tv_delete.setTypeface(tf);

            row.setTag(viewHolder);


        } else {
            row = convertView;
            viewHolder = (ViewHolder) row.getTag();
        }



        viewHolder.tv_pickup.setText(mv_datas.getPickup());
        viewHolder.tv_drop.setText(mv_datas.getDrop());
        viewHolder.tv_date.setText(mv_datas.getTime());


        viewHolder.tv_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mv_datas = ar_job_list.get(position);
                Log.e("tag","id:"+mv_datas.getBooking_id());
                booking_id = mv_datas.getBooking_id();

                editor.putString("job_id", booking_id);
                editor.commit();

                Intent i = new Intent(context, MyJobs.class);
                activity.startActivity(i);

            }
        });
////
        viewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mv_datas = ar_job_list.get(position);
                Log.e("tag","deL_id:"+mv_datas.getBooking_id());
            }
        });


        return row;
    }



    class ViewHolder {

        TextView tv_pickup,tv_drop,tv_date,tv_pick_txt,tv_drop_txt,tv_date_txt,tv_book,tv_delete;

    }



}