package com.example.servicestest;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class FirstService extends Service {
	
	private static String TAG = "BigBrother";
	
	LocationManager lm;
    LocationListener ll;
    
    int minTime;
    int minDistance;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this,"Service Created",300).show();
    }

    @Override
    public void onDestroy() {    	
        super.onDestroy();
        lm.removeUpdates(ll);
        Toast.makeText(this,"Service Destroy",300).show();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Toast.makeText(this,"Service LowMemory",300).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {    	
        super.onStart(intent, startId);
        Toast.makeText(this,"Service Started",300).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	Toast.makeText(this,"Listening...",300).show();
    	
    	minTime = intent.getIntExtra("minTime", 0);
    	minDistance= intent.getIntExtra("minDistance", 0);
        
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ll = new MyLocationListener(getApplicationContext());
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
        	minTime, minDistance, ll);
        
        return super.onStartCommand(intent, flags, startId);
    } 
    
    // to call use... 
    /*//ThreadDemo td=new ThreadDemo();
    //    td.start();
    private class ThreadDemo extends Thread{
        @Override
        public void run() {
            super.run();
            try{
            sleep(1000);
            Log.d(TAG, "Logging...");
            } catch(Exception e) {
                e.getMessage();
            }
        }
    }*/
}