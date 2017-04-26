package com.cse6324.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cse6324.adapter.ArticleListAdapter;
import com.cse6324.adapter.NoteListAdapter;
import com.cse6324.bean.ArticleAPIBean;
import com.cse6324.bean.ArticleBean;
import com.cse6324.bean.ContactBean;
import com.cse6324.bean.NoteBean;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.phms.R;
import com.cse6324.service.MyApplication;
import com.cse6324.util.UserUtil;

import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by Jarvis on 2017/4/20.
 */

public class ArticleFragment extends Fragment{
    private int page;

    private ImageView ivEmpty;
    private ArticleListAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {

            if (response == null || response.length() == 0) {
                Toast.makeText(getContext(), "Connect fail", Toast.LENGTH_SHORT).show();
            } else{
                try {
                    ArticleAPIBean articleAPIBean = JSON.parseObject(response, ArticleAPIBean.class);
                    List<ArticleBean> list = articleAPIBean.getResponse().getDocs();

                    adapter.setList(list);
                }catch(Exception e){
                    initData();
                }
            }

            swipeRefreshLayout.setRefreshing(false);
            adapter.notifyDataSetChanged();

            if (adapter.getItemCount() == 0) {
                ivEmpty.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
            } else {
                ivEmpty.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_article, null);

        ivEmpty = (ImageView) v.findViewById(R.id.iv_empty);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new ArticleListAdapter(getContext());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
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

        page = 0;

        initData();

        return v;
    }

    public void initData(){
        new HttpUtil(HttpUtil.NORMAL_PARAMS)
                .add("fq","section_name:(\"health\")")
                .add("apikey","4127868b38414e4781d608c817cddf58")
                .add("fl","web_url,headline,multimedia,lead_paragraph,pub_date,source")
                .add("page",page+"")
                .get(Constant.URL_ARTICLE,callback);
    }
}
