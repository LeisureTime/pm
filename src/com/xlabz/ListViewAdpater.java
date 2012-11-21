package com.xlabz;

import java.util.ArrayList;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xlabz.sql.NickNameAuth;
import com.xlabz.sql.NickNameAuthDataSource;
import com.xlabz.sql.PMessage;
import com.xlabz.sql.PMessageDataSource;

/*** 
 * @author Ramesh.D
 * This class listview adapter for Message list
 */

public class ListViewAdpater extends ArrayAdapter<String> {
	private final Activity context;
	private final ArrayList <String> msgs;
	private int[] read_color = new int[] {0x30808080};
	private PMessageDataSource datasource;
	private NickNameAuthDataSource nickNamedatasource;
	
	public ListViewAdpater(Activity context, ArrayList<String> msgs) {
		super(context, R.layout.sms_item, msgs);
		this.context = context;
		this.msgs = msgs;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.sms_item, null, true);
		TextView orginatedFromView = (TextView) rowView.findViewById(R.id.label1);
		TextView textView = (TextView) rowView.findViewById(R.id.label2);
		ImageView imageView1 = (ImageView) rowView.findViewById(R.id.keyword_icon1);
		
		if(MessageList.msgList.size()!=0){
			PMessage msg = MessageList.msgList.get(position);
			Log.d("MESSAGE LIST", position+" -- "+msg.getMessage());
			orginatedFromView.setText("["+msg.getOriginatingAddress()+"]");
			
			nickNamedatasource = new NickNameAuthDataSource(context);
			nickNamedatasource.open();
			ArrayList<NickNameAuth> nicknameauths = nickNamedatasource.getNickNameAuth(msg.getOriginatingAddress());
			if(nicknameauths.size() != 0){
				nicknameauths.get(0).getPassword();			
				
				// is msg read ??
				datasource = new PMessageDataSource(context);
        		datasource.open();
        		ArrayList<PMessage> list = datasource.getMessage(msg.getId());
        		Log.d("DATA:"+position, String.valueOf(msg.getId())+"---"+list.get(0).getId()+"---"+list.get(0).isIs_read());
        		if(list.size() != 0 && list.get(0).isIs_read()){
        			rowView.setBackgroundColor(read_color[0]);
        		}
        		datasource.close();
				
				try {
					String txt = Crypto.decrypt(nicknameauths.get(0).getPassword(), msg.getMessage());
					if(txt.length() > 20){
						txt = txt.substring(0, 19);
					}
					textView.setText(txt);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				String txt = msg.getMessage();
				if(txt.length() > 20){
					txt = txt.substring(0, 19);
				}
				textView.setText(txt);
			}			
			nickNamedatasource.close();
		}
		
		return rowView;
	}
}
