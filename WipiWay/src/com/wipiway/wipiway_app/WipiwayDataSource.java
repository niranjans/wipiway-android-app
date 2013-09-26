package com.wipiway.wipiway_app;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateUtils;

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
	
	public ArrayList<RecentLog> getRecentLogList(){
		
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		
		// LazyLoad in future - SELECT * FROM your_table LIMIT START, COUNT (e.g. SELECT * FROM your_table LIMIT 0, 1000 to retrieve the first 1000 rows).

		String query = "Select " + SQLiteHelper.C_ACTION_PERFORMED_DATE + ", " +  SQLiteHelper.C_USER_ACTION + ", " + SQLiteHelper.C_LOG_TEXT + 
				" from " + SQLiteHelper.TABLE_ACTION_HISTORY + " order by " + SQLiteHelper.C_ACTION_PERFORMED_DATE + " DESC";
		
		ArrayList<RecentLog> recentLogList = new ArrayList<RecentLog>();
		
		Cursor cursor = database.rawQuery(query,null);
		cursor.moveToFirst();
		
		int userAction, iconResourceId;
		
		while(!cursor.isAfterLast()){
			
			userAction = cursor.getInt(1);
			
			switch (userAction) {
			case WipiwayUtils.USER_ACTION_CALL:
			case WipiwayUtils.USER_ACTION_CALL_ME:
			case WipiwayUtils.USER_ACTION_CALL_ME_PHONE:
			case WipiwayUtils.USER_ACTION_CALL_ME_SILENT:
			case WipiwayUtils.USER_ACTION_CALL_ME_PHONE_SILENT:
				
				iconResourceId = R.drawable.ic_launcher;
				break;
				
			case WipiwayUtils.USER_ACTION_GET_BATTERY:
				iconResourceId = R.drawable.ic_launcher;
				break;
				
			case WipiwayUtils.USER_ACTION_GET_CONTACT:
				iconResourceId = R.drawable.ic_launcher;
				break;

			default:
				iconResourceId = R.drawable.ic_launcher;
				break;
			}
			
			
					
			recentLogList.add(new RecentLog(iconResourceId, 
					cursor.getString(2), 
					DateUtils.getRelativeTimeSpanString(cursor.getInt(0)).toString()));
		}
		
		cursor.close();
		
		return recentLogList;
		
	}
	

}
