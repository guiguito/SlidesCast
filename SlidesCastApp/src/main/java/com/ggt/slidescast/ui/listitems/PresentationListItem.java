package com.ggt.slidescast.ui.listitems;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ggt.slidescast.R;
import com.ggt.slidescast.model.app.Presentation;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;
import java.util.Date;

/**
 * Layout for a presentation (slideshare or local file)
 *
 * @author guiguito
 */
@EViewGroup(R.layout.listitem_slideshow)
public class PresentationListItem extends LinearLayout {

    @ViewById
    TextView mSlideShowTitleTextView, mSlideShowAuthorTextView, mSlideShowDescriptionTextView, mSlideShowLastUseTextView;

    @ViewById
    ImageView mSlideShowImageView;

    Presentation mPresentation;

    public PresentationListItem(Context context) {
        super(context);
    }

    @AfterViews
    void init() {
        // mSlideShowDescriptionTextView.setMovementMethod(LinkMovementMethod
        // .getInstance());
    }

    public Presentation getPresentation() {
        return mPresentation;
    }

    public void bind(Presentation presentation, boolean showLastUsage) {
        mPresentation = presentation;
        if (presentation.getType().equals(Presentation.PresentationType.SLIDESHARE)) {
            Picasso.with(getContext()).load(presentation.getThumbNailUrl()).error(R.drawable.slideshare_icon).into(mSlideShowImageView);
        } else {
            // icons instead
            if (presentation.getExtension().equals(Presentation.PresentationExtensions.PDF)) {
                Picasso.with(getContext()).load(R.drawable.icon_pdf).into(mSlideShowImageView);
            } else if (presentation.getExtension().equals(Presentation.PresentationExtensions.ODT)) {
                Picasso.with(getContext()).load(R.drawable.icon_odt).into(mSlideShowImageView);
            } else if (presentation.getExtension().equals(Presentation.PresentationExtensions.ODP)) {
                Picasso.with(getContext()).load(R.drawable.icon_odp).into(mSlideShowImageView);
            } else if (presentation.getExtension().equals(Presentation.PresentationExtensions.FODT)) {
                Picasso.with(getContext()).load(R.drawable.icon_fodt).into(mSlideShowImageView);
            } else if (presentation.getExtension().equals(Presentation.PresentationExtensions.ODS)) {
                Picasso.with(getContext()).load(R.drawable.icon_ods).into(mSlideShowImageView);
            } else if (presentation.getExtension().equals(Presentation.PresentationExtensions.ODF)) {
                Picasso.with(getContext()).load(R.drawable.icon_odf).into(mSlideShowImageView);
            } else {
                Picasso.with(getContext()).load(R.drawable.icon_unknown);
            }
        }
        mSlideShowTitleTextView.setText(Html.fromHtml(presentation.getTitle()));
        if (!TextUtils.isEmpty(presentation.getAuthor())) {
            mSlideShowAuthorTextView.setVisibility(View.VISIBLE);
            mSlideShowAuthorTextView.setText(Html.fromHtml(presentation.getAuthor()));
        } else {
            mSlideShowAuthorTextView.setVisibility(View.GONE);
        }
        if (showLastUsage) {
            if (presentation.getLasUse() != 0) {
                mSlideShowLastUseTextView.setVisibility(View.VISIBLE);
                mSlideShowLastUseTextView.setText(DateFormat.getDateTimeInstance().format(new Date(presentation.getLasUse())));
            } else {
                mSlideShowLastUseTextView.setVisibility(View.GONE);
            }
        }
        if (!TextUtils.isEmpty(presentation.getDescription())) {
            mSlideShowDescriptionTextView.setVisibility(View.VISIBLE);
            mSlideShowDescriptionTextView.setText(Html.fromHtml(presentation.getDescription()));
        } else {
            mSlideShowDescriptionTextView.setVisibility(View.GONE);
        }

    }

}
