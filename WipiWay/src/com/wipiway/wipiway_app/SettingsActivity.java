package com.wipiway.wipiway_app;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

public class SettingsActivity extends SherlockPreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        
        
        Preference passcodePreference  = (Preference) findPreference("prefsKeyPasscode");
        
        passcodePreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
	        	Intent i = new Intent(SettingsActivity.this, PasscodeActivity.class);
	        	i.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_PASSCODE_FLOW, WipiwayUtils.INTENT_EXTRA_VALUE_CHANGE_PASSCODE_FLOW);
	        	startActivity(i);
	        	
				return false;
			}
		});
        
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
        	Intent i = new Intent(SettingsActivity.this, StatusActivity.class);
        	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
        	startActivity(i);			
        	return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
