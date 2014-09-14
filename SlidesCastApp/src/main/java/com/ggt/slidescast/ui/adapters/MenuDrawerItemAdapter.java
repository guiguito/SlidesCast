package com.ggt.slidescast.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ggt.slidescast.model.gui.MenuDrawerItem;
import com.ggt.slidescast.ui.listitems.MenuDrawerItemListItem;
import com.ggt.slidescast.ui.listitems.MenuDrawerItemListItem_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for menu items.
 *
 * @author guiguito
 */
@EBean
public class MenuDrawerItemAdapter extends BaseAdapter {

    List<MenuDrawerItem> mItems = new ArrayList<MenuDrawerItem>();

    @RootContext
    Context mContext;

    long mSelected = -1;

    public void setItems(List<MenuDrawerItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MenuDrawerItemListItem menuItemLinearLayout;
        if (convertView == null) {
            menuItemLinearLayout = MenuDrawerItemListItem_.build(mContext);
        } else {
            menuItemLinearLayout = (MenuDrawerItemListItem) convertView;
        }

        menuItemLinearLayout.bind(getItem(position));
        if (mSelected == position) {
            menuItemLinearLayout.setSelected(true);
        } else {
            menuItemLinearLayout.setSelected(false);
        }
        return menuItemLinearLayout;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public MenuDrawerItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelected(int position) {
        mSelected = position;
    }
}
