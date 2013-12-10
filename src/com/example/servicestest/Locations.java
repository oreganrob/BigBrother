package com.example.servicestest;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

public class Locations {
	
	// locations table name
    private static final String TABLE_LOCATIONS = "locations";
 
    // locations Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGTITUDE = "longtitude";
    private static final String KEY_TIME = "time";
    private static final String KEY_TYPE = "type";
    private static final String KEY_JOURNEY = "journey_id";
    
    public static int journeyId = 0;

	public static void createTable(SQLiteDatabase db) {
		db.execSQL(
        	"CREATE TABLE " + TABLE_LOCATIONS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
        	+ KEY_LATITUDE + " REAL,"
            + KEY_LONGTITUDE + " REAL, "
            + KEY_TYPE + " TEXT, "
            + KEY_JOURNEY + " INTEGER,"
            + KEY_TIME + " INTEGER "            
            //+ KEY_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            //+ KEY_TIME + " TIMESTAMP DEFAULT (strftime('%f', 'now')), "
            //+ KEY_TIME + " TIMESTAMP, "            
            + ");");
            //+ "CREATE INDEX journey_index ON " + 
            //	TABLE_LOCATIONS + "("+KEY_JOURNEY+");");		
	}
	
	public static void dropTable(Context context) {
    	SQLiteDatabase db = DatabaseHandler.getInstance(context).
    		getMyWritableDatabase();
    	
    	// Drop older table if existed
    	Log.d("DB", "Dropping Table");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        
        // Create tables again
        createTable(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new location
    public static void addLocation(Location location, Context context) {
        SQLiteDatabase db = DatabaseHandler.getInstance(context).
        	getMyWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, location.getLatitude());
        values.put(KEY_LONGTITUDE, location.getLongitude());
        values.put(KEY_TIME, location.getTime());
        values.put(KEY_JOURNEY, journeyId);
 
        // Inserting Row
        db.insert(TABLE_LOCATIONS, null, values);
        db.close();
    }
    
    public static List<Location> getLocationsForJourney(int journeyId) {
    	return null;
    }
 
    // Getting single location
    /*public static Location getLocation(int id, Context context) {
        SQLiteDatabase db = DatabaseHandler.getInstance(context).
        	getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_LOCATIONS, new String[] {
                KEY_LATITUDE, KEY_LONGTITUDE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        Location location = new Location("");
        location.setLatitude(cursor.getDouble(0));
        location.setLongitude(cursor.getDouble(1));
        
        // return location
        return location;
    }*/
    
    public static List<Location> getAllLocations(Context context) {
    	return getAllLocations(context, 0);
    }
    
    public static void getActualDistanceTravelled(int journeyId) {
    	
    }
 
    // Getting All locations
    public static List<Location> getAllLocations(
    	Context context, int journeyId) {
        List<Location> locationList = new ArrayList<Location>();
        
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LOCATIONS;
        if(journeyId > 0) {
        	selectQuery += " WHERE "+KEY_JOURNEY+"="+journeyId;
        }
 
        SQLiteDatabase db = DatabaseHandler.getInstance(context).
        	getMyWritableDatabase(); // correct???
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Location location = new Location("");
            	location.setLatitude(cursor.getDouble(1));
            	location.setLongitude(cursor.getDouble(2));
            	location.setTime(cursor.getLong(5));
            	
            	// Adding location to list
            	locationList.add(location);
            } while (cursor.moveToNext());
        }
 
        // return location list
        return locationList;
    }
 
    // Updating single location
    /*public static int updateLocation(Location location, Context context) {
        SQLiteDatabase db = DatabaseHandler.getInstance(context).
        	getMyWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, location.getLatitude());
        values.put(KEY_LONGTITUDE, location.getLongitude());
 
        // updating row
        return db.update(TABLE_LOCATIONS, values, KEY_ID + " = ?",
        	new String[] { String.valueOf(location.getId()) });
    }
 
    // Deleting single location
    public static void deleteLocation(Location location, Context context) {
        SQLiteDatabase db = DatabaseHandler.getInstance(context).
        	getMyWritableDatabase();
        db.delete(TABLE_LOCATIONS, KEY_ID + " = ?",
                new String[] { String.valueOf(location.getId()) });
        db.close();
    }*/
 
    // Getting locations Count
    public static int getLocationsCount(Context context, int journeyId) {
    	SQLiteDatabase db = DatabaseHandler.getInstance(context).
            getReadableDatabase();    	
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_LOCATIONS;
        if(journeyId > 0) {
        	countQuery += " WHERE "+KEY_JOURNEY+"="+journeyId;
        }
        Cursor cursor = db.rawQuery(countQuery, null);
        
        // return count
        cursor.moveToFirst();
        return cursor.getInt(0);
    }
    
    public static Location getJourneyStartPoint(Context context, int journeyId) {
    	String countQuery = "SELECT * FROM " + TABLE_LOCATIONS +
    		" WHERE "+KEY_JOURNEY+"="+journeyId+
    		" ORDER BY "+KEY_TIME+" ASC LIMIT 1";
        SQLiteDatabase db = DatabaseHandler.getInstance(context).
        	getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
            
        Location location = null;
        if (cursor.moveToFirst()) {
        	location = new Location("");
        	location.setLatitude(cursor.getDouble(1));
        	location.setLongitude(cursor.getDouble(2));
        	location.setTime(cursor.getLong(5));
        }
        
        return location;
    }
    
    // TODO DRY!
    public static Location getJourneyEndPoint(Context context, int journeyId) {
    	String countQuery = "SELECT * FROM " + TABLE_LOCATIONS +
    		" WHERE "+KEY_JOURNEY+"="+journeyId+
    		" ORDER BY "+KEY_TIME+" DESC LIMIT 1";
        SQLiteDatabase db = DatabaseHandler.getInstance(context).
        	getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
            
        Location location = null;
        if (cursor.moveToFirst()) {
        	location = new Location("");
        	location.setLatitude(cursor.getDouble(1));
        	location.setLongitude(cursor.getDouble(2));
        	location.setTime(cursor.getLong(5));
        }
        
        return location;
    }
    
    public static int getJourneysCount(Context context) {
    	String countQuery = "SELECT MAX("+KEY_JOURNEY+") FROM " + TABLE_LOCATIONS;
        SQLiteDatabase db = DatabaseHandler.getInstance(context).
        	getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        
        // return count
        cursor.moveToFirst();
        return cursor.getInt(0);
    }
}
