package com.cse6324.phms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.cse6324.bean.NoteBean;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.service.MyApplication;
import com.cse6324.util.FormatUtil;
import com.cse6324.util.UserUtil;

import java.net.URLEncoder;

import static cn.finalteam.toolsfinal.StringUtils.isEmpty;

/**
 * Created by Jarvis on 2017/4/22.
 */

public class NoteActivity extends AppCompatActivity {
    private NoteBean bean;
    private TextView ivTitle;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Note");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.back);

        ivTitle = (TextView) findViewById(R.id.iv_title);
        webView = (WebView) findViewById(R.id.webView);

        try {
            bean = (NoteBean) getIntent().getSerializableExtra("note");
            ivTitle.setText(bean.getName());
            webView.loadData(URLEncoder.encode(bean.getSummary(), "utf-8"), "text/html", "utf-8");
        } catch (Exception e) {
            bean = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.action_edit:
                Intent intent = new Intent(this, EditNoteActivity.class);
                intent.putExtra("note", bean);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }
}
