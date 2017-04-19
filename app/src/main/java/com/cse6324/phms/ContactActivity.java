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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.cse6324.adapter.ContactListAdapter;
import com.cse6324.bean.ContactBean;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.service.MyApplication;
import com.cse6324.util.FormatUtil;
import com.cse6324.util.UserUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Jarvis on 2017/2/15.
 */

public class ContactActivity extends AppCompatActivity{
    private Context context;
    private RecyclerView recyclerView;
    private ContactListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MaterialDialog addContactDialog;

    private BaseHttpRequestCallback getContactCallback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            if(response==null||response.length()==0){
                Toast.makeText(context,"Connect fail",Toast.LENGTH_SHORT).show();
            }else{
                List<ContactBean> contactList = JSON.parseArray(response, ContactBean.class);
                adapter.setContactList(contactList);
            }

            swipeRefreshLayout.setRefreshing(false);
            adapter.notifyDataSetChanged();
        }
    };

    private BaseHttpRequestCallback addContactCallback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {

            if (response == null || response.length() == 0) {
                Toast.makeText(context, "Connect fail", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(context, response , Toast.LENGTH_SHORT).show();
            }
            addContactDialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_contact);

        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Contact");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.back);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new ContactListAdapter(this);
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

        addContactDialog = new MaterialDialog.Builder(this)
                .title("Add Contact")
                .customView(R.layout.dialog_add_contact,true)
                .build();
        final View view = addContactDialog.getCustomView();

        final MaterialEditText etName = (MaterialEditText) view.findViewById(R.id.name);
        final MaterialEditText etEmail = (MaterialEditText) view.findViewById(R.id.email);
        final MaterialEditText etPhone = (MaterialEditText) view.findViewById(R.id.phone);

        TextView submit = (TextView) view.findViewById(R.id.submit);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);

        submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = etName.getText().toString();
                        String email = etEmail.getText().toString();
                        String phone = etPhone.getText().toString();

                        if(FormatUtil.checkEmail(email) && !name.isEmpty() && !phone.isEmpty()){
                            new HttpUtil(HttpUtil.NORMAL_PARAMS)
                                    .add("uid",MyApplication.getPreferences(UserUtil.UID))
                                    .add("token",MyApplication.getPreferences(UserUtil.TOKEN))
                                    .add("name",name)
                                    .add("phone",phone)
                                    .add("email",email)
                                    .get(Constant.URL_ADDCONTACT, addContactCallback);
                        }else{
                            Toast.makeText(context,"Insert Invalid",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        cancle.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addContactDialog.dismiss();
                    }
                }
        );
    }

    private void initData(){
        new HttpUtil(HttpUtil.NORMAL_PARAMS)
                .add("uid",MyApplication.getPreferences(UserUtil.UID))
                .add("token",MyApplication.getPreferences(UserUtil.TOKEN))
                .get(Constant.URL_GETCONTACT, getContactCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.action_add:
                addContactDialog.show();
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
