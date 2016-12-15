package com.liudong.douban.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.liudong.douban.R;
import com.liudong.douban.ui.view.MyWebView;

import butterknife.BindView;

public class ActorDetailActivity extends BaseActivity {

    @BindView(R.id.web_layout)
    LinearLayout web_layout;

    private MyWebView mWebView;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUrl = getIntent().getStringExtra("url");
        initWebView();
    }

    private void initWebView() {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView = new MyWebView(getApplicationContext());
        mWebView.setLayoutParams(params);
        web_layout.addView(mWebView);

        mWebView.loadUrl(mUrl);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
        mWebView.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        mWebView.resumeTimers();
    }

    @Override
    protected void onDestroy() {

        if (mWebView != null) {
            mWebView.loadUrl("about:blank");
            mWebView.clearHistory();
            web_layout.removeView(mWebView);
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_actor_detail;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
