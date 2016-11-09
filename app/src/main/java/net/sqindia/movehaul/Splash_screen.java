package net.sqindia.movehaul;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rey.material.widget.Button;
import com.sloop.fonts.FontsManager;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by sqindia on 21-10-2016.
 */
public class Splash_screen extends Activity {
    Button btn_register, btn_login;
    ImageView truck_icon, logo_icon, bg_icon;
    LinearLayout lt_bottom;
    boolean isBottom = true;
    int is = 0;
    Config config;
    AVLoadingIndicatorView av_loader;
    LinearLayout lt_top;
    Snackbar snackbar;
    Typeface tf;

    public static int getDeviceWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        return width;
    }

    public static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int height = display.getHeight();
        return height;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);


        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");


        config = new Config();

        getDeviceWidth(this);
        Log.e("tag", "width0" + getDeviceWidth(this));
        final float width = getDeviceWidth(this);
        float widthby = (float) (getDeviceWidth(this) / 3.4);
        Log.e("tag", "width1" + widthby);


        lt_top = (LinearLayout) findViewById(R.id.top);

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);
        truck_icon = (ImageView) findViewById(R.id.truck_icon);
        bg_icon = (ImageView) findViewById(R.id.bg_icon);
        logo_icon = (ImageView) findViewById(R.id.logo_ico);
        lt_bottom = (LinearLayout) findViewById(R.id.layout_bottom);
        av_loader = (AVLoadingIndicatorView) findViewById(R.id.loader);

        av_loader.setVisibility(View.GONE);


        snackbar = Snackbar
                .make(lt_top, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Open Settings", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // snackbar.dismiss();
                        finish();
                    }
                });

// Changing message text color
        snackbar.setActionTextColor(Color.RED);


// Changing action button text color
        View sbView = snackbar.getView();
        android.widget.TextView textView = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);

        android.widget.TextView textView1 = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_action);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(tf);
        textView1.setTypeface(tf);


        //img_loading = (ImageView) findViewById(R.id.imageview);

        /*RotateAnimation rotate = new RotateAnimation(-90, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setRepeatMode(Animation.REVERSE);
        img_loading.startAnimation(rotate);*/

        //  ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(truck_icon, "X", width);
        //scaleAnim.setDuration(3000);
        //scaleAnim.setRepeatCount(ValueAnimator.INFINITE);
        //scaleAnim.setRepeatMode(ValueAnimator.REVERSE);
        // scaleAnim.start();


        //TranslateAnimation animation = new TranslateAnimation(0, 500, 0, 0);
        // animation.setDuration(1500);
        // animation.setFillAfter(false);

        //  truck_icon.startAnimation(animation);

        truck_icon.animate().translationX(width / (float) 1.65).setDuration(1700).withLayer();

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1500);

        final Animation fadeOut = new AlphaAnimation(1, 0);
        //fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1700);

        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        bg_icon.setAnimation(fadeIn);
        logo_icon.setAnimation(fadeIn);
        //lt_bottom.setAnimation(fadeIn);

        final float height = getDeviceHeight(this);

        final TranslateAnimation anim_btn_b2t = new TranslateAnimation(0, 0, height + lt_bottom.getHeight(), lt_bottom.getHeight());
        anim_btn_b2t.setDuration(1400);
        lt_bottom.setAnimation(anim_btn_b2t);


        final TranslateAnimation anim_btn_t2b = new TranslateAnimation(0, 0, lt_bottom.getHeight(), height + lt_bottom.getHeight());
        anim_btn_t2b.setDuration(1700);
        anim_btn_t2b.setFillAfter(false);

        final TranslateAnimation anim_truck_c2r = new TranslateAnimation(0, width, 0, 0);
        anim_truck_c2r.setDuration(2000);
        anim_truck_c2r.setFillAfter(false);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new check_internet().execute();
            }
        }, 1300);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                lt_bottom.startAnimation(anim_btn_t2b);
                truck_icon.startAnimation(anim_truck_c2r);
                bg_icon.setAnimation(fadeOut);

                // truck_icon.animate().translationX(width+400).setDuration(1700).withLayer();
                // bg_icon.animate().translationY(100).setDuration(1700);
                // lt_bottom.animate().translationY(400).setDuration(1700).withLayer();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent isd = new Intent(Splash_screen.this, LoginActivity.class);
                        Bundle bndlanimation =
                                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim1, R.anim.anim2).toBundle();
                        startActivity(isd, bndlanimation);


                        /*Intent intent = new Intent(Splash_screen.this, LoginActivity.class);
                        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(bg_icon, 0,
                                0, truck_icon.getWidth(), truck_icon.getHeight());
                        startActivity(intent, options.toBundle());*/

                    }
                }, 1000);

            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lt_bottom.startAnimation(anim_btn_t2b);
                truck_icon.startAnimation(anim_truck_c2r);
                bg_icon.setAnimation(fadeOut);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent isd = new Intent(Splash_screen.this, RegisterActivity.class);
                        Bundle bndlanimation =
                                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim1, R.anim.anim2).toBundle();
                        startActivity(isd, bndlanimation);

                    }
                }, 1000);

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("tag", "ds+" + is);
    }


    public class check_internet extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
            av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                boolean isconnected = config.isConnected(Splash_screen.this);

                if (isconnected) {
                    return "true";
                } else {
                    return "false";
                }

            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "net:" + s);

            if (s.equals("true")) {
                av_loader.setVisibility(View.GONE);
            } else if (s.equals("false")) {
                av_loader.setVisibility(View.GONE);
                snackbar.show();
            }


        }

    }

}
