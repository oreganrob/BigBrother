package com.example.servicestest;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 
public class DataAdapter extends BaseAdapter {
	
       Context mContext;
       private LayoutInflater mInflater;
       private int numJourneys;
       private Location previousLocation = null;
       float totalDistance = 0;
       float totalTime = 0;
       
       public DataAdapter(Context c) {
           mContext=c;
           mInflater = LayoutInflater.from(c);
           
           numJourneys = Locations.getJourneysCount(c);
       }
       
       public int getCount() {
           return numJourneys;
       }
       
       public Object getItem(int position) {
           return position;
       }
       
       public long getItemId(int position) {
           return position;
       }
       
       public View getView(int position, View convertView, ViewGroup parent) {
    	   
          //ViewHolder holder = null;
          DecimalFormat df1 = new DecimalFormat("#.#####");
          DecimalFormat df2 = new DecimalFormat("#.##");
          
          if(convertView == null) {
             //convertView = mInflater.inflate(
            	//R.layout.customgrid, parent,false);
             LayoutInflater mInflater = (LayoutInflater) 
            	mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             convertView = mInflater.inflate(R.layout.customgrid, null);
          }
          
          TextView startTime;
          TextView endTime;
          TextView timeTaken;
          TextView distance;
          TextView speed;
          
          startTime=(TextView)convertView.findViewById(R.id.startTime);
          startTime.setPadding(10, 10, 10, 10);
          timeTaken=(TextView)convertView.findViewById(R.id.time);
          timeTaken.setPadding(10, 10, 10, 10);
          distance=(TextView)convertView.findViewById(R.id.distance);
          distance.setPadding(10, 10, 10, 10);
          speed=(TextView)convertView.findViewById(R.id.speed);
          speed.setPadding(10, 10, 10, 10);

          // get journey details
          position++;
          DateFormat outFormat = new SimpleDateFormat("d/M/y, HH:mm:ss", Locale.UK);
    	  outFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
          Date d = null;
          
          // journey start point
          Location startPoint = Locations.getJourneyStartPoint(mContext, position);
          if(startPoint != null) {
        	  // start time
        	  d = new Date(startPoint.getTime());
        	  if(d != null) {
        		  startTime.setText("Start: "+outFormat.format(d));
        	  }
          }
          
          // journey end point
          Location endPoint = Locations.getJourneyEndPoint(mContext, position);
          if(endPoint != null) {
        	  // time taken	 		  
	 		  float TTimeTaken = ((endPoint.getTime() - startPoint.getTime())/1000);
	 		  timeTaken.setText("Duration: "+df2.format(TTimeTaken/60));
	 		  
	 		  // distance
	 		  float tDistance = startPoint.distanceTo(endPoint);
	 		  distance.setText("Distance: "+df2.format(tDistance/1000)+"k");
	 		  
	 		  // speed
	 		  speed.setText("Speed: "+df2.format(
	 			  getAverageSpeed(tDistance, TTimeTaken))+"kph");
          }
          
          return convertView;
       }
       
       // TODO move to util class?
       private float getAverageSpeed(float distance, float timeTaken) {
    	   float distancePerSecond = distance/timeTaken;
    	   float distancePerMinute = distancePerSecond*60;
    	   float distancePerHour = distancePerMinute*60;
    	   float speed = distancePerHour > 0 ? (distancePerHour/1000) : 0;
       	
    	   return speed;
       }
}