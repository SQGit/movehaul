package com.movhaul.customer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ramotion.foldingcell.FoldingCell;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;
import com.sloop.fonts.FontsManager;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
public class UpcomingAdapter extends ArrayAdapter<MV_Datas> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    Context context;
    ArrayList<MV_Datas> up_lists;
    Activity act;
    FoldingCell cell;
    MV_Datas mv_datas;
    TextView tv_title_date,tv_title_booking_id,tv_title_pickup,tv_title_drop;
    TextView tv_content_booking_id,tv_content_cost,tv_content_pickup,tv_content_drop,tv_content_dr_name,tv_content_dr_phone,tv_content_date,tv_content_time,tv_title_pickup_txt,tv_title_drop_txt,tv_content_pickup_txt,tv_content_drop_txt,tv_content_date_txt,tv_content_time_txt;
    ImageView iv_content_prof,iv_content_bg;
    Typeface tf;
    Button btn_cancel;


    public UpcomingAdapter(Context context,Activity acti, ArrayList<MV_Datas> objects) {
        super(context, 0, objects);
        this.act = acti;
        up_lists = objects;

    }

    @Override
    public int getCount() {
        return up_lists.size()-1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get item for selected view
        //Item item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
         cell = (FoldingCell) convertView;
        ViewHolder viewHolder;

        tf = Typeface.createFromAsset(act.getAssets(), "fonts/lato.ttf");

       FontsManager.initFormAssets(act, "fonts/lato.ttf");       //initialization
       FontsManager.changeFonts(act);

        mv_datas = up_lists.get(position+1);

        Log.e("tag","ds: "+mv_datas.getDate());

        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(com.movhaul.customer.R.layout.upcoming_trip_adapter, parent, false);

            viewHolder.btn_cancel = (Button) cell.findViewById(com.movhaul.customer.R.id.button_cancel);

            tv_title_date = (TextView) cell.findViewById(com.movhaul.customer.R.id.textview_title_date);
            tv_title_booking_id = (TextView) cell.findViewById(com.movhaul.customer.R.id.textview_title_booking_id);
            tv_title_pickup = (TextView) cell.findViewById(com.movhaul.customer.R.id.textview_title_pickup);
            tv_title_pickup_txt = (TextView) cell.findViewById(com.movhaul.customer.R.id.textview_title_pickup);
            tv_title_drop = (TextView) cell.findViewById(com.movhaul.customer.R.id.textview_title_drop);
            tv_title_drop_txt = (TextView) cell.findViewById(com.movhaul.customer.R.id.textview_title_drop);

            tv_title_date.setTypeface(tf);
            tv_title_booking_id.setTypeface(tf);
            tv_title_pickup.setTypeface(tf);
            tv_title_pickup_txt.setTypeface(tf);
            tv_title_drop.setTypeface(tf);
            tv_title_drop_txt.setTypeface(tf);



            tv_content_booking_id = (TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_booking_id);
            tv_content_cost = (TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_cost);
            tv_content_pickup = (TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_pickup);
            tv_content_drop = (TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_drop);
            tv_content_dr_name = (TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_name);
            tv_content_dr_phone = (TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_phone);
            tv_content_date = (TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_date);
            tv_content_time = (TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_time);
            iv_content_prof = (ImageView) cell.findViewById(com.movhaul.customer.R.id.imageview_content_profile);
            iv_content_bg = (ImageView) cell.findViewById(com.movhaul.customer.R.id.image_bg);

            btn_cancel = (Button) cell.findViewById(com.movhaul.customer.R.id.button_cancel);

            tv_content_pickup_txt = (TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_pickup_txt);
            tv_content_drop_txt = (TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_drop_txt);
            tv_content_date_txt = (TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_date_txt);
            tv_content_time_txt = (TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_time_txt);

            tv_content_booking_id.setTypeface(tf);
            tv_content_cost.setTypeface(tf);
            tv_content_pickup.setTypeface(tf);
            tv_content_pickup_txt.setTypeface(tf);
            tv_content_drop.setTypeface(tf);
            tv_content_drop_txt.setTypeface(tf);
            tv_content_dr_name.setTypeface(tf);
            tv_content_dr_phone.setTypeface(tf);
            tv_content_date.setTypeface(tf);
            tv_content_date_txt.setTypeface(tf);
            tv_content_time.setTypeface(tf);
            tv_content_time_txt.setTypeface(tf);
            btn_cancel.setTypeface(tf);

            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (unfoldedIndexes.contains(position)) {
            cell.unfold(true);
        } else {
            cell.fold(true);
        }


        tv_title_date.setText(mv_datas.getDate());
        tv_title_booking_id.setText(mv_datas.getBooking_id());
        tv_title_pickup.setText(mv_datas.getPickup());
        tv_title_drop.setText(mv_datas.getDrop());



        tv_content_booking_id.setText(mv_datas.getBooking_id());
        tv_content_cost.setText("$ "+mv_datas.getJob_cost());
        tv_content_pickup.setText(mv_datas.getPickup());
        tv_content_drop.setText(mv_datas.getDrop());
        tv_content_dr_name.setText(mv_datas.getName());
        tv_content_dr_phone.setText(mv_datas.getDriver_number());
        tv_content_date.setText(mv_datas.getDate());
        tv_content_time.setText(mv_datas.getTime());

        if(mv_datas.getVec_type().equals("Bus")){
            iv_content_bg.setImageResource(com.movhaul.customer.R.drawable.bus_profile_bg);
        }
        else{
            iv_content_bg.setImageResource(com.movhaul.customer.R.drawable.truck_ad);
        }

        Log.e("tag","d: "+mv_datas.getDriver_image());
        Log.e("tag","d: "+mv_datas.getName());

        Glide.with(act).load(Config.WEB_URL+"driver_details/"+mv_datas.getDriver_image()).into(iv_content_prof);





        viewHolder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag","buttonclick");
                cell.unfold((true));
            }
        });





        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        Button btn_cancel;

    }
}
