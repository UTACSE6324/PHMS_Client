package com.cse6324.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cse6324.adapter.ContentPagerAdapter;
import com.cse6324.phms.EditNoteActivity;
import com.cse6324.phms.R;
import com.cse6324.service.MyApplication;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by Jarvis on 2017/4/20.
 */

public class ContentFragment extends Fragment {

    ImageView ivAdd;
    private SmartTabLayout tabs;
    private ViewPager viewPager;
    private ContentPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_content, null);

        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        tabs = (SmartTabLayout) v.findViewById(R.id.tabs);

        adapter = new ContentPagerAdapter(getActivity().getSupportFragmentManager(),getContext());
        viewPager.setAdapter(adapter);
        tabs.setViewPager(viewPager);

        ivAdd = (ImageView) v.findViewById(R.id.iv_add);
        ivAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), EditNoteActivity.class);
                        getActivity().startActivity(intent);
                    }
                }
        );

        return v;
    }



    @Override
    public void onResume(){
        super.onResume();
    }
}
