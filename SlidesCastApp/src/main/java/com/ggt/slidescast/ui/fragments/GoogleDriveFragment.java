package com.ggt.slidescast.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ggt.slidescast.R;
import com.ggt.slidescast.model.app.Presentation;
import com.ggt.slidescast.model.app.Presentation.PresentationType;
import com.ggt.slidescast.ui.adapters.DriveResultsAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveApi.MetadataBufferResult;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Fragment that displays presentations from google drive.
 *
 * @author guiguito
 */
@EFragment(R.layout.fragment_google_drive)
public class GoogleDriveFragment extends MotherFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @ViewById
    ListView mListFilesListview;
    GoogleApiClient mGoogleApiClient;
    @Bean
    DriveResultsAdapter mResultsAdapter;

    Metadata mSelectedMetadata;
    private String mNextPageToken;
    private boolean mHasMore;

    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 1;

    @AfterViews
    void init() {
        mHasMore = true; // initial request assumes there are files results.
        mListFilesListview.setAdapter(mResultsAdapter);
        mListFilesListview.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            /**
             * Handles onScroll to retrieve next pages of results if there are
             * more results items to display.
             */
            @Override
            public void onScroll(AbsListView view, int first, int visible, int total) {
                if (mNextPageToken != null && first + visible + 5 < total) {
                    retrieveNextPage();
                }
            }
        });
        mListFilesListview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onMetaDataClicked(mResultsAdapter.getItem(position));
            }
        });
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).addApi(Drive.API).addScope(Drive.SCOPE_FILE)
                .addScope(new Scope("https://www.googleapis.com/auth/drive.readonly")).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
    }

    public void onMetaDataClicked(Metadata metadata) {
        mSelectedMetadata = metadata;
        Presentation presentation = new Presentation();
        presentation.setType(PresentationType.GOOGLEDRIVE);
        presentation.setTitle(metadata.getTitle());
        presentation.setDescription(metadata.getDescription());
        presentation.setUrl(metadata.getEmbedLink());
        // ((SlidesCastMainActivity)
        // getActivity()).setPresentation(presentation);
        // TODO launch cast activity
    }

    /**
     * Clears the result buffer to avoid memory leaks as soon as the activity is
     * no longer visible by the user.
     */

    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        mResultsAdapter.clear();
    }

    @Override
    public void onConnected(Bundle arg0) {
        retrieveNextPage();
    }

    private void retrieveNextPage() {
        // if there are no more results to retrieve,
        // return silently.
        if (!mHasMore) {
            return;
        }
        // retrieve the results for the next page.
        Query query = new Query.Builder().addFilter(Filters.eq(SearchableField.MIME_TYPE, "application/vnd.google-apps.presentation"))
                .setPageToken(mNextPageToken).build();
        // TODO add filter
        // Drive.DriveApi.getRootFolder(mGoogleApiClient).
        Drive.DriveApi.query(mGoogleApiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {

            @Override
            public void onResult(MetadataBufferResult result) {
                if (!result.getStatus().isSuccess()) {
                    Toast.makeText(getActivity(), "error retrieving files", Toast.LENGTH_LONG).show();
                    return;
                }
                mResultsAdapter.append(result.getMetadataBuffer());
                mNextPageToken = result.getMetadataBuffer().getNextPageToken();
                mHasMore = mNextPageToken != null;
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        handleError(-1, connectionResult, null);
    }

    @Override
    public void onConnectionSuspended(int connectionSuspendedValue) {
        handleError(connectionSuspendedValue, null, null);
    }

    private void handleError(int connectionSuspendedValue, ConnectionResult connectionResult, String message) {
        if (connectionResult != null && connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActivity(), RESOLVE_CONNECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appopriately
            }
        } else if (connectionResult != null) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), getActivity(), 0).show();
        } else if (message != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case RESOLVE_CONNECTION_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    mGoogleApiClient.connect();
                }
                break;
        }
    }
}
