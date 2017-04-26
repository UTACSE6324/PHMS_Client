package com.cse6324.phms;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.cse6324.bean.ArticleBean;
import com.cse6324.bean.NoteBean;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.service.MyApplication;
import com.cse6324.util.FormatUtil;
import com.cse6324.util.UserUtil;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by Jarvis on 2017/4/20.
 */

public class ArticleActivity extends AppCompatActivity {
    private WebView webView;
    private ArticleBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_article);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Article");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.back);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.show();

        try {
            bean = (ArticleBean) getIntent().getSerializableExtra("article");

            String url = bean.getWeb_url();

            webView = (WebView) findViewById(R.id.webView);
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return super.shouldOverrideUrlLoading(view, url);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            bean = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (bean == null || bean.getHeadline() == null)
            getMenuInflater().inflate(R.menu.menu_none, menu);
        else
            getMenuInflater().inflate(R.menu.menu_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.action_save:
                new HttpUtil(HttpUtil.NORMAL_PARAMS)
                        .add("uid", MyApplication.getPreferences(UserUtil.UID))
                        .add("token", MyApplication.getPreferences(UserUtil.TOKEN))
                        .add("date", bean.getPub_date())
                        .add("name", bean.getHeadline().getMain())
                        .add("summary", bean.getWeb_url())
                        .add("type", "article")
                        .get(Constant.URL_ADDNOTE, callback);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            if (response == null || response.length() == 0) {
                Toast.makeText(ArticleActivity.this, "Connect fail", Toast.LENGTH_SHORT).show();
            } else if (headers.get("Status-Code").equals("1")) {
                Toast.makeText(ArticleActivity.this, response, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ArticleActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
