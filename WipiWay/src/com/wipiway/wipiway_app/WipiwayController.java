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
	public void logicNewCommandWords1(String phoneNumber){
		sendMenuSms(phoneNumber);
	}
	
	/**
	 * Method to handle logic with 2 words (one of which is the trigger word)
	 * 
	 * @param phoneNumber Phone number associated with the action.
	 * @param word2 Second word in the message (where first word is the Trigger keyword)
	 */
	public void logicNewCommandWords2(String phoneNumber, String word2){
		
		if(word2.equalsIgnoreCase("call")) {
			Intent intent = new Intent(context, StatusActivity.class);
			intent.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_PERFORM_ACTION, WipiwayUtils.ACTION_CALL_ME);
			intent.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_SENDER_PHONE_NUMBER, phoneNumber);
			intent.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_IS_SILENT_CALL, false);
			
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);		
		} else if(word2.equalsIgnoreCase("battery")) {
			WipiwayUtils.sendBatteryLevelSms(context, phoneNumber);
		}
	}
	
	
	/**
	 * Method to handle logic with 3 words (one of which is the trigger word)
	 * 
	 * @param phoneNumber Phone number associated with the action.
	 * @param word2 Second word in the message (where first word is the Trigger keyword)
	 * @param word3 Third word in the message 
	 */
	public void logicNewCommandWords3(String phoneNumber, String word2, String word3){
		Log.d("WipiwayController", "logicNewCommandWords3 - start");

		if(word2.equalsIgnoreCase("call")) {
			if(word3.equalsIgnoreCase("me")){				
				Intent intent = new Intent(context, StatusActivity.class);
				intent.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_PERFORM_ACTION, WipiwayUtils.ACTION_CALL_ME);
				intent.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_SENDER_PHONE_NUMBER, phoneNumber);
				intent.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_IS_SILENT_CALL, false);
				
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
			}
		} else if(word2.equalsIgnoreCase("get")) {
			if(word3.equalsIgnoreCase("battery")){
				WipiwayUtils.sendBatteryLevelSms(context, phoneNumber);
			}
		} else if(word2.equalsIgnoreCase("open")) {
			// Last word is a URL link
			WipiwayUtils.openUrl(context, word3);	
		}
	}
	
	public void logicNewCommandWords4(String phoneNumber, String word2, String word3, String word4){
		
		if(word2.equalsIgnoreCase("call")) {
			if(word3.equalsIgnoreCase("me")){
				if(word4.equalsIgnoreCase("silent")){
					Intent intent = new Intent(context, StatusActivity.class);
					intent.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_PERFORM_ACTION, WipiwayUtils.ACTION_CALL_ME);
					intent.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_SENDER_PHONE_NUMBER, phoneNumber);
					intent.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_IS_SILENT_CALL, true);
					
	                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	                context.startActivity(intent);				}
			} 
		} else if(word2.equalsIgnoreCase("get")){
			if(word3.equalsIgnoreCase("contact")){
				WipiwayUtils.searchAndSendContactInfo(context, phoneNumber, word4);
			}
		}
	}
	
	public void logicNewCommandWords5(String phoneNumber, String word2, String word3, String word4, String word5){
		
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
	public void logicNewCommandWordsDefault(String phoneNumber){
		sendMenuSms(phoneNumber);
	}
	
	/**
	 * Method to send the Commands Menu of the SMS interface
	 * 
	 * @param phoneNumber The phone number that the message is to be sent to
	 */
	public void sendMenuSms(String phoneNumber){
		WipiwayUtils.sendSms(phoneNumber, WipiwayUtils.COMMANDS_MENU);
		
		// Set a new session with the assumed Mode and Stage whenever a new Menu Command is sent
		WipiwayUtils.setActiveSessionPresent(context, phoneNumber);
		WipiwayUtils.setActiveSessionMode(context, WipiwayUtils.MODE_BREAK_ACTION_BREAK_PASSWORD);
		WipiwayUtils.setActiveSessionStage(context, WipiwayUtils.MODE_BREAK_ACTION_BREAK_PASSWORD_STAGE_ACTION);
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
