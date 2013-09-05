package com.wipiway.wipiway_app;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class StatusActivity extends BaseActivity {

    public StatusActivity() {
		super(R.string.title_status_page);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
    }



    
}
