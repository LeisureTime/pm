package com.xlabz.contacts;

public class Contact {
	private String _id;
	private String _displayName;
	private String _phone_no;
	public String getPhone_no(){return _phone_no;}
	public void setPhone_no(String phone_no){_phone_no=phone_no;}
	public String getId(){return _id;}
	public String getDisplayName(){return _displayName;}
	public void setId(String id){_id=id;}
	public void setDisplayName(String displayName){_displayName=displayName;}
}
