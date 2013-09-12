package com.wipiway.wipiway_app;

import android.content.Context;
import android.content.Intent;

public class WipiwayController {
	
	private static WipiwayController cachedControllerInstance = null;
	private Context context;
	
	public WipiwayController(Context context) {
		this.context = context;
		
		
	}
	
	public void handleIncomingData(Intent intent){
		
		if ( isActiveSession(intent.getStringExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_SENDER_PHONE_NUMBER)) ) {
			
		} else {
			
			String smsContent = intent.getStringExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_CONTENT);
			if ( smsContent != null) {
				
//				switch(smsContent) {
//				
//				}
				
			}
			
			
		}
		
	}

	
	
	private boolean isActiveSession(String senderPhoneNumber) {
		
		// Code to check if active session present. Check against current timestamp with the DB value
		
		return false;
	}
	
	
	// Return a Pre initialized controller if possible
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
