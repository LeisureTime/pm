package com.xlabz;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * @author Ramesh.D
 * This Main class is the first Invoked Activity
 */

public class Main extends Activity {
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
     
        
     
      //BELOW IS SIGN BUTTON EVENT     
     Button signBt =    (Button) findViewById(R.id.signInBt);
     signBt.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
		final Dialog d =new Dialog(Main.this);
			d.setContentView(R.layout.sign_in);
			d.setTitle("Password");
			
			Button loginBT =    (Button) d.findViewById(R.id.loginBT);
			loginBT.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					EditText password = (EditText) d.findViewById(R.id.password);					
					SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
					if(password.getText().toString().equals(settings.getString("settings_password", ""))){
						Intent intent = new Intent(Main.this, PrivateMessage.class);
						startActivity(intent);
					}else{
						Toast.makeText(Main.this, "Wrong Password !! ",3).show();
					}
				}
			}); 
			
			d.show();
		}
	});
     
    
    //  BELOW IS SIGN UP BUTTON EVENT      
     Button signupBt =    (Button) findViewById(R.id.signUpBt);
     signupBt.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
			final	Dialog  d =new Dialog(Main.this);
			d.setContentView(R.layout.sign_up);
			d.setTitle("Sign Up");
			
			Button frm_signupBt =    (Button) d.findViewById(R.id.frm_signupBt);
		     frm_signupBt.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					EditText nickName = (EditText) d.findViewById(R.id.nickNameText);
					EditText password = (EditText) d.findViewById(R.id.passwordText);
					EditText confirmpassword = (EditText) d.findViewById(R.id.confirmpasswordText);
					
					if(!nickName.getText().toString().equals("") && password.getText().toString().equals(confirmpassword.getText().toString())){
						SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putString("settings_nickname", nickName.getText().toString());
						editor.putString("settings_password", confirmpassword.getText().toString());
						editor.commit();
						
						Intent intent = new Intent(Main.this, PrivateMessage.class);
						startActivity(intent);
					}else{
						Toast.makeText(Main.this, "Both password & confirm password should be same !!",3).show();
					}
				}
			}); 
			
			d.show();
		}
	});
    
     	//GET DETAILS FROM PREFRENCES
		SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
		String mypassword = settings.getString("settings_password", "");
		
		//GET DETAILS FROM SHARED PREFERENCES
		SharedPreferences pref_settings = PreferenceManager.getDefaultSharedPreferences(this);
		boolean is_password = pref_settings.getBoolean("user_password", false);
		//Log.d("PASSWORD chk",is_password +"---"+ mypassword);
		// CHECK, IF USER MARKED AS "SIGN IN" FORCE
		if (!is_password && !mypassword.equals("")) {
			Intent intent = new Intent(this, PrivateMessage.class);
			startActivity(intent);
		} 
    }
    
 @Override
protected void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
}
}
