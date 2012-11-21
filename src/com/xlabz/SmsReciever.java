package com.xlabz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import com.xlabz.sql.PMessageDataSource;

public class SmsReciever extends BroadcastReceiver {
	private final static String APPEND_PHRASE = "^#@6F$7";
    private SmsMessage[] msgs;
    private Context con;
    private PMessageDataSource datasource;
	@Override
    public void onReceive(Context context, Intent intent) 
    {		
        Bundle bundle = intent.getExtras();        
        msgs = null;

        if (bundle != null)
        {
        	 String bodyText = ""; String originatingAddress =""; String dateRecieved ="";
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];           

            for (int i=0; i<msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                dateRecieved =   String.valueOf(msgs[i].getTimestampMillis());
                bodyText = msgs[i].getMessageBody().toString();
                originatingAddress =  msgs[i].getOriginatingAddress();
            }

            con = context;
            if(bodyText.contains(APPEND_PHRASE)){
            	datasource = new PMessageDataSource(con);
        		datasource.open();
        		bodyText = bodyText.replace(APPEND_PHRASE, "");
        		datasource.createMessage(originatingAddress, bodyText, dateRecieved);
        		datasource.close();	    
        		NotifierHelper.sendNotification(con, MessageList.class, "PM@"+originatingAddress, bodyText, 1, false, false);
	            abortBroadcast();	            
            }
        }
    }

}

