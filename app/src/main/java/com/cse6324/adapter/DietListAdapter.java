package com.cse6324.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.cse6324.bean.DietBean;
import com.cse6324.bean.FoodAPIBean;
import com.cse6324.dialog.AddDietDialog;
import com.cse6324.dialog.CaloriePlanDialog;
import com.cse6324.fragment.DietFragment;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.phms.DietAnalysisActivity;
import com.cse6324.phms.R;
import com.cse6324.service.MyApplication;
import com.cse6324.util.DisplayUtil;
import com.cse6324.util.UserUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Jarvis on 2017/2/25.
 */

public class DietListAdapter extends RecyclerView.Adapter{

    final static int TITLE_CAL = 0;
    final static int TITLE_BF = 1;
    final static int TITLE_LUN = 2;
    final static int TITLE_DIN = 3;
    final static int TITLE_OTHER = 4;
    final static int ITEM = 5;
    final static int TITLE_BOTTOM = 6;

    private Context context;
    private List<DietBean> list1;
    private List<DietBean> list2;
    private List<DietBean> list3;
    private List<DietBean> list4;

    private float totalCal;
    private Calendar cal;

    private DietFragment fragment;

    private int lastAnimatedPosition = -1;

    private int position_title_bf;
    private  int position_title_lun;
    private int position_title_din;
    private  int position_title_other;


    public DietListAdapter(Context context, DietFragment fragment){
        this.context = context;
        this.fragment = fragment;
    }

    public void setCalendar(Calendar cal){
        this.cal = cal;
    }

    public void setDietList(List<DietBean> dietlist){
        totalCal = 0;

        this.list1 = new ArrayList<>();
        this.list2 = new ArrayList<>();
        this.list3 = new ArrayList<>();
        this.list4 = new ArrayList<>();

        for(int i = 0; i < dietlist.size(); i++){
            switch (dietlist.get(i).getType()){
                case 0:
                    list1.add(dietlist.get(i));
                    break;
                case 1:
                    list2.add(dietlist.get(i));
                    break;
                case 2:
                    list3.add(dietlist.get(i));
                    break;
                default:
                    list4.add(dietlist.get(i));
                    break;
            }

            totalCal += Float.parseFloat(dietlist.get(i).getCalorie());
        }
    }

    @Override
    public int getItemCount(){
        return list1.size() + list2.size() + list3.size() + list4.size() + 6;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view;
        switch(i){
            case ITEM:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_diet, null);
                view.setLayoutParams(lp);
                return new ItemViewHolder(view);
            case TITLE_CAL:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_head_diet, null);
                view.setLayoutParams(lp);
                return new HeadViewHolder(view);
            case TITLE_BOTTOM:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_bottom_diet, null);
                view.setLayoutParams(lp);
                return new BottomViewHolder(view);
            default:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_title_diet, null);
                view.setLayoutParams(lp);
                return new TitleViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i){
        if(viewHolder instanceof TitleViewHolder)
            setTitle((TitleViewHolder)viewHolder,i);
        else if(viewHolder instanceof HeadViewHolder){
            setHead((HeadViewHolder)viewHolder);
        }else if(viewHolder instanceof ItemViewHolder){
            DietBean bean;

            int type;
            int position;

            if(i < position_title_lun) {
                bean = list1.get(i - position_title_bf - 1);
                type = 0;
            }else if(i < position_title_din) {
                bean = list2.get(i - position_title_lun - 1);
                type = 1;
            }else if(i < position_title_other) {
                bean = list3.get(i - position_title_din - 1);
                type = 2;
            }else {
                bean = list4.get(i - position_title_other - 1);
                type = 3;
            }

            setItem((ItemViewHolder)viewHolder,bean);
        }

        if(i <= 9)
            runEnterAnimation(viewHolder.itemView, i);
    }

    private void runEnterAnimation(View view, int position) {
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(DisplayUtil.getScreenHeight(context));
            view.animate()
                    .translationY(0)
                    .setStartDelay(100 * position)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(500)
                    .start();
        }
    }

    private void setHead(HeadViewHolder viewHolder){
        float planCal;

        if(isEmpty(MyApplication.getPreferences(UserUtil.CAL)))
            planCal = 0;
        else
            planCal = Float.parseFloat(MyApplication.getPreferences(UserUtil.CAL));

        viewHolder.tvHad.setText(totalCal + "");
        viewHolder.tvPlan.setText(planCal + "");

        viewHolder.tvLeft.setText((planCal - totalCal) + "");

        if(planCal - totalCal < 0)
            viewHolder.tvLeft.setTextColor(context.getResources().getColor(R.color.red));
        else
            viewHolder.tvLeft.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
    }

    private void setTitle(TitleViewHolder viewholder, int i){
        if(getItemViewType(i) == TITLE_BF){
            viewholder.tvTitle.setText("Breakfast +");
            viewholder.icon.setImageResource(R.drawable.breakfast);
            viewholder.type = 0;
        }
        if(getItemViewType(i) == TITLE_LUN){
            viewholder.tvTitle.setText("Lunch +");
            viewholder.icon.setImageResource(R.drawable.lunch);
            viewholder.type = 1;
        }
        if(getItemViewType(i) == TITLE_DIN){
            viewholder.tvTitle.setText("Dinner +");
            viewholder.icon.setImageResource(R.drawable.dinner);
            viewholder.type = 2;
        }
        if(getItemViewType(i) == TITLE_OTHER){
            viewholder.tvTitle.setText("Snacks +");
            viewholder.icon.setImageResource(R.drawable.snack);
            viewholder.type = 3;
        }
    }

    private void setItem(ItemViewHolder viewholder, DietBean bean){
        viewholder.id = bean.getDietid();
        viewholder.tvName.setText(bean.getName());
        viewholder.tvQuantity.setText(bean.getQuantity() + " " + bean.getUnit());
        viewholder.tvCalorie.setText(bean.getCalorie());
    }

    @Override
    public int getItemViewType(int position){
        position_title_bf = 1;
        position_title_lun = list1.size() + 2;
        position_title_din = list1.size() + list2.size() + 3;
        position_title_other = list1.size() + list2.size() + list3.size() + 4;

        if(position == 0)
           return TITLE_CAL;
        else if(position == position_title_bf)
            return TITLE_BF;
        else if(position == position_title_lun)
            return TITLE_LUN;
        else if(position == position_title_din)
            return TITLE_DIN;
        else if(position == position_title_other)
            return TITLE_OTHER;
        else if(position == getItemCount() - 1)
            return TITLE_BOTTOM;
        else
            return ITEM;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{
        int id;
        TextView tvName,tvQuantity,tvCalorie;
        View divider;

        private ItemViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.name);
            tvQuantity = (TextView) itemView.findViewById(R.id.quantity);
            tvCalorie = (TextView) itemView.findViewById(R.id.calorie);

            final BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
                @Override
                public void onResponse(Response httpResponse, String response, Headers headers) {
                    if (response == null || response.length() == 0) {
                        Toast.makeText(context, "Connect fail", Toast.LENGTH_SHORT).show();
                    } else if(headers.get("Status-Code").equals("-1")){
                        Toast.makeText(context, "Delete fail", Toast.LENGTH_SHORT).show();
                    }else{
                        fragment.initData();
                    }
                }
            };

            itemView.setOnLongClickListener(
                    new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            new MaterialDialog.Builder(context)
                                    .title("Delete food?")
                                    .positiveText("Yes")
                                    .negativeText("No")
                                    .onNegative(
                                            new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    dialog.dismiss();
                                                }
                                            }
                                    )
                                    .onPositive(
                                            new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    new HttpUtil(HttpUtil.NORMAL_PARAMS)
                                                            .add("uid",MyApplication.getPreferences(UserUtil.UID))
                                                            .add("token",MyApplication.getPreferences(UserUtil.TOKEN))
                                                            .add("dietid",id+"")
                                                            .get(Constant.URL_DELETEDIETHISTORY,callback);

                                                }
                                            }
                                    )
                                    .show();
                            return false;
                        }
                    }
            );
        }
    }

    private class HeadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvPlan,tvHad,tvLeft;

        private HeadViewHolder(View itemView) {
            super(itemView);

            tvPlan = (TextView) itemView.findViewById(R.id.calorie1);
            tvHad = (TextView) itemView.findViewById(R.id.calorie2);
            tvLeft = (TextView) itemView.findViewById(R.id.calorie3);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view){
            new CaloriePlanDialog(context,caloriePlanListener).show();
        }
    }

    private CaloriePlanDialog.MyListener caloriePlanListener= new CaloriePlanDialog.MyListener(){
        @Override
        public void refreshActivity(){
            notifyItemChanged(0);
        }
    };

    private AddDietDialog.MyListener addDietListener= new AddDietDialog.MyListener(){
        @Override
        public void refreshActivity(DietBean bean, int type){
            switch (type){
                case 0:
                    list1.add(bean);
                    break;
                case 1:
                    list2.add(bean);
                    break;
                case 2:
                    list3.add(bean);
                    break;
                default:
                    list4.add(bean);
                    break;
            }
            totalCal += Float.parseFloat(bean.getCalorie());
            notifyDataSetChanged();
        }
    };

    private class TitleViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        ImageView icon;
        int type;

        private TitleViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.title);
            icon = (ImageView) itemView.findViewById(R.id.iv_icon);

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AddDietDialog(context,type,addDietListener,cal).show();
                        }
                    }
            );
        }
    }

    private class BottomViewHolder extends RecyclerView.ViewHolder{
        private BottomViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, DietAnalysisActivity.class);
                            intent.putExtra("date",cal);
                            context.startActivity(intent);
                        }
                    }
            );
        }
    }
}
