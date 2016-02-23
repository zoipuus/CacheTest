package com.zoipuus.cachetest.cache;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/2/22.
 * ImageFile operation
 */
public class BitmapFileCache implements BitmapCache {
    @Override
    public Bitmap getBitmap(String url) {
        return BitmapFileCacheUtils.getInstance().getBitmap(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        BitmapFileCacheUtils.getInstance().saveBitmap(bitmap, url);
    }
}
