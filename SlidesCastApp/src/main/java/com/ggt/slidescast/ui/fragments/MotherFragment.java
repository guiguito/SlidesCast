package com.ggt.slidescast.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ggt.slidescast.chromecast.SlidesCastEvent;
import com.ggt.slidescast.utils.GLog;

import de.greenrobot.event.EventBus;

/**
 * Mother fragment.
 *
 * @author guiguito
 */
public class MotherFragment extends Fragment {

    EventBus mEventBus;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        GLog.v(this, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLog.v(this, "onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        GLog.v(this, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        GLog.v(this, "onResume");
        mEventBus = EventBus.getDefault();
        mEventBus.register(this);
    }

    public void onEvent(SlidesCastEvent evt) {

    }

    @Override
    public void onPause() {
        super.onPause();
        GLog.v(this, "onPause");
        mEventBus.unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GLog.v(this, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GLog.v(this, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        GLog.v(this, "onDetach");
    }

}
