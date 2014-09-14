package com.ggt.slidescast.ui.adapters;

import org.androidannotations.annotations.EBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.widget.DataBufferAdapter;

/**
 * Class for google drive elements.
 * 
 * @author guiguito
 * 
 */
@EBean
public class DriveResultsAdapter extends DataBufferAdapter<Metadata> {

	public DriveResultsAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_1);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(getContext(), android.R.layout.simple_list_item_1, null);
		}
		Metadata metadata = getItem(position);
		TextView titleTextView = (TextView) convertView.findViewById(android.R.id.text1);
		titleTextView.setText(metadata.getTitle());
		return convertView;
	}
}
