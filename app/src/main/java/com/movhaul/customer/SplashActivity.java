package com.movhaul.customer;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rey.material.widget.Button;
import com.sloop.fonts.FontsManager;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by sqindia on 21-10-2016.
 */
public class SplashActivity extends Activity {
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    Button btn_register, btn_login, btn_call;
    ImageView truck_icon, logo_icon, bg_icon;
    LinearLayout lt_bottom;
    int is = 0;
    Config config;
    LinearLayout lt_top;
    Snackbar snackbar;
    Typeface tf;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TranslateAnimation anim_btn_b2t, anim_btn_t2b, anim_truck_c2r;
    Animation fadeIn, fadeOut;
    LayoutInflater mInflater;
    String currentVersion, playstoreVersion;
    Dialog dg_show_update;
    TextView tv_dg_txt, tv_dg_txt2;
    Button btn_dg_download;
    private FirebaseAnalytics mFirebaseAnalytics;

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
        setContentView(com.movhaul.customer.R.layout.splash_screen);


        FirebaseCrash.report(new Exception("Successfully Installed Customer..."));

        Log.e("tag", "In the onCreate() event");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        FontsManager.initFormAssets(this, "fonts/lato.ttf");
        FontsManager.changeFonts(this);
        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        editor = sharedPreferences.edit();
        config = new Config();


        dg_show_update = new Dialog(SplashActivity.this);
        dg_show_update.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dg_show_update.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dg_show_update.setCancelable(false);
        dg_show_update.setContentView(com.movhaul.customer.R.layout.dialog_road_confirm);

        btn_dg_download = (Button) dg_show_update.findViewById(com.movhaul.customer.R.id.button_yes);
        tv_dg_txt = (android.widget.TextView) dg_show_update.findViewById(com.movhaul.customer.R.id.textView_1);
        tv_dg_txt2 = (android.widget.TextView) dg_show_update.findViewById(com.movhaul.customer.R.id.textView_2);

        tv_dg_txt.setText("Hooray...!!!");
        tv_dg_txt2.setText("New Update available on PlayStore");
        btn_dg_download.setText("Download");

        tv_dg_txt.setTypeface(tf);
        tv_dg_txt2.setTypeface(tf);
        btn_dg_download.setTypeface(tf);

        btn_dg_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dg_show_update.dismiss();

                final String appPackageName = SplashActivity.this.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

            }
        });


        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            Log.e("Tag", "version:" + currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e("Tag", "err:" + e.toString());
        }






        // String locale = getResources().getConfiguration().locale.getDisplayCountry();
//
        // String locale1 = getResources().getConfiguration().locale.getISO3Country();
        //String locale2 = getResources().getConfiguration().locale.getDisplayName();


        // Log.e("tag","resourc: "+locale + locale2 +locale1);
        Locale loc = Locale.getDefault();

        Log.e("tag", "resourc: " + Locale.getDefault().getCountry());
        Log.e("tag", "resourc: " + Locale.getDefault().getDisplayCountry());
        Log.e("tag", "resourc: " + Locale.getDefault().getDisplayLanguage());
        Log.e("tag", "resourc: " + Locale.getDefault().getDisplayName());

        Log.e("tag", "lang:" + Locale.getDefault().getDisplayLanguage());
        Log.e("tag", "lang_code:" + Locale.getDefault().getLanguage());

        final float width = getDeviceWidth(this);
        final float height = getDeviceHeight(this);


        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FirebaseInstanceId.getInstance().getToken());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, android.os.Build.MODEL);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        lt_top = (LinearLayout) findViewById(com.movhaul.customer.R.id.top);
        btn_register = (Button) findViewById(com.movhaul.customer.R.id.btn_register);
        btn_login = (Button) findViewById(com.movhaul.customer.R.id.btn_login);
        truck_icon = (ImageView) findViewById(com.movhaul.customer.R.id.truck_icon);
        bg_icon = (ImageView) findViewById(com.movhaul.customer.R.id.bg_icon);
        logo_icon = (ImageView) findViewById(com.movhaul.customer.R.id.logo_ico);
        lt_bottom = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_bottom);

        btn_call = (Button) findViewById(com.movhaul.customer.R.id.button_call);
        btn_call.setVisibility(View.GONE);

        snackbar = Snackbar.make(lt_top, com.movhaul.customer.R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
                .setAction(com.movhaul.customer.R.string.open_settings, new View.OnClickListener() {
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

        if (!com.movhaul.customer.Config.isConnected(SplashActivity.this)) {
            snackbar.show();
        } else {
            new GetVersionCode().execute();
        }


        final ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(sbView,
                "backgroundColor",
                new ArgbEvaluator(),
                0xFF313131,
                0xff000000);
        backgroundColorAnimator.setDuration(1000);
        backgroundColorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        backgroundColorAnimator.setRepeatMode(ValueAnimator.REVERSE);
        backgroundColorAnimator.start();

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

        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) SplashActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:121"));
                if (ActivityCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.


                    final List<String> permissionsList = new ArrayList<>();
                    permissionsList.add(Manifest.permission.CALL_PHONE);


                    String message = "You need to grant access to Call Phones";


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                    }

                    return;
                }
                startActivity(callIntent);

            }
        });


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
                                ActivityOptions.makeCustomAnimation(getApplicationContext(), com.movhaul.customer.R.anim.anim1, com.movhaul.customer.R.anim.anim2).toBundle();
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
                                ActivityOptions.makeCustomAnimation(getApplicationContext(), com.movhaul.customer.R.anim.anim1, com.movhaul.customer.R.anim.anim2).toBundle();
                        startActivity(isd, bndlanimation);

                    }
                }, 1000);

            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (!Config.isConnected(SplashActivity.this)) {
            snackbar.show();
            btn_call.setVisibility(View.VISIBLE);
            lt_bottom.setVisibility(View.VISIBLE);
        } else {
            snackbar.dismiss();
            btn_call.setVisibility(View.GONE);

            if (Float.valueOf(currentVersion) < Float.valueOf(playstoreVersion)) {

                //show dialog
                dg_show_update.show();

            }
            else {

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
                                    ActivityOptions.makeCustomAnimation(getApplicationContext(), com.movhaul.customer.R.anim.anim1, com.movhaul.customer.R.anim.anim2).toBundle();
                            startActivity(isd, bndlanimation);
                        }
                    }, 1200);
                }
            }


        }

        Log.e("tag", "ds+" + is);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public class check_internet extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
            // av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                boolean isconnected = Config.isConnected(SplashActivity.this);

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
                // av_loader.setVisibility(View.GONE);

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
                                    ActivityOptions.makeCustomAnimation(getApplicationContext(), com.movhaul.customer.R.anim.anim1, com.movhaul.customer.R.anim.anim2).toBundle();
                            startActivity(isd, bndlanimation);


                        }
                    }, 1200);

                }


            } else if (s.equals("false")) {
                // av_loader.setVisibility(View.GONE);
                snackbar.show();
                btn_call.setVisibility(View.VISIBLE);
                lt_bottom.setVisibility(View.VISIBLE);
            }
        }

    }

    private class PhoneCallListener extends PhoneStateListener {

        String LOG_TAG = "tag_LOGGING 123";
        private boolean isPhoneCalling = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "Rining, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "Offline");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");


                if (isPhoneCalling) {

                    Intent i = new Intent(getApplicationContext(), SplashActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    isPhoneCalling = false;
                }

            }
        }
    }


    private class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override

        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {

                Log.e("tag", "https://play.google.com/store/apps/details?id=" + SplashActivity.this.getPackageName() + "&hl=it");

                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + SplashActivity.this.getPackageName() + "&hl=it").timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();

                Log.e("Tag", "new: " + newVersion);

                return newVersion;

            } catch (Exception e) {

                Log.e("Tag", "dreerr: " + e.toString());
                return newVersion;

            }

        }


        @Override

        protected void onPostExecute(String onlineVersion) {


            Log.e("Tag", "OUTPUT: " + onlineVersion);

            super.onPostExecute(onlineVersion);

            playstoreVersion = onlineVersion;

            if (playstoreVersion != null && !playstoreVersion.isEmpty()) {

                if (Float.valueOf(currentVersion) < Float.valueOf(playstoreVersion)) {

                    //show dialog
                    dg_show_update.show();

                }

            }

            Log.e("update", "Current version " + currentVersion + "playstore version " + playstoreVersion);

        }
    }


}
