package com.ggt.slidescast.utils;

import com.ggt.slidescast.R;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultRes;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * App preferences.
 */
@SharedPref(value = SharedPref.Scope.APPLICATION_DEFAULT)
public interface SlidesCastPrefs {

    //general persistent useful value
    @DefaultBoolean(true)
    boolean isFirstLaunch();

    //slides cast prefs
    @DefaultRes(R.string.slidescast_refresh_default_value)
    String refreshInterval();

    @DefaultBoolean(true)
    boolean keepScreenOn();

    //slideshare prefs

    @DefaultRes(R.string.slideshare_username_default_value)
    String username();

    @DefaultRes(R.string.slideshare_password_default_value)
    String password();

    String lastSlideShareSearch();

    @DefaultRes(R.string.slideshare_search_lang_default_value)
    String slideShareSearchLanguage();

}
