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

/**
 * Created by Jarvis on 2017/2/15.
 */

public class ContactListAdapter extends RecyclerView.Adapter{

    private Context context;
    private List<ContactBean> contactList;

    public  ContactListAdapter(Context context){
        this.context = context;
        this.contactList = new ArrayList<>();
    }

    public void setContactList(List<ContactBean> contactList){
        this.contactList = contactList;

        for(int i = 0; i < contactList.size(); i++){
            if(contactList.get(i).getDefaultc().equals("1")){
                ContactBean bean = contactList.get(i);
                contactList.remove(i);
                contactList.add(0,bean);
            }
        }
    }

    @Override
    public int getItemCount(){
        return contactList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        View view;
        if(i == 0)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_head_contact, null);
        else
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_contact, null);

        view.setLayoutParams(lp);
        return new ItemViewHolder(view);
    }

    @Override
    public int getItemViewType(int position){
        if(position == 0 && contactList.get(0).getDefaultc().equals("1"))
            return 0;
        else
            return 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i){
        ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;

        ContactBean bean = contactList.get(i);

        itemViewHolder.cid = bean.getCid();
        itemViewHolder.tvTitle.setText(bean.getName());
        itemViewHolder.tvContent.setText(bean.getPhone());
        itemViewHolder.tvContent2.setText(bean.getEmail());

        Random random=new Random();
        int a = random.nextInt(10);
        switch (a){
            case 0:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_01);
                break;
            case 1:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_02);
                break;
            case 2:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_03);
                break;
            case 3:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_04);
                break;
            case 4:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_05);
                break;
            case 5:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_06);
                break;
            case 6:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_07);
                break;
            case 7:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_08);
                break;
            case 8:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_09);
                break;
            case 9:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_10);
                break;
        }
    }

    private void setDefault(String cid){
        for(int i = 0; i < contactList.size(); i++){
            if(contactList.get(i).getCid().equals(cid)){
                contactList.get(i).setDefaultc("1");
                ContactBean bean = contactList.get(i);
                contactList.remove(i);
                contactList.add(0,bean);
            }
        }

        notifyDataSetChanged();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{
        String cid;
        ImageView avatar;
        TextView tvTitle,tvContent,tvContent2;

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
                            new MaterialDialog.Builder(context)
                                    .title("Default contact")
                                    .content("Set as default contact when there is conflict in your plan.")
                                    .positiveText("Yes")
                                    .negativeText("No")
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            new HttpUtil(HttpUtil.NORMAL_PARAMS)
                                                    .add("uid", MyApplication.getPreferences(UserUtil.UID))
                                                    .add("token",MyApplication.getPreferences(UserUtil.TOKEN))
                                                    .add("cid",cid)
                                                    .get(Constant.URL_SETDEFAULTCONTACT,
                                                            new BaseHttpRequestCallback() {
                                                                @Override
                                                                public void onResponse(Response httpResponse, String response, Headers headers) {
                                                                    if(response != null)
                                                                    if(headers.get("Status-Code").equals("1")){
                                                                        setDefault(cid);
                                                                    }else
                                                                        Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
                                                                }
                                                                }
                                                            );
                                        }
                                    })
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    }
            );
        }
    }
}
