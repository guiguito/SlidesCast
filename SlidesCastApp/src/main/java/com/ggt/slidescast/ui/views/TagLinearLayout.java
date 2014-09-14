package com.ggt.slidescast.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ggt.slidescast.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Linear layout for tags you attach to a slide you wan to upload.
 *
 * @author guiguito
 */
@EViewGroup(R.layout.linearlayout_tag)
public class TagLinearLayout extends LinearLayout {

    @ViewById
    TextView mTagTextView;

    @ViewById
    EditText mAddTagEditText;

    private TagAddedListener mTagAddedListener;

    public interface TagAddedListener {
        void onTagAdded(String tag);
    }

    public TagLinearLayout(Context context) {
        super(context);
        init();
    }

    public TagLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TagLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
    }

    public void setTagAddedListener(TagAddedListener tagAddedListener) {
        mTagAddedListener = tagAddedListener;
    }

    @AfterViews
    void afterViews() {
        mAddTagEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (mTagAddedListener != null) {
                        String tag = mAddTagEditText.getText().toString();
                        mTagAddedListener.onTagAdded(tag);
                        mAddTagEditText.setVisibility(View.GONE);
                        mTagTextView.setText(tag);
                        mTagTextView.setVisibility(View.VISIBLE);
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mAddTagEditText.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void setAddTagState() {
        mTagTextView.setVisibility(View.GONE);
        mAddTagEditText.setVisibility(View.VISIBLE);
    }

    public void setDisplayTagState(String tag) {
        if (tag != null) {
            mTagTextView.setText(tag);
        }
        mTagTextView.setVisibility(View.VISIBLE);
        mAddTagEditText.setVisibility(View.GONE);
    }

}
