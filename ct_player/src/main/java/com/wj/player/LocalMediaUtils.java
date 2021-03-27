package com.wj.player;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import com.wj.arch.BaseApplication;
import com.wj.player.bean.MusicEntity;
import com.wj.player.bean.VideoEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class LocalMediaUtils {

    private static ContentResolver mContentResolver;

    /**
     * 获取本机音频列表.
     *
     * @return 音频列表
     */
    public static List<MusicEntity> getLocalMusics() {
        if (mContentResolver == null) {
            mContentResolver = BaseApplication.getApplication().getContentResolver();
        }
        ArrayList<MusicEntity> musics = new ArrayList<>();
        try (Cursor cursor = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER)) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));// 路径
                if (!isExists(path)) {
                    continue;
                }
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)); // 歌曲名
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)); // 专辑
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)); // 作者
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));// 大小
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));// 时长
                int time = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));// 歌曲的id
                // int albumId = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                musics.add(new MusicEntity(name, path, album, artist, size, duration));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return musics;
    }

    /**
     * 获取本机视频列表
     *
     * @return 视频列表
     */
    public List<VideoEntity> getVideos() {
        if (mContentResolver == null) {
            mContentResolver = BaseApplication.getApplication().getContentResolver();
        }
        List<VideoEntity> videos = new ArrayList<VideoEntity>();
        try (Cursor c = mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER)) {
            // String[] mediaColumns = { "_id", "_data", "_display_name",
            // "_size", "date_modified", "duration", "resolution" };
            while (c.moveToNext()) {
                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));// 路径
                if (!isExists(path)) {
                    continue;
                }
                int id = c.getInt(c.getColumnIndexOrThrow(MediaStore.Video.Media._ID));// 视频的id
                String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)); // 视频名称
                String resolution = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION)); //分辨率
                long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));// 大小
                long duration = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));// 时长
                long date = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));//修改时间

                videos.add(new VideoEntity(id, path, name, resolution, size, date, duration));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videos;
    }

    public static boolean isExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 获取视频缩略图.
     *
     * @param mediaId media id
     * @return 视频缩略图
     */
    public Bitmap getVideoThumbnail(int mediaId) {
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = MediaStore.Video.Thumbnails.getThumbnail(mContentResolver, mediaId, MediaStore.Images.Thumbnails.MICRO_KIND, options);
        return bitmap;
    }

}
