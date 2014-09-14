package com.ggt.slidescast.httpserver;

import android.content.res.AssetManager;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * SlidesCast HttpServer.
 * This one serves files from assets and one specific file.
 *
 * @author guiguito
 */
public class SlidesCastHttpServer extends NanoHTTPD {

    private File mSelectedFilePath;
    private String mSelectedFileName;
    private String mTag;

    public SlidesCastHttpServer(int port, File wwwroot, String assetsRoot, AssetManager assetManager) throws IOException {
        super(port, wwwroot, assetsRoot, assetManager);
    }

    public void setSelectedFile(String selectedFilePath, String selectedFileName, String tag) {
        mSelectedFilePath = new File(selectedFilePath);
        mSelectedFileName = selectedFileName;
        mTag = tag;
    }

    @Override
    public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
        if (mSelectedFilePath != null && uri != null && uri.contains(mTag)) {
            // file sharing
            return super.serveFile(mSelectedFileName, header, mSelectedFilePath, false, false);
        } else {
            return super.serve(uri, method, header, parms, files);
        }
    }

}
