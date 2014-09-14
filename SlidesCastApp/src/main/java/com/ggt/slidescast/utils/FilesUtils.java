package com.ggt.slidescast.utils;

import android.content.Context;

import com.ggt.slidescast.R;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Utils for files.
 *
 * @author guiguito
 */
public class FilesUtils {

    private FilesUtils() {

    }

    /**
     * Get all files from a directory recursively.
     *
     * @param dir
     * @param extension
     * @return
     */
    public static List<File> walkdir(File dir, String... extension) {
        String[] fixedExtensions = new String[extension.length];
        for (int i = 0; i < extension.length; i++) {
            fixedExtensions[i] = "." + extension[i].toLowerCase();
        }
        File listFile[] = dir.listFiles();
        List<File> filesList = new ArrayList<File>();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    filesList.addAll(walkdir(listFile[i], extension));
                } else {
                    for (String pdfPattern : fixedExtensions) {
                        if (listFile[i].getName().toLowerCase().endsWith(pdfPattern)) {
                            // Do what ever u want
                            filesList.add(listFile[i]);
                        }
                    }
                }
            }
        }
        return filesList;
    }

    public static double getFileSizeInMB(File file) {
        double bytes = file.length();
        double kilobytes = (bytes / 1024);
        double megabytes = (kilobytes / 1024);
        return megabytes;
    }

    public static String getFormattedFileSizeInMB(Context context, File file) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        String output = nf.format(getFileSizeInMB(file));
        return output + context.getString(R.string.file_mb);
    }

    public static List<File> filterGDocsFiles(List<File> files) {
        List<File> filteredList = new ArrayList<File>();
        for (File file : files) {
            if (!file.getAbsolutePath().contains("com.google.android.apps.docs")) {
                filteredList.add(file);
            }
        }
        return filteredList;
    }
}
