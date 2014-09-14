package com.ggt.slidescast;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import com.ggt.slidescast.model.app.Presentation;
import com.ggt.slidescast.slideshare.SlideShareManager;
import com.ggt.slidescast.slideshare.model.Favorite;
import com.ggt.slidescast.slideshare.model.Favorites;
import com.ggt.slidescast.slideshare.model.SlideShareObject;
import com.ggt.slidescast.slideshare.model.Slideshow;
import com.ggt.slidescast.slideshare.model.User;
import com.ggt.slidescast.ui.PickAFileActivity;
import com.ggt.slidescast.ui.fragments.MyFilesFragment;
import com.ggt.slidescast.ui.fragments.SlideShareFavoritesFragment;
import com.ggt.slidescast.ui.fragments.SlideShareFragment;
import com.ggt.slidescast.ui.fragments.SlideShareUserFragment;
import com.ggt.slidescast.utils.SlidesCastPrefs_;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.List;

/**
 * Refresh data for the app.
 *
 * @author guiguito
 */
@EIntentService
public class RefreshService extends IntentService {


    @Bean
    SlideShareManager mSlideShareManager;

    @Pref
    SlidesCastPrefs_ mSlidesCastPrefs;

    public RefreshService() {
        super("RefreshService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //refresh my files from slideshare
        if (!TextUtils.isEmpty(mSlidesCastPrefs.username().get()) && !TextUtils.isEmpty(mSlidesCastPrefs.password().get())) {
            //my slides
            SlideShareObject slideShareObjectUser = mSlideShareManager.getSlideShowsByUserSynchronous(mSlidesCastPrefs.username().get(), mSlidesCastPrefs.username().get(), mSlidesCastPrefs.password().get(),
                    SlideShareUserFragment.MAX_SLIDES_TO_LOAD);
            if (slideShareObjectUser != null && slideShareObjectUser instanceof User
                    && ((User) slideShareObjectUser).getSlideshows() != null) {
                List<Presentation> presentations = SlideShareFragment.slideShowsToListPresentation(
                        ((User) slideShareObjectUser).getSlideshows(), SlideShareFragment.SUBTYPE_SLIDE_SHARE_USER);
                SlideShareUserFragment.saveSlideShareUserSlides(presentations);
            }
            //favorites
            SlideShareObject slideShareObject = mSlideShareManager.getUserFavoritesSynchronous(mSlidesCastPrefs.username().get());
            if (slideShareObject != null && slideShareObject instanceof Favorites
                    && ((Favorites) slideShareObject).getFavorites() != null) {
                List<Favorite> favorites = ((Favorites) slideShareObject).getFavorites();
                SlideShareFavoritesFragment.clearSlideShareFavorites();
                for (Favorite favorite : favorites) {
                    SlideShareObject slideShow = mSlideShareManager.getSlideShowSynchronous(favorite.getSlideshow_id());
                    if (slideShow != null && slideShow instanceof Slideshow) {
                        Presentation presentation = SlideShareFragment.slideShowToPresentation(((Slideshow) slideShow),
                                SlideShareFragment.SUBTYPE_SLIDE_SHARE_FAVORITES);
                        Presentation.add(presentation);
                    }
                }
            }
        }

        //refresh local files i can show on chromecast
        MyFilesFragment.loadLocalFilesForChromecastDisplay();

        //refresh local files i can send to slideshare
        PickAFileActivity.loadLocalFilesForUploadToSlideShare();
    }

}
