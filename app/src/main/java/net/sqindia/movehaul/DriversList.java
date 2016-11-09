package net.sqindia.movehaul;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;
import com.rey.material.widget.Button;
import com.rey.material.widget.ListView;
import com.sloop.fonts.FontsManager;

import java.util.ArrayList;

/**
 * Created by SQINDIA on 10/28/2016.
 */

public class DriversList extends AppCompatActivity {
    ListView lv_drv_list;
    ImageView iv_filter;
    Dialog dialog_filter;
    LinearLayout lt_filter_dialog;
    Button btn_filter;
    com.rey.material.widget.LinearLayout btn_back,btn_refresh;
    ImageView iv_close,iv_refresh;
    int i=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drivers_list);
        FontsManager.initFormAssets(DriversList.this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(DriversList.this);

        lv_drv_list = (ListView) findViewById(R.id.listview_driver);
        iv_filter = (ImageView) findViewById(R.id.imgview_filter);
        lt_filter_dialog = (LinearLayout) findViewById(R.id.filter_dialog);
        btn_filter = (Button) findViewById(R.id.btn_filter);
        iv_close = (ImageView) findViewById(R.id.button_close);
        iv_refresh = (ImageView) findViewById(R.id.refresh);

        btn_back = (com.rey.material.widget.LinearLayout) findViewById(R.id.layout_back);
        btn_refresh = (com.rey.material.widget.LinearLayout) findViewById(R.id.layout_refresh);

        lt_filter_dialog.setVisibility(View.GONE);

        final RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setInterpolator(new LinearInterpolator());


        final int height = getDeviceHeight(DriversList.this);









        final ArrayList<String> drv_arlist = new ArrayList<>();

        final DriversListAdapter drv_adapter = new DriversListAdapter(DriversList.this,DriversList.this, drv_arlist);
        lv_drv_list.setAdapter(drv_adapter);

        lv_drv_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lv_drv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                drv_adapter.registerToggle(pos);
                Log.e("tag","clicked"+pos);


            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DriversList.this, Job_review.class);
                startActivity(i);
                finish();
            }
        });
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DriversList.this, "Refresh", Toast.LENGTH_SHORT).show();
                iv_refresh.startAnimation(rotate);
            }
        });


////*******Showing Filter Options *********/////

        iv_close.setVisibility(View.GONE);

        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialog_filter.show();

                TranslateAnimation anim_btn_b2t = new TranslateAnimation(0, 0, height,0);
                anim_btn_b2t.setDuration(500);

                TranslateAnimation anim_btn_t2b = new TranslateAnimation(0, 0, 0,height);
                anim_btn_t2b.setDuration(500);


                if(i==0) {
                   // iv_filter.setImageDrawable(getResources().getDrawable(R.drawable.close_btn));
                    iv_filter.setImageResource(R.drawable.close_btn);


                    i=1;

                    lt_filter_dialog.setVisibility(View.VISIBLE);
                    lv_drv_list.setEnabled(false);
                    lt_filter_dialog.setAnimation(anim_btn_b2t);
                    btn_refresh.setVisibility(View.GONE);

                }
                else if(i ==1){
                   // iv_filter.setImageDrawable(getResources().getDrawable(R.drawable.filter_ico));
                    iv_filter.setImageResource(R.mipmap.ic_filter_ico);
                    i=0;

                    lt_filter_dialog.setVisibility(View.GONE);
                    lv_drv_list.setEnabled(true);
                    lt_filter_dialog.setAnimation(anim_btn_t2b);
                    btn_refresh.setVisibility(View.VISIBLE);
                    iv_filter.setVisibility(View.VISIBLE);
                }

            }
        });

        //********* Filtering Truck, Price& Rating  *********/////////
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lt_filter_dialog.setVisibility(View.GONE);
                lv_drv_list.setEnabled(true);

                TranslateAnimation anim_btn_t2b = new TranslateAnimation(0, 0, 0,height);
                anim_btn_t2b.setDuration(500);

                iv_filter.setImageResource(R.mipmap.ic_filter_ico);
                i=0;

                lt_filter_dialog.setAnimation(anim_btn_t2b);
                btn_refresh.setVisibility(View.VISIBLE);
                iv_filter.setVisibility(View.VISIBLE);


            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





            }
        });

       /* dialog_filter = new Dialog(DriversList.this);
        dialog_filter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_filter.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_filter.setCancelable(true);
        dialog_filter.setContentView(R.layout.dialog_filter_driver);*/



    }


    public static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int height = display.getHeight();
        return height;
    }
}
