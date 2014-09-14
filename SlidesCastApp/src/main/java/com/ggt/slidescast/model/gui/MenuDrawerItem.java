package com.ggt.slidescast.model.gui;

import android.support.v4.app.Fragment;

import com.ggt.slidescast.utils.GLog;

/**
 * Item for the menu drawer.
 *
 * @author guiguito
 */
public class MenuDrawerItem {

    private String name;
    private String title;
    private Class classType;
    private int iconResource = NO_VALUE;
    private int iconResourceSelected = NO_VALUE;
    private String iconifyValue;
    private int iconifyColor = NO_VALUE;

    public static int NO_VALUE = -1;

    public MenuDrawerItem(String name, String title, Class classType, int iconResource, int iconResourceSelected) {
        this.name = name;
        this.title = title;
        this.classType = classType;
        this.iconResource = iconResource;
        this.iconResourceSelected = iconResourceSelected;
    }

    public MenuDrawerItem(String name, String title, Class classType, String iconifyValue) {
        this.name = name;
        this.title = title;
        this.classType = classType;
        this.iconifyValue = iconifyValue;
    }

    public MenuDrawerItem(String name, String title, Class classType, String iconifyValue, int iconifyColor) {
        this.name = name;
        this.title = title;
        this.classType = classType;
        this.iconifyValue = iconifyValue;
        this.iconifyColor = iconifyColor;
    }

    public CharSequence getName() {
        return name;
    }

    public CharSequence getTitle() {
        return title;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public int getIconResourceSelected() {
        return iconResourceSelected;
    }

    public void setIconResourceSelected(int iconResourceSelected) {
        this.iconResourceSelected = iconResourceSelected;
    }

    public String getIconifyValue() {
        return iconifyValue;
    }

    public void setIconifyValue(String iconifyValue) {
        this.iconifyValue = iconifyValue;
    }

    public int getIconifyColor() {
        return iconifyColor;
    }

    public void setIconifyColor(int iconifyColor) {
        this.iconifyColor = iconifyColor;
    }

    public Fragment getFragment() {
        try {
            return (Fragment) classType.newInstance();
        } catch (InstantiationException e) {
            GLog.e(getClass().getName(), e.getMessage());
        } catch (IllegalAccessException e) {
            GLog.e(getClass().getName(), e.getMessage());
        }
        return null;
    }

}
