package com.ggt.slidescast.utils;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utils for assets.
 *
 * @author guiguito
 */
public class AssetManagerUtils {

    public static final boolean assetsExists(AssetManager assetManager, String path) {

        try {
            InputStream stream = assetManager.open(path);
            if (stream != null) {
                stream.close();
                return true;
            }
        } catch (IOException e1) {
            // nothing
        }

        try {
            if (assetManager.list(path) != null && assetManager.list(path).length > 0) {
                return true;
            }
        } catch (IOException e) {
            // nothing
        }
        return false;
    }

    public static final boolean assetsIsDirectory(AssetManager assetManager, String path) {
        try {
            if (assetManager.list(path) != null && assetManager.list(path).length > 0) {
                return true;
            }
        } catch (IOException e) {
            // nothing
        }
        return false;
    }
}
