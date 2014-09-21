package com.ggt.slidescast.slideshare;

import com.ggt.slidescast.R;
import com.ggt.slidescast.SlidesCastApplication;
import com.ggt.slidescast.slideshare.model.SlideShareObject;
import com.ggt.slidescast.utils.AeSimpleSHA1;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * SlideShare API managaer.
 * Entry point to dialog with slideshare.
 *
 * @author guiguito
 */
@EBean(scope = Scope.Singleton)
public class SlideShareManager {

    private SlideShareApi mService;
    private EventBus mEventBus;
    private static String API_KEY;
    private static String SHARED_SECRET;

    public SlideShareManager() {
        API_KEY = SlidesCastApplication.getAppContext().getString(R.string.slideshare_key);
        SHARED_SECRET = SlidesCastApplication.getAppContext().getString(R.string.slideshare_secret);
        mEventBus = EventBus.getDefault();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://www.slideshare.net/api/2").setConverter(new XStreamConverter()).build();
        mService = restAdapter.create(SlideShareApi.class);
    }

    private String getHash(long timeStamp) {
        try {
            return AeSimpleSHA1.sha12(SHARED_SECRET + timeStamp);
        } catch (NoSuchAlgorithmException e) {
            // no error
        }
        return null;
    }

    private class SlideShareEvent {
        public SlideShareObject slideShareObject;
        public Exception exception;

        public boolean isSuccessfull() {
            return exception == null;
        }
    }

    private Callback<SlideShareObject> getCallback(final SlideShareEvent evt) {
        return new Callback<SlideShareObject>() {

            @Override
            public void failure(RetrofitError error) {
                evt.exception = error;
                mEventBus.post(evt);
            }

            @Override
            public void success(SlideShareObject slideShareObject, Response response) {
                evt.slideShareObject = slideShareObject;
                mEventBus.post(evt);
            }
        };
    }

    public class SearchEvent extends SlideShareEvent {
    }

    public void search(String query, int page, int numberByPage, String lang) {
        long ts = System.currentTimeMillis() / 1000;
        mService.searchSlideShows(API_KEY, getHash(ts).toLowerCase(), ts, query, lang, page, numberByPage, getCallback(new SearchEvent()));
    }

    public class GetSlideShowsByUserEvent extends SlideShareEvent {
    }

    public void getSlideShowsByUser(String usernameFor, String username, String password, long limit) {
        long ts = System.currentTimeMillis() / 1000;
        mService.getSlideShowsByUser(API_KEY, getHash(ts).toLowerCase(), ts, usernameFor, username, password, limit,
                getCallback(new GetSlideShowsByUserEvent()));
    }

    public SlideShareObject getSlideShowsByUserSynchronous(String usernameFor, String username, String password, long limit) {
        long ts = System.currentTimeMillis() / 1000;
        return mService.getSlideShowsByUser(API_KEY, getHash(ts).toLowerCase(), ts, usernameFor, username, password, limit);
    }


    public class GetUserFavoritesEvent extends SlideShareEvent {
    }

    public void getUserFavorites(String usernameFor) {
        long ts = System.currentTimeMillis() / 1000;
        mService.getUserFavorites(API_KEY, getHash(ts).toLowerCase(), ts, usernameFor, getCallback(new GetUserFavoritesEvent()));
    }

    public SlideShareObject getUserFavoritesSynchronous(String usernameFor) {
        long ts = System.currentTimeMillis() / 1000;
        return mService.getUserFavorites(API_KEY, getHash(ts).toLowerCase(), ts, usernameFor);
    }

    public class GetSlideShowEvent extends SlideShareEvent {
    }

    public void getSlideShow(String slideshowId) {
        long ts = System.currentTimeMillis() / 1000;
        mService.getSlideShow(API_KEY, getHash(ts).toLowerCase(), ts, slideshowId, getCallback(new GetSlideShowEvent()));
    }

    public SlideShareObject getSlideShowSynchronous(String slideshowId) {
        long ts = System.currentTimeMillis() / 1000;
        return mService.getSlideShow(API_KEY, getHash(ts).toLowerCase(), ts, slideshowId);
    }

    public class DeleteSlideShowEvent extends SlideShareEvent {
    }

    public void deleteSlideShow(String username, String password, String slideshowId) {
        long ts = System.currentTimeMillis() / 1000;
        mService.deleteSlideShow(API_KEY, getHash(ts).toLowerCase(), ts, username, password, slideshowId, getCallback(new DeleteSlideShowEvent()));
    }

    public class AddFavoriteSlideShowEvent extends SlideShareEvent {
    }

    public void addFavoriteSlideShow(String username, String password, String slideshowId) {
        long ts = System.currentTimeMillis() / 1000;
        mService.addFavoriteSlideShow(API_KEY, getHash(ts).toLowerCase(), ts, username, password, slideshowId, getCallback(new AddFavoriteSlideShowEvent()));
    }

    public class UploadSlidesEvent extends SlideShareEvent {
    }

    public void uploadSlides(String username, String password, String title, TypedFile file, String description, List<String> tags, boolean makeDownloadable,
                             boolean makeSlideShowPrivate) {
        long ts = System.currentTimeMillis() / 1000;
        String makeDownloadableValue = "N";
        if (makeDownloadable) {
            makeDownloadableValue = "Y";
        }
        String makeSlideShowPrivateValue = "N";
        if (makeSlideShowPrivate) {
            makeSlideShowPrivateValue = "Y";
        }
        String tagsValue = "";
        if (tags != null) {
            for (String tag : tags) {
                tagsValue += tag + ",";
            }
        }
        mService.uploadSlides(API_KEY, getHash(ts).toLowerCase(), ts, username, password, title, file, description, tagsValue, makeDownloadableValue,
                makeSlideShowPrivateValue, getCallback(new UploadSlidesEvent()));
    }

}
