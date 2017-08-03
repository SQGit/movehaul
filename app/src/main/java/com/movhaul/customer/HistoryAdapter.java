package com.movhaul.customer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.rey.material.widget.TextView;
import com.sloop.fonts.FontsManager;

import java.util.ArrayList;
/**
 * Created by SQINDIA on 10/27/2016.
 * customer history (finished and cancelled jobs will be listed).
 */
@SuppressWarnings("deprecation")
class HistoryAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    Context context;
    Typeface tf;
    private Activity act;
    private ArrayList<MV_Datas> history_lists;
    com.rey.material.widget.TextView id;
    MV_Datas mv_datas;
    HistoryAdapter(Activity activity, ArrayList<MV_Datas> objects) {
        this.context = activity.getApplicationContext();
        this.history_lists = objects;
        inflater = LayoutInflater.from(this.context);
        act = activity;
    }
    @Override
    public int getCount() {
        return history_lists.size();
    }
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final MyViewHolder mViewHolder;
        mv_datas = history_lists.get(i);
        FontsManager.initFormAssets(act, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(act);
        tf = Typeface.createFromAsset(act.getAssets(), "fonts/lato.ttf");
        if (convertView == null) {
            convertView = inflater.inflate(com.movhaul.customer.R.layout.history_adapter, viewGroup, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        mViewHolder.tv_date.setText(mv_datas.getDate());
        mViewHolder.tv_id.setText(mv_datas.getBooking_id());
        mViewHolder.tv_pickup.setText(mv_datas.getPickup());
        mViewHolder.tv_drop.setText(mv_datas.getDrop());
        Log.e("tag","jsts: "+mv_datas.getJob_status());
        if(mv_datas.getJob_status().equals("finished")){
            mViewHolder.tv_status.setText("completed");
            mViewHolder.tv_status.setTextColor(act.getResources().getColor(R.color.green));
        }
        else{
            mViewHolder.tv_status.setText("cancelled");
            mViewHolder.tv_status.setTextColor(act.getResources().getColor(R.color.gold));
        }
        return convertView;
    }
    private class MyViewHolder {
        com.rey.material.widget.TextView tv_date, tv_id, tv_pickup, tv_drop,tv_status;
        MyViewHolder(View item) {
            tv_date = (com.rey.material.widget.TextView) item.findViewById(R.id.textview_date);
            tv_id = (com.rey.material.widget.TextView) item.findViewById(R.id.textview_book_id);
            tv_pickup = (com.rey.material.widget.TextView) item.findViewById(com.movhaul.customer.R.id.textview_book_from);
            tv_drop = (com.rey.material.widget.TextView) item.findViewById(com.movhaul.customer.R.id.textview_book_to);
            tv_status = (TextView) item.findViewById(R.id.textview_sts);
            tv_date.setTypeface(tf);
            tv_id.setTypeface(tf);
            tv_pickup.setTypeface(tf);
            tv_drop.setTypeface(tf);
            tv_status.setTypeface(tf);
        }
    }
}
