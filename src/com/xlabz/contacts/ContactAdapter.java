package com.xlabz.contacts;

import java.util.List;

import com.xlabz.R;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<Contact> {

	private final List<Contact> _contacts;
	private final Activity _context;
	
	public ContactAdapter(Activity context, List<Contact> contacts)
	{
		super(context,R.layout.row, contacts);
		this._contacts=contacts;
		this._context=context;
	}
	@Override
	public View getDropDownView(int position, View convertView,
	ViewGroup parent) {
	// TODO Auto-generated method stub
	return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
	return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
	//return super.getView(position, convertView, parent);

	LayoutInflater inflater=_context.getLayoutInflater();
	View row=inflater.inflate(R.layout.row, parent, false);
	TextView label=(TextView)row.findViewById(R.id.weekofday);
	label.setText(_contacts.get(position).getDisplayName());

	ImageView icon=(ImageView)row.findViewById(R.id.icon);

	icon.setImageResource(R.drawable.avatar);

	return row;
	}
}
