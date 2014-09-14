package com.ggt.slidescast.ui.fragments;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ggt.slidescast.R;
import com.ggt.slidescast.model.app.Presentation;
import com.ggt.slidescast.model.app.Presentation.PresentationType;
import com.ggt.slidescast.ui.SlidesCastActivity_;
import com.ggt.slidescast.ui.adapters.PresentationsAdapter;
import com.ggt.slidescast.utils.FilesUtils;
import com.ggt.slidescast.utils.SlidesCastConstants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment displaying local files.
 *
 * @author guiguito
 */

@EFragment(R.layout.fragment_my_files)
public class MyFilesFragment extends MotherFragment implements OnRefreshListener {

    @ViewById
    ListView mPdfUserFilesListview;

    @ViewById
    SwipeRefreshLayout mSwipeContainerRefreshLayout;

    @Bean
    PresentationsAdapter mLocalFilesAdapter;

    @AfterViews
    void init() {
        mSwipeContainerRefreshLayout.setOnRefreshListener(this);
        mSwipeContainerRefreshLayout.setColorScheme(R.color.slidescast_red, R.color.slidescast_green, R.color.slidescast_blue, R.color.slidescast_yellow);

        mPdfUserFilesListview.setAdapter(mLocalFilesAdapter);
        mPdfUserFilesListview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onLocalFileClicked((Presentation) mLocalFilesAdapter.getItem(position));
            }
        });
        List<Presentation> presentations = Presentation.getAllByType(PresentationType.LOCALFILE);
        if (presentations.size() > 0) {
            mLocalFilesAdapter.addAll(presentations);
        } else {
            loadFiles();
        }
    }

    @Background
    void loadFiles() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mSwipeContainerRefreshLayout.setRefreshing(true);
            }
        });
        final List<Presentation> presentations = loadLocalFilesForChromecastDisplay();
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    mLocalFilesAdapter.clear();
                    mLocalFilesAdapter.addAll(presentations);
                    mSwipeContainerRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    private void onLocalFileClicked(Presentation presentation) {
        Intent intent = new Intent(getActivity(), SlidesCastActivity_.class);
        intent.putExtra(SlidesCastConstants.KEY_PRESENTATION_ID, presentation.getId());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        loadFiles();
    }

    public static List<Presentation> loadLocalFilesForChromecastDisplay() {
        List<File> files = FilesUtils.filterGDocsFiles(FilesUtils.walkdir(Environment.getExternalStorageDirectory(), "pdf", "odp", "odt", "ods", "fodt"));
        List<Presentation> presentations = new ArrayList<Presentation>();
        for (File file : files) {
            presentations.add(fileToPresentation(file));
        }
        Presentation.deleteAllByType(PresentationType.LOCALFILE);
        Presentation.addAll(presentations);
        return presentations;
    }

    public static Presentation fileToPresentation(File file) {
        Presentation presentation = new Presentation();
        presentation.setExternalId(file.getAbsolutePath());
        presentation.setType(PresentationType.LOCALFILE);
        presentation.setExtension(Presentation.PresentationExtensions.stringToPresentationExtensions(file.getName().substring(
                file.getName().lastIndexOf(".") + 1)));
        presentation.setTitle(file.getName());
        presentation.setTag(SlidesCastConstants.TAG_DOCUMENT + "." + presentation.getExtension());
        presentation.setFilepath(file.getParent());
        presentation.setFilename(file.getName());
        return presentation;
    }


}
