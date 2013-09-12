package com.wipiway.wipiway_app;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.format.Time;

public class SmsBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		// Get message received timestamp
		Time messageReceivedTime = new Time();
		messageReceivedTime.setToNow();
		
		
		
		// Initializing and getting message details
		SmsMessage[] smsMessage = extractSmsMessage(intent);
		String senderPhoneNumber = smsMessage[0].getDisplayOriginatingAddress();
		String smsContent = smsMessage[0]
				.getDisplayMessageBody().toLowerCase().trim();
		ArrayList<String> wordsList = getWords(smsContent);

		// Check if incoming SMS was meant for WipiWay
		if (wordsList.size() > 0
				&& wordsList.get(0).toString()
						.equalsIgnoreCase(WipiwayUtils.SMS_TRIGGER_KEYWORD)) {
			
			// Start activity
			Intent i = new Intent(context, StatusActivity.class);
			
			// Let the other end know where the intent is generated from (for future proofing)
			i.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_METHOD_GENERATED_FROM, WipiwayUtils.INTENT_EXTRA_VALUE_METHOD_GENERATED_FROM_SMS_RECEIVER);	
			i.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_SENDER_PHONE_NUMBER,
					senderPhoneNumber);
			i.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_RECEIVED_TIME, messageReceivedTime.toString());
			i.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_CONTENT, smsContent);
			i.putStringArrayListExtra(
					WipiwayUtils.INTENT_EXTRA_KEY_SMS_CONTENT_ARRAYLIST, wordsList);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			
			context.startActivity(i);
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

	public ArrayList<String> getWords(String msg) {

		StringTokenizer token = new StringTokenizer(msg);

		ArrayList<String> wordsList = new ArrayList<String>();

		String nextWord;
		while (token.hasMoreTokens()) {

			nextWord = token.nextToken();
			// Log.d(TAG, " inside getWords while loop.. nextToken - " +
			// nextWord);

			wordsList.add(nextWord);

		}

		return wordsList;
	}

}
