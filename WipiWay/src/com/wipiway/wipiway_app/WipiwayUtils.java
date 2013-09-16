package com.wipiway.wipiway_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class WipiwayUtils {
	
	public static final String SMS_TRIGGER_KEYWORD = "Wipiway";
	
	public static final String INTENT_EXTRA_KEY_METHOD_GENERATED_FROM = "INTENT_EXTRA_KEY_METHOD_GENERATED_FROM";
	public static final String INTENT_EXTRA_KEY_SMS_RECEIVED_TIME = "INTENT_EXTRA_KEY_SMS_RECEIVED_TIME";
	public static final String INTENT_EXTRA_KEY_SMS_CONTENT = "INTENT_EXTRA_KEY_SMS_CONTENT";
	public static final String INTENT_EXTRA_KEY_SMS_CONTENT_ARRAYLIST = "INTENT_EXTRA_KEY_SMS_CONTENT_ARRAYLIST";
	public static final String INTENT_EXTRA_KEY_SMS_SENDER_PHONE_NUMBER = "INTENT_EXTRA_KEY_SMS_SENDER_PHONE_NUMBER";
	
	public static final String INTENT_EXTRA_VALUE_METHOD_GENERATED_FROM_SMS_RECEIVER = "INTENT_EXTRA_VALUE_METHOD_GENERATED_FROM_SMS_RECEIVER";
	public static final String INTENT_EXTRA_VALUE_ACTION_CALL = "INTENT_EXTRA_KEY_ACTION_CALL";
	public static final String INTENT_EXTRA_VALUE_ACTION_CALL_SILENT = "INTENT_EXTRA_KEY_ACTION_CALL_SILENT";
	public static final String INTENT_EXTRA_VALUE_ACTION_GET_CONTACT = "INTENT_EXTRA_KEY_ACTION_GET_CONTACT";
	
	public static final String FLAG_FIRST_VISIT = "FLAG_FIRST_VISIT";
	
	public static final String PREFS_KEY_ACTIVE_SESSION_PHONE_NUMBER = "PREFS_KEY_ACTIVE_SESSION_PHONE_NUMBER";
	public static final long ACTIVE_SESSION_TIME_LIMIT = 120000;	// 2 mins - Time in milis 1000 * 60 * 2
   
	/*
	 * Wipiway SMS Modes of Interaction:
	 * 
	 * 1 - Wipiway 1234 call me
	 * 2 - Wipiway call me --> 1234
	 * 3 - Wipiway --> call me --> 1234
	 * 4 - Wipiway --> call me 1234
	 * 5 - Wipiway 1234 --> call me
	 */
	
    public static final int MODE_COMPLETE = 1;
    public static final int MODE_ACTION_BREAK_PASSWORD = 2;
    public static final int MODE_BREAK_ACTION_BREAK_PASSWORD = 3;
    public static final int MODE_BREAK_ACTION_PASSWORD = 4;
    public static final int MODE_PASSWORD_BREAK_ACTION = 5;
    
    /* 
     * Stages
     * 
     * 0 - Action completed, ready for logs
     *            
     *         		| Stage |
     *            
     * Mode 1  	=> 		10 - complete
     * 
     * Mode 2	=> 		1 - get password
     * 
     * Mode 3	=> 		1 - get action
     * 			=> 		2 - get password
     * 
     * Mode 4	=> 		1 - get action, password
     * 
     * Mode 5	=> 		1 - get action
     * 
     */
    
    public static final int STAGE_COMPLETE = 0;
    
    
    
	
	public static boolean isFirstVisit(Context context) {
		
		// Returns true if this is first visit of the user
		//return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(WipiwayUtils.FLAG_FIRST_VISIT, true);
		
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		
		return prefs.getBoolean(WipiwayUtils.FLAG_FIRST_VISIT, true);
				
	}
	
	public static void unsetFirstVisit(Context context, boolean flag) {
		// Set flag to false
		if(isFirstVisit(context)){
			
			SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
			
			
			prefs.edit().putBoolean(WipiwayUtils.FLAG_FIRST_VISIT, false).commit();
		}
				
	}
	
	// Check if Active session with this phone number present -- quick way to check for each incoming message without checking the database
	public static boolean isActiveSessionPresent(Context context, String phoneNumber ) {
		
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		
		// Is the phoneNumber stored in preference
		if(prefs.getString(PREFS_KEY_ACTIVE_SESSION_PHONE_NUMBER, "NIL").equalsIgnoreCase(phoneNumber)) {
			
			// Now check if the time is within the Active Session time limit
			Long timeDifference = System.currentTimeMillis() - prefs.getLong(SQLiteHelper.C_LAST_MESSAGE_RECEIVED, 0);
			if( timeDifference < ACTIVE_SESSION_TIME_LIMIT){
				
				// Update with new time
				prefs.edit().putLong(SQLiteHelper.C_LAST_MESSAGE_RECEIVED, System.currentTimeMillis()).commit();
				return true;
			} else {
				
				// Time ran out. Close session
				resetActiveSessionPresent(context);
				return false;
			}
			
			
		}
		
		
		return false;
		
	}
	
	public static void setActiveSessionPresent(Context context, String phoneNumber) {
		
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		
		prefs.edit().putString(PREFS_KEY_ACTIVE_SESSION_PHONE_NUMBER, phoneNumber).commit();
		prefs.edit().putLong(SQLiteHelper.C_LAST_MESSAGE_RECEIVED, System.currentTimeMillis()).commit();
	
	}
	
	public static void resetActiveSessionPresent(Context context) {
		
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		
		prefs.edit().putString(PREFS_KEY_ACTIVE_SESSION_PHONE_NUMBER, "NIL").commit();
		prefs.edit().putLong(SQLiteHelper.C_LAST_MESSAGE_RECEIVED, 0).commit();
	
	}
	
	 

	
	


}
