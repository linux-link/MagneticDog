package com.wujia.jetpack.storage;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.wujia.jetpack.utils.AppGlobal;

@Database(entities = {StorageEntity.class}, version = 1)
public abstract class StorageDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "STORAGE_DATABASE";

    private static StorageDatabase sStorageDatabase;

    public static StorageDatabase get() {
        if (sStorageDatabase == null) {
            synchronized (StorageDatabase.class) {
                if (sStorageDatabase == null) {
                    sStorageDatabase = Room.databaseBuilder(
                            AppGlobal.getApplication(), StorageDatabase.class, DATABASE_NAME
                    ).build();
                }
            }
        }
        return sStorageDatabase;
    }

    public abstract StorageDao getStorageDao();

}
