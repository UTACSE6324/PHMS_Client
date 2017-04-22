package com.cse6324.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cse6324.bean.FoodAPIBean;
import com.cse6324.dialog.AddDietDialog;
import com.cse6324.phms.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jarvis on 2017/2/26.
 */

public class FoodListAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<FoodAPIBean.FoodResultBean.FoodBean> list;

    private AddDietDialog dialog;

    public FoodListAdapter(Context context, AddDietDialog dialog){
        this.context = context;
        this.list = new ArrayList<>();
        this.dialog = dialog;
    }

    public void setList(List<FoodAPIBean.FoodResultBean.FoodBean> list){
        this.list = list;
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_normal, null);
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

        itemViewHolder.tvName.setText(list.get(i).getItem_name());
        itemViewHolder.tvCal.setText(list.get(i).getNf_calories());
        itemViewHolder.position = i;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvCal;
        int position;

        private ItemViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvCal = (TextView) itemView.findViewById(R.id.tv_cal);

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.getSearchResult(list.get(position));
                        }
                    }
            );
        }
    }
}
