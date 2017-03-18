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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


/**
 * Created by Salman on 7/28/2016.
 */

public class Dialog_VehicleType extends Dialog {

    public Activity activity;
    TextView tv_header;
    ImageView img_back;
    Typeface tf;
    ArrayList<String> aaaa = new ArrayList<>();
    ArrayList<String> state_lists = new ArrayList<>();
    ArrayList<String> ar_sub_type = new ArrayList<>();
    ListAdapter adapter1, adapter2, adapter3;
    android.widget.RelativeLayout rll;
    Context context;
    Cursor cursor;
    String query, str_country, str_state, str_zip;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<String> ar_truck_type = new ArrayList<>();
    HashMap<String, String> hash_subtype = new HashMap<String, String>();
    HashMap<String, String> hash_truck_imgs = new HashMap<String, String>();
    ListView lview_cont, lview_state, lview_zip;
    ImageView iv_truck_ico,image;
    ArrayList<String> ar_truck_imgs = new ArrayList<>();
    ArrayList<String> ar_truck_iimgs;


    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                //iv_truck_ico.setImageDrawable(activity.getResources().getDrawable(R.drawable.truck_1));
               // image.setVisibility(View.GONE);
            } else {
                Log.e("tag","from listner"+hash_subtype.size());
                ar_sub_type.clear();
                String shard = sharedPreferences.getString("truck_type","");


                for (int i=0;i<hash_subtype.size();i++) {

                    Log.e("tag","ar :"+aaaa.get(i));
                    String asdf = aaaa.get(i);
                    String secc = null;
                    try {
                       // Log.e("tag","e "+hash_subtype.get(asdf));
                        secc = hash_subtype.get(asdf);

                        Log.e("tag","ss: "+sharedPreferences.getString("truck_type",""));
                        Log.e("tag","a : "+secc);

                        if(sharedPreferences.getString("truck_type","").equals(hash_subtype.get(asdf))){
                            Log.e("tag",i+" d: "+aaaa.get(i));
                            ar_sub_type.add(aaaa.get(i));
                        }

                    }
                    catch (Exception e){
                        Log.e("tag","sa: "+e.toString());
                    }


                }

                adapter2.notifyDataSetChanged();



            }
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {  }
        @Override
        public void onPageScrollStateChanged(int arg0) {  }
    };
    private ViewPager viewPager;
   // private WrappingViewPager viewpager;

    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;


    public Dialog_VehicleType(Activity activity, ArrayList<String> ar_trucks, HashMap<String, String> hash_subtype, HashMap<String, String> hash_subimg, ArrayList<String> ar_trucksss, ArrayList<String> truck_imgs) {
        super(activity);
        this.activity = activity;
        this.ar_truck_type = ar_trucks;
        this.hash_subtype = hash_subtype;
        this.hash_truck_imgs = hash_subimg;
        this.aaaa = ar_trucksss;
        this.ar_truck_imgs = truck_imgs;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.vineture.movhaul.R.layout.dialog_vehicletype);

        rll = (android.widget.RelativeLayout) findViewById(com.vineture.movhaul.R.id.rl);

        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/lato.ttf");

        layouts = new int[]{
                com.vineture.movhaul.R.layout.register_country,
                com.vineture.movhaul.R.layout.register_state,
        };

        viewPager = (ViewPager) findViewById(com.vineture.movhaul.R.id.view_pager);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = sharedPreferences.edit();



        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);









        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        viewPager.beginFakeDrag();



    }


    public class MyViewPagerAdapter extends PagerAdapter {

        ListView lview_cont, lview_state;
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);





            if (position == 0) {

                HashSet<String> listToSet = new HashSet<String>(ar_truck_type);
                ArrayList<String> ar_truck_types = new ArrayList<String>(listToSet);

                HashSet<String> listToSetimg = new HashSet<String>(ar_truck_imgs);
                Log.e("tag","ss"+listToSetimg.size());
                ar_truck_iimgs = new ArrayList<String>(listToSetimg);
                Log.e("tag","im"+ar_truck_iimgs.size());
                for(int i=0;i<ar_truck_iimgs.size();i++){
                    Log.e("tag","aa:"+ar_truck_iimgs.get(i));
                }
                lview_cont = (ListView) view.findViewById(com.vineture.movhaul.R.id.lview);
                adapter1 = new ListAdapter(activity.getApplicationContext(), com.vineture.movhaul.R.layout.dialog_truck_type, ar_truck_types,0);
                lview_cont.setAdapter(adapter1);



            } else {

                Log.e("tag","from_pager");
                lview_state = (ListView) view.findViewById(com.vineture.movhaul.R.id.lview);
                adapter2 = new ListAdapter(activity.getApplicationContext(), com.vineture.movhaul.R.layout.dialog_vehicle_types, ar_sub_type,1);
                lview_state.setAdapter(adapter2);



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
    }

    public class ListAdapter extends ArrayAdapter<String> {

        Context cc;
        ArrayList<String> data_lists;
        int resourceid;
        int page;
        View div_view;

        public ListAdapter(Context context, int textViewResourceId, ArrayList<String> objects,int page) {
            super(context, textViewResourceId, objects);
            this.cc = context;
            this.data_lists = objects;
            this.resourceid = textViewResourceId;
            this.page = page;

        }

        @Override
        public View getDropDownView(int posi, View convertView, ViewGroup parent) {
            return getCustomView(posi, convertView, parent);
        }

        @Override
        public View getView(int posi, View convertView, ViewGroup parent) {
            return getCustomView(posi, convertView, parent);
        }


        public View getCustomView(int posi, View row, ViewGroup parent) {

            Typeface tf = Typeface.createFromAsset(cc.getAssets(), "fonts/lato.ttf");
            LayoutInflater inflater = (LayoutInflater) cc.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View arow = inflater.inflate(resourceid, parent, false);

            final TextView label = (TextView) arow.findViewById(com.vineture.movhaul.R.id.textview_header);

            div_view = arow.findViewById(com.vineture.movhaul.R.id.divider_view);

            label.setTypeface(tf);

            if(page==0) {
                iv_truck_ico = (ImageView) arow.findViewById(com.vineture.movhaul.R.id.image_icon);
                //iv_truck_ico.setImageDrawable(cc.getResources().getDrawable(R.drawable.truck_1));
                Log.e("tag", posi+" w: "+Config.WEB_URL+"vehicle_types/"+ar_truck_iimgs.get(posi));
                Glide.with(cc).load(Config.WEB_URL+"vehicle_types/"+ar_truck_iimgs.get(posi)).into(iv_truck_ico);

            }
            else{
                image = (ImageView) arow.findViewById(com.vineture.movhaul.R.id.image);
            }


            if(page ==0){
                label.setText(data_lists.get(posi));
                Log.e("tag", "s: " + data_lists.get(posi));
            }
            else{
                label.setText(data_lists.get(posi));
                Log.e("tag", "sd: " + data_lists.get(posi));
            }

            if( posi == data_lists.size()-1)
                div_view.setVisibility(View.GONE);


            arow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Handler handler = new Handler();

                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            editor.putString("sub_truck_type", label.getText().toString());
                            editor.commit();
                            dismiss();
                        }
                    };

                    if(page ==0){
                        editor.putString("truck_type", label.getText().toString());
                        editor.commit();
                        Log.e("tag","edi: "+label.getText().toString());
                        viewPager.setCurrentItem(1);

                    }
                    else{
                        handler.postDelayed(runnable, 500);
                        image = (ImageView) arow.findViewById(com.vineture.movhaul.R.id.image);
                        image.setImageDrawable(cc.getResources().getDrawable(com.vineture.movhaul.R.mipmap.select_tick));
                       // Log.e("tag","po: "+posi);
                    }







                }
            });


            return arow;
        }
    }




}