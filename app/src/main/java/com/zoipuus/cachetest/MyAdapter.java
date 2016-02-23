package com.zoipuus.cachetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoipuus.cachetest.cache.BitmapFileCacheUtils;

public class MyAdapter extends BaseAdapter {

    private static final String[] URLS = {
            "http://avatar.csdn.net/E/3/5/1_hqocshheqing.jpg",
            "http://gtb.baidu.com/HttpService/get?p=dHlwZT1pbWFnZS9qcGVnJm49dmlzJnQ9YWRpbWcmYz10YjppZyZyPTU3NDc1MjIyMCwyMzQzNTcyMTAw",
            "http://b.hiphotos.baidu.com/image/h%3D200/sign=8c8dc382d239b60052ce08b7d9513526/b58f8c5494eef01fa36ad8a4e7fe9925bc317d51.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1478257864,2882073929&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=2282547951,3816622274&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=1231062057,3852413437&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3527986333,3942718311&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1020667791,3260921600&fm=21&gp=0.jpg",
            "http://www.szbike.com/bbs/forum.php?mod=attachment&aid=MjAyNDk0fDQ0NzQ3ZjRkfDE0NTYxOTU5NTd8MHw0MDM2Nw%3D%3D",
            "http://www.szbike.com/bbs/forum.php?mod=attachment&aid=MjAyNTE1fDA2ZjQ5NDU3fDE0NTYxOTU5NTd8MHw0MDM2Nw%3D%3D",
            "http://www.szbike.com/bbs/forum.php?mod=attachment&aid=MjAyNTE0fDhkNzYyZDg4fDE0NTYxOTU5NTd8MHw0MDM2Nw%3D%3D"};
    private int mCount;
    private Context mContext;
    private boolean mBusy = false;

    public void setFlagBusy(boolean busy) {
        this.mBusy = busy;
    }

    public MyAdapter(int count, Context context) {
        this.mCount = count;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView
                    .findViewById(R.id.tv_tips);
            viewHolder.mImageView = (ImageView) convertView
                    .findViewById(R.id.iv_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!mBusy) {
            BitmapFileCacheUtils.getInstance().display(viewHolder.mImageView, URLS[position % URLS.length]);
            viewHolder.mTextView.setText("--" + position
                    + "--IDLE ||TOUCH_SCROLL");
        } else {
            Bitmap bitmap = BitmapFileCacheUtils.getInstance().getBitmap(URLS[position % URLS.length]);
            if (bitmap != null) {
                viewHolder.mImageView.setImageBitmap(bitmap);
            } else {
                viewHolder.mImageView.setImageResource(R.drawable.ic_launcher);
            }
            viewHolder.mTextView.setText("--" + position + "--FLING");
        }
        return convertView;
    }

    static class ViewHolder {
        TextView mTextView;
        ImageView mImageView;
    }

}
