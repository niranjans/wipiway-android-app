package com.wipiway.wipiway_app;

import android.app.IntentService;
import android.content.Intent;

/**
 * Service calss that gets started when a trigger command for the app has been received
 * @author niranjan
 *
 */
public class SmsIntentService extends IntentService {

	public SmsIntentService() {
		super("SmsIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		// Main logic
		
		
	}

}
