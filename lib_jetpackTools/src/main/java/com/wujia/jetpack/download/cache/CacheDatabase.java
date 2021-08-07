package com.wujia.jetpack.download.cache;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.wujia.jetpack.utils.AppGlobal;

@Database(entities = {CacheEntity.class}, version = 1)
public abstract class CacheDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "CACHE_DATABASE";

    private static CacheDatabase sCacheDatabase;

    public static CacheDatabase get() {
        if (sCacheDatabase == null) {
            synchronized (CacheDatabase.class) {
                if (sCacheDatabase == null) {
                    sCacheDatabase = Room.databaseBuilder(
                            AppGlobal.getApplication(), CacheDatabase.class, DATABASE_NAME
                    ).build();
                }
            }
        }
        return sCacheDatabase;
    }

    public abstract CacheDao getCacheDao();

}
