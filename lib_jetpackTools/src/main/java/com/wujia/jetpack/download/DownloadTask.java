package com.wujia.jetpack.download;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 对应一个下载任务.
 *
 * @author WuJia
 * @version 1.0
 * @date 2021/6/27
 */
class DownloadTask implements Serializable {

    private String mUrl;
    private String mDownloadFileName;
    private String mDownloadFilePath;
    // 下载进度
    private float mProgress;
    // 根据每个任务的线程数划分
    private List<RunnableConfig> mRunnableConfigList;

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getDownloadFileName() {
        return mDownloadFileName;
    }

    public void setDownloadFileName(String downloadFileName) {
        mDownloadFileName = downloadFileName;
    }

    public String getDownloadFilePath() {
        return mDownloadFilePath;
    }

    public void setDownloadFilePath(String downloadFilePath) {
        mDownloadFilePath = downloadFilePath;
    }

    public List<RunnableConfig> getRunnableConfigList() {
        return mRunnableConfigList;
    }

    public void setRunnableConfigList(List<RunnableConfig> runnableConfigList) {
        mRunnableConfigList = runnableConfigList;
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        mProgress = progress;
    }

    public long getId(){
        return hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DownloadTask that = (DownloadTask) o;
        return Objects.equals(mUrl, that.mUrl) &&
                Objects.equals(mDownloadFileName, that.mDownloadFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mUrl, mDownloadFileName);
    }

    public static class RunnableConfig implements Serializable {
        private String mFieName;
        private Long mStartPosition;
        private Long mEndPosition;
        private Long mProgressPosition;
        private String mDownloadUrl;
        private String mRunnableId;

        public Long getStartPosition() {
            return mStartPosition;
        }

        public void setStartPosition(Long startPosition) {
            this.mStartPosition = startPosition;
        }

        public Long getEndPosition() {
            return mEndPosition;
        }

        public void setEndPosition(Long endPosition) {
            this.mEndPosition = endPosition;
        }

        public Long getProgressPosition() {
            return mProgressPosition;
        }

        public void setProgressPosition(Long progressPosition) {
            this.mProgressPosition = progressPosition;
        }

        public String getDownloadUrl() {
            return mDownloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.mDownloadUrl = downloadUrl;
        }

        public String getRunnableId() {
            return mRunnableId;
        }

        public void setRunnableId(String runnableId) {
            this.mRunnableId = runnableId;
        }

        public String getFieName() {
            return mFieName;
        }

        public void setFieName(String fieName) {
            mFieName = fieName;
        }
    }

}
