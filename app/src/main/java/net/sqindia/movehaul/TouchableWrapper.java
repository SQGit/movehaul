package net.sqindia.movehaul;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class TouchableWrapper extends FrameLayout {

    Context context;
    DashboardNavigation dash;

    public TouchableWrapper(Context context) {
        super(context);
        this.context = context;
       // dash = new DashboardNavigation();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        dash = new DashboardNavigation();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                DashboardNavigation.mMapIsTouched = true;
                Log.e("tagmap","enabled");
                dash.show_disable();

             //  Intent i = new Intent();
             //   i.setAction("appendChatScreenMsg");
              // context.sendBroadcast(i);

                break;

            case MotionEvent.ACTION_UP:
                DashboardNavigation.mMapIsTouched = false;
                Log.e("tagmap","disabled");
                dash.show_disable();
               // Intent ii = new Intent();
              //  ii.setAction("appendChatScreenMsg");
               // context.sendBroadcast(ii);
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}