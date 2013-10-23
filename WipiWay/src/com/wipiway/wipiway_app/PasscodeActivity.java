package com.wipiway.wipiway_app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PasscodeActivity extends Activity {
	
	private String p1;
	private String p2;
	private String p3;
	private String p4;
	
	private String passcodeAttempt1;
	private String passcodeAttempt2;
	
	private boolean isFirstAttempt;
	private boolean flagCheckingCurrentPasscode=false;
	
	private int passcodeFlow;
	
	EditText editTextPasscode1;
	EditText editTextPasscode2;
	EditText editTextPasscode3;
	EditText editTextPasscode4;
	TextView textViewPasscodeInput;
	
	// Will be needed when removing
	GenericTextWatcher textWatcherPasscode1;
	GenericTextWatcher textWatcherPasscode2;
	GenericTextWatcher textWatcherPasscode3;
	GenericTextWatcher textWatcherPasscode4;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_passcode);
		
		initializeVariables();
		
		switch (passcodeFlow) {
		case WipiwayUtils.INTENT_EXTRA_VALUE_NEW_PASSCODE_FLOW:
			resetPasscodeInput(getResources().getString(R.string.enter_new_passcode), true);

			break;
		case WipiwayUtils.INTENT_EXTRA_VALUE_CHANGE_PASSCODE_FLOW:
			changePasscode();
			break;
		default:
			break;
		}
		
		
		
	}
	
	private void changePasscode() {
		flagCheckingCurrentPasscode = true;
		resetPasscodeInput(getResources().getString(R.string.enter_your_current_passcode), true);

		
	}



	private void resetPasscodeInput(String labelText, boolean isFirstAttempt){
		
		this.isFirstAttempt = isFirstAttempt;
		
		if(isFirstAttempt){
			passcodeAttempt1 = "";
			passcodeAttempt2 = "";
		}

		
		p1 = "";
		p2 = "";
		p3 = "";
		p4 = "";
		 
		textViewPasscodeInput.setText(labelText);
		
		editTextPasscode1.removeTextChangedListener(textWatcherPasscode1);
		editTextPasscode2.removeTextChangedListener(textWatcherPasscode2);
		editTextPasscode3.removeTextChangedListener(textWatcherPasscode3);
		editTextPasscode4.removeTextChangedListener(textWatcherPasscode4);
		
		editTextPasscode1.setText("");
		editTextPasscode2.setText("");
		editTextPasscode3.setText("");
		editTextPasscode4.setText("");

		editTextPasscode1.addTextChangedListener(textWatcherPasscode1);
		editTextPasscode2.addTextChangedListener(textWatcherPasscode2);
		editTextPasscode3.addTextChangedListener(textWatcherPasscode3);
		editTextPasscode4.addTextChangedListener(textWatcherPasscode4);
		
  	    editTextPasscode1.requestFocus();

		
	}
	
	private class GenericTextWatcher implements TextWatcher{

	    private View view;
	    private GenericTextWatcher(View view) {
	        this.view = view;
	    }

	    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
	    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

	    public void afterTextChanged(Editable editable) {
	        String text = editable.toString();
	        switch(view.getId()){
	            case R.id.editTextPasscode1:
		        	  p1 = text;
		        	  editTextPasscode2.requestFocus();
	                break;
	            case R.id.editTextPasscode2:
		        	  p2 = text;
		        	  editTextPasscode3.requestFocus();
	                break;
	            case R.id.editTextPasscode3:
		        	  p3 = text;
		        	  editTextPasscode4.requestFocus();
	                break;
	            case R.id.editTextPasscode4:
		        	  p4 = text;
		        	  checkInput();
	                break;
	        }
	    }
	}
	
	private void initializeVariables(){
		textViewPasscodeInput = (TextView) findViewById(R.id.textViewPasscodeInput);
		
		editTextPasscode1 = (EditText) findViewById(R.id.editTextPasscode1);
		editTextPasscode2 = (EditText) findViewById(R.id.editTextPasscode2);
		editTextPasscode3 = (EditText) findViewById(R.id.editTextPasscode3);
		editTextPasscode4 = (EditText) findViewById(R.id.editTextPasscode4);

		textWatcherPasscode1 = new GenericTextWatcher(editTextPasscode1);
		textWatcherPasscode2 = new GenericTextWatcher(editTextPasscode2);
		textWatcherPasscode3 = new GenericTextWatcher(editTextPasscode3);
		textWatcherPasscode4 = new GenericTextWatcher(editTextPasscode4);
		
		passcodeFlow = getIntent().getIntExtra(WipiwayUtils.INTENT_EXTRA_KEY_PASSCODE_FLOW, 0);
	}
	
	private void checkInput() {
		Log.d("PasscodeActivity", "checkInput - " + p1+p2+p3+p4);
		
		if(flagCheckingCurrentPasscode){
			// First check current passcode
			passcodeAttempt1 = p1+p2+p3+p4;
			
			if(WipiwayUtils.isCorrectPasscode(PasscodeActivity.this, passcodeAttempt1)){
				flagCheckingCurrentPasscode = false;
				resetPasscodeInput(getResources().getString(R.string.enter_new_passcode), true);
			} else {
				// flagCheckingCurrentPasscode is still set so process will repeat
				Toast.makeText(getApplicationContext(), "Wrong Passcode! Try Again.", Toast.LENGTH_LONG).show();
				resetPasscodeInput(getResources().getString(R.string.enter_your_current_passcode), true);
				
			}
 
			
		} else {
			
			if(isFirstAttempt){
				passcodeAttempt1 = p1+p2+p3+p4;
				resetPasscodeInput(getResources().getString(R.string.re_enter_new_passcode), false);
				return;
			} else {
				passcodeAttempt2 = p1+p2+p3+p4;
				if(passcodeAttempt1.contentEquals(passcodeAttempt2)) {

					WipiwayUtils.setPasscode(PasscodeActivity.this, passcodeAttempt1);
					Toast.makeText(getApplicationContext(), "Passcode saved", Toast.LENGTH_LONG).show();
					
					Intent i;
					
					if(passcodeFlow == WipiwayUtils.INTENT_EXTRA_VALUE_NEW_PASSCODE_FLOW)
						 i = new Intent(PasscodeActivity.this, StatusActivity.class);
					else
						 i = new Intent(PasscodeActivity.this, SettingsActivity.class);

					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
					startActivity(i);
					
				} else {
					Toast.makeText(getApplicationContext(), "Passcode does not match", Toast.LENGTH_LONG).show();
					resetPasscodeInput(getResources().getString(R.string.re_enter_new_passcode), false);
				}
			}
			
		}
		
 
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.passcode, menu);
		return true;
	}

}
