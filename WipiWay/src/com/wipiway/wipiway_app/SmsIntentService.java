package com.wipiway.wipiway_app;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 
 * @author Niranjan Singh - singh@wipiway.com
 * 
 * Service that gets started when a trigger command for the app has been received
 * 
 *
 */
public class SmsIntentService extends IntentService {
	
	private Context context;
	private String senderPhoneNumber;
	private ArrayList<String> wordsList;
	private String smsContent;

	public SmsIntentService() {
		super("SmsIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		Log.d("SmsIntentService", "onHandleIntent");

		// Main logic
		context = SmsIntentService.this;
		senderPhoneNumber = intent.getStringExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_SENDER_PHONE_NUMBER);
		wordsList = intent.getStringArrayListExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_CONTENT_ARRAYLIST);
		smsContent = intent.getStringExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_CONTENT);
		
		if(intent.getBooleanExtra(WipiwayUtils.INTENT_EXTRA_KEY_IS_KEYWORD_PRESENT, false)) {
			
			wordsList.remove(0);	// Remove the keyword, not needed anymore
			// Execute a new command
			int userAction = matchCommand(true);
			if(userAction == WipiwayUtils.USER_ACTION_INVALID) {
				WipiwayUtils.sendSms(senderPhoneNumber, WipiwayUtils.REPLY_COMMAND_INVALID);
			}
			
		} else {
			
			// From Active Session because keyword not present but still started service
			activeSession();
			
		}

	}
	
	private void activeSession(){
		
		int mode = WipiwayUtils.getActiveSessionMode(context);
		int stage = WipiwayUtils.getActiveSessionStage(context);
		int userAction;
		
		switch (mode) {
		case WipiwayUtils.MODE_ACTION_BREAK_PASSWORD:
			
			break;
		case WipiwayUtils.MODE_BREAK_ACTION_BREAK_PASSWORD:
			
					switch (stage) {
					case WipiwayUtils.MODE_BREAK_ACTION_BREAK_PASSWORD_STAGE_ACTION:
						userAction = matchCommand(false);
						
						if(userAction != WipiwayUtils.USER_ACTION_INVALID) {
							// Proceed - send message asking for password
							WipiwayUtils.sendSms(senderPhoneNumber, WipiwayUtils.REPLY_ASK_FOR_PASSWORD);
							WipiwayUtils.setActiveSessionStage(context, WipiwayUtils.MODE_BREAK_ACTION_BREAK_PASSWORD_STAGE_PASSWORD);
							WipiwayUtils.setActiveSessionStringExtra(context, smsContent); // Store to interpret later
						} else {
							// Do not change the active session details. The user can try again with a command. Or start over.
							WipiwayUtils.sendSms(senderPhoneNumber, WipiwayUtils.REPLY_COMMAND_INVALID);
							
						}
						break;
					case WipiwayUtils.MODE_BREAK_ACTION_BREAK_PASSWORD_STAGE_PASSWORD:
						
							if(WipiwayUtils.isCorrectPasscode(context, wordsList.get(0))){
								// If PIN code matches, do the following
								wordsList = WipiwayUtils.splitInputStringIntoWords(WipiwayUtils.getActiveSessionStringExtra(context));
								matchCommand(true); // Already checked earlier (in above case) that command matches
							} else {
								// Do not change the active session details. The user can try again with a command. Or start over.
								WipiwayUtils.sendSms(senderPhoneNumber, WipiwayUtils.REPLY_INCORRECT_PASSCODE);
								WipiwayUtils.resetActiveSessionPresent(context);
							}

						
						break;
					default:
						break;
					}
			
			break;
		case WipiwayUtils.MODE_BREAK_ACTION_PASSWORD:
			
			break;
		case WipiwayUtils.MODE_PASSWORD_BREAK_ACTION:
			
			break;
		case WipiwayUtils.MODE_YES_MORE:
			
			break;
		default:
			break;
		}
		
	}
	
	private int matchCommand(boolean flagExecute){
		
		Log.d("SmsIntentService", "onHandleIntent - New command start");
		
		int userAction = WipiwayUtils.USER_ACTION_INVALID;

		// TODO - Check password first and then proceed
		int wordsListSize;
		wordsListSize = wordsList.size();
		
		WipiwayController controller = WipiwayController.getWipiwayController(context);
		
		switch(wordsListSize) {
		case 0:
			// Just the trigger keyword
			userAction = controller.logicNewCommandWords0(senderPhoneNumber);				
			break;
		case 1:
			userAction = controller.logicNewCommandWords1(senderPhoneNumber,flagExecute, wordsList.get(0));
			break;
		case 2:
			userAction = controller.logicNewCommandWords2(senderPhoneNumber,flagExecute, wordsList.get(0), wordsList.get(1));
			break;
		case 3:
			userAction = controller.logicNewCommandWords3(senderPhoneNumber,flagExecute, wordsList.get(0), wordsList.get(1), wordsList.get(2));
			break;
		case 4:
			break;
		case 5:
			break;
		default:
			userAction =  controller.logicNewCommandWordsDefault(senderPhoneNumber);
			break;
		}
		
		return userAction;
		
	}

}
