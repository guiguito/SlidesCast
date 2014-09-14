package com.ggt.slidescast.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.ggt.slidescast.R;
import com.ggt.slidescast.model.app.Presentation;
import com.ggt.slidescast.model.app.Presentation.PresentationType;
import com.ggt.slidescast.slideshare.SlideShareManager;
import com.ggt.slidescast.slideshare.SlideShareManager.AddFavoriteSlideShowEvent;
import com.ggt.slidescast.slideshare.SlideShareManager.SearchEvent;
import com.ggt.slidescast.slideshare.model.SlideShareServiceError;
import com.ggt.slidescast.slideshare.model.Slideshows;
import com.ggt.slidescast.ui.SlidesCastActivity_;
import com.ggt.slidescast.ui.adapters.PresentationsAdapter;
import com.ggt.slidescast.ui.fragments.DialogConfirmationDialog.DialogConfirmationDialogListener;
import com.ggt.slidescast.utils.GLog;
import com.ggt.slidescast.utils.SlidesCastConstants;
import com.ggt.slidescast.utils.SlidesCastPrefs_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.List;

/**
 * Fragement for slideshare search.
 *
 * @author guiguito
 */
@EFragment(R.layout.fragment_slideshare_search)
public class SlideShareSearchFragment extends MotherFragment implements OnRefreshListener, DialogConfirmationDialogListener {

    @ViewById
    ListView mSlideShareSearchFilesListview;
    @ViewById
    Button mSearchButton;
    @ViewById
    EditText mQueryEditText;
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

    private static final String DIALOG_CONFIRMATION = "DIALOG_CONFIRMATION";

    @AfterViews
    void init() {
        mSwipeContainerRefreshLayout.setOnRefreshListener(this);
        mSwipeContainerRefreshLayout.setColorScheme(R.color.slidescast_red, R.color.slidescast_green, R.color.slidescast_blue, R.color.slidescast_yellow);

        if (!TextUtils.isEmpty(mSlidesCastPrefs.lastSlideShareSearch().get())) {
            mQueryEditText.setText(mSlidesCastPrefs.lastSlideShareSearch().get());
            List<Presentation> presentations = Presentation.getAllByTypeAndSubType(PresentationType.SLIDESHARE, SlideShareFragment.SUBTYPE_SLIDE_SHARE_SEARCH);
            if (presentations.size() > 0) {
                mResultsAdapter.addAll(presentations);
            } else {
                search();
            }
        }
        mQueryEditText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    search();
                    handled = true;
                }
                return handled;
            }
        });
        mSlideShareSearchFilesListview.setAdapter(mResultsAdapter);
        mSlideShareSearchFilesListview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onSlideShowClicked((Presentation) mResultsAdapter.getItem(position));
            }
        });
        mSlideShareSearchFilesListview.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                onSlideShowLongClicked((Presentation) mResultsAdapter.getItem(position));
                return true;
            }
        });
    }

    @Click(R.id.mSearchButton)
    void search() {
        mSwipeContainerRefreshLayout.setRefreshing(true);
        mSlidesCastPrefs.edit().lastSlideShareSearch().put(mQueryEditText.getText().toString()).apply();
        mSlideShareManager.search(mQueryEditText.getText().toString(), 1, 30, mSlidesCastPrefs.slideShareSearchLanguage().get());
        //hide keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mQueryEditText.getWindowToken(), 0);
    }

    public void onEventMainThread(SearchEvent searchEvent) {
        mSwipeContainerRefreshLayout.setRefreshing(false);
        if (searchEvent.isSuccessfull()) {
            if (searchEvent.slideShareObject != null && searchEvent.slideShareObject instanceof Slideshows
                    && ((Slideshows) searchEvent.slideShareObject).getSlideShows() != null) {
                List<Presentation> presentations = SlideShareFragment.slideShowsToListPresentation(((Slideshows) searchEvent.slideShareObject).getSlideShows(),
                        SlideShareFragment.SUBTYPE_SLIDE_SHARE_SEARCH);
                Presentation.deleteAllByTypeAndSubType(PresentationType.SLIDESHARE, SlideShareFragment.SUBTYPE_SLIDE_SHARE_SEARCH);
                Presentation.addAll(presentations);
                mResultsAdapter.clear();
                mResultsAdapter.addAll(presentations);
            } else if (searchEvent.slideShareObject instanceof SlideShareServiceError) {
                GLog.e(getClass().getName(), ((SlideShareServiceError) searchEvent.slideShareObject).getMessage().getContent());
            }
        } else if (searchEvent.exception != null) {
            GLog.e(getClass().getName(), searchEvent.exception.getMessage());
        }
    }

    public void onEventMainThread(AddFavoriteSlideShowEvent addFavoriteSlideShowEvent) {
        mSwipeContainerRefreshLayout.setRefreshing(false);
        Toast.makeText(getActivity(), getString(R.string.toast_added_to_favorite, mSelectedPresentation.getTitle()), Toast.LENGTH_LONG).show();
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

    @Override
    public void onRefresh() {
        search();
    }

    private void showConfirmationDialog() {
        mDialogFragment = new DialogConfirmationDialog();
        mDialogFragment.setDialogConfirmationDialogListener(this);
        Bundle args = new Bundle();
        args.putString(DialogConfirmationDialog.KEY_TITLE, getString(R.string.title_add_favorite_dialog));
        args.putString(DialogConfirmationDialog.KEY_MESSAGE, getString(R.string.message_add_favorite_dialog, mSelectedPresentation.getTitle()));
        mDialogFragment.setArguments(args);
        mDialogFragment.show(getActivity().getSupportFragmentManager(), DIALOG_CONFIRMATION);
    }

    private void hideConfirmationDialog() {
        mDialogFragment.dismiss();
    }

    @Override
    public void onOkClicked() {
        mSwipeContainerRefreshLayout.setRefreshing(true);
        mSlideShareManager.addFavoriteSlideShow(mSlidesCastPrefs.username().get(), mSlidesCastPrefs.password().get(), mSelectedPresentation.getExternalId());
    }

    @Override
    public void onCancelClicked() {
        hideConfirmationDialog();
    }
}
