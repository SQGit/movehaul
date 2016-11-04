package net.sqindia.movehaul;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

//asdf
public class Config {


    // static final String REG_URL = "http://oonbux.sqindia.net/";


    public static final String WEB_URL = "http://104.197.7.143:3030/";


    static final String REG_URL = "http://androidtesting.newlogics.in/";

    static final String CSS_URL = "http://oonbux.sqindia.net/region";


    public static final String GCM_PROJ_ID = "600977208793";
    static final String PKG_NAME = "sqindia.net.oonbux";
    static final String GCM_API_KEY = "AIzaSyA4QlRGMEdX-GPcFsC5VjzVxZqH5knJwFo";


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



}






