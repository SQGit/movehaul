package net.sqindia.movehaul;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
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
public class SplashActivity extends Activity {
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
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TranslateAnimation anim_btn_b2t, anim_btn_t2b, anim_truck_c2r;
    Animation fadeIn, fadeOut;

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
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        editor = sharedPreferences.edit();
        config = new Config();

        final float width = getDeviceWidth(this);
        final float height = getDeviceHeight(this);
        float widthby = (float) (getDeviceWidth(this) / 3.4);

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
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        android.widget.TextView textView = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);

        android.widget.TextView textView1 = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_action);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(tf);
        textView1.setTypeface(tf);

        if (sharedPreferences.getString("login", "").equals("success")) {
            lt_bottom.setVisibility(View.GONE);
        }

        truck_icon.animate().translationX(width / (float) 1.65).setDuration(1700).withLayer();
        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1500);
        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(1700);

        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        bg_icon.setAnimation(fadeIn);
        logo_icon.setAnimation(fadeIn);



        anim_btn_b2t = new TranslateAnimation(0, 0, height + lt_bottom.getHeight(), lt_bottom.getHeight());
        anim_btn_b2t.setDuration(1400);
        lt_bottom.setAnimation(anim_btn_b2t);


        anim_btn_t2b = new TranslateAnimation(0, 0, lt_bottom.getHeight(), height + lt_bottom.getHeight());
        anim_btn_t2b.setDuration(1700);
        anim_btn_t2b.setFillAfter(false);

        anim_truck_c2r = new TranslateAnimation(0, width, 0, 0);
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


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent isd = new Intent(SplashActivity.this, LoginActivity.class);
                        Bundle bndlanimation =
                                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim1, R.anim.anim2).toBundle();
                        startActivity(isd, bndlanimation);

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

                        Intent isd = new Intent(SplashActivity.this, RegisterActivity.class);
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
        if (!config.isConnected(SplashActivity.this)) {
            snackbar.show();
        } else {
           snackbar.dismiss();
        }

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

                boolean isconnected = config.isConnected(SplashActivity.this);

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

                if (sharedPreferences.getString("login", "").equals("success")) {

                    lt_bottom.startAnimation(anim_btn_t2b);
                    truck_icon.startAnimation(anim_truck_c2r);
                    bg_icon.setAnimation(fadeOut);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent isd = new Intent(SplashActivity.this, DashboardNavigation.class);
                            Bundle bndlanimation =
                                    ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim1, R.anim.anim2).toBundle();
                            startActivity(isd, bndlanimation);


                        }
                    }, 1200);

                }



            } else if (s.equals("false")) {
                av_loader.setVisibility(View.GONE);
                snackbar.show();
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
