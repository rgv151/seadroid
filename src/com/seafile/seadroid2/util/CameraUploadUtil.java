package com.seafile.seadroid2.util;

import android.util.Log;
import android.webkit.MimeTypeMap;
import com.google.common.collect.Lists;
import com.seafile.seadroid2.SettingsManager;
import com.seafile.seadroid2.gallery.ImageManager;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

public class CameraUploadUtil {

    private static final String HIDDEN_PREFIX = ".";

    private static final FileFilter MEDIA_FILTER = new FileFilter() {
        @Override
        public boolean accept(File file) {
            return isMimeMedia(CameraUploadUtil.getMimeType(file.getName()));
        }
    };

    /**
     * {get photo path from sd-card @link
     * http://stackoverflow.com/questions/3873496
     * /how-to-get-image-path-from-images-stored-on-sd-card}
     */
    private static final FileFilter MEDIA_DIR_FILTER = new FileFilter() {
        @Override
        public boolean accept(File folder) {
            try {
                // Checking only directories
                if (folder.isDirectory()
                        && !folder.getName().startsWith(HIDDEN_PREFIX)) {
                    File[] listOfFiles = folder.listFiles();

                    if (listOfFiles == null)
                        return false;

                    // For each file in the directory
                    for (File file : listOfFiles) {
                        if (isMimeMedia(CameraUploadUtil.getMimeType(file.getName()))) {
                            return true;
                        }
                    }
                }
                return false;
            } catch (SecurityException e) {
                Log.v("debug", "Access Denied");
                return false;
            }
        }
    };

    private static List<File> getMediaAbsolutePathList(String path) {

        List<File> list = Lists.newArrayList();

        // Current directory File instance
        final File pathDir = new File(path);
        if (!pathDir.isDirectory()) {
            return null;
        }
        // List folders in this directory with the directory filter
        final File[] dirs = pathDir.listFiles(MEDIA_DIR_FILTER);
        if (dirs != null) {
            // Add each folder to the File list for the list adapter
            for (File dir : dirs) {
                // List photos and videos inside each directory with the media filter
                final File[] mediaFiles = dir.listFiles(MEDIA_FILTER);
                if (mediaFiles != null) {
                    // Add each file to the File list for the list adapter
                    for (File file : mediaFiles) {
                        list.add(file);
                    }
                }
            }
        }
        // List photos and videos in this directory with the media filter
        final File[] medias = pathDir.listFiles(MEDIA_FILTER);
        if (medias != null) {
            // Add each file to the File list for the list adapter
            for (File file : medias) {
                list.add(file);
            }
        }
        return list;
    }

    public static List<File> getAllMediaAbsolutePathList() {
        List<File> list = Lists.newArrayList();
        List<File> mediaAbsolutePathList;

        List<String> paths = ImageManager.getAllPath();
        for (String path : paths) {
            mediaAbsolutePathList = getMediaAbsolutePathList(path);
            if (mediaAbsolutePathList != null) {
                for (File sf : mediaAbsolutePathList) {
                    if (!list.contains(sf)) {
                        list.add(sf);
                    }
                }
            }
        }
        return list;
    }

    public static String getMimeType(String fileName) {
        int dotIndex = fileName.lastIndexOf('.') + 1;
        String ext = fileName.substring(dotIndex).toLowerCase();
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
    }

    public static Boolean isMimeMedia(String mime) {
        if (mime == null) return false;
        return (mime.startsWith("image/") || (SettingsManager.instance().isCameraUploadIncludeVideos() && mime.startsWith("video/")));
    }
}