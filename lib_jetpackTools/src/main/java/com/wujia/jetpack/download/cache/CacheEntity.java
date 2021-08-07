package com.wujia.jetpack.download.cache;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;

@Entity(tableName = "table_download_cache")
public class CacheEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "key")
    public String mKey;

    @ColumnInfo(name = "data")
    public byte[] mData;

    public CacheEntity(@NonNull String key, byte[] data) {
        mKey = key;
        mData = data;
    }

    @Override
    public String toString() {
        return "CacheEntity{" +
                "mKey='" + mKey + '\'' +
                ", mData=" + Arrays.toString(mData) +
                '}';
    }
}
