package com.ggt.slidescast.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ggt.slidescast.ui.views.SimplePagerItem;
import com.ggt.slidescast.utils.GLog;

import java.util.List;

/**
 * Adapter for slidesshare tabs, in the slideshare part of the app.
 *
 * @author guiguito
 */
public class SlideShareTabsAdapter extends FragmentStatePagerAdapter {

    List<SimplePagerItem> mTabs;
    FragmentManager mFragmentManager;

    public SlideShareTabsAdapter(FragmentManager fm, List<SimplePagerItem> tabs) {
        super(fm);
        mFragmentManager = fm;
        mTabs = tabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).getTitle();
    }

    private Fragment retrieveFragment(Class fragmentClass) {
        List<Fragment> fragments = mFragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragmentClass.equals(fragment.getClass())) {
                return fragment;
            }
        }
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        try {
            Fragment fragment = retrieveFragment(mTabs.get(position).getFragmentClass());
            if (fragment != null) {
                // dirty fix for navigation drawer + viewpager with
                // fragmentStatePagerAdapter. Case where fragments are leaked.
                mFragmentManager.beginTransaction().remove(fragment).commit();
            }
            fragment = (Fragment) mTabs.get(position).getFragmentClass().newInstance();
            return fragment;
        } catch (InstantiationException e) {
            GLog.e(this, e.getMessage());
            return null;
        } catch (IllegalAccessException e) {
            GLog.e(this, e.getMessage());
            return null;
        }
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

}
