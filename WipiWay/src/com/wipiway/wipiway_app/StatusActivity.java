package com.wipiway.wipiway_app;


import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

/**
 * @author Niranjan Singh - singh@wipiway.com
 * 
 * Main activity which shows the Status of the remote controlling service and also shows the history logs.
 *
 */
public class StatusActivity extends BaseActivity {
	 private int action;
	
	

    public StatusActivity() {
		super(R.string.title_status_page);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        
        setSlidingActionBarEnabled(false);
        
        // Always gets a value only if generated from the service
        Intent intent = getIntent();
        action = intent.getIntExtra(WipiwayUtils.INTENT_EXTRA_KEY_PERFORM_ACTION, 0);
        if( action != 0) {
        	performAction(intent, action);
        }

    }
	


	@Override
	public void onNewIntent(Intent intent){
		Log.d("StatucActivity", "on new intent");
		
        // Always gets a value only if generated from the service
        action = intent.getIntExtra(WipiwayUtils.INTENT_EXTRA_KEY_PERFORM_ACTION, 0);
        if( action != 0) {
        	performAction(intent, action);
        }


		
	}
	
	public void performAction(Intent intent, int action) {
		
		switch(action) {
		case WipiwayUtils.ACTION_CALL:
		case WipiwayUtils.ACTION_CALL_ME:
		case WipiwayUtils.ACTION_CALL_ME_PHONE:
		case WipiwayUtils.ACTION_CALL_ME_SILENT:
		case WipiwayUtils.ACTION_CALL_ME_PHONE_SILENT:
			
			String phoneNumber = intent.getStringExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_SENDER_PHONE_NUMBER);
			boolean isSilentCall = intent.getBooleanExtra(WipiwayUtils.INTENT_EXTRA_KEY_IS_SILENT_CALL, false);
			
			CallPhone callPhone = new CallPhone();
			callPhone.startCall(StatusActivity.this, phoneNumber, isSilentCall);
			
			break;

			
			
		
		}
		
	}

    
}
