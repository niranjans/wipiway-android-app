package com.wipiway.wipiway_app;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

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
		
		// Main logic
		context = SmsIntentService.this;
		String senderPhoneNumber = intent.getStringExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_SENDER_PHONE_NUMBER);
		ArrayList<String> wordsList = intent.getStringArrayListExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_CONTENT_ARRAYLIST);
		int wordsListSize;
		
		if(intent.getBooleanExtra(WipiwayUtils.INTENT_EXTRA_KEY_IS_ACTIVE_SESSION, false)) {
			// From Active Session
			
		} else {
			// New command
			
			// TODO - Check password first and then proceed

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

}
