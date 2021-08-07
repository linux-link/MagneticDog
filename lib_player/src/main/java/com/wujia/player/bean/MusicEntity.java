package com.wujia.player.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MusicEntity implements Parcelable {

    /**
     * 歌曲名
     */
    private String name;
    /**
     * id
     */
    private String  mediaId;
    /**
     * 所属专辑
     */
    private String album;
    /**
     * 艺术家(作者)
     */
    private String artist;
    /**
     * 时长
     */
    private long duration;

    public MusicEntity(String name, String mediaId, String album, String artist, long duration) {
        this.name = name;
        this.mediaId = mediaId;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.mediaId);
        dest.writeString(this.album);
        dest.writeString(this.artist);
        dest.writeLong(this.duration);
    }

    protected MusicEntity(Parcel in) {
        this.name = in.readString();
        this.mediaId = in.readString();
        this.album = in.readString();
        this.artist = in.readString();
        this.duration = in.readLong();
    }

    public static final Creator<MusicEntity> CREATOR = new Creator<MusicEntity>() {
        @Override
        public MusicEntity createFromParcel(Parcel source) {
            return new MusicEntity(source);
        }

        @Override
        public MusicEntity[] newArray(int size) {
            return new MusicEntity[size];
        }
    };
}
