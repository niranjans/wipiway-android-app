package com.wipiway.wipiway_app;


import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class StatusActivity extends BaseActivity {
	 
	private WipiwayController wipiwayController = null;
	private Button button1;
	private Button button2;
	
	

    public StatusActivity() {
		super(R.string.title_status_page);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        
        setSlidingActionBarEnabled(false);
        // Testing database insert
  /*      
        button1 = (Button) findViewById(R.id.button1);
        
        button1.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
            public void onClick(View v) {
            	
                WipiwayDataSource datasourse = new WipiwayDataSource(StatusActivity.this);
                
                datasourse.insertActionStatus("222222222", 2, 1, 3, null);
            
            }
        });
        
        button2 = (Button) findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
            public void onClick(View v) {
            	
                WipiwayDataSource datasourse = new WipiwayDataSource(StatusActivity.this);
                
                ContentValues values = datasourse.getLastSession("222222222");
                datasourse.updateActionStatus(values.getAsInteger(WipiwaySQLiteHelper.C_ID), 9, 9, 9, null);
            }
        });
        

        ActionStatus as = new ActionStatus();
        long id = as.getId();
        
        if( id == null) {
        	
        }

   */
        

    }
	


	@Override
	public void onNewIntent(Intent intent){
		Log.d("StatucActivity", "on new intent");
		

		
	}

    
}
