package com.wujia.jetpack.download;

import android.os.Process;
import android.util.Log;

import androidx.annotation.WorkerThread;

import com.wujia.jetpack.task.TaskExecutors;
import com.wujia.jetpack.utils.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Download.
 *
 * @author WuJia
 * @version 1.0
 * @date 2021/6/27
 */
class OkHttpDownload {

    public static final String TAG = OkHttpDownload.class.getSimpleName();

    private final List<DownloadTask> mDownloadTaskList = new ArrayList<>();

    private final OkHttpClient mHttpClient;

    public OkHttpDownload() {
        mHttpClient = new OkHttpClient()
                .newBuilder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build();
        // TODO:
        TaskExecutors.getInstance().execute(() -> {
            mDownloadTaskList.addAll(CacheAccess.getAllData());
        });
    }

    /**
     * 同步请求
     *
     * @param url
     * @return
     */
    public Response requestByRange(String url, long start, long end) {
        Request request = new Request.Builder().url(url)
                .addHeader("Range", "bytes=" + start + "-" + end)
                .build();
        try {
            return mHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Pause download task.
     *
     * @param id task id
     */
    public void pause(long id) {
        DownloadTask task = null;
        for (DownloadTask downloadTask : mDownloadTaskList) {
            LogUtil.logI(TAG, "download task id " + downloadTask.hashCode());
            if (downloadTask.hashCode() == id) {
                task = downloadTask;
                break;
            }
        }
        if (task == null) {
            LogUtil.logE(TAG, "no find need pause task,id=" + id);
            return;
        }
        // 先保存数据
        CacheAccess.saveData(String.valueOf(task.hashCode()), task);
        // 停止下载任务的线程池
        for (DownloadTask.RunnableConfig config : task.getRunnableConfigList()) {
            Thread thread = TaskExecutors.getInstance().getThreadList().get(config.getRunnableId());
            if (thread != null) {
                thread.interrupt();
                LogUtil.logW(TAG, "runnable " + thread.getName() + " interrupt!");
            } else {
                LogUtil.logW(TAG, "runnable not find");
            }
        }
    }

    public void pauseAll() {
        mDownloadTaskList.clear();
        mDownloadTaskList.addAll(CacheAccess.getAllData());
        for (DownloadTask task : mDownloadTaskList) {
            pause(task.getId());
        }
    }

    public void resume(long id) {
        DownloadTask task = CacheAccess.getData(String.valueOf(id));
        if (task == null) {
            LogUtil.logW(TAG, "resume task id null! id= " + id);
            return;
        }
        Log.e(TAG, "resume: " + task.getRunnableConfigList().size());
        for (DownloadTask.RunnableConfig config : task.getRunnableConfigList()) {
            TaskExecutors.getInstance().execute(
                    Process.THREAD_PRIORITY_BACKGROUND,
                    new DownloadRunnable(config, this),
                    config.getRunnableId()
            );
        }
        mDownloadTaskList.add(task);

    }

    public void remove(long id) {
        mDownloadTaskList.clear();
    }

    @WorkerThread
    public long download(final DownloadRequest downloadRequest) {
        Request request = new Request.Builder().url(downloadRequest.getUrl()).build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    LogUtil.logE(TAG, "network error!!");
                }
                long contentLength = -1L;
                if (response.body() != null) {
                    contentLength = response.body().contentLength();
                }

                if (contentLength <= -1) {
                    LogUtil.logE(TAG, "network error! content length = " + contentLength);
                }
                handleDownloadTask(downloadRequest, contentLength);
            }
        });
        return downloadRequest.getId();
    }

    private void handleDownloadTask(DownloadRequest request, long contentLength) {
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.setUrl(request.getUrl());
        downloadTask.setDownloadFileName(request.getDownloadFileName());
        downloadTask.setDownloadFilePath(request.getDownloadFilePath());
        List<DownloadTask.RunnableConfig> configList = new ArrayList<>();
        // 该任务需要启动的线程数
        int maxSize = request.getThreadSize();
        long threadSize = contentLength / request.getThreadSize();
        for (int i = 0; i < maxSize; i++) {
            DownloadTask.RunnableConfig config = new DownloadTask.RunnableConfig();
            config.setDownloadUrl(request.getUrl());
            long startSize = i * threadSize;
            config.setStartPosition(startSize);
            long endSize = 0L;
            if (i == maxSize - 1) {
                endSize = contentLength - 1;
            } else {
                endSize = (i + 1) * threadSize - 1;
            }
            config.setEndPosition(endSize);
            config.setRunnableId(request.getDownloadFileName() + "Runnable #" + i);
            config.setFieName(request.getDownloadFileName());
            configList.add(config);
            // 启动一个线程，下载
            TaskExecutors.getInstance().execute(
                    Process.THREAD_PRIORITY_BACKGROUND,
                    new DownloadRunnable(config, this),
                    config.getRunnableId()
            );
        }
        downloadTask.setRunnableConfigList(configList);
        // 将初识的任务信息保存到缓存中
        CacheAccess.saveData(String.valueOf(downloadTask.hashCode()), downloadTask);
        // 添加到task集合中
        mDownloadTaskList.add(downloadTask);
    }

}
