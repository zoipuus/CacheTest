package com.zoipuus.cachetest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.zoipuus.cachetest.cache.BitmapFileCacheUtils;

public class MainActivity extends AppCompatActivity {

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
        ImageView imageView = (ImageView) findViewById(R.id.image);
        BitmapFileCacheUtils.getInstance().display(imageView, "http://avatar.csdn.net/E/3/5/1_hqocshheqing.jpg");
    }
}
