package com.ggt.slidescast.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ggt.slidescast.R;
import com.ggt.slidescast.model.app.Presentation;
import com.ggt.slidescast.ui.listitems.PresentationListItem;
import com.ggt.slidescast.ui.listitems.PresentationListItem_;

import org.androidannotations.annotations.EBean;

/**
 * Presentations adapter. For slides.
 *
 * @author guiguito
 */
@EBean
public class PresentationsAdapter extends ArrayAdapter<Presentation> {

    public PresentationsAdapter(Context context) {
        super(context, R.layout.listitem_slideshow);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = PresentationListItem_.build(getContext());
        }
        Presentation presentation = getItem(position);
        ((PresentationListItem) convertView).bind(presentation, false);
        return convertView;
    }
}
