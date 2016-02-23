package com.zoipuus.cachetest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.zoipuus.cachetest.cache.BitmapFileCacheUtils;

public class MainActivity extends AppCompatActivity {

    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setupViews();
    }

    private void setupViews() {
        ListView mListView = (ListView) findViewById(R.id.main_lv_list);
        adapter = new MyAdapter(500, this);
        mListView.setAdapter(adapter);
        mListView.setOnScrollListener(mScrollListener);
        ImageView imageView = (ImageView) findViewById(R.id.image);
        BitmapFileCacheUtils.getInstance().display(imageView, "http://avatar.csdn.net/E/3/5/1_hqocshheqing.jpg");
    }

    OnScrollListener mScrollListener = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_FLING:
                    adapter.setFlagBusy(true);
                    break;
                case OnScrollListener.SCROLL_STATE_IDLE:
                    adapter.setFlagBusy(false);
                    break;
                case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    adapter.setFlagBusy(false);
                    break;
                default:
                    break;
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
        }
    };

}
