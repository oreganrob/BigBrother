package com.example.servicestest;

import com.example.servicestest.DatabaseHandler;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/* Class My Location Listener */
public class MyLocationListener implements LocationListener
{
	private static String TAG = "BigBrother";
	DatabaseHandler db;
	Context context;
	
	public MyLocationListener(Context context) {
		this.context = context;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		Locations.addLocation(location, context);
	}
		
	@Override
	public void onProviderDisabled(String provider) {	
		/*Toast.makeText(context,	
			“Gps Disabled”,	Toast.LENGTH_SHORT ).show();*/
	}
	
	@Override	
	public void onProviderEnabled(String provider) {	
		/*Toast.makeText(context,
				“Gps Enabled”, Toast.LENGTH_SHORT).show();*/
	}
	
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}