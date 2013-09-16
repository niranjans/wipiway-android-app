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

    public static final String TABLE_ACTION_STATUS = "action_status";
    public static final String TABLE_ACTION_LOG = "action_log";
    
    // Columns for action_status Table
    public static final String C_ID = "id";
    public static final String C_PHONE_NUMBER = "phone_number";
    public static final String C_LAST_MESSAGE_RECEIVED = "message_received";	// unix time (integer)
    public static final String C_MODE = "mode";
    public static final String C_STAGE = "stage";
    public static final String C_ACTION = "action";
    public static final String C_ARGUMENT = "argument";
    
    // Columns for action_log
    public static final String C_LOG_TEXT = "log_text";
    public static final String C_ACTION_PERFORMED_DATE = "action_performed_date";
    

    // Create table statement
    public static final String TABLE_ACTION_STATUS_CREATION = "create table " + TABLE_ACTION_STATUS 
    					+ " (" + C_ID + " integer primary key autoincrement, " 
    					+ 		 C_PHONE_NUMBER + " text,"
    					+		 C_LAST_MESSAGE_RECEIVED + " integer not null,"
    					+		 C_MODE + " integer,"
    					+		 C_STAGE + " integer,"
    					+		 C_ACTION + " integer,"
    					+		 C_ARGUMENT + " text"
    					+ " );";
    
    // Create table statement
    public static final String TABLE_ACTION_LOG_CREATION = "create table " + TABLE_ACTION_LOG 
			+ " (" + C_ID + " integer primary key," 
    		+ 		 C_LOG_TEXT + " text,"
			+		 C_ACTION_PERFORMED_DATE + " integer not null,"
			+		 "FOREIGN KEY(" + C_ID + ") REFERENCES " +  TABLE_ACTION_STATUS + "( " + C_ID + ")"
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
		db.execSQL(TABLE_ACTION_STATUS_CREATION);
		db.execSQL(TABLE_ACTION_LOG_CREATION);
		
		Log.d(TAG, "Tables created!");
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Migrate data
		
		// Drop tables
		db.execSQL("DROP TABLE " + TABLE_ACTION_LOG + ";");
		db.execSQL("DROP TABLE " + TABLE_ACTION_STATUS + ";");
		onCreate(db);
		
	}

}
