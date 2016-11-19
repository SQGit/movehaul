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
    Button btn_continue;
    TextView tv_timer;
    public Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_review);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);

        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        tv_timer = (TextView) findViewById(R.id.text_Timer);

        btn_continue.setEnabled(false);

        new CountDownTimer(30000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                tv_timer.setText(""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                if((TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)==0) &&
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))==10)
                {

                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    v.vibrate(5000);
                }

            }

            public void onFinish() {
                //onClickVibrate();
                tv_timer.setText("done!");
                btn_continue.setEnabled(true);
            }
        }.start();

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //vibrator.cancel();
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

    }
    public void onClickVibrate()
    {
        //Set the pattern, like vibrate for 300 milliseconds and then stop for 200 ms, then
        //vibrate for 300 milliseconds and then stop for 500 ms. You can change the pattern and
        // test the result for better clarity.
        long pattern[]={0,300,200,300,500};

        // Start the vibration
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        //start vibration with repeated count, use -1 if you don't want to repeat the vibration
        vibrator.vibrate(pattern, 0);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Job_review.this,DashboardNavigation.class);
        startActivity(i);
        finish();
    }
}
