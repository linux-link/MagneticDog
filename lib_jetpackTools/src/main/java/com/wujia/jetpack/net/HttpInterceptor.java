package com.wujia.jetpack.net;

public interface HttpInterceptor {

    boolean intercept(Chain chain);

    interface Chain{

        boolean mIsRequestPeriod = false;

        Request request();

        Response<?> response();

    }
}
