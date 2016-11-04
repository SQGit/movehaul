package net.sqindia.movehaul;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;
import com.sloop.fonts.FontsManager;

/**
 * Created by sqindia on 02-11-2016.
 */

public class Payment_Card_Details extends Activity {
    Button btn_paynow,btn_ok;
    Dialog dialog1;
    ImageView btn_close;
    TextView tv_dialog1,tv_dialog2,tv_dialog3,tv_dialog4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_carddetails);
        FontsManager.initFormAssets(this,"fonts/CATAMARAN-REGULAR.TTF");       //initialization
        FontsManager.changeFonts(this);
        btn_paynow = (Button) findViewById(R.id.btn_paynow);

        btn_paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        dialog1 = new Dialog(Payment_Card_Details.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.dialogue_job_posting);
        btn_ok = (Button) dialog1.findViewById(R.id.button_ok);
        btn_close = (ImageView) dialog1.findViewById(R.id.button_close);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                Intent i = new Intent(Payment_Card_Details.this, DashboardNavigation.class);
                startActivity(i);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Payment_Card_Details.this,Payment_Details.class);
        startActivity(i);
        finish();
    }
}
