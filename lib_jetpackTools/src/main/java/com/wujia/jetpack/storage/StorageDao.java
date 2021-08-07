package com.wujia.jetpack.storage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wujia.jetpack.download.cache.CacheEntity;

import java.util.List;

/**
 * Cache Dao.
 *
 * @author WuJia
 * @version 1.0
 * @date 2021/6/27
 */
@Dao
public interface StorageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveData(StorageEntity entity);

    @Query("SELECT * FROM table_storage WHERE `key` = :key")
    StorageEntity getData(String key);

    @Query("SELECT * FROM table_storage")
    List<StorageEntity> getAllData();

    @Delete
    void removeData(StorageEntity entity);

    @Query("DELETE FROM table_storage WHERE `key` = :key")
    void removeData(String key);

    @Query("DELETE FROM table_storage")
    void clear();

}
