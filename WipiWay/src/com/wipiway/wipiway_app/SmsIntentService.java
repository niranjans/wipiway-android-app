package com.wipiway.wipiway_app;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Service calss that gets started when a trigger command for the app has been received
 * @author niranjan
 *
 */
public class SmsIntentService extends IntentService {
	
	private Context context;

	public SmsIntentService() {
		super("SmsIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		Log.d("SmsIntentService", "onHandleIntent");

		// Main logic
		context = SmsIntentService.this;
		String senderPhoneNumber = intent.getStringExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_SENDER_PHONE_NUMBER);
		ArrayList<String> wordsList = intent.getStringArrayListExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_CONTENT_ARRAYLIST);

		
		if(intent.getBooleanExtra(WipiwayUtils.INTENT_EXTRA_KEY_IS_ACTIVE_SESSION, false)) {
			// From Active Session
			activeSession(wordsList, senderPhoneNumber);
		} else {
			// New command
			newCommand(wordsList, senderPhoneNumber);
			
		}

	}
	
	private void activeSession(ArrayList<String> wordsList, String senderPhoneNumber){
		
		int mode = WipiwayUtils.getActiveSessionMode(context);
		
		switch (mode) {
		case WipiwayUtils.MODE_ACTION_BREAK_PASSWORD:
			
			break;

		default:
			break;
		}
		
	}
	
	private void newCommand(ArrayList<String> wordsList, String senderPhoneNumber){
		
		Log.d("SmsIntentService", "onHandleIntent - New command start");

		// TODO - Check password first and then proceed
		int wordsListSize;
		wordsListSize = wordsList.size();
		
		WipiwayController controller = WipiwayController.getWipiwayController(context);
		
		switch(wordsListSize) {
		case 1:
			// Just the trigger keyword
			controller.logicNewCommandWords1(senderPhoneNumber);				
			break;
		case 2:
			controller.logicNewCommandWords2(senderPhoneNumber, wordsList.get(1));
			break;
		case 3:
			Log.d("SmsIntentService", "onHandleIntent - New command start - 3 words");

			controller.logicNewCommandWords3(senderPhoneNumber, wordsList.get(1), wordsList.get(2));
			break;
		case 4:
			controller.logicNewCommandWords4(senderPhoneNumber, wordsList.get(1), wordsList.get(2), wordsList.get(3));
			break;
		case 5:
			break;
		case 6:
			break;
		default:
			controller.logicNewCommandWordsDefault(senderPhoneNumber);
			break;
		}
	}

}
