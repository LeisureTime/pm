package com.xlabz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xlabz.sql.PMessage;
import com.xlabz.sql.PMessageDataSource;
import com.xlabz.sql.NickNameAuthDataSource;
/**
 * 
 * @author Ramesh.D
 *  This class list the messages that are sent..
 */
public class MessageList extends ListActivity {
	private PMessageDataSource datasource;
	private NickNameAuthDataSource nickNamedatasource;
	static final ArrayList<PMessage> msgList = new ArrayList<PMessage>();
	
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_list);


		datasource = new PMessageDataSource(this);
		datasource.open();
		msgList.clear();
		msgList.addAll(datasource.getAllMessages());
		Collections.reverse(msgList);
		Iterator itr = msgList.iterator();
		ArrayList<String> txtmsg = new ArrayList<String>();
		while(itr.hasNext()){
			PMessage msg = (PMessage)itr.next();
			txtmsg.add(msg.getMessage());
		}
		
		Log.d("PM - COUNT: ", String.valueOf(msgList.size()));
		setListAdapter(new ListViewAdpater(this,txtmsg));
		datasource.close();
		
		final ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
	        final PMessage msg = msgList.get(position);
	        	// OPEN POP
	        	nickNamedatasource = new NickNameAuthDataSource(MessageList.this);
				nickNamedatasource.open();
				List list = nickNamedatasource.getNickNameAuth(msg.getOriginatingAddress());
				nickNamedatasource.close();
				
				
				//GET DETAILS FROM SHARED PREFERENCES
				SharedPreferences pref_settings = PreferenceManager.getDefaultSharedPreferences(MessageList.this);
				boolean is_FN_allowed = pref_settings.getBoolean("friend_nickname", false);
				
				if(list.size() == 0 || is_FN_allowed){ // NOT YET REGISTERED TO OUR APPS
		        	final Dialog d =new Dialog(MessageList.this);
					d.setContentView(R.layout.nickname_in);
					d.setTitle("NickName");
					
					Button nickCheckBT =    (Button) d.findViewById(R.id.nickCheckBT);
					nickCheckBT.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {							
							EditText nickName_password = (EditText) d.findViewById(R.id.nickName_password);
							String decyptedText = "";
								try {
									//decyptedText = Crypto.decrypt(nickName_password.getText().toString(), msg.getMessage());
									decyptedText = msg.getMessage();
									Log.d("DECRYPTED TEXT", decyptedText);
								} catch (Exception e) {
									e.printStackTrace();
								}
							if(!decyptedText.equals("")){
								nickNamedatasource = new NickNameAuthDataSource(MessageList.this);
								nickNamedatasource.open();
								if(nickNamedatasource.getNickNameAuth(msg.getOriginatingAddress()).size() == 0){
									nickNamedatasource.createNickNameAuth(msg.getOriginatingAddress(), "----", nickName_password.getText().toString());
								}
								//update password saved.. to 1
								nickNamedatasource.updateNickNameAuth(msg.getOriginatingAddress());
								
				        		nickNamedatasource.close();
				        		
				        		// update is read to 1
				        		datasource.open();
				        		datasource.updateMessage(msg.getId());
				        		datasource.close();
								
								Intent intent = new Intent(MessageList.this, PrivateMessage.class);
								intent.putExtra("decyptedText", decyptedText);
								intent.putExtra("originatingAddress", msg.getOriginatingAddress());
								startActivity(intent);
						  }else{
								Toast.makeText(MessageList.this, "Wrong NickName !! ",3).show();
						  }
					  }
					}); 					
					d.show();				
				}else{ // WHEN MSG IS RECIPIENT ALREADY REGISTERED...					
					// update is read to 1
	        		datasource.open();
	        		datasource.updateMessage(msg.getId());
	        		datasource.close();
					
					Intent intent = new Intent(MessageList.this, PrivateMessage.class);
					intent.putExtra("decyptedText", ((TextView)v.findViewById(R.id.label2)).getText().toString());
					intent.putExtra("originatingAddress", msg.getOriginatingAddress());
					startActivity(intent);
				}
	        }});
	}
		
	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
}
