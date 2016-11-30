package net.sqindia.movehaul;

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

import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Salman on 7/28/2016.
 */

public class Dialog_Region1 extends Dialog {

    public Activity activity;
    TextView tv_header;
    ImageView img_back;
    Typeface tf;
    ArrayList<String> country_lists = new ArrayList<>();
    ArrayList<String> state_lists = new ArrayList<>();
    ArrayList<String> zip_lists = new ArrayList<>();
    ListAdapter adapter1, adapter2, adapter3;

    Context context;
    Cursor cursor;
    String query, str_country, str_state, str_zip;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<String> ar_truck_type = new ArrayList<>();
    HashMap<String, String> hash_subtype = new HashMap<String, String>();
    HashMap<String, String> hash_truck_imgs = new HashMap<String, String>();
    ListView lview_cont, lview_state, lview_zip;


    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {

            } else {

            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    private ViewPager viewPager;
    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;


    public Dialog_Region1(Activity activity, ArrayList<String> ar_trucks, HashMap<String, String> hash_subtype, HashMap<String, String> hash_subimg) {
        super(activity);
        this.activity = activity;
        this.ar_truck_type = ar_trucks;
        this.hash_subtype = hash_subtype;
        this.hash_truck_imgs = hash_subimg;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_regions);

        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/lato.ttf");

        layouts = new int[]{
                R.layout.register_country,
                R.layout.register_state,
        };

        viewPager = (ViewPager) findViewById(R.id.view_pager);


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

        img_back.setOnClickListener(new View.OnClickListener() {
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
        });


    }


    public class MyViewPagerAdapter extends PagerAdapter {

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
                adapter1 = new ListAdapter(activity.getApplicationContext(), R.layout.dialog_region_txts, ar_truck_type);
                lview_cont.setAdapter(adapter1);


            } else {


                lview_state = (ListView) view.findViewById(R.id.lview);
                adapter2 = new ListAdapter(activity.getApplicationContext(), R.layout.dialog_region_txts, ar_truck_type);
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

        public ListAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            this.cc = context;
            this.data_lists = objects;
            this.resourceid = textViewResourceId;
        }

        @Override
        public int getCount() {
            return ar_truck_type.size();
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

            final TextView label = (TextView) arow.findViewById(R.id.textview_header);

            final ImageView image = (ImageView) arow.findViewById(R.id.image);

            label.setTypeface(tf);

            label.setText(data_lists.get(posi));
            Log.e("tag", "s: " + data_lists.get(posi));

            arow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    image.setImageDrawable(cc.getResources().getDrawable(R.mipmap.select_tick));

                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(cc);
                    editor = sharedPreferences.edit();

                    editor.putString("goods", label.getText().toString());
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