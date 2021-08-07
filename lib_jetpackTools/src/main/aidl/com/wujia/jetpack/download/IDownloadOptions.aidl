package com.wujia.jetpack.download;

import com.wujia.jetpack.download.DownloadRequest;

interface IDownloadOptions {

    long enqueue(in DownloadRequest request);

    void pause(long id);

    void pauseAll();

    void resume(long id);

    void resumeAll();

    void remove(long id);

    void removeAll();
}