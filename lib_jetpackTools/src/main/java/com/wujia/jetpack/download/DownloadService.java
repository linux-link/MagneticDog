package com.wujia.jetpack.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.wujia.jetpack.task.TaskExecutors;
import com.wujia.jetpack.utils.LogUtil;

import okhttp3.internal.concurrent.Task;

/**
 * Download service.
 *
 * @author WuJia
 * @version 1.0
 * @date 2021/6/26
 */
public class DownloadService extends Service {

    private static final String TAG = DownloadService.class.getSimpleName();

    private OkHttpDownload mHttpDownload;

    @Override
    public void onCreate() {
        super.onCreate();
        mHttpDownload = new OkHttpDownload();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return null;
        }
        if (DownloadSdk.DOWNLOAD_ACTION.equals(action)) {
            return mDownloadOptions;
        }
        return null;
    }

    private final IDownloadOptions.Stub mDownloadOptions = new IDownloadOptions.Stub() {

        @Override
        public long enqueue(DownloadRequest request) {
            LogUtil.logI(TAG, "enqueue::" + request.getDownloadFileName());
            return mHttpDownload.download(request);
        }

        @Override
        public void pause(long id) {
            mHttpDownload.pause(id);
        }

        @Override
        public void pauseAll() {
            mHttpDownload.pauseAll();
        }

        @Override
        public void resume(long id) {
            TaskExecutors.getInstance().execute(() -> {
                mHttpDownload.resume(id);
            });
        }

        @Override
        public void resumeAll() {

        }

        @Override
        public void remove(long id) {
            mHttpDownload.remove(id);
        }

        @Override
        public void removeAll() {

        }
    };

}
