package com.example.servicestest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class LogActivity extends Activity {

	public boolean started = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
		
		ListView listview = (ListView) findViewById(R.id.listview);  
		listview.setAdapter(new DataAdapter(this));
		
		// set list click listener for list
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // switch to the map to display the journey
				switchToMap(position+1);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_log, menu);
		return true;
	}
	
	public void startJourney(MenuItem m) {    	
    	// set journey id
    	if(!started) {
    		started = true;
	    	Locations.journeyId = Locations.getJourneysCount(this)+1;
	    	
	    	// create service
	    	Intent mIntent = new Intent(this, FirstService.class);
			mIntent.putExtra("minTime", 0); // TODO
			mIntent.putExtra("minDistance", 0); // TODO
	    	startService(mIntent);
    	} else {
    		Toast.makeText(this,"Journey already started",300).show();
    	}
    }
	
	public void endJourney(MenuItem m) {
		stopService(new Intent(this, FirstService.class));
	}
	
	public void switchToMap(MenuItem m) {
    	Intent i = new Intent(getApplicationContext(), BigBrother.class);
    	i.putExtra("journeyId", 0);
		startActivity(i);
    }
	
	public void switchToMap(int journeyId) {
    	Intent i = new Intent(getApplicationContext(), BigBrother.class);
    	i.putExtra("journeyId", journeyId);
		startActivity(i);
    }
    
    public void switchToSettings(MenuItem m) {
    	Intent i = new Intent(getApplicationContext(), MainActivity.class);    	
		startActivity(i);
    }
    
    public void onItemClickListener() {
    	
    }
}
