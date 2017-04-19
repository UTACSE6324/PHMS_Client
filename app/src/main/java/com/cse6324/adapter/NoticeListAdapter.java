package com.cse6324.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cse6324.bean.NoteBean;
import com.cse6324.bean.NoticeBean;
import com.cse6324.phms.EditNoteActivity;
import com.cse6324.phms.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jarvis on 2017/4/11.
 */

public class NoticeListAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<NoticeBean> list;

    public NoticeListAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    public void setNoticeList(List<NoticeBean> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_note, null);
        view.setLayoutParams(lp);
        return new ItemViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;

        NoticeBean bean = list.get(i);

        itemViewHolder.bean = bean;
        itemViewHolder.tvTitle.setText(bean.getDescription());
        itemViewHolder.tvDate.setText("Click to see details");
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        NoticeBean bean;
        TextView tvTitle, tvDate;

        private ItemViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new MaterialDialog.Builder(context)
                                    .content(Html.fromHtml(bean.getSummary()))
                                    .positiveText("OK")
                                    .onPositive(
                                            new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    dialog.dismiss();
                                                }
                                            }
                                    )
                                    .show();
                        }
                    }
            );
        }
    }
}
