package com.liudong.douban.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
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
        mWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_actor_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_browser:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
                startActivity(Intent.createChooser(intent, "请选择浏览器"));
                return true;
            case R.id.action_refresh:
                mWebView.reload();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
