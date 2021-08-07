package com.wujia.jetpack.net;

import java.io.IOException;

public interface Call<Data> {

    Response<Data> execute() throws IOException;

    void enqueue(Callback<Data> callback);

    interface Factory {
        Call<?> newCall(Request request);
    }

}
