package com.wipiway.wipiway_app;


import java.util.ArrayList;
import java.util.HashMap;

import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
        
        
        if(!WipiwayUtils.isPasscodeSet(this)){
        	Intent i = new Intent(this, PasscodeActivity.class);
        	i.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_PASSCODE_FLOW, WipiwayUtils.INTENT_EXTRA_VALUE_NEW_PASSCODE_FLOW);
        	startActivity(i);
        	
        	finish(); // Clears this current activity from the back stack
        	
        }
        
        populateRecentActivity();
        
        
        // Comment of http://stackoverflow.com/a/3859298/804503 -- time_ago_in_words
       // DateUtils.getRelativeTimeSpanString(startTime);

    }
	


	private void populateRecentActivity() {
		
		WipiwayDataSource datasource = new WipiwayDataSource(StatusActivity.this);
		
		ArrayList<RecentLog> recentLogList = datasource.getRecentLogList();
		
		ListView listViewRecentActivity = (ListView) findViewById(R.id.listViewRecentActivity);
		

		listViewRecentActivity.setAdapter(new RecentLogListAdapter(recentLogList, StatusActivity.this));
		listViewRecentActivity.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
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
	
	private class RecentLogListAdapter extends BaseAdapter {
		private ArrayList<RecentLog> recentLogList;
		private LayoutInflater layoutInflater;

		public RecentLogListAdapter(ArrayList<RecentLog> recentLogList,
				Context context) {

			this.recentLogList = recentLogList;
			layoutInflater = LayoutInflater.from(context);

		}

		public int getCount() {
			return recentLogList.size();
		}

		public Object getItem(int position) {
			return recentLogList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder myViewHolder = null;

			if (convertView == null) {
				convertView = layoutInflater
						.inflate(R.layout.recent_activity_row, null);

				myViewHolder = new ViewHolder();
				myViewHolder.textViewLogText = (TextView) convertView
						.findViewById(R.id.textViewLogText);
				myViewHolder.textViewTimeAgo = (TextView) convertView
						.findViewById(R.id.textViewTimeAgo);
				myViewHolder.imageViewIcon = (ImageView) convertView
						.findViewById(R.id.row_icon);

				convertView.setTag(myViewHolder);

			} else {
				myViewHolder = (ViewHolder) convertView.getTag();
			}

			myViewHolder.textViewLogText.setText(recentLogList.get(
					position).getLogText());
			myViewHolder.textViewTimeAgo.setText(recentLogList.get(
					position).getTimeAgo());
			
			myViewHolder.imageViewIcon.setImageResource(recentLogList
					.get(position).getIconResourceId());

			return convertView;
		}

		class ViewHolder {
			TextView textViewLogText = null;
			TextView textViewTimeAgo = null;

			ImageView imageViewIcon = null;

		}

	}
	
	/**
	 * Method to perform the action from the intent that was sent from the IntentService. 
	 * 
	 * Sometimes, this main activity needs to be run on the phone and then the action needs to be performed. 
	 * For example: This activity is shown just before performing the calling action. 
	 * 
	 * It is not necessary to open the activity for the actions but since the screen in going to be woken up, 
	 * it is a good idea to show Wipiway for the branding. 
	 * 
	 * @param intent Intent object coming from the calling component (IntentService)
	 * @param action Action to be performed. This could have been extracted from the intent but this has already been done before
	 */
	public void performAction(Intent intent, int action) {
		Context context = StatusActivity.this;
		switch(action) {
		case WipiwayUtils.USER_ACTION_CALL:
		case WipiwayUtils.USER_ACTION_CALL_ME:
		case WipiwayUtils.USER_ACTION_CALL_ME_PHONE:
		case WipiwayUtils.USER_ACTION_CALL_ME_SILENT:
		case WipiwayUtils.USER_ACTION_CALL_ME_PHONE_SILENT:
			
			String phoneNumber = intent.getStringExtra(WipiwayUtils.INTENT_EXTRA_KEY_SMS_SENDER_PHONE_NUMBER);
			boolean isSilentCall = intent.getBooleanExtra(WipiwayUtils.INTENT_EXTRA_KEY_IS_SILENT_CALL, false);
			
			CallPhone callPhone = new CallPhone();
			callPhone.startCall(context, phoneNumber, isSilentCall);
			
			break;
		
		case WipiwayUtils.USER_ACTION_OPEN_LINK:
			String url = intent.getStringExtra(WipiwayUtils.INTENT_EXTRA_LINK_URL);
			WipiwayUtils.openUrl(context, url);	
			break;
			
			
		
		}
		
	}

    
}
