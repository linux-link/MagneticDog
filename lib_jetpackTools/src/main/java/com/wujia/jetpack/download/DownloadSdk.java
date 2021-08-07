package com.wujia.jetpack.download;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.wujia.jetpack.utils.AppGlobal;
import com.wujia.jetpack.utils.LogUtil;
import com.wujia.jetpack.utils.Remote;

import java.util.concurrent.CountDownLatch;

/**
 * Download SDK.
 *
 * @author WuJia
 * @version 1.0
 * @date 2021/6/26
 */
class DownloadSdk {

    public static final String TAG = DownloadSdk.class.getSimpleName();
    public static final String DOWNLOAD_ACTION = "com.wujia.action.download";

    private CountDownLatch mConnectLock;
    private IDownloadOptions mOptions;

    private final ServiceConnection mBinderPoolConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder service) {
            mOptions = IDownloadOptions.Stub.asInterface(service);
            Remote.tryExec(() -> {
                mOptions.asBinder().linkToDeath(mIBinderPoolDeathRecipient, 0);
            });
            if (mConnectLock != null) {
                mConnectLock.countDown();
            }
            LogUtil.logW(TAG, "onServiceConnected");
        }


        public void onServiceDisconnected(ComponentName name) {
            LogUtil.logW(TAG, "onServiceDisconnected");
        }
    };

    private final IBinder.DeathRecipient mIBinderPoolDeathRecipient = new IBinder.DeathRecipient() {

        public void binderDied() {
            mOptions.asBinder().unlinkToDeath(mIBinderPoolDeathRecipient, 0);
            mOptions = null;
            connectService();
        }
    };

    public DownloadSdk() {
        connectService();
    }

    private void connectService() {
        if (isBinderAlive()) {
            return;
        }
        Intent intent = new Intent(AppGlobal.getApplication(), DownloadService.class);
        intent.setAction(DownloadSdk.DOWNLOAD_ACTION);
        mConnectLock = new CountDownLatch(1);
        boolean msg = AppGlobal.getApplication().bindService(intent, mBinderPoolConnection,
                Context.BIND_AUTO_CREATE);
        LogUtil.logI(TAG, "connectService result::" + msg);
        try {
            mConnectLock.await();
        } catch (InterruptedException ex) {
            LogUtil.logE(TAG, ex.toString());
        }
    }

    /**
     * Remote service is alive.
     *
     * @return result
     */
    public boolean isBinderAlive() {
        if (mOptions != null) {
            return mOptions.asBinder().isBinderAlive();
        } else {
            return false;
        }
    }

    public long enqueue(DownloadRequest request) {
        connectService();
        return Remote.exec(() -> mOptions.enqueue(request));
    }

    public void pause(long id) {
        connectService();
        Remote.exec(() -> mOptions.pause(id));
    }

    public void pauseAll() {
        connectService();
        Remote.exec(() -> mOptions.pauseAll());
    }

    public void resume(long id) {
        connectService();
        Remote.exec(() -> mOptions.resume(id));
    }

    public void resumeAll() {
        connectService();
        Remote.exec(() -> mOptions.resumeAll());
    }

    public void remove(long id) {
        connectService();
        Remote.exec(() -> mOptions.remove(id));
    }

    public void removeAll() {
        connectService();
        Remote.exec(() -> mOptions.removeAll());
    }
}
