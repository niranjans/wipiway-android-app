package com.wipiway.wipiway_app;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Niranjan Singh - singh@wipiway.com
 *
 * Main controller class
 * 
 * This is the main controller class which is responsible for performing all the different actions of the remote controlling functionality.
 * 
 */
public class WipiwayController {
	
	private static WipiwayController cachedControllerInstance = null;
	private Context context;
	
	public WipiwayController(Context context) {
		this.context = context;
		
		
	}
	
	/**
	 * 
	 * Method to handle action to be performed with 1 trigger word 
	 * 
	 * @param phoneNumber Phone number associated with the action.
	 */
	public int logicNewCommandWords0(String phoneNumber){
		sendMenuSms(phoneNumber);
		return WipiwayUtils.USER_ACTION_MENU;
	}
	
	/**
	 * Method to handle logic with 2 words (one of which is the trigger word)
	 * 
	 * @param phoneNumber Phone number associated with the action.
	 * @param word2 Second word in the message (where first word is the Trigger keyword)
	 */
	public int logicNewCommandWords1(String phoneNumber, boolean flagExecute, String word2){
		
		int userAction = WipiwayUtils.USER_ACTION_INVALID;
		
		if(word2.equalsIgnoreCase("call")) {
			if(flagExecute) executeCallAction(phoneNumber, false);          
			userAction = WipiwayUtils.USER_ACTION_CALL;
		} else if(word2.equalsIgnoreCase("battery")) {
			if(flagExecute) executeBatterySms(phoneNumber);
			userAction = WipiwayUtils.USER_ACTION_GET_BATTERY;
		}
		
		return userAction;
	}
	
	
	/**
	 * Method to handle logic with 3 words (one of which is the trigger word)
	 * 
	 * @param phoneNumber Phone number associated with the action.
	 * @param word2 Second word in the message (where first word is the Trigger keyword)
	 * @param word3 Third word in the message 
	 */
	public int logicNewCommandWords2(String phoneNumber,boolean flagExecute, String word2, String word3){
		Log.d("WipiwayController", "logicNewCommandWords3 - start");
		
		int userAction = WipiwayUtils.USER_ACTION_INVALID;
		
		if(word2.equalsIgnoreCase("call")) {
			if(word3.equalsIgnoreCase("me")){				
				if(flagExecute) executeCallAction(phoneNumber, false);
				userAction = WipiwayUtils.USER_ACTION_CALL_ME;
			}
		} else if(word2.equalsIgnoreCase("get")) {
			if(word3.equalsIgnoreCase("battery")){
				if(flagExecute) executeBatterySms(phoneNumber);
				userAction = WipiwayUtils.USER_ACTION_GET_BATTERY;			
			} 
		} else if(word2.equalsIgnoreCase("open")) {
			// Last word is a URL link
			if(flagExecute) executeOpenUrl(phoneNumber, word3);
			userAction = WipiwayUtils.USER_ACTION_OPEN_LINK;
			
		}
		
		return userAction;

	}
	
	public int logicNewCommandWords3(String phoneNumber, boolean flagExecute,  String word2, String word3, String word4){
		
		int userAction = WipiwayUtils.USER_ACTION_INVALID;
		
		if(word2.equalsIgnoreCase("call")) {
			if(word3.equalsIgnoreCase("me")){
				if(word4.equalsIgnoreCase("silent")){
					if(flagExecute) executeCallAction(phoneNumber, true);
					userAction =  WipiwayUtils.USER_ACTION_CALL_ME_SILENT;			 
				}
			}
		} else if(word2.equalsIgnoreCase("get")){
			if(word3.equalsIgnoreCase("contact")){
				if(flagExecute) executeGetContact(phoneNumber, word4);
				userAction =  WipiwayUtils.USER_ACTION_GET_CONTACT;
			}  
		}
		
		return userAction;

	}
	
	public void logicNewCommandWords4(String phoneNumber, String word2, String word3, String word4, String word5){
		
		if(word2.equalsIgnoreCase("get")){
			if(word3.equalsIgnoreCase("contact")){
					// TODO get contact name - word4, word5
			}
		}
	}
	
	/**
	 * Method to handle logic when command has unkown number of words
	 * 
	 * @param phoneNumber Phone number associated with the action.
	 */
	public int logicNewCommandWordsDefault(String phoneNumber){
		sendMenuSms(phoneNumber);
		return WipiwayUtils.USER_ACTION_MENU;

	}
	
	/* 
	 * *****************************************************
	 * Execution stuff
	 * *****************************************************
	 */
	
	/**
	 * Method to send the Commands Menu of the SMS interface
	 * 
	 * @param phoneNumber The phone number that the message is to be sent to
	 */
	public void sendMenuSms(String phoneNumber){
		WipiwayUtils.sendSms(phoneNumber, WipiwayUtils.REPLY_COMMANDS_MENU);
		
		// Set a new session with the assumed Mode and Stage whenever a new Menu Command is sent
		WipiwayUtils.setActiveSessionPresent(context, phoneNumber);
		WipiwayUtils.setActiveSessionMode(context, WipiwayUtils.MODE_BREAK_ACTION_BREAK_PASSWORD);
		WipiwayUtils.setActiveSessionStage(context, WipiwayUtils.MODE_BREAK_ACTION_BREAK_PASSWORD_STAGE_ACTION);
	}
	
	
	public void executeCallAction(String phoneNumber, boolean isSilent) {
		WipiwayDataSource datasource = new WipiwayDataSource(context);
		datasource.insertActionHistory(phoneNumber, WipiwayUtils.USER_ACTION_CALL, null, null, "Called back " + phoneNumber);
		
		Intent intent = new Intent(context, StatusActivity.class);
		intent.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_PERFORM_ACTION, WipiwayUtils.USER_ACTION_CALL_ME);
		intent.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_SENDER_PHONE_NUMBER, phoneNumber);
		intent.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_IS_SILENT_CALL, isSilent);
		
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
	}
	
	public void executeBatterySms(String phoneNumber) {
		WipiwayDataSource datasource = new WipiwayDataSource(context);
		datasource.insertActionHistory(phoneNumber, WipiwayUtils.USER_ACTION_GET_BATTERY, null, null, "Sent battery info");
		
		WipiwayUtils.sendBatteryLevelSms(context, phoneNumber);
	}
	
	public void executeOpenUrl(String phoneNumber, String url) {
		  
		WipiwayDataSource datasource = new WipiwayDataSource(context);
		datasource.insertActionHistory(phoneNumber, WipiwayUtils.USER_ACTION_OPEN_LINK, url, null, "Opened link " + url);
		
		Intent intent = new Intent(context, StatusActivity.class);
		intent.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_PERFORM_ACTION, WipiwayUtils.USER_ACTION_OPEN_LINK);
		intent.putExtra(WipiwayUtils.INTENT_EXTRA_LINK_URL, url);
		
		
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
	}
	
	public void executeGetContact(String phoneNumber, String searchName){
		
		WipiwayDataSource datasource = new WipiwayDataSource(context);
		datasource.insertActionHistory(phoneNumber, WipiwayUtils.USER_ACTION_GET_CONTACT, searchName, null, "Searched for contact '" + searchName + "'");
		
		WipiwayUtils.searchAndSendContactInfo(context, phoneNumber, searchName);
	}

	/**
	 * Method which returns a pre-initialized controller if possible (Singleton)
	 * 
	 * @param context
	 * @return Controller instance
	 */
	public static synchronized WipiwayController getWipiwayController(Context context) {
		if (cachedControllerInstance==null){
			synchronized(WipiwayController.class) {
				if (cachedControllerInstance == null)
					cachedControllerInstance = new WipiwayController(context);
			}
		}
		return cachedControllerInstance;
	}
	
	private static void deleteCacheInstance() {
		if (cachedControllerInstance != null)
			cachedControllerInstance = null;
	}
	
}
