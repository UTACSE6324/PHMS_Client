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
import android.widget.Toast;

import com.cse6324.bean.ArticleBean;
import com.cse6324.bean.ContactBean;
import com.cse6324.phms.ArticleActivity;
import com.cse6324.phms.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jarvis on 2017/4/20.
 */

public class ArticleListAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ArticleBean> list;

    public ArticleListAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    public void setList(List<ArticleBean> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_article, null);
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

        ArticleBean bean = list.get(i);

        itemViewHolder.bean = bean;
        itemViewHolder.title.setText(bean.getHeadline().getMain());
        itemViewHolder.content.setText(bean.getLead_paragraph());
        itemViewHolder.source.setText("From " + bean.getSource());
        itemViewHolder.date.setText(bean.getPub_date().substring(0, 10) + "  "
                + bean.getPub_date().substring(11, 16));

        List<ArticleBean.ImgBean> imgList = bean.getMultimedia();
        for (int j = 0; j < imgList.size(); j++) {
            if (imgList.get(j).getSubtype().equals("xlarge")) {
                Picasso.with(context).load("https://static01.nyt.com/" + imgList.get(j).getUrl())
                        .into(itemViewHolder.image);
                j = imgList.size();
            }
        }

        if (imgList.size() == 0)
            itemViewHolder.image.setVisibility(View.GONE);
        else
            itemViewHolder.image.setVisibility(View.VISIBLE);

    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        ArticleBean bean;
        ImageView image;
        TextView title, content, date, source;

        private ItemViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
            date = (TextView) itemView.findViewById(R.id.date);
            source = (TextView) itemView.findViewById(R.id.source);

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ArticleActivity.class);
                            intent.putExtra("article",bean);
                            context.startActivity(intent);
                        }
                    }
            );
        }
    }
}
