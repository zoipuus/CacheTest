package com.zoipuus.cachetest.cache;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/2/22.
 * BitmapCache interface
 */
public interface BitmapCache {
    Bitmap getBitmap(String url);

    void putBitmap(String url, Bitmap bitmap);
}
