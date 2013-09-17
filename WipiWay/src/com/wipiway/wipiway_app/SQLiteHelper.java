package com.wipiway.wipiway_app;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String TAG = "WipiwayDB";
	
	private static SQLiteHelper helperInstance;

	
    private static final String DATABASE_NAME = "wipiwayDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ACTION_HISTORY = "action_history";
    
    // Columns for action_status Table
    public static final String C_ID = "id";
    public static final String C_PHONE_NUMBER = "phone_number";
    public static final String C_INTENT = "intent";				// What was the intent of the user eg - call me / get location, etc
    public static final String C_ARGUMENT1 = "argument1";
    public static final String C_ARGUMENT2 = "argument2";
    public static final String C_LOG_TEXT = "log_text";
    public static final String C_ACTION_PERFORMED_DATE = "action_performed_date";		// Unix epoch time
    
    

    // Create table statement
    public static final String TABLE_ACTION_HISTORY_CREATION = "create table " + TABLE_ACTION_HISTORY 
    					+ " (" + C_ID + " integer primary key autoincrement, " 
    					+ 		 C_PHONE_NUMBER + " text,"
    					+		 C_ACTION_PERFORMED_DATE + " integer not null,"
    					+		 C_INTENT + " integer,"
    					+		 C_ARGUMENT1 + " text,"
    					+		 C_ARGUMENT2 + " text,"
    					+		 C_LOG_TEXT + "text"
    					+ " );";
    
	 
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION); 
		
	}

	// To maintain 1 connection from different threads - http://touchlabblog.tumblr.com/post/24474750219/single-sqlite-connection
    public static synchronized SQLiteHelper getHelper(Context context)
    {
        if (helperInstance == null) {
        	helperInstance = new SQLiteHelper(context);
        }
        	
        return helperInstance;
    }
    
    // Close everything when object being destroyed
    public void finalize() throws Throwable {
        if(helperInstance != null)
        	helperInstance.close();
           super.finalize();
    }

    
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create tables
		db.execSQL(TABLE_ACTION_HISTORY_CREATION);

		Log.d(TAG, "Tables created!");
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Migrate data
		
		// Drop tables
		db.execSQL("DROP TABLE " + TABLE_ACTION_HISTORY + ";");
		onCreate(db);
		
	}

}
