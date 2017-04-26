package com.cse6324.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.cse6324.bean.ContactBean;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.phms.R;
import com.cse6324.service.MyApplication;
import com.cse6324.util.UserUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

import static com.cse6324.service.MyApplication.getContext;

/**
 * Created by Jarvis on 2017/2/15.
 */

public class ShareContactListAdapter extends RecyclerView.Adapter {

    private Context context;
    private String startdate, enddate;
    private List<ContactBean> contactList;

    public ShareContactListAdapter(Context context, String startdate, String enddate) {
        this.context = context;
        this.startdate = startdate;
        this.enddate = enddate;
        this.contactList = new ArrayList<>();
    }

    public void setContactList(List<ContactBean> contactList) {
        this.contactList = contactList;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_contact, null);
        view.setLayoutParams(lp);
        return new ItemViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;

        ContactBean bean = contactList.get(i);

        itemViewHolder.avatar.setVisibility(View.GONE);

        itemViewHolder.cid = bean.getCid();
        itemViewHolder.email = bean.getEmail();
        itemViewHolder.tvTitle.setText(bean.getName());
        itemViewHolder.tvContent.setText(bean.getPhone());
        itemViewHolder.tvContent2.setText(bean.getEmail());
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        String cid, email;
        ImageView avatar;
        TextView tvTitle, tvContent, tvContent2;

        private ItemViewHolder(View itemView) {
            super(itemView);

            avatar = (ImageView) itemView.findViewById(R.id.iv_item);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvContent2 = (TextView) itemView.findViewById(R.id.tv_content2);

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new HttpUtil(HttpUtil.NORMAL_PARAMS)
                                    .add("uid", MyApplication.getPreferences(UserUtil.UID))
                                    .add("startdate", startdate)
                                    .add("enddate", enddate)
                                    .add("email", email)
                                    .get(Constant.URL_SENDEMAIL, callback);
                        }
                    }
            );
        }
    }

    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
        }
    };
}
