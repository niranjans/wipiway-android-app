package com.wipiway.wipiway_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

// Help from http://www.vogella.com/articles/AndroidSQLite/article.html

public class WipiwayDataSource {

	//private SQLiteDatabase database;
	private SQLiteHelper dbHelper;
	
	public WipiwayDataSource(Context context) {
	    dbHelper = SQLiteHelper.getHelper(context);
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
		values.put(SQLiteHelper.C_PHONE_NUMBER, phoneNumber);
		values.put(SQLiteHelper.C_LAST_MESSAGE_RECEIVED, System.currentTimeMillis()); 	// Current time
		values.put(SQLiteHelper.C_MODE, mode);
		values.put(SQLiteHelper.C_STAGE, stage);
		values.put(SQLiteHelper.C_ACTION, action);
		values.put(SQLiteHelper.C_ARGUMENT, argument);
		
		database.insert(SQLiteHelper.TABLE_ACTION_STATUS, null, values);
		
	}
	
	public void updateActionStatus( int id, int mode, int stage, int action, String argument ) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.C_LAST_MESSAGE_RECEIVED, System.currentTimeMillis()); 	// Current time
		values.put(SQLiteHelper.C_MODE, mode);
		values.put(SQLiteHelper.C_STAGE, stage);
		values.put(SQLiteHelper.C_ACTION, action);
		values.put(SQLiteHelper.C_ARGUMENT, argument);
				
		// Update the row with the new info. Match with ID
		database.update(SQLiteHelper.TABLE_ACTION_STATUS, values, SQLiteHelper.C_ID + "=" + id,  null);
		
	}
	
	

	// Search and get details of the active session from the phoneNumber
	public ContentValues getLastSession(String phoneNumber) {
		
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		
		String query = "Select * from " + SQLiteHelper.TABLE_ACTION_STATUS + " where " + SQLiteHelper.C_PHONE_NUMBER + " = " + phoneNumber 
						+ " order by " + SQLiteHelper.C_LAST_MESSAGE_RECEIVED + " DESC Limit 1";
		
		Cursor cursor = database.rawQuery(query,null);
		
		ContentValues values = new ContentValues(); 
		
		if (cursor != null) {
            cursor.moveToFirst();

            values.put(SQLiteHelper.C_ID, cursor.getInt(cursor.getColumnIndex(SQLiteHelper.C_ID)));
            values.put(SQLiteHelper.C_MODE, cursor.getInt(cursor.getColumnIndex(SQLiteHelper.C_MODE)));
            values.put(SQLiteHelper.C_STAGE, cursor.getInt(cursor.getColumnIndex(SQLiteHelper.C_STAGE)));
            values.put(SQLiteHelper.C_ACTION, cursor.getInt(cursor.getColumnIndex(SQLiteHelper.C_ACTION)));
            values.put(SQLiteHelper.C_ARGUMENT, cursor.getString(cursor.getColumnIndex(SQLiteHelper.C_ARGUMENT)));
            
        }
		
		return values;
		
	}
	
	// Check later if the following 3 functions are needed. 
	public void insertActionStatus(ActionStatus actionStatus) {
		
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		
		ContentValues values = populateContentValues(actionStatus);
		
		database.insert(SQLiteHelper.TABLE_ACTION_STATUS, null, values);
		
	}
	
	public void updateActionStatus( ActionStatus actionStatus ) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		
		ContentValues values = populateContentValues(actionStatus);
				
		// Update the row with the new info. Match with ID
		database.update(SQLiteHelper.TABLE_ACTION_STATUS, values, SQLiteHelper.C_ID + " = " + actionStatus.getId(),  null);
		
	}
	
	private ContentValues populateContentValues(ActionStatus actionStatus) {
		
		ContentValues values = new ContentValues();
		
		if(actionStatus.getPhoneNumber()!=null)
			values.put(SQLiteHelper.C_PHONE_NUMBER, actionStatus.getPhoneNumber());
		
		// while touching database, always update with current time
		values.put(SQLiteHelper.C_LAST_MESSAGE_RECEIVED, System.currentTimeMillis()); 
		
		if(actionStatus.getMode() != 0)
			values.put(SQLiteHelper.C_MODE, actionStatus.getMode());
		
		if(actionStatus.getStage() != 0)
			values.put(SQLiteHelper.C_STAGE, actionStatus.getStage());
		
		if(actionStatus.getAction() != 0)
			values.put(SQLiteHelper.C_ACTION, actionStatus.getAction());
		
		if(actionStatus.getArgument() != null)
			values.put(SQLiteHelper.C_ARGUMENT, actionStatus.getArgument());
		
		return values;
		
	}
	

}
