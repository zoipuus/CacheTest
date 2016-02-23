package com.zoipuus.cachetest;

import android.util.Log;

/**
 * Created by Administrator on 2016/2/22.
 */
public class LogUtils {
    private static final String TAG = "CacheTest";

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }
}
