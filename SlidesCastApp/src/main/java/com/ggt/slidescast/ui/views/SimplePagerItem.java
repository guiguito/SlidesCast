package com.ggt.slidescast.ui.views;

/**
 * Pager item for play store like pager.
 *
 * @author guiguito
 */
public class SimplePagerItem {

    private String title;
    private int tabColor;
    private int dividerColor;
    private Class fragmentClass;

    public SimplePagerItem(String title, int tabColor, int dividerColor, Class fragmentClass) {
        super();
        this.title = title;
        this.tabColor = tabColor;
        this.dividerColor = dividerColor;
        this.fragmentClass = fragmentClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTabColor() {
        return tabColor;
    }

    public void setTabColor(int tabColor) {
        this.tabColor = tabColor;
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
    }

    public Class getFragmentClass() {
        return fragmentClass;
    }

    public void setFragmentClass(Class fragmentClass) {
        this.fragmentClass = fragmentClass;
    }

}
