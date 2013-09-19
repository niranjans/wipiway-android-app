package com.wipiway.wipiway_app;

import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
 
/**
 * @author Niranjan Singh - singh@wipiway.com
 *
 * Generic Utility class
 * 
 *  This class provides common static utility methods and constants used regularly in the App.
 */
public class WipiwayUtils {

	public static final String SMS_TRIGGER_KEYWORD = "Wipiway";
	
	public static final String INTENT_EXTRA_KEY_METHOD_GENERATED_FROM = "intent_extra_key_method_generated_from";
	public static final String INTENT_EXTRA_KEY_SMS_RECEIVED_TIME = "intent_extra_key_sms_received_time";
	public static final String INTENT_EXTRA_KEY_SMS_CONTENT = "intent_extra_key_sms_content";
	public static final String INTENT_EXTRA_KEY_SMS_CONTENT_ARRAYLIST = "intent_extra_key_sms_content_arraylist";
	public static final String INTENT_EXTRA_KEY_SMS_SENDER_PHONE_NUMBER = "intent_extra_key_sms_sender_phone_number";
	public static final String INTENT_EXTRA_KEY_IS_ACTIVE_SESSION = "intent_extra_key_is_active_session";
	public static final String INTENT_EXTRA_KEY_PERFORM_ACTION = "intent_extra_key_perform_action";
	public static final String INTENT_EXTRA_KEY_IS_SILENT_CALL = "intent_extra_key_is_silent_call";

	
	public static final String INTENT_EXTRA_VALUE_METHOD_GENERATED_FROM_SMS_RECEIVER = "intent_extra_value_method_generated_from_sms_receiver";
	public static final String INTENT_EXTRA_VALUE_ACTION_CALL = "intent_extra_key_action_call";
	public static final String INTENT_EXTRA_VALUE_ACTION_CALL_SILENT = "intent_extra_key_action_call_silent";
	public static final String INTENT_EXTRA_VALUE_ACTION_GET_CONTACT = "intent_extra_key_action_get_contact";
	
	public static final String FLAG_FIRST_VISIT = "flag_first_visit";
	
	public static final String PREFS_KEY_ACTIVE_SESSION_PHONE_NUMBER = "prefs_key_active_session_phone_number";
	public static final String PREFS_KEY_LAST_MESSAGE_RECEIVED = "prefs_key_last_message_received";
	public static final String PREFS_KEY_MODE = "prefs_key_mode";
	public static final String PREFS_KEY_STAGE = "prefs_key_stage";
	
	public static final long ACTIVE_SESSION_TIME_LIMIT = 120000;	// 2 mins - Time in milis 1000 * 60 * 2
   
	public static final String COMMANDS_MENU = "Wipiway SMS Service\nReply with a command: Call me, Call me silent, Call [NUMBER], Get contact [NAME]";
	
    /**
     * Complete command. Example: Wipiway 1234 call me
     */
    public static final int MODE_COMPLETE = 1;
    /**
     * Example: Wipiway call me --> 1234
     */
    public static final int MODE_ACTION_BREAK_PASSWORD = 2;
    /**
     * Example: Wipiway --> call me --> 1234
     */
    public static final int MODE_BREAK_ACTION_BREAK_PASSWORD = 3;
    /**
     * Example: Wipiway --> call me 1234
     */
    public static final int MODE_BREAK_ACTION_PASSWORD = 4;
    /**
     * Example: Wipiway 1234 --> call me
     */
    public static final int MODE_PASSWORD_BREAK_ACTION = 5;
    
    
    public static final int MODE_ACTION_BREAK_PASSWORD_STAGE_ACTION = 1;
    public static final int MODE_BREAK_ACTION_BREAK_PASSWORD_STAGE_ACTION = 1;
    public static final int MODE_BREAK_ACTION_BREAK_PASSWORD_STAGE_PASSWORD = 2;
    public static final int MODE_BREAK_ACTION_PASSWORD_STAGE_ACTION_PASSWORD = 1;
    public static final int MODE_PASSWORD_BREAK_ACTION_STAGE_ACTION = 1;

    // Actions that can be done by the app
    public static final int ACTION_CALL = 1;
    public static final int ACTION_CALL_ME = 2;
    public static final int ACTION_CALL_ME_SILENT = 3;
    public static final int ACTION_CALL_ME_PHONE = 4;
    public static final int ACTION_CALL_ME_PHONE_SILENT = 5;
    
    public static final int ACTION_GET_CONTACT = 6;




    
    public static final int STAGE_COMPLETE = 10;
	
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
	
	/* 
	 * *****************************************************
	 * SMS messaging stuff
	 * *****************************************************
	 */

	/**
	 * Method to send SMS message
	 * 
	 * @param phoneNumber The phone number that the message is to be sent to
	 * @param body content of the SMS message
	 */
	public static void sendSms(String phoneNumber, String body) {

		SmsManager sms = SmsManager.getDefault();
        try {
			sms.sendTextMessage(phoneNumber, null, body, null, null);
		} catch (Exception e) {
			Log.d("WipiwayController", e.toString());
		}  

		
	}
	
	/**
	 * Method to send SMS message with a Pending Intent 
	 * 
	 * @param phoneNumber The phone number that the message is to be sent to
	 * @param body content of the SMS message
	 * @param sentPI Pending intent that receives broadcast of when message is sent
	 */
	public static void sendSms(String phoneNumber, String body, PendingIntent sentPI) {

		SmsManager sms = SmsManager.getDefault();
        try {
			sms.sendTextMessage(phoneNumber, null, body, sentPI, null);
		} catch (Exception e) {
			Log.d("WipiwayController", e.toString());
		}  

		
	}
	
	
	/* 
	 * *****************************************************
	 * Active Session tracking stuff 
	 * *****************************************************
	 */
	
	// Check if Active session with this phone number present -- quick way to check for each incoming message without checking the database
	/**
	 *  Method to check if Active session with this phone number present. 
	 *  This is a quick way to check for each incoming message without hitting the database.
	 *  
	 * @param context Context
	 * @param phoneNumber Phone number that needs to be looked up for an Active session
	 * @return Returns True if active session is present
	 */
	public static boolean isActiveSessionPresent(Context context, String phoneNumber ) {
		
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		
		// Is the phoneNumber stored in preference
		if(prefs.getString(PREFS_KEY_ACTIVE_SESSION_PHONE_NUMBER, "NIL").equalsIgnoreCase(phoneNumber)) {
			
			// Now check if the time is within the Active Session time limit
			Long timeDifference = System.currentTimeMillis() - prefs.getLong(PREFS_KEY_LAST_MESSAGE_RECEIVED, 0);
			if( timeDifference < ACTIVE_SESSION_TIME_LIMIT){
				
				// Update with new time
				prefs.edit().putLong(PREFS_KEY_LAST_MESSAGE_RECEIVED, System.currentTimeMillis()).commit();
				return true;
			} else {
				
				// Time ran out. Close session
				resetActiveSessionPresent(context);
				return false;
			}
			
			
		}
		
		
		return false;
		
	}
	
	/**
	 * Method to get the last assumed Mode of the active session 
	 * 
	 * @param context Context
	 * @return Last assumed Mode of the active session 
	 */
	public static int getActiveSessionMode(Context context) {
		
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		
		return prefs.getInt(PREFS_KEY_MODE, 0);
		
	}
	
	/**
	 * Method to get the last assumed Stage of the active session 
	 * 
	 * @param context Context
	 * @return Last assumed Stage of the active session 
	 */
	public static int getActiveSessionStage(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		
		return prefs.getInt(PREFS_KEY_STAGE, 0);
	}
	
	/**
	 * Method to set the new assumed Mode of the active session 
	 * 
	 * @param context
	 * @param mode The new assumed Mode of the active session 
	 */
	public static void setActiveSessionMode(Context context, int mode) {
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

		prefs.edit().putInt(PREFS_KEY_MODE, mode).commit();
	}
	
	/**
	 * Method to set the new assumed Stage of the active session 
	 * 
	 * @param context
	 * @param mode The new assumed Stage of the active session 
	 */
	public static void setActiveSessionStage(Context context, int stage) {
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

		prefs.edit().putInt(PREFS_KEY_STAGE, stage).commit();
	}
	
	// Mode and stage is supposed to be set separately
	/**
	 * Method to set a new active session
	 * 
	 * @param context Context
	 * @param phoneNumber Phone number that is associated with the active session
	 */
	public static void setActiveSessionPresent(Context context, String phoneNumber) {
		
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		
		prefs.edit().putString(PREFS_KEY_ACTIVE_SESSION_PHONE_NUMBER, phoneNumber).commit();
		prefs.edit().putLong(PREFS_KEY_LAST_MESSAGE_RECEIVED, System.currentTimeMillis()).commit();
	
	}
	
	// Reset all active session data
	/**
	 * Method to reset the the Active session information
	 * 
	 * @param context
	 */
	public static void resetActiveSessionPresent(Context context) {
		
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		
		prefs.edit().putString(PREFS_KEY_ACTIVE_SESSION_PHONE_NUMBER, "").commit();
		prefs.edit().putLong(PREFS_KEY_LAST_MESSAGE_RECEIVED, 0).commit();
		prefs.edit().putInt(PREFS_KEY_STAGE, 0).commit();
		prefs.edit().putInt(PREFS_KEY_MODE, 0).commit();
	}
	
	
	

	
	 

	
	


}

/*
 * Wipiway SMS Modes of Interaction:
 * 
 * 1 - Wipiway 1234 call me
 * 2 - Wipiway call me --> 1234
 * 3 - Wipiway --> call me --> 1234
 * 4 - Wipiway --> call me 1234
 * 5 - Wipiway 1234 --> call me
 */

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

