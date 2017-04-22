package com.cse6324.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cse6324.adapter.MedicineListAdapter;
import com.cse6324.bean.MedicineBean;
import com.cse6324.dialog.AddMedicineDialog;
import com.cse6324.dialog.SetReminderDialog;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.phms.R;
import com.cse6324.service.MyApplication;
import com.cse6324.util.UserUtil;

import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by Jarvis on 2017/2/11.
 */

public class MedicineFragment extends Fragment {
    private ImageView ivEmpty;
    private MedicineListAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {

            if (response == null || response.length() == 0) {
                Toast.makeText(getContext(), "Connect fail", Toast.LENGTH_SHORT).show();
            } else {
                if (adapter == null) {
                    adapter = new MedicineListAdapter(MedicineFragment.this.getContext(), setReminderListener, refreshListener);
                    recyclerView.setAdapter(adapter);
                }

                List<MedicineBean> list = JSON.parseArray(response, MedicineBean.class);
                adapter.setList(list);
            }

            swipeRefreshLayout.setRefreshing(false);
            adapter.notifyDataSetChanged();

            if (adapter.getItemCount() == 0) {
                ivEmpty.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
            } else {
                ivEmpty.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_medicine, null);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle("Medicine");

        ivEmpty = (ImageView) v.findViewById(R.id.iv_empty);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                initData();
                            }
                        }, 1000);
                    }
                }
        );

        ImageView ivAdd = (ImageView) v.findViewById(R.id.iv_add);
        ivAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AddMedicineDialog(getContext(), addMedicineListener).show();
                    }
                }
        );

        initData();

        return v;
    }

    public void initData() {
        new HttpUtil(HttpUtil.NORMAL_PARAMS)
                .add("uid", MyApplication.getPreferences(UserUtil.UID))
                .add("token", MyApplication.getPreferences(UserUtil.TOKEN))
                .get(Constant.URL_GETMEDICINE, callback);
    }

    private AddMedicineDialog.MyListener addMedicineListener = new AddMedicineDialog.MyListener() {
        @Override
        public void refreshActivity() {
            initData();
        }
    };
    private SetReminderDialog.MyListener setReminderListener = new SetReminderDialog.MyListener() {
        @Override
        public void refreshActivity() {
            initData();
        }
    };
    private MedicineListAdapter.MyListener refreshListener = new MedicineListAdapter.MyListener() {
        @Override
        public void refreshActivity() {
            initData();
        }
    };
}
