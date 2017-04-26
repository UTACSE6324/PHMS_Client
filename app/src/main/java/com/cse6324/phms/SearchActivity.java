package com.cse6324.phms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cse6324.adapter.NoticeListAdapter;
import com.cse6324.adapter.SearchListAdapter;
import com.cse6324.bean.ContactBean;
import com.cse6324.bean.DietBean;
import com.cse6324.bean.MedicineBean;
import com.cse6324.bean.NoteBean;
import com.cse6324.bean.NoticeBean;
import com.cse6324.bean.SearchBean;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.service.MyApplication;
import com.cse6324.util.UserUtil;

import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by Jarvis on 2017/4/23.
 */

public class SearchActivity extends AppCompatActivity {
    private ImageView ivEmpty;
    private EditText etSearch;
    private RecyclerView recyclerView;
    private SearchListAdapter adapter;

    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            try {
                SearchBean searchBean = JSON.parseObject(response, SearchBean.class);

                List<MedicineBean> medicineList = JSON.parseArray(searchBean.getMedicine(),MedicineBean.class);
                List<DietBean> dietList = JSON.parseArray(searchBean.getDiet(),DietBean.class);
                List<NoteBean> noteList = JSON.parseArray(searchBean.getNote(),NoteBean.class);
                List<ContactBean> contactList = JSON.parseArray(searchBean.getContact(),ContactBean.class);
                List<NoticeBean> noticeList = JSON.parseArray(searchBean.getNotice(),NoticeBean.class);

                adapter.setList(medicineList,dietList,noteList,contactList,noticeList);
                adapter.notifyDataSetChanged();

            }catch (Exception e){
                Toast.makeText(SearchActivity.this,"Error",Toast.LENGTH_LONG).show();
            }

            if (adapter.getItemCount() == 0) {
                ivEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                ivEmpty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        ivEmpty = (ImageView) findViewById(R.id.iv_empty);

        adapter = new SearchListAdapter(this);
        recyclerView.setAdapter(adapter);

        etSearch = (EditText) findViewById(R.id.et_search);
        etSearch.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() > 0) {
                            new HttpUtil(HttpUtil.NORMAL_PARAMS)
                                    .add("uid", MyApplication.getPreferences(UserUtil.UID))
                                    .add("token", MyApplication.getPreferences(UserUtil.TOKEN))
                                    .add("key", s.toString())
                                    .get(Constant.URL_SEARCH, callback);
                        }else if(s.length() == 0){
                            ivEmpty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                }
        );

        toolbar.setNavigationIcon(R.mipmap.back);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_none, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
