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
import java.util.HashSet;
import java.util.List;
import java.util.Map;


/**
 * Created by Salman on 7/28/2016.
 */

public class Dialog_Region1 extends Dialog {

    public Activity activity;
    TextView tv_header;
    ImageView img_back;
    Typeface tf;
    ArrayList<String> aaaa = new ArrayList<>();
    ArrayList<String> state_lists = new ArrayList<>();
    ArrayList<String> ar_sub_type = new ArrayList<>();
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
    ImageView iv_truck_ico,image;


    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                //iv_truck_ico.setImageDrawable(activity.getResources().getDrawable(R.drawable.truck_1));
                image.setVisibility(View.GONE);
            } else {
                Log.e("tag","from listner"+hash_subtype.size());
                ar_sub_type.clear();
                String shard = sharedPreferences.getString("truck_type","");



                for (int i=0;i<hash_subtype.size();i++) {

                    Log.e("tag","ar :"+aaaa.get(i));
                    String asdf = aaaa.get(i);
                    String secc = null;
                   /* try {
                        Log.e("tag","s: "+hash_subtype.get(i));
                    }
                    catch (Exception e){
                        Log.e("tag","see: "+e.toString());
                    }*/

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





                    /*if (hash_subtype.get(aaaa.get(i)) == shard) {

                        Log.e("tag","ss: "+aaaa.get(i));

                         }*/
                }

                adapter2.notifyDataSetChanged();

               /* for (int i=0;i<hash_subtype.size();i++){
                   // Log.e("tag","hs: "+hash_subtype.get(sharedPreferences.getString("truck_type","")));



                    String key= null;
                    String value="somename";
                    for(Map.Entry<String,String> entry: hash_subtype.entrySet()){
                        if(shard.equals(entry.getValue())){
                            key = entry.getKey();
                            Log.e("tag","kk: "+key);
                            ar_sub_type.add(key);
                            break; //breaking because its one to one map
                        }
                    }*/


                    /*if(entry.getValue() == shard){
                        Log.e("tag","entry: "+entry.getKey());
                    }
                    else{
                        Log.e("tag","val: "+entry.getValue());

                    }*/

                   /* for (Map.Entry<String, String> entry : hash_subtype.entrySet()) {
                        if (entry.getValue().equals(sharedPreferences.getString("truck_type",""))) {
                            ar_sub_type.add(entry.getKey());
                            Log.e("tag", "hsa: " + ar_sub_type.get(i));
                        }
                    }*/

                  /*  Log.e("tag","has: "+hash_subtype.get(sharedPreferences.getString("truck_type","")));
                    Log.e("tag","sar: "+sharedPreferences.getString("truck_type",""));

                    if(hash_subtype.get(i) == sharedPreferences.getString("truck_type","")) {
                        ar_sub_type.add(hash_subtype.get(sharedPreferences.getString("truck_type", "")));
                        Log.e("tag", "hsa: " + ar_sub_type.get(i));
                    }*/



               // lview_state = (ListView) findViewById(R.id.lview);

              //  adapter2 = new ListAdapter(activity.getApplicationContext(), R.layout.dialog_region_txts1, ar_sub_type,1);
              //  lview_state.setAdapter(adapter2);




                iv_truck_ico.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);

            }
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {  }
        @Override
        public void onPageScrollStateChanged(int arg0) {  }
    };
    private ViewPager viewPager;
    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;


    public Dialog_Region1(Activity activity, ArrayList<String> ar_trucks, HashMap<String, String> hash_subtype, HashMap<String, String> hash_subimg, ArrayList<String> ar_trucksss) {
        super(activity);
        this.activity = activity;
        this.ar_truck_type = ar_trucks;
        this.hash_subtype = hash_subtype;
        this.hash_truck_imgs = hash_subimg;
        this.aaaa = ar_trucksss;

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

                lview_cont = (ListView) view.findViewById(R.id.lview);
                adapter1 = new ListAdapter(activity.getApplicationContext(), R.layout.dialog_region_txts1, ar_truck_types,0);
                lview_cont.setAdapter(adapter1);

            } else {

                Log.e("tag","from_pager");
                lview_state = (ListView) view.findViewById(R.id.lview);
                adapter2 = new ListAdapter(activity.getApplicationContext(), R.layout.dialog_region_txts1, ar_sub_type,1);
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

        public ListAdapter(Context context, int textViewResourceId, ArrayList<String> objects,int page) {
            super(context, textViewResourceId, objects);
            this.cc = context;
            this.data_lists = objects;
            this.resourceid = textViewResourceId;
            this.page = page;


        }

      /*  @Override
        public int getCount() {
            return ar_truck_type.size();
        }*/

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

            image = (ImageView) arow.findViewById(R.id.image);
            iv_truck_ico = (ImageView) arow.findViewById(R.id.image_icon);

            label.setTypeface(tf);

            if(page==0) {

                iv_truck_ico.setImageDrawable(cc.getResources().getDrawable(R.drawable.truck_1));
                image.setVisibility(View.GONE);
            }
            else{
                iv_truck_ico.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
            }


            if(page ==0){
                label.setText(data_lists.get(posi));
                Log.e("tag", "s: " + data_lists.get(posi));
            }
            else{
                label.setText(data_lists.get(posi));
                Log.e("tag", "sd: " + data_lists.get(posi));
            }


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
                        image.setImageDrawable(cc.getResources().getDrawable(R.mipmap.select_tick));
                    }







                }
            });


            return arow;
        }
    }


}