package com.ggt.slidescast.ui;

import android.content.Intent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ggt.slidescast.R;
import com.ggt.slidescast.chromecast.SlidesCastEvent;
import com.ggt.slidescast.httpserver.SlidesCastHttpServer;
import com.ggt.slidescast.model.app.Presentation;
import com.ggt.slidescast.model.app.Presentation.PresentationType;
import com.ggt.slidescast.ui.listitems.PresentationListItem;
import com.ggt.slidescast.ui.listitems.PresentationListItem_;
import com.ggt.slidescast.utils.NetworkUtils;
import com.ggt.slidescast.utils.SlidesCastConstants;
import com.joanzapata.android.iconify.Iconify;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

/**
 * Activity that displays a presentation.
 *
 * @author guiguito
 */
@EActivity(R.layout.activity_slidescast)
public class SlidesCastActivity extends SlidesCastMotherActivity {

    @ViewById
    TextView mLeftButton, mRightButton, mFirstButton, mLastButton;

    @ViewById
    LinearLayout mSlidesCastActivityMainLinearLayout;

    @ViewById
    WebView mPresentationWebView;

    SlidesCastHttpServer mServer;
    Presentation mSelectedPresentation;

    private static final String SITE_ASSETS_ROOT = "viewer";

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
        PresentationListItem slideShowHeaderLinearLayout = PresentationListItem_.build(this);
        slideShowHeaderLinearLayout.bind(mSelectedPresentation, false);
        mSlidesCastActivityMainLinearLayout.addView(slideShowHeaderLinearLayout, 0);
        Iconify.addIcons(mFirstButton, mLastButton, mLeftButton, mRightButton);
        mPresentationWebView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mPresentationWebView.getSettings().setJavaScriptEnabled(true);
        if (mSelectedPresentation.getType() == PresentationType.LOCALFILE) {
            String myIpAddress = NetworkUtils.getWifiDetails(getApplicationContext()).ipAddress;
            mSelectedPresentation.setUrl("http://" + myIpAddress + ":" + SlidesCastConstants.LOCAL_SERVER_PORT + "/#" + mSelectedPresentation.getTag());
            try {
                mServer = new SlidesCastHttpServer(SlidesCastConstants.LOCAL_SERVER_PORT, null, SITE_ASSETS_ROOT, getAssets());
                mServer.setSelectedFile(mSelectedPresentation.getFilepath(), mSelectedPresentation.getFilename(), mSelectedPresentation.getTag());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), getString(R.string.cant_load_file), Toast.LENGTH_LONG).show();
                finish();
            }
        }
        String htmlPage = getString(R.string.cast_html_page, mSelectedPresentation.getTitle(), mSelectedPresentation.getUrl(), mSelectedPresentation.getType());
        mPresentationWebView.loadData(htmlPage, "text/html", "UTF-8");
        mPresentationWebView.postDelayed(new Runnable() {

            @Override
            public void run() {
                mPresentationWebView.loadUrl("javascript:startCast(\"" + mSelectedPresentation.getUrl() + "\",\"" + mSelectedPresentation.getType() + "\");");
            }
        }, 250);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_cast:
                // not handle
                Intent intent = new Intent(this, SlidesCastFullScreenActivity_.class);
                intent.putExtra(SlidesCastConstants.KEY_PRESENTATION_ID, mSelectedPresentation.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServer != null) {
            mServer.stop();
        }
    }

    @Click(R.id.mFirstButton)
    public void first() {
        mPresentationWebView.loadUrl("javascript: window.first();");
    }

    @Click(R.id.mLeftButton)
    public void left() {
        mPresentationWebView.loadUrl("javascript: window.left();");
    }

    @Click(R.id.mRightButton)
    public void right() {
        mPresentationWebView.loadUrl("javascript: window.right();");
    }

    @Click(R.id.mLastButton)
    public void last() {
        mPresentationWebView.loadUrl("javascript: window.last();");
    }

}
