package net.sqindia.movehaul;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

import java.util.concurrent.TimeUnit;

/**
 * Created by sqindia on 27-10-2016.
 */

public class Job_review extends Activity {
    LinearLayout btn_back;
    Button btn_review;
    TextView tv_timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_review);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);

        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        btn_review = (Button) findViewById(R.id.btn_review);
        tv_timer = (TextView) findViewById(R.id.text_Timer);


        new CountDownTimer(100000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                if((TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)==4) &&
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))==55)
                {

                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    v.vibrate(500);
                }
                tv_timer.setText(""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));


            }

            public void onFinish() {
                tv_timer.setText("done!");
            }
        }.start();

        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Job_review.this,DriversList.class);
                startActivity(i);
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Job_review.this,DashboardNavigation.class);
                startActivity(i);
                finish();
            }
        });

/*
        new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {
                tv_timer.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                tv_timer.setText("done!");
            }
        }.start();
*/
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Job_review.this,DashboardNavigation.class);
        startActivity(i);
        finish();
    }
}
