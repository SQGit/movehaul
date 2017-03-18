package net.sqindia.movhaul;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.sloop.fonts.FontsManager;

import java.util.ArrayList;

/**
 * Created by SQINDIA on 10/27/2016.
 */

public class Payment_Adapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    Typeface tf;
    ArrayList<String> myList;
    Activity act;



    public Payment_Adapter(Activity activity, ArrayList array_list) {

        this.context = activity.getApplicationContext();
        this.myList = array_list;
        inflater = LayoutInflater.from(this.context);
        act =activity;

    }

    @Override
    public int getCount() {
        return 5;
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
        final TextView tv_vin_no, tv_vin_make, tv_add;

        FontsManager.initFormAssets(act, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(act);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.payment_adapter, viewGroup, false);
            mViewHolder = new MyViewHolder(convertView);

           // tv_vin_no = (TextView) convertView.findViewById(R.id.textview_vin_no);
           // tv_vin_make = (TextView) convertView.findViewById(R.id.textview_vin_make);


            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();

            tv_vin_no = mViewHolder.tv_vin_no;
            tv_vin_make = mViewHolder.tv_vin_make;

        }


        return convertView;
    }


    private class MyViewHolder {
        TextView tv_vin_no, tv_vin_make;
        Spinner spin_start, spin_end;

        public MyViewHolder(View item) {
          //  tv_vin_no = (TextView) item.findViewById(R.id.textview_vin_no);
           // tv_vin_make = (TextView) item.findViewById(R.id.textview_vin_make);

        }
    }
}
