package com.wujia.jetpack.task;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class TaskMainHandler {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    public static void post(Runnable runnable) {
        HANDLER.post(runnable);
    }

    public static void postDelay(Runnable runnable, long mill) {
        HANDLER.postDelayed(runnable, mill);
    }

    public static void sendAtFrontOfQueue(Runnable runnable) {
        Message message = Message.obtain(HANDLER, runnable);
        HANDLER.sendMessageAtFrontOfQueue(message);
    }

    public static void remove(Runnable runnable) {
        HANDLER.removeCallbacks(runnable);
    }
}
