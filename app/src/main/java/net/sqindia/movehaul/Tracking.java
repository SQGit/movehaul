package net.sqindia.movehaul;

import android.app.Activity;
import android.os.Bundle;

import com.sloop.fonts.FontsManager;

/**
 * Created by sqindia on 02-11-2016.
 */

public class Tracking extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracking);
        FontsManager.initFormAssets(this, "fonts/CATAMARAN-REGULAR.TTF");       //initialization
        FontsManager.changeFonts(this);
    }
}
