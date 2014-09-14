package com.ggt.slidescast.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.support.v4.preference.PreferenceFragment;

import com.ggt.slidescast.AlarmManagerReceiver;
import com.ggt.slidescast.R;
import com.ggt.slidescast.utils.SlidesCastPrefs_;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.sharedpreferences.Pref;

import de.psdev.licensesdialog.LicensesDialogFragment;

/**
 * GUI for preferences of the app.
 *
 * @author guiguito
 */
@EFragment
public class PreferencesFragment extends PreferenceFragment {

    private static final int CANCEL_ALARM = -1;

    @Pref
    SlidesCastPrefs_ mSlidesCastPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        getPreferenceScreen().findPreference(getString(R.string.opensource_key)).setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                final LicensesDialogFragment fragment = LicensesDialogFragment.newInstance(R.raw.notices, false);
                fragment.show(getActivity().getSupportFragmentManager(), null);
                return true;
            }
        });
        getPreferenceScreen().findPreference(getString(R.string.rate_key)).setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
                return true;
            }
        });
        getPreferenceScreen().findPreference(getString(R.string.slidescast_refresh_key)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int value = Integer.parseInt(newValue.toString());
                mSlidesCastPrefs.edit().refreshInterval().put("" + value).apply();
                ((ListPreference) getPreferenceScreen().findPreference(getString(R.string.slidescast_refresh_key))).setValue(newValue.toString());
                if (value != CANCEL_ALARM) {
                    AlarmManagerReceiver.setAlarm(getActivity().getApplicationContext(), value);
                } else {
                    AlarmManagerReceiver.cancelAlarm(getActivity().getApplicationContext());
                }
                return false;
            }
        });
    }
}
