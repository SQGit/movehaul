package net.sqindia.movehaul;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rey.material.widget.LinearLayout;
import com.rey.material.widget.ListView;
import com.sloop.fonts.FontsManager;

import java.util.ArrayList;

/**
 * Created by sqindia on 01-11-2016.
 */

public class Payment extends Activity {
    ListView lv_payment_list;
    LinearLayout btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        FontsManager.initFormAssets(this, "fonts/CATAMARAN-REGULAR.TTF");       //initialization
        FontsManager.changeFonts(this);

        lv_payment_list = (ListView) findViewById(R.id.listview_payment);
        btn_back = (LinearLayout) findViewById(R.id.layout_back);

        final ArrayList<String> payment_arlist = new ArrayList<>();
       // ht_arlist = new ArrayList<>();

        Payment_Adapter adapter = new Payment_Adapter(Payment.this, payment_arlist);

        lv_payment_list.setAdapter(adapter);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Payment.this,DashboardNavigation.class);
                startActivity(i);
                finish();
            }
        });
    }
}
