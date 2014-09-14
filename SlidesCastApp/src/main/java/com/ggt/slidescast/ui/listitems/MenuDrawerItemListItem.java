package com.ggt.slidescast.ui.listitems;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ggt.slidescast.R;
import com.ggt.slidescast.model.gui.MenuDrawerItem;
import com.joanzapata.android.iconify.Iconify;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * The layout for a menu item.
 *
 * @author guiguito
 */
@EViewGroup(R.layout.listitem_menudraweritem)
public class MenuDrawerItemListItem extends LinearLayout {

    MenuDrawerItem mMenuDrawerItem;

    @ViewById
    TextView mMenuDrawerItemNameTextView, mMenuDrawerItemNameIconifyView;

    @ViewById
    ImageView mMenuDrawerItemNameImageView;

    public MenuDrawerItemListItem(Context context) {
        super(context);
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected) {
            mMenuDrawerItemNameTextView.setTextColor(getContext().getResources().getColor(R.color.White));
            mMenuDrawerItemNameIconifyView.setTextColor(getContext().getResources().getColor(R.color.White));
            setBackgroundColor(getContext().getResources().getColor(R.color.slidescast_turquoise));
            if (mMenuDrawerItem.getIconResourceSelected() != MenuDrawerItem.NO_VALUE) {
                mMenuDrawerItemNameImageView.setImageResource(mMenuDrawerItem.getIconResourceSelected());
            }
        } else {
            mMenuDrawerItemNameTextView.setTextColor(getContext().getResources().getColor(R.color.Black));
            setBackgroundColor(getContext().getResources().getColor(R.color.White));
            mMenuDrawerItemNameIconifyView.setTextColor(mMenuDrawerItem.getIconifyColor());
            if (mMenuDrawerItem.getIconResource() != MenuDrawerItem.NO_VALUE) {
                mMenuDrawerItemNameImageView.setImageResource(mMenuDrawerItem.getIconResource());
            }
        }
    }

    public void bind(MenuDrawerItem menuDrawerItem) {
        mMenuDrawerItem = menuDrawerItem;
        mMenuDrawerItemNameIconifyView.setVisibility(View.GONE);
        mMenuDrawerItemNameImageView.setVisibility(View.GONE);
        if (menuDrawerItem.getIconResource() != MenuDrawerItem.NO_VALUE) {
            mMenuDrawerItemNameImageView.setVisibility(View.VISIBLE);
            mMenuDrawerItemNameImageView.setImageResource(menuDrawerItem.getIconResource());
        }
        if (menuDrawerItem.getIconifyValue() != null) {
            mMenuDrawerItemNameIconifyView.setVisibility(View.VISIBLE);
            mMenuDrawerItemNameIconifyView.setText("{" + menuDrawerItem.getIconifyValue() + "}");
            Iconify.addIcons(mMenuDrawerItemNameIconifyView);
            if (menuDrawerItem.getIconifyColor() != MenuDrawerItem.NO_VALUE) {
                mMenuDrawerItemNameIconifyView.setTextColor(menuDrawerItem.getIconifyColor());
            }
        }
        mMenuDrawerItemNameTextView.setText(menuDrawerItem.getName());
    }
}
