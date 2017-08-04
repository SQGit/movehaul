package com.movhaul.customer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sloop.fonts.FontsManager;

import java.util.ArrayList;
/**
 * Created by SQINDIA on 10/27/2016.
 * payment
 */
class Payment_Adapter extends BaseAdapter {
    private LayoutInflater inflater;
    Context context;
    Typeface tf;
    private ArrayList<MV_Datas> payment_list;
    private Activity act;
    MV_Datas mv_datas;
    Payment_Adapter(Activity activity, ArrayList<MV_Datas> array_list) {
        this.context = activity.getApplicationContext();
        this.payment_list = array_list;
        inflater = LayoutInflater.from(this.context);
        act =activity;
    }
    @Override
    public int getCount() {
        return payment_list.size();
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final MyViewHolder mViewHolder;
        final TextView tv_booking_id,tv_date,tv_cost;
        FontsManager.initFormAssets(act, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(act);
        if (convertView == null) {
            convertView = inflater.inflate(com.movhaul.customer.R.layout.payment_adapter, viewGroup, false);
            mViewHolder = new MyViewHolder(convertView);
            tv_booking_id = (TextView) convertView.findViewById(R.id.textview_booking_id);
            tv_date = (TextView) convertView.findViewById(R.id.textview_date);
            tv_cost = (TextView) convertView.findViewById(R.id.textview_cost);
            tv_booking_id.setTypeface(tf);
            tv_date.setTypeface(tf);
            tv_cost.setTypeface(tf);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
            tv_booking_id = mViewHolder.tv_booking_id;
            tv_date = mViewHolder.tv_date;
            tv_cost = mViewHolder.tv_cost;
        }
        mv_datas = payment_list.get(position);
        tv_booking_id.setText(mv_datas.getBooking_id());
        tv_date.setText(mv_datas.getDate());
        tv_cost.setText("₦ "+mv_datas.getJob_cost());
        return convertView;
    }
    private class MyViewHolder {
        TextView tv_booking_id,tv_date,tv_cost;
        MyViewHolder(View item) {
            tv_booking_id = (TextView) item.findViewById(R.id.textview_booking_id);
            tv_date = (TextView) item.findViewById(R.id.textview_date);
            tv_cost = (TextView) item.findViewById(R.id.textview_cost);
        }
    }
}
