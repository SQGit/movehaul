package net.sqindia.movehaul;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

/**
 * Created by sqindia on 02-11-2016.
 */

public class Payment_Details extends Activity {
    Button btn_continue;
    LinearLayout btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_details);
        FontsManager.initFormAssets(this, "fonts/CATAMARAN-REGULAR.TTF");       //initialization
        FontsManager.changeFonts(this);

        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_back = (LinearLayout) findViewById(R.id.layout_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Payment_Details.this, DashboardNavigation.class);
                startActivity(i);
                finish();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Payment_Details.this,Payment_Card_Details.class);
                startActivity(i);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Payment_Details.this,DashboardNavigation.class);
        startActivity(i);
        finish();
    }
}
