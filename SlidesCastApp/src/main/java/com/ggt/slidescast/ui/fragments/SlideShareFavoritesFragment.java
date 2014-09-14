package com.ggt.slidescast.ui.fragments;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ggt.slidescast.R;
import com.ggt.slidescast.model.app.Presentation;
import com.ggt.slidescast.model.app.Presentation.PresentationType;
import com.ggt.slidescast.slideshare.SlideShareManager;
import com.ggt.slidescast.slideshare.SlideShareManager.AddFavoriteSlideShowEvent;
import com.ggt.slidescast.slideshare.SlideShareManager.GetSlideShowEvent;
import com.ggt.slidescast.slideshare.SlideShareManager.GetUserFavoritesEvent;
import com.ggt.slidescast.slideshare.model.Favorite;
import com.ggt.slidescast.slideshare.model.Favorites;
import com.ggt.slidescast.slideshare.model.SlideShareServiceError;
import com.ggt.slidescast.slideshare.model.Slideshow;
import com.ggt.slidescast.ui.SlidesCastActivity_;
import com.ggt.slidescast.ui.adapters.PresentationsAdapter;
import com.ggt.slidescast.ui.views.SlideShareErrorLinearLayout;
import com.ggt.slidescast.ui.views.SlideShareErrorLinearLayout.SlideShareErrorLinearLayoutListener;
import com.ggt.slidescast.utils.EventBusEvents;
import com.ggt.slidescast.utils.GLog;
import com.ggt.slidescast.utils.SlidesCastConstants;
import com.ggt.slidescast.utils.SlidesCastPrefs_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.List;

/**
 * Fragment that displays favorites from  Slideshare.
 *
 * @author guiguito
 */
@EFragment(R.layout.fragment_slideshare_favorites)
public class SlideShareFavoritesFragment extends MotherFragment implements SlideShareErrorLinearLayoutListener, OnRefreshListener {

    @ViewById
    ListView mFavoritesFilesListview;

    @ViewById
    SlideShareErrorLinearLayout mSlideShareErrorLinearLayout2;

    @ViewById
    SwipeRefreshLayout mSwipeContainerRefreshLayout;

    @Bean
    PresentationsAdapter mResultsAdapter;

    @Pref
    SlidesCastPrefs_ mSlidesCastPrefs;

    @Bean
    SlideShareManager mSlideShareManager;

    @AfterViews
    void init() {
        mSwipeContainerRefreshLayout.setOnRefreshListener(this);
        mSwipeContainerRefreshLayout.setColorScheme(R.color.slidescast_red, R.color.slidescast_green, R.color.slidescast_blue, R.color.slidescast_yellow);
        mSlideShareErrorLinearLayout2.setSlideShareErrorLinearLayoutListener(this);
        mFavoritesFilesListview.setAdapter(mResultsAdapter);
        mFavoritesFilesListview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onSlideShowClicked((Presentation) mResultsAdapter.getItem(position));
            }
        });
        if (!TextUtils.isEmpty(mSlidesCastPrefs.username().get()) && !TextUtils.isEmpty(mSlidesCastPrefs.password().get())) {
            mSlideShareErrorLinearLayout2.setVisibility(View.GONE);
            mFavoritesFilesListview.setVisibility(View.VISIBLE);

            List<Presentation> presentations = Presentation.getAllByTypeAndSubType(PresentationType.SLIDESHARE,
                    SlideShareFragment.SUBTYPE_SLIDE_SHARE_FAVORITES);
            if (presentations.size() > 0) {
                mResultsAdapter.addAll(presentations);
            } else {
                getUserFavorites();
            }
        } else {
            mFavoritesFilesListview.setVisibility(View.GONE);
            mSlideShareErrorLinearLayout2.showError(R.string.user_credentials_error, R.string.go_to_param);
        }
    }

    private void getUserFavorites() {
        mSwipeContainerRefreshLayout.setRefreshing(true);
        mSlideShareManager.getUserFavorites(mSlidesCastPrefs.username().get());
    }

    public void onEventMainThread(GetUserFavoritesEvent getUserFavoritesEvent) {
        mSwipeContainerRefreshLayout.setRefreshing(false);
        if (getUserFavoritesEvent.isSuccessfull()) {
            if (getUserFavoritesEvent.slideShareObject != null && getUserFavoritesEvent.slideShareObject instanceof SlideShareServiceError) {
                // TODO improve slideshare error handling with error number
                // check
                mFavoritesFilesListview.setVisibility(View.GONE);
                mSlideShareErrorLinearLayout2.showError(R.string.user_auth_error, R.string.check_in_param);
            } else if (getUserFavoritesEvent.slideShareObject != null && getUserFavoritesEvent.slideShareObject instanceof Favorites
                    && ((Favorites) getUserFavoritesEvent.slideShareObject).getFavorites() != null) {
                List<Favorite> favorites = ((Favorites) getUserFavoritesEvent.slideShareObject).getFavorites();
                mResultsAdapter.clear();
                clearSlideShareFavorites();
                for (Favorite favorite : favorites) {
                    mSlideShareManager.getSlideShow(favorite.getSlideshow_id());
                }
            } else {
                Presentation.deleteAllByTypeAndSubType(PresentationType.SLIDESHARE, SlideShareFragment.SUBTYPE_SLIDE_SHARE_FAVORITES);
                mResultsAdapter.clear();
            }
        } else {
            mFavoritesFilesListview.setVisibility(View.GONE);
            mSlideShareErrorLinearLayout2.showError(R.string.slideshare_connect_error);
        }
    }

    public static void clearSlideShareFavorites() {
        Presentation.deleteAllByTypeAndSubType(PresentationType.SLIDESHARE, SlideShareFragment.SUBTYPE_SLIDE_SHARE_FAVORITES);
    }

    public void onEventMainThread(GetSlideShowEvent getSlideShowEvent) {
        if (getSlideShowEvent.isSuccessfull()) {
            if (getSlideShowEvent.slideShareObject != null && getSlideShowEvent.slideShareObject instanceof Slideshow) {
                Presentation presentation = SlideShareFragment.slideShowToPresentation(((Slideshow) getSlideShowEvent.slideShareObject),
                        SlideShareFragment.SUBTYPE_SLIDE_SHARE_FAVORITES);
                Presentation.add(presentation);
                mResultsAdapter.add(presentation);
            }
        } else {
            // ignore the error
            GLog.e(getClass().getName(), getSlideShowEvent.exception.getMessage());
        }
    }

    public void onEventMainThread(AddFavoriteSlideShowEvent addFavoriteSlideShowEvent) {
        if (addFavoriteSlideShowEvent.isSuccessfull()) {
            getUserFavorites();
        }
    }

    private void onSlideShowClicked(Presentation presentation) {
        Intent intent = new Intent(getActivity(), SlidesCastActivity_.class);
        intent.putExtra(SlidesCastConstants.KEY_PRESENTATION_ID, presentation.getId());
        startActivity(intent);
    }

    @Override
    public void onParametersClicked() {
        mEventBus.post(new EventBusEvents.ShowSettings());
    }

    @Override
    public void onRefresh() {
        getUserFavorites();
    }
}
