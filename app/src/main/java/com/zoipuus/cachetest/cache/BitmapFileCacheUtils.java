package com.zoipuus.cachetest.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.widget.ImageView;

import com.zoipuus.cachetest.LogUtils;
import com.zoipuus.cachetest.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Administrator on 2016/2/22.
 * BitmapFileCacheUtils
 */
public class BitmapFileCacheUtils {
    private static final String CACHE_DIR = "BitmapCache";//缓存目录
    private static final String WHOLESALE_CONV = ".cache";//缓存文件后缀名
    private static final int MB = 1024 * 1024;
    private static final int CACHE_SIZE = 5;//缓存最大容量（超过就会利用lru算法删除最近最少使用的缓存文件）
    private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;//缓存所需SD卡所剩的最小容量

    private static BitmapFileCacheUtils instance = null;

    /**
     * singleton
     *
     * @return Bitmap Object
     */
    public static BitmapFileCacheUtils getInstance() {
        if (instance == null) {
            synchronized (BitmapFileCacheUtils.class) {
                if (instance == null) {
                    instance = new BitmapFileCacheUtils();
                }
            }
        }
        return instance;
    }

    public BitmapFileCacheUtils() {
        removeCache(getDirectory());
    }

    /**
     * get picture from cache
     *
     * @param url file link
     * @return bitmap object
     */
    public Bitmap getBitmap(final String url) {
        final String path = getDirectory() + "/" + convertUrlToFileName(url);
        File file = new File(path);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap == null) {
                file.delete();
                return null;
            } else {
                updateFileTime(path);//更新文件最新访问时间
                return bitmap;
            }
        } else {
            return null;
        }
    }

    /**
     * get the cache dir
     *
     * @return dir
     */
    private String getDirectory() {
        return getSDPath() + "/" + CACHE_DIR;
    }


    /**
     * get the sd path
     *
     * @return path
     */
    private String getSDPath() {
        File sdDir;
        boolean adCardExit = Environment.getExternalStorageState()
                .endsWith(Environment.MEDIA_MOUNTED);//判断SD卡是否挂载
        if (adCardExit) {
            sdDir = Environment.getExternalStorageDirectory();//获取根目录
        } else {
            sdDir = Environment.getDownloadCacheDirectory();
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
        }
    }

    /**
     * update file modified time
     *
     * @param path filePath
     */
    private void updateFileTime(String path) {
        File file = new File(path);
        long newModifyTime = System.currentTimeMillis();
        file.setLastModified(newModifyTime);
    }

    /**
     * save the bitmap to cache space
     *
     * @param bitmap bitmap
     * @param url    link
     */
    public void saveBitmap(Bitmap bitmap, String url) {
        if (bitmap == null) {
            return;
        }
        //判断SD卡上的空间
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            return;
        }
        String fileName = convertUrlToFileName(url);
        LogUtils.e(fileName);
        String dir = getDirectory();
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(dir + "/" + fileName);
        try {
            file.createNewFile();
            OutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * calculate the remaining space of SD card
     *
     * @return freeSpace
     */
    private int freeSpaceOnSd() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double) statFs.getAvailableBlocks() * (double) statFs.getBlockSize()) / MB;
        return (int) sdFreeMB;
    }

    /**
     * 将url转成文件名
     * convert Url To FileName
     *
     * @param url url
     * @return fileName
     */
    private String convertUrlToFileName(String url) {
        String[] str = url.split("/");
        return str[str.length - 1] + WHOLESALE_CONV;
    }

    /**
     * calculate all files size of dir(计算存储目录下的文件大小)
     * 当文件总大小大于规定的大小或者sd卡剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定时
     * ，那么删除40%最近没有被使用的文件
     *
     * @param dirPath dirPath
     * @return true:success;false:failed
     */
    private boolean removeCache(String dirPath) {
        File dirFile = new File(dirPath);
        File[] files = dirFile.listFiles();
        if (files == null || files.length <= 0) {
            return true;
        }
        //如果sd卡没有挂载
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        int dirSize = 0;
        for (File file : files) {
            if (file.getName().contains(WHOLESALE_CONV)) {
                dirSize += file.length();
            }
        }
        if (dirSize > CACHE_SIZE * MB || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            int removeFactor = (int) ((0.4 * files.length) + 1);
            Arrays.sort(files, new FileLastModifySoft());
            for (int i = 0; i < removeFactor; i++) {
                if (files[i].getName().contains(WHOLESALE_CONV)) {
                    files[i].delete();
                }
            }
        }
        return freeSpaceOnSd() > CACHE_SIZE;
    }

    public void display(ImageView imageView, String url) {
        Bitmap bitmap = getBitmap(url);
        if (bitmap == null) {
            imageView.setImageResource(R.drawable.ic_launcher);
            BitmapDownloaderTask task = new BitmapDownloaderTask();
            task.execute(url, imageView);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * Comparator
     */
    private class FileLastModifySoft implements Comparator<File> {
        @Override
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}