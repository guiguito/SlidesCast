package com.ggt.slidescast.ui;

import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.ggt.slidescast.R;
import com.ggt.slidescast.chromecast.SlidesCastCommand;
import com.ggt.slidescast.chromecast.SlidesCastEvent;
import com.ggt.slidescast.model.app.Presentation;
import com.ggt.slidescast.model.app.PresentationUse;
import com.ggt.slidescast.utils.SlidesCastConstants;
import com.ggt.slidescast.utils.SlidesCastPrefs_;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.joanzapata.android.iconify.Iconify;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Activity that displays a presentation in fullscreen while casting it.
 *
 * @author guiguito
 */
@EActivity(R.layout.activity_slidescast_fullscreen)
public class SlidesCastFullScreenActivity extends SlidesCastMotherActivity {

    @ViewById
    TextView mLeftButton, mRightButton, mFirstButton, mLastButton;

    @ViewById
    WebView mPresentationWebView;

    @Pref
    SlidesCastPrefs_ mSlidesCastPrefs;

    Presentation mSelectedPresentation;

    Runnable mHideAgain;

    public void onEvent(SlidesCastEvent evt) {

    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @AfterViews
    void initViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mSelectedPresentation = Presentation.load(Presentation.class, getIntent().getLongExtra(SlidesCastConstants.KEY_PRESENTATION_ID, -1));
        Iconify.addIcons(mFirstButton, mLastButton, mLeftButton, mRightButton);
        mPresentationWebView.getSettings().setJavaScriptEnabled(true);
        mPresentationWebView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mHideAgain == null) {
                    mHideAgain = new Runnable() {
                        public void run() {
                            hideSystemUI();
                            mHideAgain = null;
                        }
                    };
                    mPresentationWebView.postDelayed(mHideAgain, 2000);
                }
                return true;
            }
        });
        // mPresentationWebView.loadUrl(mSelectedPresentation.getUrl());
        String htmlPage = getString(R.string.cast_html_page, mSelectedPresentation.getTitle(), mSelectedPresentation.getUrl(), mSelectedPresentation.getType());
        mPresentationWebView.loadData(htmlPage, "text/html", "UTF-8");
        mPresentationWebView.postDelayed(new Runnable() {

            @Override
            public void run() {
                castPresentation();
            }
        }, 250);
        hideSystemUI();
        if (mSlidesCastPrefs.keepScreenOn().get()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishPresentation();
    }

    public void finishPresentation() {
        if (mSelectedPresentation != null) {
            SlidesCastCommand command = new SlidesCastCommand();
            command.setCommand(SlidesCastCommand.Command.FINISH);
            sendCastCommand(command);
        }
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_cast).setVisible(false);
        return true;
    }

    public void castPresentation() {
        if (mSelectedPresentation != null) {
            EasyTracker.getInstance(this).send(MapBuilder.createEvent("ui_action", "button_press", "play_button", null).build());
            PresentationUse.addOrUpdate(mSelectedPresentation.getType(), mSelectedPresentation.getExternalId());
            mSelectedPresentation.save();
            SlidesCastCommand command = new SlidesCastCommand();
            command.setCommand(SlidesCastCommand.Command.START_CAST);
            command.setCastType(mSelectedPresentation.getType().toString());
            command.setValue(mSelectedPresentation.getUrl());
            sendCastCommand(command);
            mPresentationWebView.loadUrl("javascript:startCast(\"" + mSelectedPresentation.getUrl() + "\",\"" + mSelectedPresentation.getType() + "\");");
        }
    }

    @Click(R.id.mFirstButton)
    public void first() {
        SlidesCastCommand command = new SlidesCastCommand();
        command.setCommand(SlidesCastCommand.Command.FIRST);
        sendCastCommand(command);
        mPresentationWebView.loadUrl("javascript: window.first();");
    }

    @Click(R.id.mLeftButton)
    public void left() {
        SlidesCastCommand command = new SlidesCastCommand();
        command.setCommand(SlidesCastCommand.Command.LEFT);
        sendCastCommand(command);
        mPresentationWebView.loadUrl("javascript: window.left();");
    }

    @Click(R.id.mRightButton)
    public void right() {
        SlidesCastCommand command = new SlidesCastCommand();
        command.setCommand(SlidesCastCommand.Command.RIGHT);
        sendCastCommand(command);
        mPresentationWebView.loadUrl("javascript: window.right();");
    }

    @Click(R.id.mLastButton)
    public void last() {
        SlidesCastCommand command = new SlidesCastCommand();
        command.setCommand(SlidesCastCommand.Command.LAST);
        sendCastCommand(command);
        mPresentationWebView.loadUrl("javascript: window.last();");
    }

}
