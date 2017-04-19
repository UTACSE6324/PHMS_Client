package com.cse6324.phms;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cse6324.adapter.ContactListAdapter;
import com.cse6324.adapter.NoticeListAdapter;
import com.cse6324.bean.ContactBean;
import com.cse6324.bean.NoticeBean;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.service.MyApplication;
import com.cse6324.util.UserUtil;

import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Jarvis on 2017/4/11.
 */

public class NoticeActivity  extends AppCompatActivity{
    private Context context;
    private RecyclerView recyclerView;
    private NoticeListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private BaseHttpRequestCallback getNoticeCallback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            if(response==null||response.length()==0){
                Toast.makeText(context,"Connect fail",Toast.LENGTH_SHORT).show();
            }else{
                List<NoticeBean> noticeList = JSON.parseArray(response, NoticeBean.class);
                adapter.setNoticeList(noticeList);
            }

            swipeRefreshLayout.setRefreshing(false);
            adapter.notifyDataSetChanged();
        }
    };

    private BaseHttpRequestCallback deleteNoticeCallback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
           initData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_contact);

        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Notice");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.back);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new NoticeListAdapter(this);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                initData();
                            }
                        }, 1000);
                    }
                }
        );

        initData();
    }

    private void initData(){
        new HttpUtil(HttpUtil.NORMAL_PARAMS)
                .add("uid",MyApplication.getPreferences(UserUtil.UID))
                .add("token",MyApplication.getPreferences(UserUtil.TOKEN))
                .get(Constant.URL_GETNOTICE, getNoticeCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.action_clear:
                new HttpUtil(HttpUtil.NORMAL_PARAMS)
                        .add("uid",MyApplication.getPreferences(UserUtil.UID))
                        .add("token",MyApplication.getPreferences(UserUtil.TOKEN))
                        .get(Constant.URL_DELETENOTICE, deleteNoticeCallback);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束Activity&从栈中移除该Activity
        MyApplication.getInstance().finishActivity(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
