package com.wujia.jetpack.download;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.wujia.jetpack.download.cache.CacheDatabase;
import com.wujia.jetpack.download.cache.CacheEntity;
import com.wujia.jetpack.task.TaskExecutors;
import com.wujia.jetpack.utils.ByteUtil;
import com.wujia.jetpack.utils.LogUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Cache storage power by room.
 *
 * @author WuJia
 * @version 1.0
 * @date 2021/6/26
 */
class CacheAccess {

    private static final String TAG = CacheAccess.class.getSimpleName();

    @WorkerThread
    public static <T extends Serializable> void saveData(@NonNull String key, @NonNull T data) {
        LogUtil.logI(TAG, "saveData:" + key + " data:" + data.hashCode());
        CacheEntity cacheEntity = new CacheEntity(
                key, ByteUtil.toByteArray(data)
        );
        long result = CacheDatabase.get().getCacheDao().saveData(cacheEntity);
        LogUtil.logI(TAG, "saveData result:" + result);
    }

    @WorkerThread
    public static <T extends Serializable> T getData(@NonNull String key) {
        LogUtil.logI(TAG, "getData:" + key);
        CacheEntity entity = CacheDatabase.get().getCacheDao().getData(key);
        LogUtil.logI(TAG, "getData result:" + entity.toString());
        if (entity == null) {
            return null;
        } else {
            return (T) ByteUtil.toObject(entity.mData);
        }
    }

    @WorkerThread
    public static List<DownloadTask> getAllData() {
        LogUtil.logI(TAG, "getAllData:");
        List<CacheEntity> list = CacheDatabase.get().getCacheDao().getAllData();
        List<DownloadTask> tmp = new ArrayList<>();
        if (list == null || list.size() == 0) {
            return tmp;
        }
        for (CacheEntity entity : list) {
            tmp.add((DownloadTask) ByteUtil.toObject(entity.mData));
        }
        return tmp;
    }

    public static void removeData(@NonNull CacheEntity entity) {
        LogUtil.logI(TAG, "removeData:" + entity.mKey);
        TaskExecutors.getInstance().execute(() -> {
            CacheDatabase.get().getCacheDao().removeData(entity);
        });
    }

    public static void removeData(@NonNull String key) {
        LogUtil.logI(TAG, "removeData:" + key);
        TaskExecutors.getInstance().execute(() -> {
            CacheDatabase.get().getCacheDao().removeData(key);
        });
    }

    public static void clear() {
        LogUtil.logI(TAG, "!!!!!clear all storage data!!!!!!");
        TaskExecutors.getInstance().execute(() -> {
            CacheDatabase.get().getCacheDao().clear();
        });
    }
}
