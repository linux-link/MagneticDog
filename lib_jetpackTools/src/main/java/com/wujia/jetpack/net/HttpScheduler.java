package com.wujia.jetpack.net;

import android.util.Log;

import androidx.annotation.NonNull;

import com.wujia.jetpack.net.annotation.CacheStrategy;
import com.wujia.jetpack.task.TaskExecutors;
import com.wujia.jetpack.task.TaskMainHandler;

import java.io.IOException;
import java.util.List;

public class HttpScheduler {

    private final Call.Factory mFactory;
    private final List<HttpInterceptor> mInterceptorList;

    public HttpScheduler(Call.Factory factory, List<HttpInterceptor> interceptorList) {
        mFactory = factory;
        mInterceptorList = interceptorList;
    }

    public Call<?> newCall(Request request) {
        Call<?> newCall = mFactory.newCall(request);
        return new ProxyCall<>(newCall, request);
    }

    private class ProxyCall<T> implements Call<T> {
        private final Call<T> mDelegate;
        private final Request mRequest;

        public ProxyCall(Call<T> delegate, Request request) {
            mDelegate = delegate;
            mRequest = request;
        }

        @Override
        public Response<T> execute() throws IOException {
            dispatchInterceptor(mRequest, null);
            if (mRequest.getCacheStrategy() == CacheStrategy.CACHE_FIRST) {
                Response<T> cacheResponse = readCache();
                if (cacheResponse.getData() != null) {
                    return cacheResponse;
                }
            }
            Response<T> response = mDelegate.execute();
            saveCacheIfNeed(response);
            dispatchInterceptor(mRequest, response);
            return response;
        }

        @Override
        public void enqueue(Callback<T> callback) {
            dispatchInterceptor(mRequest, null);
            if (mRequest.getCacheStrategy() == CacheStrategy.CACHE_FIRST) {
                TaskExecutors.getInstance().execute(() -> {
                    Response<T> cache = readCache();
                    if (cache.getData() != null) {
                        TaskMainHandler.sendAtFrontOfQueue(() -> {
                            callback.onSuccess(cache);
                        });
                        Log.d("Http-enqueue:", "cache :" + mRequest.getCacheKey());
                    }
                });
            }
            mDelegate.enqueue(new Callback<T>() {
                @Override
                public void onSuccess(Response<T> response) {
                    dispatchInterceptor(mRequest, response);
                    saveCacheIfNeed(response);
                    callback.onSuccess(response);
                    Log.d("Http-enqueue:", "remote :" + mRequest.getCacheKey());

                }

                @Override
                public void onFailed(Throwable throwable) {
                    callback.onFailed(throwable);
                }
            });
        }

        @NonNull
        private Response<T> readCache() {
            //historage 查询缓存 需要提供一个cache key
            //request的 url+参数
            String cacheKey = mRequest.getCacheKey();
            // TODO: 持久化存储
            Response<T> cacheResponse = new Response<>();
//            cacheResponse.setData();
            cacheResponse.setCode(Response.CACHE_SUCCESS);
            cacheResponse.setMsg("获取缓存成功");
            return cacheResponse;
        }

        private void saveCacheIfNeed(Response<T> response) {
            if (mRequest.getCacheStrategy() == CacheStrategy.CACHE_FIRST
            || mRequest.getCacheStrategy()==CacheStrategy.NET_FIRST){
                if (response.getData()!=null){
                    TaskExecutors.getInstance().execute(()->{
                        //TODO: 持久化存储数据
                    });
                }
            }
        }

        private void dispatchInterceptor(Request request, Response<T> response) {
            if (mInterceptorList == null || mInterceptorList.size() == 0) {
                return;
            }
            new InterceptorChain(request, response);
        }

        private class InterceptorChain implements HttpInterceptor.Chain {
            private final Request mRequest;
            private final Response<T> mResponse;
            private int mCallIndex;

            public InterceptorChain(Request request, Response<T> response) {
                mRequest = request;
                mResponse = response;
            }

            public void dispatch() {
                HttpInterceptor interceptor = mInterceptorList.get(mCallIndex);
                boolean intercept = interceptor.intercept(this);
                mCallIndex++;
                if (!intercept && mCallIndex < mInterceptorList.size()) {
                    dispatch();
                }
            }

            public boolean isRequestPeriod() {
                return mResponse == null;
            }

            @Override
            public Request request() {
                return mRequest;
            }

            @Override
            public Response<?> response() {
                return mResponse;
            }
        }
    }

}
