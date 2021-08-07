package com.wujia.player;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.session.MediaSession;
import android.provider.MediaStore;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import com.wj.lib.multimedia.AppGlobal;
import com.wj.lib.multimedia.bean.MusicEntity;
import com.wj.lib.multimedia.bean.VideoEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class LocalMediaUtils {

    private static ContentResolver mContentResolver;

    /**
     * 获取本机音频列表.
     *
     * @return 音频列表
     */
    public static ArrayList<MusicEntity> getLocalMusics() {
        if (mContentResolver == null) {
            mContentResolver = AppGlobal.getApplication().getContentResolver();
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
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));// 时长
                String mediaId = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));// 歌曲的id
                // int albumId = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                musics.add(new MusicEntity(name, mediaId, album, artist, duration));
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
            mContentResolver = AppGlobal.getApplication().getContentResolver();
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

    public static MusicEntity Metadata2Music(MediaMetadataCompat metadataCompat){
        return new MusicEntity(
                metadataCompat.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE),
                metadataCompat.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID),
                metadataCompat.getString(MediaMetadataCompat.METADATA_KEY_ALBUM),
                metadataCompat.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST),
                metadataCompat.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
        );
    }

    public static List<MusicEntity> QueueList2Music(List<MediaSessionCompat.QueueItem> itemList){
        List<MusicEntity> list = new ArrayList<>();
        for (MediaSessionCompat.QueueItem queueItem : itemList) {
            MusicEntity info=new MusicEntity(
                    Objects.requireNonNull(queueItem.getDescription().getTitle()).toString(),
                    queueItem.getDescription().getMediaId(),
                    null,
                    Objects.requireNonNull(queueItem.getDescription().getSubtitle()).toString(),
                    0

            );
            list.add(info);
        }
        return list;
    }

}
