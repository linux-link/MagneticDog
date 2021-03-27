package com.wj.player.bean;

public class VideoEntity {

    private int id;
    private String path;
    private String name;
    private String resolution;// 分辨率
    private long size;
    private long date;
    private long duration;

    public VideoEntity(int id, String path, String name, String resolution, long size, long date, long duration) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.resolution = resolution;
        this.size = size;
        this.date = date;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
