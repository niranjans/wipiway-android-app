package com.wipiway.wipiway_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class WipiwayUtils {
	
	public static final String FLAG_FIRST_VISIT = "FLAG_FIRST_VISIT";
	
//	public static boolean isSmsServiceRegistered(Context context) {
//		
//		return 
//				
//	}
//	
	public static boolean isFirstVisit(Context context) {
		
		// Returns true if this is first visit of the user
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(WipiwayUtils.FLAG_FIRST_VISIT, true); 
				
	}
	
	public static void unsetFirstVisit(Context context, boolean flag) {
		// Set flag to false
		if(isFirstVisit(context)){
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean(WipiwayUtils.FLAG_FIRST_VISIT, false);
		}
				
	}

	
	


}
