package com.ggt.slidescast.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.ggt.slidescast.R;
import com.ggt.slidescast.model.app.Presentation;
import com.ggt.slidescast.model.app.Presentation.PresentationType;
import com.ggt.slidescast.slideshare.SlideShareManager;
import com.ggt.slidescast.slideshare.SlideShareManager.DeleteSlideShowEvent;
import com.ggt.slidescast.slideshare.SlideShareManager.GetSlideShowsByUserEvent;
import com.ggt.slidescast.slideshare.SlideShareManager.UploadSlidesEvent;
import com.ggt.slidescast.slideshare.model.SlideShareServiceError;
import com.ggt.slidescast.slideshare.model.User;
import com.ggt.slidescast.ui.SlidesCastActivity_;
import com.ggt.slidescast.ui.adapters.PresentationsAdapter;
import com.ggt.slidescast.ui.fragments.DialogConfirmationDialog.DialogConfirmationDialogListener;
import com.ggt.slidescast.ui.views.SlideShareErrorLinearLayout;
import com.ggt.slidescast.ui.views.SlideShareErrorLinearLayout.SlideShareErrorLinearLayoutListener;
import com.ggt.slidescast.utils.EventBusEvents;
import com.ggt.slidescast.utils.SlidesCastConstants;
import com.ggt.slidescast.utils.SlidesCastPrefs_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.List;

/**
 * Fragement that displays user's presentations from SlideShare.
 *
 * @author guiguito
 */
@EFragment(R.layout.fragment_slideshare_user)
public class SlideShareUserFragment extends MotherFragment implements SlideShareErrorLinearLayoutListener, OnRefreshListener, DialogConfirmationDialogListener {

    @ViewById
    ListView mUserFilesListview;

    @ViewById
    SlideShareErrorLinearLayout mSlideShareErrorLinearLayout;

    @ViewById
    SwipeRefreshLayout mSwipeContainerRefreshLayout;

    DialogConfirmationDialog mDialogFragment;

    @Pref
    SlidesCastPrefs_ mSlidesCastPrefs;

    @Bean
    PresentationsAdapter mResultsAdapter;

    @Bean
    SlideShareManager mSlideShareManager;

    Presentation mSelectedPresentation;

    private boolean mIsLoaded = false;

    public static final int MAX_SLIDES_TO_LOAD = 100;

    private static final int DELAY_RELOAD_AFTER_UPLOAD = 10000;
    private static final String DIALOG_CONFIRMATION = "DIALOG_CONFIRMATION";

    @AfterViews
    void init() {
        mSwipeContainerRefreshLayout.setOnRefreshListener(this);
        mSwipeContainerRefreshLayout.setColorScheme(R.color.slidescast_red, R.color.slidescast_green, R.color.slidescast_blue, R.color.slidescast_yellow);
        mSlideShareErrorLinearLayout.setSlideShareErrorLinearLayoutListener(this);
        mUserFilesListview.setAdapter(mResultsAdapter);
        mUserFilesListview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onSlideShowClicked((Presentation) mResultsAdapter.getItem(position));
            }
        });
        mUserFilesListview.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                onSlideShowLongClicked((Presentation) mResultsAdapter.getItem(position));
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsLoaded) {
            checkCredentialsAndLoad();
        }
    }

    private void checkCredentialsAndLoad() {
        if (!TextUtils.isEmpty(mSlidesCastPrefs.username().get()) && !TextUtils.isEmpty(mSlidesCastPrefs.password().get())) {
            mSlideShareErrorLinearLayout.setVisibility(View.GONE);
            mUserFilesListview.setVisibility(View.VISIBLE);

            List<Presentation> presentations = Presentation.getAllByTypeAndSubType(PresentationType.SLIDESHARE, SlideShareFragment.SUBTYPE_SLIDE_SHARE_USER);
            if (presentations.size() > 0) {
                mIsLoaded = true;
                mResultsAdapter.clear();
                mResultsAdapter.addAll(presentations);
            } else {
                loadUserSlideShows();
            }
        } else {
            mUserFilesListview.setVisibility(View.GONE);
            mSlideShareErrorLinearLayout.showError(R.string.user_credentials_error, R.string.go_to_param);
        }
    }

    void loadUserSlideShows() {
        mSwipeContainerRefreshLayout.setRefreshing(true);
        mSlideShareManager.getSlideShowsByUser(mSlidesCastPrefs.username().get(), mSlidesCastPrefs.username().get(), mSlidesCastPrefs.password().get(),
                MAX_SLIDES_TO_LOAD);
    }

    public void onEventMainThread(GetSlideShowsByUserEvent getSlideShowsByUserEvent) {
        mSwipeContainerRefreshLayout.setRefreshing(false);
        if (getSlideShowsByUserEvent.isSuccessfull()) {
            if (getSlideShowsByUserEvent.slideShareObject != null && getSlideShowsByUserEvent.slideShareObject instanceof SlideShareServiceError) {
                // TODO improve slideshare error handling with error number
                // check
                mUserFilesListview.setVisibility(View.GONE);
                mSlideShareErrorLinearLayout.showError(R.string.user_auth_error, R.string.check_in_param);
            } else if (getSlideShowsByUserEvent.slideShareObject != null && getSlideShowsByUserEvent.slideShareObject instanceof User
                    && ((User) getSlideShowsByUserEvent.slideShareObject).getSlideshows() != null) {
                mIsLoaded = true;
                List<Presentation> presentations = SlideShareFragment.slideShowsToListPresentation(
                        ((User) getSlideShowsByUserEvent.slideShareObject).getSlideshows(), SlideShareFragment.SUBTYPE_SLIDE_SHARE_USER);
                saveSlideShareUserSlides(presentations);
                mResultsAdapter.clear();
                mResultsAdapter.addAll(presentations);
            } else {
                Presentation.deleteAllByTypeAndSubType(PresentationType.SLIDESHARE, SlideShareFragment.SUBTYPE_SLIDE_SHARE_USER);
                mResultsAdapter.clear();
            }
        } else {
            mSwipeContainerRefreshLayout.setRefreshing(false);
            mUserFilesListview.setVisibility(View.GONE);
            mSlideShareErrorLinearLayout.showError(R.string.slideshare_connect_error);
        }
    }

    public static void saveSlideShareUserSlides(List<Presentation> presentations) {
        Presentation.deleteAllByTypeAndSubType(PresentationType.SLIDESHARE, SlideShareFragment.SUBTYPE_SLIDE_SHARE_USER);
        Presentation.addAll(presentations);
    }

    public void onEventMainThread(UploadSlidesEvent uploadSlidesEvent) {
        if (uploadSlidesEvent.isSuccessfull() && mSwipeContainerRefreshLayout != null) {
            mSwipeContainerRefreshLayout.postDelayed(new Runnable() {

                @Override
                public void run() {
                    loadUserSlideShows();
                }
            }, DELAY_RELOAD_AFTER_UPLOAD);
        }
    }

    public void onEventMainThread(DeleteSlideShowEvent deleteSlideShowEvent) {
        mSwipeContainerRefreshLayout.setRefreshing(false);
        if (deleteSlideShowEvent.isSuccessfull() && !(deleteSlideShowEvent.slideShareObject instanceof SlideShareServiceError)
                && mSwipeContainerRefreshLayout != null) {
            loadUserSlideShows();
        }
    }

    private void onSlideShowClicked(Presentation presentation) {
        Intent intent = new Intent(getActivity(), SlidesCastActivity_.class);
        intent.putExtra(SlidesCastConstants.KEY_PRESENTATION_ID, presentation.getId());
        startActivity(intent);
    }

    private void onSlideShowLongClicked(Presentation presentation) {
        mSelectedPresentation = presentation;
        showConfirmationDialog();
    }

    private void showConfirmationDialog() {
        mDialogFragment = new DialogConfirmationDialog();
        mDialogFragment.setDialogConfirmationDialogListener(this);
        Bundle args = new Bundle();
        args.putString(DialogConfirmationDialog.KEY_TITLE, getString(R.string.title_delete_dialog));
        args.putString(DialogConfirmationDialog.KEY_MESSAGE, getString(R.string.message_delete_dialog, mSelectedPresentation.getTitle()));
        mDialogFragment.setArguments(args);
        mDialogFragment.show(getActivity().getSupportFragmentManager(), DIALOG_CONFIRMATION);
    }

    private void hideConfirmationDialog() {
        mDialogFragment.dismiss();
    }

    @Override
    public void onParametersClicked() {
        mEventBus.post(new EventBusEvents.ShowSettings());
    }

    @Override
    public void onRefresh() {
        loadUserSlideShows();
    }

    @Override
    public void onOkClicked() {
        mSwipeContainerRefreshLayout.setRefreshing(true);
        mSlideShareManager.deleteSlideShow(mSlidesCastPrefs.username().get(), mSlidesCastPrefs.password().get(), mSelectedPresentation.getExternalId());
    }

    @Override
    public void onCancelClicked() {
        hideConfirmationDialog();
    }
}
