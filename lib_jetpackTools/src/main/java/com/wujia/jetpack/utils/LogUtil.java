/*
 * Copyright © 2020 SAIC MOTOR Z-ONE SOFTWARE COMPANY. All rights reserved.
 */

package com.wujia.jetpack.utils;

import android.util.Log;

public class LogUtil {
    private static final boolean DEBUG = true;
    private static final boolean VERBOSE = true;
    private static final boolean WARN = true;
    private static final boolean INFO = true;
    private static final boolean ERROR = true;

    /**
     * Print debug log info.
     *
     * @param tag  title
     * @param info description
     */
    public static void logD(String tag, String info) {
        if (DEBUG) {
            Log.d(tag, info);
        }
    }

    /**
     * Print verbose log info.
     *
     * @param tag  title
     * @param info description
     */
    public static void logV(String tag, String info) {
        if (VERBOSE) {
            Log.v(tag, info);
        }
    }

    /**
     * Print info log info.
     *
     * @param tag  title
     * @param info description
     */
    public static void logI(String tag, String info) {
        if (INFO) {
            Log.i(tag, info);
        }
    }

    /**
     * Print warn log info.
     *
     * @param tag  title
     * @param info description
     */
    public static void logW(String tag, String info) {
        if (WARN) {
            Log.w(tag, info);
        }
    }

    /**
     * Print error log info.
     *
     * @param tag  title
     * @param info description
     */
    public static void logE(String tag, String info) {
        if (ERROR) {
            Log.e(tag, info);
        }
    }
}
