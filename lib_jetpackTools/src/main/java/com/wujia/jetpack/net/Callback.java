package com.wujia.jetpack.net;

public interface Callback<Data> {

    void onSuccess(Response<Data> response);

    void onFailed(Throwable throwable);

}
