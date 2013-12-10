package com.example.servicestest;

import java.util.List;
import java.util.Random;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class BigBrother extends FragmentActivity implements LocationListener {
	
	GoogleMap googleMap;
	DatabaseHandler db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		centerMapOnMe();        
		//addMarkers();
		addJourneys();		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		Intent i;
		switch (item.getItemId()) {
	        case R.id.settings:
	        	i = new Intent(getApplicationContext(), MainActivity.class);
	    		startActivity(i);
	            return true;
	        case R.id.log:
	        	i = new Intent(getApplicationContext(), LogActivity.class);
	    		startActivity(i);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void addJourneys() {
		// add as polygons
		GoogleMap googleMap;
		googleMap = ((SupportMapFragment)(getSupportFragmentManager().
        	findFragmentById(R.id.map))).getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        
        // draw one specific journey?
        Bundle extras = getIntent().getExtras();
        int journeyId = 0;
        if (extras != null) {
        	journeyId = extras.getInt("journeyId");
        }
        
        if(journeyId > 0) {
        	drawJourney(journeyId);
        } else {
	        int numJourneys = Locations.getJourneysCount(this);
	        for(int i=1; i<=numJourneys; i++) {
	        	drawJourney(i);
	        }
        }
	}
	
	private void drawJourney(int journeyId) {
		List<Location> locations = 
			Locations.getAllLocations(this, journeyId);
    	
        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions journey = new PolylineOptions();
        for(Location location : locations) {
        	journey.add(new LatLng(location.getLatitude(), location.getLongitude()));
    	}
		
        // Set the paths colour and width        
        Random rnd = new Random(); 
        journey.color(Color.argb(255, rnd.nextInt(256), 
        	rnd.nextInt(256), rnd.nextInt(256)));
        journey.width(10);

        // Get back the mutable Polyline
        Polyline polyline = googleMap.addPolyline(journey);
	}
	
	// TODO better in separate class?
	private void addMarkers() {
		// add as pins
		List<Location> locations = 
			Locations.getAllLocations(this);
    	for(Location location : locations) {
    		addMarker(new LatLng(location.getLatitude(), location.getLongitude()));
    	}
	}
	 
    private void addMarker(LatLng latLng) {
    	GoogleMap googleMap;
        googleMap = ((SupportMapFragment)(getSupportFragmentManager().
        	findFragmentById(R.id.map))).getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("My Spot")
                .snippet("This is my spot!")
                .icon(BitmapDescriptorFactory
                	.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        //googleMap.getUiSettings().setCompassEnabled(true);
        //googleMap.getUiSettings().setZoomControlsEnabled(true);
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
    }
    
    // move to separate class?
    private void centerMapOnMe() {
    	// Getting Google Play availability status
        int status = GooglePlayServicesUtil.
        	isGooglePlayServicesAvailable(getBaseContext());
 
        // Showing status
        if(status!=ConnectionResult.SUCCESS) { // Google Play Services are not available
 
            int requestCode = 10;
            Dialog dialog = 
            	GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
 
        } else { // Google Play Services are available
 
            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = 
            	(SupportMapFragment) getSupportFragmentManager().
            		findFragmentById(R.id.map);
 
            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();
 
            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);
 
            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = 
            	(LocationManager) getSystemService(LOCATION_SERVICE);
           
            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();
 
            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);
 
            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);
 
            if(location!=null){
                onLocationChanged(location);
            }
            
            //locationManager.requestLocationUpdates(provider, 10000, 0, this);
            //locationManager.requestLocationUpdates(
            //	LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }
    
    @Override
    public void onLocationChanged(Location location) {
 
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
 
        // Showing the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
 
        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
 
        // Setting latitude and longitude in the TextView tv_location
        //((TextView) findViewById(R.id.tv_location))
        //	.setText("Count: "+Locations.getLocationsCount(getApplicationContext(), 0)
        //		+ ", Time: "+location.getTime());
        
        ((TextView) findViewById(R.id.tv_location))
    		.setText("Count: "+Locations.getLocationsCount(getApplicationContext(), 0)
    		+ ", Act Dist: ");
        
        // draw realtime path
        //addPolylines();
    }
 
    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}
