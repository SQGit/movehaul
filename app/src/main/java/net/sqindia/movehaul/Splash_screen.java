package net.sqindia.movehaul;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.rey.material.widget.Button;
import com.sloop.fonts.FontsManager;

/**
 * Created by sqindia on 21-10-2016.
 */
public class Splash_screen extends Activity {
    Button btn_register,btn_login;
    ImageView truck_icon,img_loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        FontsManager.initFormAssets(this, "fonts/CATAMARAN-REGULAR.TTF");       //initialization
        FontsManager.changeFonts(this);


        getDeviceWidth(this);
        Log.e("tag","width"+getDeviceWidth(this));
        float width = (float) (getDeviceWidth(this)/3.4);
        Log.e("tag","width"+width);


        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);
        truck_icon = (ImageView) findViewById(R.id.truck_icon);

        img_loading = (ImageView) findViewById(R.id.imageview);

        RotateAnimation rotate = new RotateAnimation(-90, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setRepeatMode(Animation.REVERSE);
        img_loading.startAnimation(rotate);

        ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(truck_icon, "X", width);
        scaleAnim.setDuration(3000);
        //scaleAnim.setRepeatCount(ValueAnimator.INFINITE);
        //scaleAnim.setRepeatMode(ValueAnimator.REVERSE);
        scaleAnim.start();



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Splash_screen.this,MyTrips.class);
                startActivity(i);
                finish();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Splash_screen.this, Register.class);
                startActivity(i);
                finish();
            }
        });
    }
    public static int getDeviceWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width=display.getWidth();
        return width;
    }
}
