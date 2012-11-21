package com.xlabz;

import java.util.List;

import android.widget.AdapterView;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xlabz.contacts.Contact;
import com.xlabz.contacts.ContactAdapter;
import com.xlabz.contacts.ContactList;
public class PrivateMessage extends Activity implements
AdapterView.OnItemSelectedListener{
	
	String[] DayOfWeek = {"Sunday", "Monday", "Tuesday",
			"Wednesday", "Thursday", "Friday", "Saturday"};
	
	final static String APPEND_PHRASE = "^#@6F$7";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		//CONTACTS SET IN SPINNER
		ContactList contactList = getContactNumbers(PrivateMessage.this);
		final List<Contact> contacts = contactList.getContacts();
        ArrayAdapter<Contact> adapter=new ContactAdapter(this,contacts);
        final Spinner spinner = (Spinner)	findViewById(R.id.spinner1);
		spinner.setAdapter(adapter);
		//---- ENDS
		
		TextView msgText = (TextView) findViewById(R.id.repliedTxt);		
		final Bundle extras = getIntent().getExtras(); 
		final SharedPreferences  settings = getSharedPreferences("MYPREFS", 0);

		Button spreadWordBt = (Button) findViewById(R.id.spreadWordBt);
		spreadWordBt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String messageText = ((EditText) findViewById(R.id.messageText)).getText().toString();
				String phone_no = contacts.get(spinner.getSelectedItemPosition()).getPhone_no();
				Log.d("PHONE NO????: ",phone_no);
			 if (messageText.equals("")) {
					Toast.makeText(PrivateMessage.this, "Fill Message ", 3).show();
				} else {
					try {
						//String ecryptedText = Crypto.encrypt(settings.getString("settings_nickname", ""),messageText);
						//ecryptedText = ecryptedText.substring(0, 4)+ APPEND_PHRASE + ecryptedText.substring(4);
						//Log.d("ECRYPTEDTEXT ????: ",settings.getString("settings_nickname", "") +" -- "+ecryptedText);
						Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + phone_no ) ); 
						intent.putExtra( "sms_body", Rot13.rot(messageText));
						//sendBroadcast(intent);
						startActivity( intent ); 						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		Button inboxBt = (Button) findViewById(R.id.inboxBt);
		inboxBt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(PrivateMessage.this,
						MessageList.class);
				startActivity(intent);
			}
		});
		
		Button settingBt = (Button) findViewById(R.id.settingBt);
		settingBt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(PrivateMessage.this,Preferences.class);
				startActivity(intent);
			}
		});
		
	
		if(extras != null){
			msgText.setVisibility(View.VISIBLE);
			spinner.setVisibility(View.GONE);
		 msgText.setText(extras.getString("decyptedText"));
		}else{
			msgText.setVisibility(View.GONE);
		}
	}
   
	
	/** Access Contacts from device
	 * @param context
	 * @return
	 */
   public static ContactList getContactNumbers(Context context) {
	   ContactList contactList=new ContactList();
       String contactNumber = null;
       int contactNumberType = Phone.TYPE_MOBILE;
       String nameOfContact = null;
       
           ContentResolver cr = context.getContentResolver();
           Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                   null, null, null);
           if (cur.getCount() > 0) {
               while (cur.moveToNext()) {
                   String id = cur.getString(cur.getColumnIndex(BaseColumns._ID));
                   nameOfContact = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                   if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                       Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                       null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                               + " = ?", new String[] { id },
                                       null);

                       while (phones.moveToNext()) {
                    	   Contact c =new Contact();
                           contactNumber = phones.getString(phones
                                   .getColumnIndex(Phone.NUMBER));
                           contactNumberType = phones.getInt(phones
                                   .getColumnIndex(Phone.TYPE));
                           Log.i("------->", "...Contact Name ...." + nameOfContact
                                   + "...contact Number..." + contactNumber);
                        c.setId(id);
               			c.setDisplayName(nameOfContact);
               			c.setPhone_no(contactNumber);
               			contactList.addContact(c);

                       }
                       phones.close();
                   }
               }
           }// end of contact name cursor
           cur.close();
           return contactList;
       }

@Override
public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	// TODO Auto-generated method stub
	
}

@Override
public void onNothingSelected(AdapterView<?> arg0) {
	// TODO Auto-generated method stub
	
}
   
}