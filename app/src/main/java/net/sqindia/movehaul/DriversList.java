package net.sqindia.movehaul;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drivers_list);

        lv_drv_list = (ListView) findViewById(R.id.listview_driver);
        iv_filter = (ImageView) findViewById(R.id.imgview_filter);
        lt_filter_dialog = (LinearLayout) findViewById(R.id.filter_dialog);
        btn_filter = (Button) findViewById(R.id.btn_filter);

        lt_filter_dialog.setVisibility(View.GONE);

        FontsManager.initFormAssets(DriversList.this, "fonts/CATAMARAN-REGULAR.TTF");       //initialization
        FontsManager.changeFonts(DriversList.this);

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

////*******Showing Filter Options *********/////
        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialog_filter.show();

                lt_filter_dialog.setVisibility(View.VISIBLE);
                lv_drv_list.setEnabled(false);
            }
        });

        //********* Filtering Truck, Price& Rating  *********/////////
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lt_filter_dialog.setVisibility(View.GONE);
                lv_drv_list.setEnabled(true);
            }
        });

       /* dialog_filter = new Dialog(DriversList.this);
        dialog_filter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_filter.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_filter.setCancelable(true);
        dialog_filter.setContentView(R.layout.dialog_filter_driver);*/



    }
}
