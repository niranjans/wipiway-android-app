package com.wipiway.wipiway_app;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class StatusActivity extends BaseActivity {
	 
	private WipiwayController wipiwayController = null;

    public StatusActivity() {
		super(R.string.title_status_page);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        
        Intent intent = getIntent();
        
        // Check for trigger action
        if(intent.getStringExtra(WipiwayUtils.INTENT_EXTRA_KEY_METHOD_GENERATED_FROM)!= null) {
            if(wipiwayController == null){
            	wipiwayController = WipiwayController.getWipiwayController(StatusActivity.this);	// check which context - http://stackoverflow.com/a/7298955/804503
            }
            
            wipiwayController.handleIncomingData(intent);
        }
        
        Log.d("StatucActivity", "onCreate with phone number - " + intent.getStringExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_SENDER_PHONE_NUMBER));
    }
	


	@Override
	public void onNewIntent(Intent intent){
		Log.d("StatucActivity", "on new intent with phone number - " + intent.getStringExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_SENDER_PHONE_NUMBER));
		
        // Check for trigger action
        if(intent.getStringExtra(WipiwayUtils.INTENT_EXTRA_KEY_METHOD_GENERATED_FROM)!= null) {
            if(wipiwayController == null){
            	wipiwayController = WipiwayController.getWipiwayController(StatusActivity.this);	// check which context - http://stackoverflow.com/a/7298955/804503
            }
            
            wipiwayController.handleIncomingData(intent);
        }
		
	}

    
}
