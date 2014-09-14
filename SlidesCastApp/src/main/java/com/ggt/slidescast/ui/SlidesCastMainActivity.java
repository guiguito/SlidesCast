package com.ggt.slidescast.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.ggt.slidescast.R;
import com.ggt.slidescast.model.gui.MenuDrawerItem;
import com.ggt.slidescast.ui.adapters.MenuDrawerItemAdapter;
import com.ggt.slidescast.ui.fragments.HomeFragment;
import com.ggt.slidescast.ui.fragments.HomeFragment_;
import com.ggt.slidescast.ui.fragments.MyFilesFragment_;
import com.ggt.slidescast.ui.fragments.PreferencesFragment_;
import com.ggt.slidescast.ui.fragments.SlideShareFragment_;
import com.ggt.slidescast.utils.EventBusEvents;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Main activity, with menu initialization.
 *
 * @author guiguito
 */
@EActivity(R.layout.activity_slidescastmain)
public class SlidesCastMainActivity extends SlidesCastMotherActivity {

    @ViewById
    FrameLayout mContentFrame;
    @ViewById
    DrawerLayout mDrawerLayout;
    @ViewById
    ListView mLeftDrawer;

    @Bean
    MenuDrawerItemAdapter mMenuItemAdapter;

    ActionBarDrawerToggle mActionBarDrawerToggle;
    CharSequence mCharSequence;
    ArrayList<MenuDrawerItem> mItems;

    private static final int PARAMETERS_POSITION = 3;

    public static final String LOCAL_FILES_ICONIFY = "fa-home";

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @AfterViews
    void initViews() {
        mItems = new ArrayList<MenuDrawerItem>();
        mItems.add(new MenuDrawerItem(getString(R.string.tab_home), getString(R.string.app_name), HomeFragment_.class, LOCAL_FILES_ICONIFY, getResources().getColor(
                R.color.slidescast_red)));
        // items.add(new MenuDrawerItem("Drive", "Drive",
        // GoogleDriveFragment_.class), 0);
        mItems.add(new MenuDrawerItem(getString(R.string.tab_my_files), getString(R.string.tab_my_files), MyFilesFragment_.class, "fa-file-text",
                getResources().getColor(R.color.slidescast_green)));
        mItems.add(new MenuDrawerItem(getString(R.string.tab_slideshare), getString(R.string.tab_slideshare), SlideShareFragment_.class,
                R.drawable.slideshare_icon_menu2, R.drawable.slideshare_icon_menu_selected));
        mItems.add(PARAMETERS_POSITION, new MenuDrawerItem(getString(R.string.tab_settings), getString(R.string.tab_settings), PreferencesFragment_.class,
                "fa-cogs", getResources().getColor(R.color.slidescast_yellow)));
        mMenuItemAdapter.setItems(mItems);
        mLeftDrawer.setAdapter(mMenuItemAdapter);
        mLeftDrawer.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMenuItemAdapter.setSelected(position);
                onMenuDrawerItemClicked(position, mMenuItemAdapter.getItem(position));
            }

        });

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_navigation_drawer, R.string.app_name, R.string.app_name) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                setTitle(mCharSequence);
                supportInvalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle(mCharSequence);
                supportInvalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        onMenuDrawerItemClicked(0, mItems.get(0));
        mMenuItemAdapter.setSelected(0);
    }

    public void onEvent(EventBusEvents.ShowSettings event) {
        onMenuDrawerItemClicked(PARAMETERS_POSITION, mItems.get(PARAMETERS_POSITION));
    }

    public void onEventMainThread(HomeFragment.CastFromSlideShareEvent event) {
        onMenuDrawerItemClicked(2, mItems.get(2));
        mMenuItemAdapter.setSelected(2);
    }

    public void onEventMainThread(HomeFragment.CastFromLocalFilesEvent event) {
        onMenuDrawerItemClicked(1, mItems.get(1));
        mMenuItemAdapter.setSelected(1);
    }

    private void showFragment(Fragment fragment) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mContentFrame, fragment).commit();
    }

    private void changeItemMenu(int position, MenuDrawerItem menuDrawerItem) {
        // Highlight the selected item, update the title, and close the drawer
        mLeftDrawer.setItemChecked(position, true);
        mCharSequence = menuDrawerItem.getTitle();
        setTitle(menuDrawerItem.getTitle());
        showFragment(menuDrawerItem.getFragment());
        mDrawerLayout.closeDrawer(mLeftDrawer);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void onMenuDrawerItemClicked(int position, MenuDrawerItem menuDrawerItem) {
        changeItemMenu(position, menuDrawerItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_cast).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

}
