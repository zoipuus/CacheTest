package com.zoipuus.cachetest.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/2/22.
 * download image
 */
public class BitmapDownloaderTask extends AsyncTask<Object, Void, Bitmap> {
    String url;
    ImageView imageView;

    @Override
    protected Bitmap doInBackground(Object... params) {
        url = (String) params[0];
        imageView = (ImageView) params[1];
        return loadImageFromInternet(url);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
//        adapter.notifyDataSetChanged();// 触发getView方法执行，这个时候getView实际上会拿到刚刚缓存好的图片
        BitmapFileCacheUtils.getInstance().saveBitmap(bitmap, url);
        imageView.setImageBitmap(bitmap);
    }

    public Bitmap loadImageFromInternet(String url) {
        Bitmap bitmap = null;
        HttpClient client = AndroidHttpClient.newInstance("Android");
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 3000);
        HttpConnectionParams.setSocketBufferSize(params, 3000);
        HttpResponse response;
        InputStream inputStream = null;
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(url);
            response = client.execute(httpGet);
            int stateCode = response.getStatusLine().getStatusCode();
            if (stateCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try {
                    inputStream = entity.getContent();
                    return bitmap = BitmapFactory.decodeStream(inputStream);
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (IOException e) {
            httpGet.abort();
            e.printStackTrace();
        } finally {
            ((AndroidHttpClient) client).close();
        }
        return bitmap;
    }
}
