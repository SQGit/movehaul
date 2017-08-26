package com.movhaul.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

//configuration information and service call address
public class Config {
    public static final String WEB_URL = "http://104.197.80.225:3030/";
    public static final String WEB_URL_IMG ="http://104.197.80.225:8080/movehaul/assets/img/";
    public static final String WEB_URL_IMG_NEW ="http://104.197.80.225:3030/customerdetails/";
    public static boolean isStringNullOrWhiteSpace(String value) {
        if (value == null) {
            return true;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    public static boolean isNetworkAvailable(Context c1) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c1.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static boolean isConnected(Context context) {
        ConnectivityManager
                cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }
    public  Snackbar snackbar(Snackbar snack, final Activity activity, TextView tv_snack, Typeface tf){
            snack = Snackbar
                    .make(activity.findViewById(com.movhaul.customer.R.id.top), "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Open Settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });
            snack.setActionTextColor(Color.RED);
            View sbView = snack.getView();
            tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            android.widget.TextView textView = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_action);
            tv_snack.setTextColor(Color.WHITE);
            tv_snack.setTypeface(tf);
            textView.setTypeface(tf);
            //snack.show();
        return snack;
    }
}






