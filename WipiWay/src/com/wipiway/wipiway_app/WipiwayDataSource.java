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
	
	
	public void insertActionHistory(String phoneNumber, int intent, String argument1, String argument2, String logText) {
	
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.C_PHONE_NUMBER, phoneNumber);
		values.put(SQLiteHelper.C_ACTION_PERFORMED_DATE, System.currentTimeMillis()); 	// Current time
		values.put(SQLiteHelper.C_USER_ACTION, intent);
		values.put(SQLiteHelper.C_ARGUMENT1, argument1);
		values.put(SQLiteHelper.C_ARGUMENT2, argument2);
		values.put(SQLiteHelper.C_LOG_TEXT, logText);
		
		database.insert(SQLiteHelper.TABLE_ACTION_HISTORY, null, values);
		
	}
	

}
