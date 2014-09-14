package com.ggt.slidescast.ui.fragments;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ggt.slidescast.R;
import com.ggt.slidescast.model.app.Presentation;
import com.ggt.slidescast.model.app.PresentationUse;
import com.ggt.slidescast.ui.SlidesCastActivity_;
import com.ggt.slidescast.ui.SlidesCastMainActivity;
import com.ggt.slidescast.ui.listitems.PresentationListItem;
import com.ggt.slidescast.ui.listitems.PresentationListItem_;
import com.ggt.slidescast.utils.SlidesCastConstants;
import com.joanzapata.android.iconify.Iconify;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Home fragment.
 * Show indications or last slides displayed.
 *
 * @author guiguito
 */

@EFragment(R.layout.fragment_home)
public class HomeFragment extends MotherFragment {

    @ViewById
    LinearLayout mLastSlidesContainerLinearLayout, mLastSlidesLinearLayout, mNoLastSlidesContainerLinearLayout;

    @ViewById
    TextView mLocalSlidesIconifyView;

    // TODO improve small design and display details
    // TODO test fullscreen on other android versions
    @AfterViews
    void init() {
        //icon init
        mLocalSlidesIconifyView.setText("{" + SlidesCastMainActivity.LOCAL_FILES_ICONIFY + "}");
        mLocalSlidesIconifyView.setTextColor(getResources().getColor(R.color.slidescast_green));
        Iconify.addIcons(mLocalSlidesIconifyView);

        //load last slides used
        List<Presentation> presentations = PresentationUse.getLastPresentationsUsed();
        if (presentations.size() > 0) {
            mLastSlidesContainerLinearLayout.setVisibility(View.VISIBLE);
            mNoLastSlidesContainerLinearLayout.setVisibility(View.GONE);
            for (Presentation presentation : presentations) {
                PresentationListItem presentationListItem = PresentationListItem_.build(getActivity());
                presentationListItem.bind(presentation, true);
                presentationListItem.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onPresentationClicked(((PresentationListItem) v).getPresentation());
                    }
                });
                mLastSlidesLinearLayout.addView(presentationListItem);
            }
        } else {
            mLastSlidesContainerLinearLayout.setVisibility(View.GONE);
            mNoLastSlidesContainerLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void onPresentationClicked(Presentation presentation) {
        Intent intent = new Intent(getActivity(), SlidesCastActivity_.class);
        intent.putExtra(SlidesCastConstants.KEY_PRESENTATION_ID, presentation.getId());
        startActivity(intent);
    }

    public class CastFromSlideShareEvent {
    }

    @Click(R.id.mCastFromSlideshareTextView)
    void onSlideShareClicked() {
        mEventBus.post(new CastFromSlideShareEvent());
    }

    public class CastFromLocalFilesEvent {
    }

    @Click({R.id.mCastFromLocalFilesTextView, R.id.mLocalSlidesIconifyView})
    void onLocalFilesClicked() {
        mEventBus.post(new CastFromLocalFilesEvent());
    }

}
