package com.wujia.jetpack.storage;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.wujia.jetpack.task.TaskExecutors;
import com.wujia.jetpack.utils.ByteUtil;
import com.wujia.jetpack.utils.LogUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Cache storage power by room.
 *
 * @author WuJia
 * @version 1.0
 * @date 2021/6/26
 */
public class StorageAccess {

    private static final String TAG = StorageAccess.class.getSimpleName();

    public static <T extends Serializable> void saveData(@NonNull String key, @NonNull T data) {
        LogUtil.logI(TAG, "saveData:" + key + " data:" + data.hashCode());
        TaskExecutors.getInstance().execute(() -> {
            StorageEntity storageEntity = new StorageEntity(
                    key, ByteUtil.toByteArray(data)
            );
            long result = StorageDatabase.get().getStorageDao().saveData(storageEntity);
            LogUtil.logI(TAG, "saveData result:" + result);
        });
    }

    @WorkerThread
    public static <T extends Serializable> T getData(@NonNull String key) {
        LogUtil.logI(TAG, "getData:" + key);
        StorageEntity entity = StorageDatabase.get().getStorageDao().getData(key);
        if (entity == null) {
            return null;
        } else {
            return (T) ByteUtil.toObject(entity.mData);
        }
    }

    @WorkerThread
    public static List<StorageEntity> getAllData() {
        LogUtil.logI(TAG, "getAllData:");
        return StorageDatabase.get().getStorageDao().getAllData();
    }

    public static void removeData(@NonNull StorageEntity entity) {
        LogUtil.logI(TAG, "removeData:" + entity.mKey);
        TaskExecutors.getInstance().execute(() -> {
            StorageDatabase.get().getStorageDao().removeData(entity);
        });
    }

    public static void removeData(@NonNull String key) {
        LogUtil.logI(TAG, "removeData:" + key);
        TaskExecutors.getInstance().execute(() -> {
            StorageDatabase.get().getStorageDao().removeData(key);
        });
    }

    public static void clear() {
        LogUtil.logI(TAG, "!!!!!clear all storage data!!!!!!");
        TaskExecutors.getInstance().execute(() -> {
            StorageDatabase.get().getStorageDao().clear();
        });
    }
}
