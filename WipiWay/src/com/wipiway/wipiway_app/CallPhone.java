package com.wipiway.wipiway_app;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

// http://danielthat.blogspot.com/2013/06/android-make-phone-call-with-speaker-on.html
/**
 * @author Niranjan Singh - singh@wipiway.com
 * 
 * Class to handle the calling functionality through the app.
 *
 */
public class CallPhone {
	
	TelephonyManager manager;
	 StatePhoneReceiver myPhoneStateListener;
	 boolean callFromApp=false; // To control the call has been made from the application
	 boolean callFromOffHook=false; // To control the change to idle state is from the app call
	 boolean isSilentCall=false;
	 
	 public void startCall(Context context, String phoneNumber, boolean isSilentCall) {
		 
		 Log.d("CallPhone", "startCall - start");
		 
		 this.isSilentCall= isSilentCall;
		
	    //To be notified of changes of the phone state create an instance
	    //of the TelephonyManager class and the StatePhoneReceiver class
	    myPhoneStateListener = new StatePhoneReceiver(context);
	    manager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
	
		manager.listen(myPhoneStateListener,PhoneStateListener.LISTEN_CALL_STATE); // start listening to the phone changes
	    callFromApp=true;
	    Intent i = new Intent(android.content.Intent.ACTION_CALL,
	                          Uri.parse("tel:+" + phoneNumber)); // Make the call      
	    //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity(i);   

		 
	 }
	 
	// Monitor for changes to the state of the phone
	 public class StatePhoneReceiver extends PhoneStateListener {
	     Context context;
	     public StatePhoneReceiver(Context context) {
	         this.context = context;
	     }
	 
	     @Override
	     public void onCallStateChanged(int state, String incomingNumber) {
	         super.onCallStateChanged(state, incomingNumber);
	         
			 Log.d("StatePhoneReceiver", "onCallStateChanged - start");

	         
	         switch (state) {
	         
	         case TelephonyManager.CALL_STATE_OFFHOOK: //Call is established
	          if (callFromApp) {
	              callFromApp=false;
	              callFromOffHook=true;
	              
	 			 Log.d("StatePhoneReceiver", "onCallStateChanged - call established");

	              // If not silent, turn on speaker
	              if(!isSilentCall){
	            	  try {
	  	                Thread.sleep(500); // Delay 0,5 seconds to handle better turning on loudspeaker
	  	              } catch (InterruptedException e) {
	  	              }
	  	           
	  	              //Activate loudspeaker
	  	              AudioManager audioManager = (AudioManager)
	  	                                          context.getSystemService(Context.AUDIO_SERVICE);
	  	              audioManager.setMode(AudioManager.MODE_IN_CALL);
	  	              audioManager.setSpeakerphoneOn(true);
	              }
	            
	           }
	           break;
	         
	        case TelephonyManager.CALL_STATE_IDLE: //Call is finished
	          if (callFromOffHook) {
	                callFromOffHook=false;
	                
		              
		 			 Log.d("StatePhoneReceiver", "onCallStateChanged - call hung up");

	                
	                if(!isSilentCall){
	                	AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		                audioManager.setMode(AudioManager.MODE_NORMAL); //Deactivate loudspeaker
	                }
	                
	                manager.listen(myPhoneStateListener, // Remove listener
	                      PhoneStateListener.LISTEN_NONE);
	                
	                
	             }
	          break;
	         }
	     }
	 }
	 
}
