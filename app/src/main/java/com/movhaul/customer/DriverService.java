package com.movhaul.customer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Salman on 22-03-2017.
 */

public class DriverService extends Service {

    private  String TAG = "tagss";

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        Log.e(TAG, "FirstService started");
        DashboardNavigation.geee();
        //this.stopSelf();
    }



    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.e(TAG, "FirstService destroyed");
    }

}
