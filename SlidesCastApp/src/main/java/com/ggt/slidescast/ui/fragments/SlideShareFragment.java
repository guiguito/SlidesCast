package com.ggt.slidescast.ui.fragments;

import android.graphics.Color;
import android.support.v4.view.ViewPager;

import com.ggt.slidescast.R;
import com.ggt.slidescast.model.app.Presentation;
import com.ggt.slidescast.model.app.Presentation.PresentationType;
import com.ggt.slidescast.slideshare.model.Slideshow;
import com.ggt.slidescast.ui.adapters.SlideShareTabsAdapter;
import com.ggt.slidescast.ui.views.SimplePagerItem;
import com.ggt.slidescast.ui.views.SlidingTabLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Main slideshare fragment. Hosts a pager with various fragments.
 *
 * @author guiguito
 */
@EFragment(R.layout.fragment_slideshare)
public class SlideShareFragment extends MotherFragment {

    @ViewById
    SlidingTabLayout mSlideShareSlidingTabLayout;

    @ViewById
    ViewPager mSlideShareViewPager;

    SlideShareTabsAdapter mSlideShareTabsAdapter;

    List<SimplePagerItem> mTabs = new ArrayList<SimplePagerItem>();

    public static final String SUBTYPE_SLIDE_SHARE_USER = "SUBTYPE_SLIDE_SHARE_USER";
    public static final String SUBTYPE_SLIDE_SHARE_FAVORITES = "SUBTYPE_SLIDE_SHARE_FAVORITES";
    public static final String SUBTYPE_SLIDE_SHARE_SEARCH = "SUBTYPE_SLIDE_SHARE_SEARCH";

    @AfterViews
    void init() {
        mTabs.add(new SimplePagerItem(getString(R.string.fragment_user_title), getResources().getColor(R.color.slidescast_red), Color.GRAY,
                SlideShareUserFragment_.class));
        mTabs.add(new SimplePagerItem(getString(R.string.fragment_favorites_title), getResources().getColor(R.color.slidescast_green), Color.GRAY,
                SlideShareFavoritesFragment_.class));
        mTabs.add(new SimplePagerItem(getString(R.string.fragment_search_title), getResources().getColor(R.color.slidescast_blue), Color.GRAY,
                SlideShareSearchFragment_.class));
        mTabs.add(new SimplePagerItem(getString(R.string.fragment_upload_slides_title), getResources().getColor(R.color.slidescast_yellow), Color.GRAY,
                SlideShareUploadFragment_.class));
        mSlideShareTabsAdapter = new SlideShareTabsAdapter(getFragmentManager(), mTabs);
        mSlideShareViewPager.setOffscreenPageLimit(3);
        mSlideShareViewPager.setAdapter(mSlideShareTabsAdapter);
        mSlideShareSlidingTabLayout.setViewPager(mSlideShareViewPager);
        mSlideShareSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return mTabs.get(position).getTabColor();
            }

            @Override
            public int getDividerColor(int position) {
                return mTabs.get(position).getDividerColor();
            }
        });
    }

    public static final Presentation slideShowToPresentation(Slideshow slideshow, String subtype) {
        Presentation presentation = new Presentation();
        presentation.setType(PresentationType.SLIDESHARE);
        presentation.setSubType(subtype);
        presentation.setExternalId(slideshow.getID());
        presentation.setTitle(slideshow.getTitle());
        presentation.setDescription(slideshow.getDescription());
        presentation.setAuthor(slideshow.getUsername());
        String fullEmbed = slideshow.getEmbed();
        int firstQuote = fullEmbed.indexOf("\"");
        firstQuote++;
        String url = fullEmbed.substring(firstQuote, fullEmbed.indexOf("\"", firstQuote));
        presentation.setUrl(url + "?jsapi=true");
        presentation.setThumbNailUrl("http:" + slideshow.getThumbnailURL());
        return presentation;
    }

    public static final List<Presentation> slideShowsToListPresentation(List<Slideshow> slideshows, String subType) {
        List<Presentation> presentations = new ArrayList<Presentation>();
        for (Slideshow slideshow : slideshows) {
            presentations.add(SlideShareFragment.slideShowToPresentation(slideshow, subType));
        }
        return presentations;
    }
}
