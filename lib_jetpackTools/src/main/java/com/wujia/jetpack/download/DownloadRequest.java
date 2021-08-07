package com.wujia.jetpack.download;

import android.os.Parcel;
import android.os.Parcelable;

import com.wujia.jetpack.utils.Md5Utill;

import java.io.Serializable;
import java.util.Objects;

/**
 * Download request.
 *
 * @author WuJia
 * @version 1.0
 * @date 2021/6/26
 */
public class DownloadRequest implements Parcelable {

    private final String mUrl;
    private String mDownloadFileName;
    private String mDownloadFilePath;
    private int mThreadSize = 2;

    public DownloadRequest(String url) {
        mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }

    public int getThreadSize() {
        return mThreadSize;
    }

    public String getDownloadFileName() {
        if (mDownloadFileName == null) {
            return Md5Utill.generateCode(mUrl);
        }
        return mDownloadFileName;
    }

    public String getDownloadFilePath() {
        return mDownloadFilePath;
    }

    public int getId() {
        return hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DownloadRequest request = (DownloadRequest) o;
        return Objects.equals(mUrl, request.mUrl) &&
                Objects.equals(mDownloadFileName, request.mDownloadFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mUrl, mDownloadFileName);
    }

    public static class Builder {

        private final DownloadRequest mRequest;

        public Builder(String url) {
            mRequest = new DownloadRequest(url);
        }

        public Builder setDownloadFileName(String name) {
            mRequest.mDownloadFileName = name;
            return this;
        }

        public Builder setDownloadFilePath(String path) {
            mRequest.mDownloadFilePath = path;
            return this;
        }

        public Builder setThreadSize(int size) {
            mRequest.mThreadSize = size;
            return this;
        }

        public DownloadRequest build() {
            return mRequest;
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mUrl);
        dest.writeString(this.mDownloadFileName);
        dest.writeString(this.mDownloadFilePath);
        dest.writeInt(this.mThreadSize);
    }

    protected DownloadRequest(Parcel in) {
        this.mUrl = in.readString();
        this.mDownloadFileName = in.readString();
        this.mDownloadFilePath = in.readString();
        this.mThreadSize = in.readInt();
    }

    public static final Creator<DownloadRequest> CREATOR = new Creator<DownloadRequest>() {
        @Override
        public DownloadRequest createFromParcel(Parcel source) {
            return new DownloadRequest(source);
        }

        @Override
        public DownloadRequest[] newArray(int size) {
            return new DownloadRequest[size];
        }
    };
}
