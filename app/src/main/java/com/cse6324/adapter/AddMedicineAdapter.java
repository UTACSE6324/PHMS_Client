package com.cse6324.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cse6324.bean.FoodAPIBean;
import com.cse6324.bean.MedicineAPIBean;
import com.cse6324.dialog.AddDietDialog;
import com.cse6324.dialog.AddMedicineDialog;
import com.cse6324.phms.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hopelty on 2017/4/8.
 */

public class AddMedicineAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<MedicineAPIBean> list;

    private AddMedicineDialog dialog;

    public AddMedicineAdapter(Context context, AddMedicineDialog dialog){
        this.context = context;
        this.list = new ArrayList<>();
        this.dialog = dialog;
    }

    public void setList(List<MedicineAPIBean> list){
        this.list = list;
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_add_medicine, null);
        view.setLayoutParams(lp);
        return new AddMedicineAdapter.ItemViewHolder(view);
    }

    @Override
    public int getItemViewType(int position){
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i){
        ItemViewHolder itemViewHolder = (AddMedicineAdapter.ItemViewHolder) viewHolder;

        itemViewHolder.tvName.setText(list.get(i).getName());
        itemViewHolder.tvForm.setText(list.get(i).getStrength_And_Forms());
        itemViewHolder.position = i;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvForm;
        int position;

        private ItemViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvForm = (TextView) itemView.findViewById(R.id.tv_form);

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
