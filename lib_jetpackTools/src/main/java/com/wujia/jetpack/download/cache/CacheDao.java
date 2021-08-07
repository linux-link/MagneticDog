package com.wujia.jetpack.download.cache;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Cache Dao.
 *
 * @author WuJia
 * @version 1.0
 * @date 2021/6/27
 */
@Dao
public interface CacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveData(CacheEntity entity);

    @Query("SELECT * FROM table_download_cache WHERE `key` = :key")
    CacheEntity getData(String key);

    @Query("SELECT * FROM table_download_cache")
    List<CacheEntity> getAllData();

    @Delete
    void removeData(CacheEntity entity);

    @Query("DELETE FROM table_download_cache WHERE `key` = :key")
    void removeData(String key);

    @Query("DELETE FROM table_download_cache")
    void clear();

}
