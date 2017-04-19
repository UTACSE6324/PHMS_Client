package com.cse6324.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cse6324.bean.NoteBean;
import com.cse6324.phms.EditNoteActivity;
import com.cse6324.phms.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jarvis on 2017/4/9.
 */

public class NoteListAdapter extends RecyclerView.Adapter{

    private Context context;
    private List<NoteBean> list;

    public  NoteListAdapter(Context context){
        this.context = context;
        this.list = new ArrayList<>();
    }

    public void setNoteList(List<NoteBean> list){
        this.list = list;
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_note, null);
        view.setLayoutParams(lp);
        return new ItemViewHolder(view);
    }

    @Override
    public int getItemViewType(int position){
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i){
        ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;

        NoteBean bean = list.get(i);

        itemViewHolder.bean = bean;
        itemViewHolder.tvTitle.setText(bean.getName());
        itemViewHolder.tvDate.setText(bean.getDate());
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        NoteBean bean;
        TextView tvTitle,tvDate;

        private ItemViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, EditNoteActivity.class);
                            intent.putExtra("note",bean);
                            context.startActivity(intent);
                        }
                    }
            );
        }
    }
}
