package com.example.servicestest;

import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class DatabaseHandler extends SQLiteOpenHelper {
	
	public final Context myContext;
	
    private static DatabaseHandler mInstance;
    private static SQLiteDatabase myWritableDb;
	
	private static String TAG = "BigBrother";
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "BigBrother";
    
    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }
    
    public static DatabaseHandler getInstance(Context context) {
    	if (mInstance == null) {
    		mInstance = new DatabaseHandler(context);    	
    	}
    	return mInstance;    	
    }
    
    public SQLiteDatabase getMyWritableDatabase() {
    	if ((myWritableDb == null) || (!myWritableDb.isOpen())) {    	
    		myWritableDb = this.getWritableDatabase();    	
    	}
    	return myWritableDb;
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	Log.d("DB", "Creating Tables");
    	Locations.createTable(db);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
    	dropTables();
 
        // Create tables again
        onCreate(db);
    }
    
    private void dropTables() {
    	Locations.dropTable(myContext);
    }
    
    @Override
    public void close() {    
        super.close();
        if (myWritableDb != null) {
            myWritableDb.close();
            myWritableDb = null;
        }
    }
}