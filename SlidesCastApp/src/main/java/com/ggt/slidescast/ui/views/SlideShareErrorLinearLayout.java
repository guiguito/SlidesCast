package com.ggt.slidescast.ui.views;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ggt.slidescast.R;
import com.joanzapata.android.iconify.Iconify;

@EViewGroup(R.layout.linearlayout_slideshare_error)
public class SlideShareErrorLinearLayout extends LinearLayout {

	@ViewById
	TextView mUserErrorTextView;

	@ViewById
	LinearLayout mGoToParamLinearLayout;

	@ViewById
	TextView mGoToParamTextView, mGoToParamIconifyView;

	SlideShareErrorLinearLayoutListener mSlideShareErrorLinearLayoutListener;

	public interface SlideShareErrorLinearLayoutListener {
		void onParametersClicked();
	}

	public SlideShareErrorLinearLayout(Context context) {
		super(context);
		init();
	}

	public SlideShareErrorLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SlideShareErrorLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setOrientation(LinearLayout.VERTICAL);
	}

	@AfterViews
	void afterViews() {
		if (!isInEditMode()) {
			Iconify.addIcons(mGoToParamIconifyView);
		}
	}

	public void setSlideShareErrorLinearLayoutListener(SlideShareErrorLinearLayoutListener slideShareErrorLinearLayoutListener) {
		mSlideShareErrorLinearLayoutListener = slideShareErrorLinearLayoutListener;
	}

	public void showError(int textUserError, int textGoToParam) {
		setVisibility(View.VISIBLE);
		mGoToParamLinearLayout.setVisibility(View.VISIBLE);
		mUserErrorTextView.setText(getContext().getString(textUserError));
		mGoToParamTextView.setText(getContext().getString(textGoToParam));
	}

	public void showError(int textUserError) {
		setVisibility(View.VISIBLE);
		mGoToParamLinearLayout.setVisibility(View.GONE);
		mUserErrorTextView.setText(getContext().getString(textUserError));
	}

	@Click(R.id.mGoToParamLinearLayout)
	void openParameters() {
		if (mSlideShareErrorLinearLayoutListener != null) {
			mSlideShareErrorLinearLayoutListener.onParametersClicked();
		}
	}
}
