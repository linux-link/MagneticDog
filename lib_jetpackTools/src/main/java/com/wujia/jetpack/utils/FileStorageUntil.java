package com.wujia.jetpack.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 描述：
 *
 * @author WJ
 * @date 2017/12/12
 */

public class FileStorageUntil {

    private static final String TAG = FileStorageUntil.class.getSimpleName();

    public static File getFileByName(String fileName) {
        File parent;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            parent = AppGlobal.getApplication().getExternalCacheDir();
        } else {
            parent = AppGlobal.getApplication().getCacheDir();
        }
        Log.e(TAG, "getFileByName: " + parent);
        File file = new File(parent, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;

    }

}
