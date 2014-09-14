package com.ggt.slidescast.ui;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ggt.slidescast.R;
import com.ggt.slidescast.model.LocalFile;
import com.ggt.slidescast.ui.adapters.LocalFilesAdapter;
import com.ggt.slidescast.utils.FilesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity used to pick a file to upload.
 *
 * @author guiguito
 */
@EActivity(R.layout.activity_pick_a_file)
public class PickAFileActivity extends SlidesCastMotherActivity implements OnRefreshListener {

    @ViewById
    ListView mUserFilesListview;

    @ViewById
    SwipeRefreshLayout mSwipeContainerRefreshLayout;

    @Bean
    LocalFilesAdapter mLocalFilesAdapter;

    public static final String KEY_LOCAL_FILE = "KEY_LOCAL_FILE";

    @AfterViews
    void init() {
        mSwipeContainerRefreshLayout.setOnRefreshListener(this);
        mSwipeContainerRefreshLayout.setColorScheme(R.color.slidescast_red, R.color.slidescast_green, R.color.slidescast_blue, R.color.slidescast_yellow);

        mUserFilesListview.setAdapter(mLocalFilesAdapter);
        mUserFilesListview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onLocalFileClicked((LocalFile) mLocalFilesAdapter.getItem(position));
            }
        });
        List<LocalFile> localFiles = LocalFile.getAll();
        if (localFiles.size() > 0) {
            mLocalFilesAdapter.addAll(localFiles);
        } else {
            loadFiles();
        }
    }

    @Background
    void loadFiles() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mSwipeContainerRefreshLayout.setRefreshing(true);
            }
        });
        final List<LocalFile> localFiles = loadLocalFilesForUploadToSlideShare();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mLocalFilesAdapter.clear();
                mLocalFilesAdapter.addAll(localFiles);
                mSwipeContainerRefreshLayout.setRefreshing(false);
            }
        });
    }

    public static List<LocalFile> loadLocalFilesForUploadToSlideShare() {
        List<File> files = FilesUtils.filterGDocsFiles(FilesUtils.walkdir(Environment.getExternalStorageDirectory(), "pdf", "odp", "odt", "ppt", "pps", "pptx",
                "ppsx", "pot", "potx", "doc", "docx"));//removed text files /*, "rtf", "txt"*/
        final List<LocalFile> localFiles = new ArrayList<LocalFile>();
        for (File file : files) {
            localFiles.add(fileToLocalFile(file));
        }
        LocalFile.deleteAll();
        LocalFile.addAll(localFiles);
        return localFiles;
    }

    private static LocalFile fileToLocalFile(File file) {
        LocalFile localFile = new LocalFile();
        localFile.setFilePath(file.getParent());
        localFile.setFileName(file.getName());
        return localFile;
    }

    private void onLocalFileClicked(LocalFile localFile) {
        setResult(RESULT_OK, new Intent().putExtra(KEY_LOCAL_FILE, localFile));
        finish();
    }

    @Override
    public void onRefresh() {
        loadFiles();
    }
}
