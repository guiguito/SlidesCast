package com.ggt.slidescast.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ggt.slidescast.R;
import com.ggt.slidescast.model.LocalFile;
import com.ggt.slidescast.slideshare.SlideShareManager;
import com.ggt.slidescast.slideshare.SlideShareManager.UploadSlidesEvent;
import com.ggt.slidescast.slideshare.model.SlideShareServiceError;
import com.ggt.slidescast.ui.PickAFileActivity;
import com.ggt.slidescast.ui.PickAFileActivity_;
import com.ggt.slidescast.ui.views.SlideShareErrorLinearLayout;
import com.ggt.slidescast.ui.views.SlideShareErrorLinearLayout.SlideShareErrorLinearLayoutListener;
import com.ggt.slidescast.ui.views.TagLinearLayout;
import com.ggt.slidescast.ui.views.TagLinearLayout.TagAddedListener;
import com.ggt.slidescast.ui.views.TagLinearLayout_;
import com.ggt.slidescast.utils.EventBusEvents;
import com.ggt.slidescast.utils.FilesUtils;
import com.ggt.slidescast.utils.SlidesCastPrefs_;
import com.joanzapata.android.iconify.Iconify;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.File;
import java.util.ArrayList;

import retrofit.mime.TypedFile;

/**
 * Fragment used for file upload on slideshare.
 *
 * @author guiguito
 */
@EFragment(R.layout.fragment_slideshare_upload_slides)
public class SlideShareUploadFragment extends MotherFragment implements SlideShareErrorLinearLayoutListener, TagAddedListener {

    @ViewById
    TextView mFileChosenTextView, mFileChosenSizeTextView;

    @ViewById
    Button mPickFileButton, mUploadFileButton, mAddTagButton, mClearTagsButton;

    @ViewById
    SlideShareErrorLinearLayout mSlideShareErrorLinearLayout3;

    @Pref
    SlidesCastPrefs_ mSlidesCastPrefs;

    @ViewById
    LinearLayout mSlideShareUploadLinearLayout, mTagLinearLayout;

    @ViewById
    EditText mTitleEditText, mDescriptionEditText;

    @ViewById
    CheckBox mDownloadableCheckBox;

    @Bean
    SlideShareManager mSlideShareManager;

    DialogProgressFragment mProgressFragment;

    File mSelectedFile;
    ArrayList<String> mTags = new ArrayList<String>();
    String mRestoredTitle = "";
    String mRestoredDesctiption = "";

    public static final int PICK_A_FILE_REQUEST_CODE = 1;

    private static final String DIALOG_LOADER = "DIALOG_LOADER";

    private static final String KEY_SELECTED_FILE = "KEY_SELECTED_FILE";
    private static final String KEY_FILE_TITLE = "KEY_FILE_TITLE";
    private static final String KEY_FILE_DESCRIPTION = "KEY_FILE_DESCRIPTION";
    private static final String KEY_FILE_TAGS = "KEY_FILE_TAGS";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mProgressFragment = DialogProgressFragment_.builder().build();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_SELECTED_FILE)) {
                mSelectedFile = (File) savedInstanceState.getSerializable(KEY_SELECTED_FILE);
            }
            mRestoredTitle = savedInstanceState.getString(KEY_FILE_TITLE);
            mRestoredDesctiption = savedInstanceState.getString(KEY_FILE_DESCRIPTION);
            mTags = (ArrayList<String>) savedInstanceState.getSerializable(KEY_FILE_TAGS);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mSelectedFile != null) {
            outState.putSerializable(KEY_SELECTED_FILE, mSelectedFile);
        }
        outState.putString(KEY_FILE_TITLE, mTitleEditText.getText().toString());
        outState.putString(KEY_FILE_DESCRIPTION, mDescriptionEditText.getText().toString());
        outState.putSerializable(KEY_FILE_TAGS, mTags);
    }

    @AfterViews
    void init() {
        Iconify.addIcons(mClearTagsButton);
        mSlideShareErrorLinearLayout3.setSlideShareErrorLinearLayoutListener(this);
        checkCredentials();
        if (mSelectedFile != null) {
            mFileChosenTextView.setText(mSelectedFile.getName());
            mFileChosenSizeTextView.setText(FilesUtils.getFormattedFileSizeInMB(getActivity().getApplicationContext(), mSelectedFile));
        }
        mTitleEditText.setText(mRestoredTitle);
        mDescriptionEditText.setText(mRestoredDesctiption);
        if (mTags != null) {
            for (String tag : mTags) {
                addTagLayout(tag);
            }

        }
        checkReadyForUpload();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkCredentials();
    }

    private void showLoader() {
        mProgressFragment.show(getActivity().getSupportFragmentManager(), DIALOG_LOADER);
    }

    private void hideLoader() {
        mProgressFragment.dismiss();
    }

    private void checkCredentials() {
        if (!TextUtils.isEmpty(mSlidesCastPrefs.username().get()) && !TextUtils.isEmpty(mSlidesCastPrefs.password().get())) {
            mSlideShareErrorLinearLayout3.setVisibility(View.GONE);
            mSlideShareUploadLinearLayout.setVisibility(View.VISIBLE);
        } else {
            mSlideShareUploadLinearLayout.setVisibility(View.GONE);
            mSlideShareErrorLinearLayout3.showError(R.string.user_credentials_error, R.string.go_to_param);
        }
    }

    @FocusChange(R.id.mTitleEditText)
    void checkReadyForUpload() {
        if (mSelectedFile != null && !TextUtils.isEmpty(mTitleEditText.getText().toString())) {
            mUploadFileButton.setEnabled(true);
        } else {
            mUploadFileButton.setEnabled(false);
        }
    }

    @Click(R.id.mPickFileButton)
    void pickFile() {
        Intent intent = new Intent(getActivity(), PickAFileActivity_.class);
        startActivityForResult(intent, PICK_A_FILE_REQUEST_CODE);
    }

    @Click(R.id.mAddTagButton)
    void addTag() {
        // show dialog
        addTagLayout(null);
    }

    @Click(R.id.mClearTagsButton)
    void clearTags() {
        mTags.clear();
        mTagLinearLayout.removeAllViews();
    }

    @Click(R.id.mUploadFileButton)
    void uploadFile() {
        showLoader();
        mSlideShareManager.uploadSlides(
                mSlidesCastPrefs.username().get(),
                mSlidesCastPrefs.password().get(),
                mTitleEditText.getText().toString(),
                new TypedFile(MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                        mSelectedFile.getName().substring(mSelectedFile.getName().lastIndexOf(".") + 1)), mSelectedFile), mDescriptionEditText.getText()
                        .toString(), mTags, mDownloadableCheckBox.isChecked(), false);
    }

    public void onEventMainThread(UploadSlidesEvent uploadSlidesEvent) {
        hideLoader();
        if (uploadSlidesEvent.isSuccessfull()) {
            if (uploadSlidesEvent.slideShareObject instanceof SlideShareServiceError) {
                Toast.makeText(getActivity(), ((SlideShareServiceError) uploadSlidesEvent.slideShareObject).getMessage().getContent(), Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(getActivity(), getString(R.string.file_uploaded_successfully), Toast.LENGTH_LONG).show();
                mSelectedFile = null;
                mTitleEditText.setText("");
                mDescriptionEditText.setText("");
                mFileChosenTextView.setText(getString(R.string.no_file_picked));
                mFileChosenSizeTextView.setText("");
                clearTags();
                checkReadyForUpload();
            }
        } else {
            mSlideShareUploadLinearLayout.setVisibility(View.GONE);
            mSlideShareErrorLinearLayout3.showError(R.string.slideshare_connect_error);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_A_FILE_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    LocalFile localFile = (LocalFile) data.getParcelableExtra(PickAFileActivity.KEY_LOCAL_FILE);
                    if (localFile != null) {
                        mSelectedFile = new File(localFile.getFilePath() + File.separator + localFile.getFileName());
                        if (mSelectedFile.exists()) {
                            mFileChosenTextView.setText(mSelectedFile.getName());
                            mFileChosenSizeTextView.setText(FilesUtils.getFormattedFileSizeInMB(getActivity().getApplicationContext(), mSelectedFile));
                            checkReadyForUpload();
                        } else {
                            mSelectedFile = null;
                            mFileChosenSizeTextView.setText("");
                            checkReadyForUpload();
                            Toast.makeText(getActivity(), getString(R.string.file_doesnt_exist), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void addTagLayout(String tag) {
        if (tag == null) {
            // case for editable tag
            TagLinearLayout tagLinearLayout = (TagLinearLayout) TagLinearLayout_.build(getActivity());
            tagLinearLayout.setAddTagState();
            tagLinearLayout.setTagAddedListener(this);
            mTagLinearLayout.addView(tagLinearLayout);
        } else {
            // just tag display
            TagLinearLayout tagLinearLayout = (TagLinearLayout) TagLinearLayout_.build(getActivity());
            tagLinearLayout.setDisplayTagState(tag);
            mTagLinearLayout.addView(tagLinearLayout);
        }
    }

    @Override
    public void onParametersClicked() {
        mEventBus.post(new EventBusEvents.ShowSettings());
    }

    @Override
    public void onTagAdded(String tag) {
        mTags.add(tag);
    }
}
