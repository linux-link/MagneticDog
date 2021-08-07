package com.wujia.jetpack.storage;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;

@Entity(tableName = "table_storage")
public class StorageEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "key")
    public String mKey;

    @ColumnInfo(name = "data")
    public byte[] mData;

    public StorageEntity(@NonNull String key, byte[] data) {
        mKey = key;
        mData = data;
    }

    @Override
    public String toString() {
        return "StorageEntity{" +
                "mKey='" + mKey + '\'' +
                ", mData=" + Arrays.toString(mData) +
                '}';
    }
}
