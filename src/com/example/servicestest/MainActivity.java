package com.example.servicestest;

import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ReceiverCallNotAllowedException;
import android.location.Location;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity implements OnClickListener {
	
	private static String TAG = "BigBrother";
	private int minTime = 0;
	private int minDistance = 0;
	
	//public boolean started = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // button click listeners
        setButtonListeners();
        
        // seekbar listeners
        minTimeSeekBar();
        minDistanceSeekBar();
    }
    
    private void setButtonListeners() {
    	//findViewById(R.id.startJourney).setOnClickListener(this);
    	//findViewById(R.id.stopJourney).setOnClickListener(this);
        findViewById(R.id.locations).setOnClickListener(this);
        findViewById(R.id.journeys).setOnClickListener(this);
        findViewById(R.id.show).setOnClickListener(this);
        findViewById(R.id.empty).setOnClickListener(this);
    }
    
    // TODO Should be in separate class?
    private void minTimeSeekBar() {
    	SeekBar seekbar = (SeekBar) findViewById(R.id.minTimeBar);
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {		
        	public void onProgressChanged(
        		SeekBar seekBar, int progress, boolean fromUser) {
        		minTime = progress*1000;
        		((TextView) findViewById(R.id.minTimeText)).
        			setText("Min Time: "+progress);
		    }
		
		    public void onStartTrackingTouch(SeekBar seekBar) {
		    	// TODO Auto-generated method stub
		    }
		
		    public void onStopTrackingTouch(SeekBar seekBar) {
		    	// TODO Auto-generated method stub
		    }
		});
    }
    
    // TODO Should be in separate class?
    private void minDistanceSeekBar() {
    	SeekBar seekbar = (SeekBar) findViewById(R.id.minDistanceBar);
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {		
        	public void onProgressChanged(
        		SeekBar seekBar, int progress, boolean fromUser) {
        		minDistance = progress;
        		((TextView) findViewById(R.id.minDistanceText)).
		        	setText("Min Distance: "+progress);
		    }
		
		    public void onStartTrackingTouch(SeekBar seekBar) {
		    	// TODO Auto-generated method stub
		    }
		
		    public void onStopTrackingTouch(SeekBar seekBar) {
		    	// TODO Auto-generated method stub
		    }
		});
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        	/*case R.id.startJourney:
	        	startJourney();
        		break;
        	case R.id.stopJourney:
        		started = false;
        		stopService(new Intent(this, FirstService.class));
        		break;*/
	        case R.id.locations:
	        	getLocationCount();
	            break;
	        case R.id.journeys:
	        	getJourneysCount();
	            break;
	        case R.id.show:
	        	showAllLocations();
	            break;
	        case R.id.empty:
	        	emptyTable();
	            break;
	        default:
	        	Toast.makeText(this,"Unknown Click!",300).show();
        }
    }
    
    /*private void startJourney() {
    	
    	// set journey id
    	if(!started) {
    		started = true;
	    	Locations.journeyId = Locations.getJourneysCount(this)+1;
	    	
	    	// create service
	    	Intent mIntent = new Intent(this, FirstService.class);
			mIntent.putExtra("minTime", minTime);
			mIntent.putExtra("minDistance", minDistance);
	    	startService(mIntent);
    	} else {
    		Toast.makeText(this,"Journey already started",300).show();
    	}
    }*/
    
    private void getLocationCount() {
    	Toast.makeText(this,"Locations: "+
            Locations.getLocationsCount(this, 0),300).show();
    }
    
    private void getJourneysCount() {
    	Toast.makeText(this,"Journeys: "+
            Locations.getJourneysCount(this),300).show();
    }
    
    private void showAllLocations() {
    	List<Location> locations = Locations.getAllLocations(this);
    	
    	TextView text = (TextView) findViewById(R.id.locationsView);
    	text.setMovementMethod(new ScrollingMovementMethod());
    	text.setMaxLines(20);
    	    	
    	Location previousLocation = null;
    	float distance = 0;
    	float totalDistance = 0;
    	float totalTime = 0;
    	float timeTaken = 0;
    	DecimalFormat df1 = new DecimalFormat("#.#####");
    	DecimalFormat df2 = new DecimalFormat("#.##");
    	
    	// loop through and append each location
    	for(Location location : locations) {
    		
    		// work out distance from previous position
    		if(previousLocation != null) {
    			distance = location.distanceTo(previousLocation);
    			totalDistance += distance;
    		}
    		
    		// time between points
    		if(previousLocation != null) {
    			timeTaken = ((location.getTime() - previousLocation.getTime())/1000);
    			totalTime += timeTaken;
    		}
    		
    		if(distance > 0) {
	    		text.append(df1.format(location.getLatitude())
	    			+ ", "+df1.format(location.getLongitude())	    			
	    			+ ", "+df2.format((totalTime/60))+" ("
	    			+ df2.format(timeTaken)+"s)"
	    			+ ", "+df2.format(distance)+"m "
	    			+ df2.format(getAverageSpeed(distance, timeTaken))+"kmh\n");
    		}
    		
    		// set previous location
    		previousLocation = location;
    	}
    	
    	// update summary of all locations
    	TextView summary = (TextView) findViewById(R.id.summary);    	
    	summary.setText("Count: "+locations.size());
    	summary.append(", Dist: "+df2.format(totalDistance/1000));
    	summary.append(", Time: "+df2.format(totalTime/60));
    	summary.append(", Speed: "+df2.format(
    		getAverageSpeed(totalDistance, totalTime)));
    }
    
    // TODO move to util class?
    private float getAverageSpeed(float distance, float timeTaken) {
    	float distancePerSecond = distance/timeTaken;
		float distancePerMinute = distancePerSecond*60;
		float distancePerHour = distancePerMinute*60;
		float speed = distancePerHour > 0 ? (distancePerHour/1000) : 0;
    	
    	return speed;
    }
    
    // TODO move to Locations class?
    private void emptyTable() {
    	Locations.dropTable(this);    	
    	
    	Toast.makeText(this,"Table Emptied...",300).show();
    	getLocationCount();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void switchToMap(MenuItem m) {
    	Intent i = new Intent(getApplicationContext(), BigBrother.class);    	
		startActivity(i);
    }
    
    public void switchToLog(MenuItem m) {
    	Intent i = new Intent(getApplicationContext(), LogActivity.class);    	
		startActivity(i);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }	
}