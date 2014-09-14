package com.ggt.slidescast.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ggt.slidescast.model.LocalFile;

import org.androidannotations.annotations.EBean;

/**
 * Adapters for local files.
 *
 * @author guiguito
 */
@EBean
public class LocalFilesAdapter extends ArrayAdapter<LocalFile> {

    public LocalFilesAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), android.R.layout.simple_list_item_1, null);
        }
        LocalFile localFile = getItem(position);
        TextView titleTextView = (TextView) convertView.findViewById(android.R.id.text1);
        titleTextView.setText(localFile.getFileName());
        return convertView;
    }

}
