package net.sqindia.movehaul;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;
import com.rey.material.widget.LinearLayout;
import com.rey.material.widget.ListView;
import com.rey.material.widget.TabIndicatorView;
import com.rey.material.widget.TextView;
import com.sloop.fonts.FontsManager;

import java.util.ArrayList;

/**
 * Created by SQINDIA on 10/26/2016.
 */

public class MyTrips extends AppCompatActivity {

    private ViewPager viewPager;
    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;
    TabIndicatorView tiv;
    ListView ht_lview;
    LinearLayout btn_back;
    ArrayList<String> ht_arlist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytrips);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");
        FontsManager.changeFonts(this);
        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyTrips.this, DashboardNavigation.class);
                startActivity(i);
                finish();
            }
        });

        layouts = new int[]{
                R.layout.current_trips,
                R.layout.history_trips,
                R.layout.upcoming_trips,};

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tiv = (TabIndicatorView) findViewById(R.id.tab_indicator);
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        tiv.setTabIndicatorFactory(new TabIndicatorView.ViewPagerIndicatorFactory(viewPager));


    }


    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
           /*FontsManager.initFormAssets(getApplicationContext(), "fonts/lato.ttf");       //initialization
            FontsManager.changeFonts((Activity) getApplicationContext());*/

            if (position == 0)
            {


            }
            else if (position == 1)
            {
                ht_lview = (ListView) view.findViewById(R.id.lview);
                ht_arlist = new ArrayList<>();
                HistoryAdapter adapter = new HistoryAdapter(MyTrips.this, ht_arlist);
                ht_lview.setAdapter(adapter);
            }
            else
            {
                android.widget.ListView up_lview;
                up_lview = (android.widget.ListView) view.findViewById(R.id.lview);
                final ArrayList<String> up_arlist = new ArrayList<>();
                final UpcomingAdapter up_adapter = new UpcomingAdapter(MyTrips.this,MyTrips.this, up_arlist);
                up_lview.setAdapter(up_adapter);
                up_lview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                up_lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l)
                    {
                        // toggle clicked cell state
                        ((FoldingCell) view).toggle(false);
                        // register in adapter that state for selected cell is toggled
                        up_adapter.registerToggle(pos);
                        Log.e("tag","clicked"+pos);

                    }
                });

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


        @Override
        public CharSequence getPageTitle(int position) {
            String title;

            if (position == 0) {
                title = "Current";
            } else if (position == 1) {
                title = "History";
            } else {
                title = "Upcoming";
            }

            return title;
        }



    }


    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {


            if (position == 0) {

                FontsManager.initFormAssets(MyTrips.this, "fonts/lato.ttf");       //initialization
                FontsManager.changeFonts(MyTrips.this);


            } else if (position == 1) {


            } else {


            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }


    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MyTrips.this,DashboardNavigation.class);
        startActivity(i);
        finish();
    }
}
