package com.wujia.jetpack.download;

import android.os.Looper;

import androidx.annotation.WorkerThread;

import com.wujia.jetpack.task.TaskExecutors;
import com.wujia.jetpack.utils.LogUtil;

/**
 * Download access.
 *
 * @author WuJia
 * @version 1.0
 * @date 2021/6/26
 */
public class DownloadAccess {

    private static final String TAG = DownloadAccess.class.getSimpleName();

    private volatile static DownloadAccess sManager;
    private DownloadSdk mDownloadSdk;

    private DownloadAccess() {

    }

    public static DownloadAccess get() {
        if (sManager == null) {
            synchronized (DownloadAccess.class) {
                if (sManager == null) {
                    sManager = new DownloadAccess();
                }
            }
        }
        return sManager;
    }

    @WorkerThread
    private DownloadSdk getSdk() {
        if (Thread.currentThread().getName().equals(Looper.getMainLooper().getThread().getName())) {
            throw new IllegalThreadStateException("this method must call in work thread!!");
        }
        if (mDownloadSdk == null) {
            mDownloadSdk = new DownloadSdk();
        }
        return mDownloadSdk;
    }

    public void enqueue(DownloadRequest request) {
        LogUtil.logI(TAG, "enqueue:" + request);
        TaskExecutors.getInstance().execute(() -> {
            get().getSdk().enqueue(request);
        });
    }

    public void pause(long id) {
        LogUtil.logI(TAG, "pause:" + id);
        TaskExecutors.getInstance().execute(() -> {
            get().getSdk().pause(id);
        });
    }

    public void pauseAll() {
        TaskExecutors.getInstance().execute(get().getSdk()::pauseAll);
    }

    public void resume(long id) {
        TaskExecutors.getInstance().execute(() -> {
            get().getSdk().resume(id);
        });
    }

    public void resumeAll() {
        TaskExecutors.getInstance().execute(get().getSdk()::resumeAll);
    }

    public void remove(long id) {
        TaskExecutors.getInstance().execute(() -> {
            get().getSdk().remove(id);
        });
    }

    public void removeAll() {
        TaskExecutors.getInstance().execute(get().getSdk()::resumeAll);
    }
}
