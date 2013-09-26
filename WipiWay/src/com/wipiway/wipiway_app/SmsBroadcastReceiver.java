package com.wipiway.wipiway_app;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.format.Time;
import android.util.Log;

/**
 * @author Niranjan Singh - singh@wipiway.com
 * 
 * Class to receive new SMS broacasts and begin the process of checking / controlling. 
 *
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.d("SmsBroadcastReceiver", "onReceive start");
		
		// Get message received timestamp
		Time messageReceivedTime = new Time();
		messageReceivedTime.setToNow();
		
		
		
		// Initializing and getting message details
		SmsMessage[] smsMessage = extractSmsMessage(intent);
		String senderPhoneNumber = smsMessage[0].getDisplayOriginatingAddress();
		String smsContent = smsMessage[0]
				.getDisplayMessageBody().trim();
		ArrayList<String> wordsList = WipiwayUtils.splitInputStringIntoWords(smsContent);
		
		if (wordsList.size() > 0 ) {
			
			boolean isActiveSession = WipiwayUtils.isActiveSessionPresent(context, senderPhoneNumber);
			boolean isKeywordPresent = wordsList.get(0).toString().equalsIgnoreCase(WipiwayUtils.SMS_TRIGGER_KEYWORD) ;
			
			// If message contains the keyword OR an active session with the Phone number is present
			if( isKeywordPresent || isActiveSession) {
				
				// Good to go, move forward
				
				Intent i = new Intent(context, SmsIntentService.class);
			    
				i.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_SENDER_PHONE_NUMBER,
						senderPhoneNumber);
				i.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_CONTENT, smsContent);
				i.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_IS_KEYWORD_PRESENT, isKeywordPresent);
				i.putStringArrayListExtra(
						WipiwayUtils.INTENT_EXTRA_KEY_SMS_CONTENT_ARRAYLIST, wordsList);

				Log.d("SmsBroadcastReceiver", "just before starting service");

				
				// Start the intent service
			    context.startService(i);
				
			}
		
		}


	}
	

	// Get the SMS content from the intent
	public SmsMessage[] extractSmsMessage(Intent intent) {
		// Extracting sms body stuff
		Bundle bundle = intent.getExtras();

		Object messages[] = (Object[]) bundle.get("pdus");
		SmsMessage smsMessage[] = new SmsMessage[messages.length];
		for (int n = 0; n < messages.length; n++) {
			smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
		}

		return smsMessage;
	}



}
