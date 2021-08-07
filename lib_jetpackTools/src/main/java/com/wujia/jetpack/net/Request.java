package com.wujia.jetpack.net;

import android.text.TextUtils;

import androidx.annotation.IntDef;

import com.wujia.jetpack.net.annotation.CacheStrategy;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private String mCacheStrategyKey;
    private int mCacheStrategy = CacheStrategy.NET_ONLY;

    @Method
    private int mHttpMethod;
    private Map<String, String> mHeaders;
    private Map<String, String> mParameters;
    private String mDomainUrl;
    private String mRelativeUrl;
    private Type mReturnType;
    private boolean mFormPost;

    @IntDef(value = {Method.GET,Method.POST})
    public @interface Method {
        int GET = 0;
        int POST = 1;
        int PUT = 2;
        int DELETE = 3;
    }

    public String getEndPointUrl() {
        if (mRelativeUrl == null) {
            throw new IllegalStateException("relative url must bot be null ");
        }
        if (!mRelativeUrl.startsWith("/")) {
            return mDomainUrl + mRelativeUrl;
        }

        int indexOf = mDomainUrl.indexOf("/");
        return mDomainUrl.substring(0, indexOf) + mRelativeUrl;
    }

    public void addHeaders(String name, String value) {
        if (mHeaders == null) {
            mHeaders = new HashMap<>();
        }
        mHeaders.put(name, value);
    }

    public String getCacheKey() {
        if (!TextUtils.isEmpty(mCacheStrategyKey)) {
            return mCacheStrategyKey;
        }
        StringBuilder builder = new StringBuilder();
        String endUrl = getEndPointUrl();
        builder.append(endUrl);
        if (endUrl.indexOf("?") > 0 || endUrl.indexOf("&") > 0) {
            builder.append("&");
        } else {
            builder.append("?");
        }

        if (mParameters != null) {
            for (Map.Entry<String, String> entity : mParameters.entrySet()) {
                try {
                    String encodeValue = URLEncoder.encode(entity.getValue(), "UTF-8");
                    builder.append(entity.getKey()).append("=").append(encodeValue).append("&");
                } catch (Exception exception) {
                    //ignore
                }
            }
            builder.deleteCharAt(builder.length() - 1);
            mCacheStrategyKey = builder.toString();
        } else {
            mCacheStrategyKey = endUrl;
        }

        return mCacheStrategyKey;
    }

    public String getCacheStrategyKey() {
        return mCacheStrategyKey;
    }

    public void setCacheStrategyKey(String cacheStrategyKey) {
        mCacheStrategyKey = cacheStrategyKey;
    }

    public int getCacheStrategy() {
        return mCacheStrategy;
    }

    public void setCacheStrategy(int cacheStrategy) {
        mCacheStrategy = cacheStrategy;
    }

    public int getHttpMethod() {
        return mHttpMethod;
    }

    public void setHttpMethod(int httpMethod) {
        mHttpMethod = httpMethod;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public void setHeaders(Map<String, String> headers) {
        mHeaders = headers;
    }

    public Map<String, String> getParameters() {
        return mParameters;
    }

    public void setParameters(Map<String, String> parameters) {
        mParameters = parameters;
    }

    public String getDomainUrl() {
        return mDomainUrl;
    }

    public void setDomainUrl(String domainUrl) {
        mDomainUrl = domainUrl;
    }

    public String getRelativeUrl() {
        return mRelativeUrl;
    }

    public void setRelativeUrl(String relativeUrl) {
        mRelativeUrl = relativeUrl;
    }

    public Type getReturnType() {
        return mReturnType;
    }

    public void setReturnType(Type returnType) {
        mReturnType = returnType;
    }

    public boolean isFormPost() {
        return mFormPost;
    }

    public void setFormPost(boolean formPost) {
        mFormPost = formPost;
    }
}
