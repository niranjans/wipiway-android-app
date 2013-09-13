package com.wipiway.wipiway_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

// Help from http://www.vogella.com/articles/AndroidSQLite/article.html

public class WipiwayDataSource {

	//private SQLiteDatabase database;
	private WipiwaySQLiteHelper dbHelper;
	
	public WipiwayDataSource(Context context) {
	    dbHelper = WipiwaySQLiteHelper.getHelper(context);
	  }
	
//	public void open() throws SQLException {
//	   //  database = dbHelper.getWritableDatabase();
//	  }
//	
//	public void close() {
//	    dbHelper.close();
//	  }
	

	
	public void insertActionStatus(String phoneNumber, int mode, int stage, int action, String argument) {
	
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(WipiwaySQLiteHelper.C_PHONE_NUMBER, phoneNumber);
		values.put(WipiwaySQLiteHelper.C_LAST_MESSAGE_RECEIVED, System.currentTimeMillis()); 	// Current time
		values.put(WipiwaySQLiteHelper.C_MODE, mode);
		values.put(WipiwaySQLiteHelper.C_STAGE, stage);
		values.put(WipiwaySQLiteHelper.C_ACTION, action);
		values.put(WipiwaySQLiteHelper.C_ARGUMENT, argument);
		
		database.insert(WipiwaySQLiteHelper.TABLE_ACTION_STATUS, null, values);
		
	}
	
	public void updateActionStatus( int id, int mode, int stage, int action, String argument ) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(WipiwaySQLiteHelper.C_LAST_MESSAGE_RECEIVED, System.currentTimeMillis()); 	// Current time
		values.put(WipiwaySQLiteHelper.C_MODE, mode);
		values.put(WipiwaySQLiteHelper.C_STAGE, stage);
		values.put(WipiwaySQLiteHelper.C_ACTION, action);
		values.put(WipiwaySQLiteHelper.C_ARGUMENT, argument);
				
		// Update the row with the new info. Match with ID
		database.update(WipiwaySQLiteHelper.TABLE_ACTION_STATUS, values, WipiwaySQLiteHelper.C_ID + "=" + id,  null);
		
	}
	
	

	// Search and get details of the active session from the phoneNumber
	public ContentValues getLastSession(String phoneNumber) {
		
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		
		String query = "Select * from " + WipiwaySQLiteHelper.TABLE_ACTION_STATUS + " where " + WipiwaySQLiteHelper.C_PHONE_NUMBER + " = " + phoneNumber 
						+ " order by " + WipiwaySQLiteHelper.C_LAST_MESSAGE_RECEIVED + " DESC Limit 1";
		
		Cursor cursor = database.rawQuery(query,null);
		
		ContentValues values = new ContentValues(); 
		
		if (cursor != null) {
            cursor.moveToFirst();

            values.put(WipiwaySQLiteHelper.C_ID, cursor.getInt(cursor.getColumnIndex(WipiwaySQLiteHelper.C_ID)));
            values.put(WipiwaySQLiteHelper.C_MODE, cursor.getInt(cursor.getColumnIndex(WipiwaySQLiteHelper.C_MODE)));
            values.put(WipiwaySQLiteHelper.C_STAGE, cursor.getInt(cursor.getColumnIndex(WipiwaySQLiteHelper.C_STAGE)));
            values.put(WipiwaySQLiteHelper.C_ACTION, cursor.getInt(cursor.getColumnIndex(WipiwaySQLiteHelper.C_ACTION)));
            values.put(WipiwaySQLiteHelper.C_ARGUMENT, cursor.getString(cursor.getColumnIndex(WipiwaySQLiteHelper.C_ARGUMENT)));
            
        }
		
		return values;
		
	}
	
	// Check later if the following 3 functions are needed. 
	private ContentValues actionContentValues(ActionStatus actionStatus) {
		
		ContentValues values = new ContentValues();
		values.put(WipiwaySQLiteHelper.C_PHONE_NUMBER, actionStatus.getPhoneNumber());
		values.put(WipiwaySQLiteHelper.C_LAST_MESSAGE_RECEIVED, System.currentTimeMillis()); 	// Current time
		values.put(WipiwaySQLiteHelper.C_MODE, actionStatus.getMode());
		values.put(WipiwaySQLiteHelper.C_STAGE, actionStatus.getStage());
		values.put(WipiwaySQLiteHelper.C_ACTION, actionStatus.getAction());
		values.put(WipiwaySQLiteHelper.C_ARGUMENT, actionStatus.getArgument());
		
		return values;
		
	}
	
	public void insertActionStatus(ActionStatus actionStatus) {
		
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		
		ContentValues values = actionContentValues(actionStatus);
		
		database.insert(WipiwaySQLiteHelper.TABLE_ACTION_STATUS, null, values);
		
	}
	
	public void updateActionStatus( ActionStatus actionStatus ) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		
		ContentValues values = actionContentValues(actionStatus);
				
		// Update the row with the new info. Match with ID
		database.update(WipiwaySQLiteHelper.TABLE_ACTION_STATUS, values, WipiwaySQLiteHelper.C_ID + "=" + actionStatus.getId(),  null);
		
	}
}
