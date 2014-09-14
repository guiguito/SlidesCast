package com.ggt.slidescast.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ggt.slidescast.BuildConfig;
import com.ggt.slidescast.R;
import com.ggt.slidescast.SlidesCastApplication;
import com.ggt.slidescast.chromecast.SlidesCastCommand;
import com.ggt.slidescast.chromecast.SlidesCastEvent;
import com.ggt.slidescast.utils.GLog;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.gson.Gson;
import com.google.sample.castcompanionlibrary.cast.DataCastManager;
import com.google.sample.castcompanionlibrary.cast.exceptions.NoConnectionException;
import com.google.sample.castcompanionlibrary.cast.exceptions.TransientNetworkDisconnectionException;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify.IconValue;

import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * Mother activity of all activities within this app.
 *
 * @author guiguito
 */
public class SlidesCastMotherActivity extends ActionBarActivity {

    DataCastManager mDataCastManager;
    EventBus mEventBus;
    Gson mGson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventBus = EventBus.getDefault();
        mEventBus.register(this);
        DataCastManager.checkGooglePlayServices(this);
        mDataCastManager = SlidesCastApplication.getDataCastManager(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        mDataCastManager.addMediaRouterButton(menu, R.id.media_route_menu_item);
        if (BuildConfig.DEBUG) {
            menu.findItem(R.id.menu_reload).setIcon(new IconDrawable(this, IconValue.fa_refresh).colorRes(R.color.White).actionBarSize());
        } else {
            menu.findItem(R.id.menu_reload).setVisible(false);
        }
        menu.findItem(R.id.menu_cast).setIcon(new IconDrawable(this, IconValue.fa_play).colorRes(R.color.White).actionBarSize());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_reload:
                reload();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void sendCastCommand(SlidesCastCommand command) {
        DataCastManager dataCastManager = SlidesCastApplication.getDataCastManager(this);
        try {
            dataCastManager.sendDataMessage(mGson.toJson(command), SlidesCastApplication.NAMESPACE);
        } catch (IllegalArgumentException e) {
            GLog.e(getLocalClassName(), e.getMessage());
        } catch (IllegalStateException e) {
            GLog.e(getLocalClassName(), e.getMessage());
        } catch (IOException e) {
            GLog.e(getLocalClassName(), e.getMessage());
        } catch (TransientNetworkDisconnectionException e) {
            GLog.e(getLocalClassName(), e.getMessage());
        } catch (NoConnectionException e) {
            GLog.e(getLocalClassName(), "NoConnectionException" + e.getMessage());
        }
    }

    void handleError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void onEvent(SlidesCastEvent evt) {

    }

    void reload() {
        SlidesCastCommand command = new SlidesCastCommand();
        command.setCommand(SlidesCastCommand.Command.RELOAD);
        sendCastCommand(command);
    }
}
