package com.ggt.slidescast.chromecast;

import com.ggt.slidescast.utils.GLog;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.common.api.Status;
import com.google.sample.castcompanionlibrary.cast.callbacks.DataCastConsumerImpl;

import de.greenrobot.event.EventBus;

/**
 * Class to update if we want to listen messages from chromecast.
 *
 * @author guiguito
 */
public class SlidesCastDataCastConsumer extends DataCastConsumerImpl {

    EventBus mEventBus;

    public SlidesCastDataCastConsumer() {
        super();
        mEventBus = EventBus.getDefault();
    }

    @Override
    public void onMessageReceived(CastDevice castDevice, String namespace, String message) {
        super.onMessageReceived(castDevice, namespace, message);
        // mEventBus.post(event);
        // TODO implement if we want to handle messages from the chromecast
    }

    @Override
    public void onMessageSendFailed(Status status) {
        super.onMessageSendFailed(status);
         //mEventBus.post(event);
        // TODO implement if we want to handle messages from the chromecast
        GLog.e(getClass().getName(), status.getStatus().getStatusMessage());
    }

}
