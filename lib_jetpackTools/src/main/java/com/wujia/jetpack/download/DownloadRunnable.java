package com.wujia.jetpack.download;

import com.wujia.jetpack.utils.FileStorageUntil;
import com.wujia.jetpack.utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Response;

/**
 * Download runnable.
 *
 * @author WuJia
 * @version 1.0
 * @date 2021/6/26
 */
class DownloadRunnable implements Runnable {

    private static final String TAG = DownloadRunnable.class.getSimpleName();

    private final DownloadTask.RunnableConfig mConfig;
    private final OkHttpDownload mHttpDownload;

    public DownloadRunnable(DownloadTask.RunnableConfig config, OkHttpDownload okHttpDownload) {
        mConfig = config;
        mHttpDownload = okHttpDownload;
    }

    @Override
    public void run() {
        Response response = mHttpDownload.requestByRange(mConfig.getDownloadUrl(),
                mConfig.getStartPosition(), mConfig.getEndPosition());
        if (response == null || response.body() == null) {
            LogUtil.logE(TAG, "request by range is null!");
            return;
        }
        LogUtil.logI(TAG, "download info::start position=" + mConfig.getStartPosition() + " end position=" + mConfig.getEndPosition());
        File file = FileStorageUntil.getFileByName(mConfig.getFieName());
        long finishProgress = mConfig.getProgressPosition() == null ? 0 : mConfig.getProgressPosition();
        long progress = 0;

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.seek(mConfig.getStartPosition());
            byte[] buffer = new byte[1024 * 500];
            int len;
            InputStream inputStream = response.body().byteStream();
            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                randomAccessFile.write(buffer, 0, len);
                progress += len;
                mConfig.setProgressPosition(progress);
//                LogUtil.logI(TAG, Thread.currentThread().getName() + " download progress ---->" + progress);
            }
            inputStream.close();
            mConfig.setProgressPosition(mConfig.getProgressPosition() + finishProgress);
            randomAccessFile.close();
            // TODO: callback
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 保存到缓存中
//            StorageAccess.saveData(String.valueOf(mConfig.mDownloadTaskId), mConfig);
        }
    }

}
