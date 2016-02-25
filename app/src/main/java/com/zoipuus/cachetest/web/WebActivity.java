package com.zoipuus.cachetest.web;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.zoipuus.cachetest.LogUtils;
import com.zoipuus.cachetest.R;

public class WebActivity extends Activity implements View.OnClickListener {

    private WebView webView;
    private EditText url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

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
        findViewById(R.id.search).setOnClickListener(this);
        url = (EditText) findViewById(R.id.url);
        webView = (WebView) findViewById(R.id.web);
        WebSettings webSetting = webView.getSettings();
        //setting cache mode
        webSetting.setAppCacheEnabled(true);
        webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/gefdemoweb";
//        webSetting.setAppCachePath(path);

        //support JavaScript
        webSetting.setJavaScriptEnabled(true);
        webView.loadUrl("http://www.szbike.com");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                LogUtils.d("progress->" + newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.search:
                webView.loadUrl(url.getText().toString());
                break;
        }
    }
}
