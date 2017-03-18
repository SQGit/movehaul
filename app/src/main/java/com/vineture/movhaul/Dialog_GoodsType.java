package com.vineture.movhaul;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
 */

public class Dialog_GoodsType extends Dialog {

    public Activity activity;
    TextView tv_header;
    ImageView img_back;
    Typeface tf;
    ArrayList<String> country_lists = new ArrayList<>();
    ArrayList<String> state_lists = new ArrayList<>();
    ArrayList<String> zip_lists = new ArrayList<>();
    ListAdapter adapter1, adapter2, adapter3;
    View div_view;
    Context context;
    Cursor cursor;
    String query, str_country, str_state, str_zip;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<String> ar_goods_type = new ArrayList<>();
    ListView lview_cont, lview_state, lview_zip;


    /*ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                tv_header.setText("Country");
                img_back.setVisibility(View.GONE);
                country_lists.clear();
                adapter1.notifyDataSetChanged();

            } else if (position == 1) {
                tv_header.setText("State");
                img_back.setVisibility(View.GONE);
                state_lists.clear();
                adapter2.notifyDataSetChanged();
            } else {
                tv_header.setText("Zip");
                img_back.setVisibility(View.GONE);
                zip_lists.clear();
                adapter3.notifyDataSetChanged();
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };*/
   // private ViewPager viewPager;
   // private int[] layouts;
    //private MyViewPagerAdapter myViewPagerAdapter;


    public Dialog_GoodsType(Activity activity, ArrayList<String> ar_goods) {
        super(activity);
        this.activity = activity;
        this.ar_goods_type = ar_goods;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.vineture.movhaul.R.layout.dialog_region);

        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/lato.ttf");




/*
        layouts = new int[]{
                R.layout.register_country,
       *//*         R.layout.register_state,*//*
                };*/








        lview_cont = (ListView) findViewById(com.vineture.movhaul.R.id.lview);
        adapter1 = new ListAdapter(activity.getApplicationContext(), com.vineture.movhaul.R.layout.dialog_vehicle_types, ar_goods_type);
        lview_cont.setAdapter(adapter1);


/*        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);


        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });*/

        // viewPager.beginFakeDrag();

       /* img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int current = 0;
                current = viewPager.getCurrentItem() - 1;
                // Do something after 5s = 5000ms
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);

                } else {
                    // launchHomeScreen();
                }

            }
        });*/


    }



  /*  public class MyViewPagerAdapter extends PagerAdapter {

        ListView lview_cont, lview_state, lview_zip;
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            if (position == 0) {

                lview_cont = (ListView) view.findViewById(R.id.lview);
                adapter1 = new ListAdapter(activity.getApplicationContext(), R.layout.dialog_vehicle_types, country_lists);
                lview_cont.setAdapter(adapter1);


            } else if (position == 1) {

                state_lists.clear();


                lview_state = (ListView) view.findViewById(R.id.lview);
                adapter2 = new ListAdapter(activity.getApplicationContext(), R.layout.dialog_vehicle_types, state_lists);
                lview_state.setAdapter(adapter2);


            } else {

                lview_zip = (ListView) view.findViewById(R.id.lview);
                adapter3 = new ListAdapter(activity.getApplicationContext(), R.layout.dialog_vehicle_types, zip_lists);
                lview_zip.setAdapter(adapter3);

            }


            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }*/

    public class ListAdapter extends ArrayAdapter<String> {

        Context cc;
        ArrayList<String> data_lists;
        int resourceid;

        public ListAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
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
        public View getDropDownView(int posi, View convertView, ViewGroup parent) {
            return getCustomView(posi, convertView, parent);
        }

        @Override
        public View getView(int posi, View convertView, ViewGroup parent) {
            return getCustomView(posi, convertView, parent);
        }


        public View getCustomView(final int posi, View row, ViewGroup parent) {

            Typeface tf = Typeface.createFromAsset(cc.getAssets(), "fonts/lato.ttf");

            LayoutInflater inflater = (LayoutInflater) cc.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View arow = inflater.inflate(resourceid, parent, false);

            final TextView label = (TextView) arow.findViewById(com.vineture.movhaul.R.id.textview_header);

            div_view = arow.findViewById(com.vineture.movhaul.R.id.divider_view);

            if(posi == ar_goods_type.size()-1)
                div_view.setVisibility(View.GONE);

            final ImageView image = (ImageView) arow.findViewById(com.vineture.movhaul.R.id.image);

            label.setTypeface(tf);

            label.setText(data_lists.get(posi));
            Log.e("tag","s: "+data_lists.get(posi));

            arow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    image.setImageDrawable(cc.getResources().getDrawable(com.vineture.movhaul.R.mipmap.select_tick));

                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(cc);
                    editor = sharedPreferences.edit();

                    editor.putString("goods",label.getText().toString());
                    editor.commit();

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