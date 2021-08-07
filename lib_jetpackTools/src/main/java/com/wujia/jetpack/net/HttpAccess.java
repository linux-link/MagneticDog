package com.wujia.jetpack.net;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class HttpAccess {

    private final List<HttpInterceptor> mInterceptorList = new ArrayList<>();
    private final ConcurrentHashMap<Method, MethodParser> mMethodParserMap = new ConcurrentHashMap<>();
    private final String mBaseUrl;
    private HttpScheduler mScheduler;

    public HttpAccess(String baseUrl, Call.Factory factory) {
        mBaseUrl = baseUrl;
        mScheduler = new HttpScheduler(factory,mInterceptorList);
    }

    public <T> T create(Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        MethodParser methodParser = mMethodParserMap.get(method);
                        if (methodParser==null){
                            methodParser = MethodParser.parse(mBaseUrl,method);
                            mMethodParserMap.put(method,methodParser);
                        }
                        Request request = methodParser.newRequest(method,args);
                        return mScheduler.newCall(request);
                    }
                });
    }
}
