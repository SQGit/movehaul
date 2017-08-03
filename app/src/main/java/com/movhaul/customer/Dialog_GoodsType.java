package com.movhaul.customer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.rey.material.widget.TextView;

import java.util.ArrayList;
/**
 * Created by Salman on 7/28/2016.
 * goods type
 */
class Dialog_GoodsType extends Dialog {
    public Activity activity;
    Typeface tf;
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ArrayList<String> ar_goods_type = new ArrayList<>();
    Dialog_GoodsType(Activity activity, ArrayList<String> ar_goods) {
        super(activity);
        this.activity = activity;
        this.ar_goods_type = ar_goods;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.movhaul.customer.R.layout.dialog_region);
        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/lato.ttf");
        ListView lview_cont = (ListView) findViewById(R.id.lview);
        ListAdapter adapter1 = new ListAdapter(activity.getApplicationContext(), R.layout.dialog_vehicle_types, ar_goods_type);
        lview_cont.setAdapter(adapter1);
    }
    @SuppressWarnings({"UnusedParameters", "deprecation"})
    private class ListAdapter extends ArrayAdapter<String> {
        Context cc;
        ArrayList<String> data_lists;
        int resourceid;
        ListAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            this.cc = context;
            this.data_lists = objects;
            this.resourceid = textViewResourceId;
        }
        @Override
        public int getCount() {
            return ar_goods_type.size();
        }
        @Override
        public View getDropDownView(int posi, View convertView, @NonNull ViewGroup parent) {
            return getCustomView(posi, convertView, parent);
        }
        @NonNull
        @Override
        public View getView(int posi, View convertView, @NonNull ViewGroup parent) {
            return getCustomView(posi, convertView, parent);
        }
        View getCustomView(final int posi, View row, ViewGroup parent) {
            Typeface tf = Typeface.createFromAsset(cc.getAssets(), "fonts/lato.ttf");
            LayoutInflater inflater = (LayoutInflater) cc.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View arow = inflater.inflate(resourceid, parent, false);
            final TextView label = (TextView) arow.findViewById(com.movhaul.customer.R.id.textview_header);
            View div_view = arow.findViewById(R.id.divider_view);
            if(posi == ar_goods_type.size()-1)
                div_view.setVisibility(View.GONE);
            final ImageView image = (ImageView) arow.findViewById(com.movhaul.customer.R.id.image);
            label.setTypeface(tf);
            label.setText(data_lists.get(posi));
            Log.e("tag","s: "+data_lists.get(posi));
            arow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    image.setImageDrawable(cc.getResources().getDrawable(com.movhaul.customer.R.mipmap.select_tick));
                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(cc);
                    editor = sharedPreferences.edit();
                    editor.putString("goods",label.getText().toString());
                    editor.apply();
                    final Handler handler = new Handler();
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                          dismiss();
                        }
                    };
                    handler.postDelayed(runnable, 500);
                }
            });
            return arow;
        }
    }
}