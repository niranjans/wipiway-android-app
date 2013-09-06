package com.wipiway.wipiway_app;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		// Initializing and getting message details
		SmsMessage[] smsMessage = extractSmsMessage(intent);
		String senderPhoneNumber = smsMessage[0].getDisplayOriginatingAddress();
		ArrayList<String> wordsList = getWords(smsMessage[0]
				.getDisplayMessageBody().toLowerCase().trim());

		// Check if incoming SMS was meant for WipiWay
		if (wordsList.size() > 0
				&& wordsList.get(0).toString()
						.equalsIgnoreCase(WipiwayUtils.SMS_TRIGGER_KEYWORD)) {
			
			// Start activity
			Intent i = new Intent(context, StatusActivity.class);
			i.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_SENDER_PHONE_NUMBER,
					senderPhoneNumber);
			i.putStringArrayListExtra(
					WipiwayUtils.INTENT_EXTRA_KEY_SMS_CONTENT, wordsList);
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
