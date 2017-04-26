package com.cse6324.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cse6324.bean.ArticleBean;
import com.cse6324.bean.ContactBean;
import com.cse6324.bean.DietBean;
import com.cse6324.bean.MedicineBean;
import com.cse6324.bean.NoteBean;
import com.cse6324.bean.NoticeBean;
import com.cse6324.bean.TypeBean;
import com.cse6324.dialog.NoticeDialog;
import com.cse6324.phms.ArticleActivity;
import com.cse6324.phms.NoteActivity;
import com.cse6324.phms.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jarvis on 2017/4/23.
 */

public class SearchListAdapter extends RecyclerView.Adapter {
    public final static int MED = -1;
    public final static int DIET = -2;
    public final static int NOTE = -3;
    public final static int CONTACT = -4;
    public final static int NOTICE = -5;

    private Context context;
    private List<TypeBean> typeList;
    private List<MedicineBean> medicineList;
    private List<DietBean> dietList;
    private List<NoteBean> noteList;
    private List<ContactBean> contactList;
    private List<NoticeBean> noticeList;

    public SearchListAdapter(Context context) {
        this.context = context;
        this.typeList = new ArrayList<>();
    }

    public void setList(List<MedicineBean> medicineList, List<DietBean> dietList,
                        List<NoteBean> noteList, List<ContactBean> contactList, List<NoticeBean> noticeList) {
        this.medicineList = medicineList;
        this.dietList = dietList;
        this.noteList = noteList;
        this.contactList = contactList;
        this.noticeList = noticeList;

        typeList = new ArrayList<>();

        if (medicineList.size() > 0) {
            typeList.add(new TypeBean(-1, MED));
            for (int i = 0; i < medicineList.size(); i++) {
                typeList.add(new TypeBean(i, MED));
            }
        }

        if (dietList.size() > 0) {
            typeList.add(new TypeBean(-1, DIET));
            for (int i = 0; i < dietList.size(); i++) {
                typeList.add(new TypeBean(i, DIET));
            }
        }

        if (noteList.size() > 0) {
            typeList.add(new TypeBean(-1, NOTE));
            for (int i = 0; i < noteList.size(); i++) {
                typeList.add(new TypeBean(i, NOTE));
            }
        }

        if (contactList.size() > 0) {
            typeList.add(new TypeBean(-1, CONTACT));
            for (int i = 0; i < contactList.size(); i++) {
                typeList.add(new TypeBean(i, CONTACT));
            }
        }

        if (noticeList.size() > 0) {
            typeList.add(new TypeBean(-1, NOTICE));
            for (int i = 0; i < noticeList.size(); i++) {
                typeList.add(new TypeBean(i, NOTICE));
            }
        }
        for (int i = 0; i < typeList.size(); i++) {
            System.out.println(">>>>" + typeList.get(i).getPosition());
        }
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (typeList.get(position).getPosition() == -1)
            return -1;
        else
            return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view;
        if (i == -1) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_head_search, null);
            view.setLayoutParams(lp);
            return new HeadViewHolder(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_search, null);
            view.setLayoutParams(lp);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (typeList.get(i).getPosition() == -1) {
            setHead((HeadViewHolder) viewHolder, i);
        } else {
            switch (typeList.get(i).getType()) {
                case MED:
                    setMed((ItemViewHolder) viewHolder, i);
                    break;
                case DIET:
                    setDiet((ItemViewHolder) viewHolder, i);
                    break;
                case NOTE:
                    setNote((ItemViewHolder) viewHolder, i);
                    break;
                case NOTICE:
                    setNotice((ItemViewHolder) viewHolder, i);
                    break;
                case CONTACT:
                    setContact((ItemViewHolder) viewHolder, i);
                    break;
            }
        }
    }

    private void setHead(HeadViewHolder viewHolder, int i) {
        switch (typeList.get(i).getType()) {
            case MED:
                viewHolder.tvContent.setText("Medicine");
                break;
            case DIET:
                viewHolder.tvContent.setText("Diet");
                break;
            case NOTE:
                viewHolder.tvContent.setText("Note");
                break;
            case NOTICE:
                viewHolder.tvContent.setText("Notice");
                break;
            case CONTACT:
                viewHolder.tvContent.setText("Contact");
                break;
        }
    }

    private void setMed(ItemViewHolder viewHolder, int i) {
        MedicineBean bean = medicineList.get(typeList.get(i).getPosition());
        viewHolder.tvTitle.setText(bean.getName());
        viewHolder.tvContent.setText(
                bean.getQuantity() + "  " + bean.getInstructions()
        );
        viewHolder.tvDate.setText("");
    }

    private void setDiet(ItemViewHolder viewHolder, int i) {
        DietBean bean = dietList.get(typeList.get(i).getPosition());

        String type = "";
        switch (bean.getType()) {
            case 0:
                type = "breakfast";
                break;
            case 1:
                type = "launch";
                break;
            case 2:
                type = "dinner";
                break;
            case 3:
                type = "snack";
                break;
        }

        viewHolder.tvTitle.setText(bean.getName());
        viewHolder.tvContent.setText(
                type + "    " + bean.getQuantity() + "    " + bean.getUnit()
        );
        viewHolder.tvDate.setText(bean.getDate());
    }

    private void setNote(ItemViewHolder viewHolder, int i) {
        final NoteBean bean = noteList.get(typeList.get(i).getPosition());
        viewHolder.tvTitle.setText(bean.getName());
        viewHolder.tvDate.setText(bean.getDate().substring(0, 10));
        viewHolder.tvContent.setText("");

        viewHolder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        if (bean.getType().equals("normal")) {
                            intent = new Intent(context, NoteActivity.class);
                            intent.putExtra("note", bean);
                            context.startActivity(intent);
                        }
                        if (bean.getType().equals("article")) {
                            ArticleBean articleBean = new ArticleBean();
                            articleBean.setWeb_url(bean.getSummary());

                            intent = new Intent(context, ArticleActivity.class);
                            intent.putExtra("article", articleBean);
                            context.startActivity(intent);
                        }
                    }
                }
        );
    }

    private void setNotice(ItemViewHolder viewHolder, int i) {
        final NoticeBean bean = noticeList.get(typeList.get(i).getPosition());

        viewHolder.tvTitle.setText(bean.getDescription());
        viewHolder.tvContent.setText("");
        viewHolder.tvDate.setText("");

        viewHolder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new NoticeDialog(context,bean.getSummary(),bean.getDescription()).show();
                    }
                }
        );
    }

    private void setContact(ItemViewHolder viewHolder, int i) {
        ContactBean bean = contactList.get(typeList.get(i).getPosition());

        viewHolder.tvTitle.setText(bean.getName());
        viewHolder.tvContent.setText(bean.getEmail() + "  " + bean.getPhone());
        viewHolder.tvDate.setText("");
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tvContent, tvTitle, tvDate;

        private ItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }

    private class HeadViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tvContent;

        private HeadViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }
}
