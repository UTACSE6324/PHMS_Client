package com.cse6324.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.cse6324.adapter.ShareContactListAdapter;
import com.cse6324.bean.ContactBean;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.phms.R;
import com.cse6324.service.MyApplication;
import com.cse6324.util.UserUtil;

import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

import static com.cse6324.service.MyApplication.getContext;

/**
 * Created by Jarvis on 2017/4/24.
 */

public class ContactDialog {
    MaterialDialog dialog;
    RecyclerView recyclerView;
    ShareContactListAdapter adapter;

    public ContactDialog(Context context, String startdate, String enddate){

        dialog = new MaterialDialog
                .Builder(context)
                .customView(R.layout.dialog_contact,false)
                .title("Select your contact")
                .positiveText("Close")
                .onPositive(
                        new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        }
                )
                .build();

        View layout = dialog.getCustomView();

        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new ShareContactListAdapter(context,startdate,enddate);
        recyclerView.setAdapter(adapter);

        new HttpUtil(HttpUtil.NORMAL_PARAMS)
                .add("uid", MyApplication.getPreferences(UserUtil.UID))
                .add("token",MyApplication.getPreferences(UserUtil.TOKEN))
                .get(Constant.URL_GETCONTACT, getContactCallback);
    }

    public void show(){
        dialog.show();
    }

    private BaseHttpRequestCallback getContactCallback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            if(response==null||response.length()==0){
                Toast.makeText(getContext(),"Connect fail",Toast.LENGTH_SHORT).show();
            }else{
                List<ContactBean> contactList = JSON.parseArray(response, ContactBean.class);
                adapter.setContactList(contactList);
            }

            adapter.notifyDataSetChanged();
        }
    };
}
