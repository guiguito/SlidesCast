package com.ggt.slidescast;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.crashlytics.android.Crashlytics;
import com.ggt.slidescast.utils.SlidesCastPrefs_;
import com.google.sample.castcompanionlibrary.cast.DataCastManager;

/**
 * Application class for SlidesCast.
 * Init db and schedule auto refresh.
 * Hosts Chromecast datacastmanager.
 *
 * @author guiguito
 */
public class SlidesCastApplication extends Application {

    private static final String CAST_APP_ID = "077F04E0";// CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID;
    public static final String NAMESPACE = "urn:x-cast:com.ggt.slidescast";
    private static DataCastManager mDataCastManager;

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        scheduleAutomaticRefresh();
        Crashlytics.start(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

    public static DataCastManager getDataCastManager(Context ctx) {
        if (null == mDataCastManager) {
            mDataCastManager = DataCastManager.initialize(ctx.getApplicationContext(), CAST_APP_ID,
                    NAMESPACE);
            // mDataCastManager
            // .addDataCastConsumer(new SlidesCastDataCastConsumer());
        }
        return mDataCastManager;
    }

    private void scheduleAutomaticRefresh() {
        SlidesCastPrefs_ slidesCastPrefs = new SlidesCastPrefs_(this);
        if (slidesCastPrefs.isFirstLaunch().get()) {
            int day = Integer.parseInt(slidesCastPrefs.refreshInterval().get());
            AlarmManagerReceiver.setAlarm(this, day);
            slidesCastPrefs.edit().isFirstLaunch().put(false).apply();
        }
    }
}
