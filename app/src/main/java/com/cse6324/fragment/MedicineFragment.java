package com.cse6324.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.cse6324.adapter.MedicineListAdapter;
import com.cse6324.bean.MedicineBean;
import com.cse6324.dialog.AddMedicineDialog;
import com.cse6324.dialog.SetReminderDialog;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.phms.LoginActivity;
import com.cse6324.phms.R;
import com.cse6324.phms.SearchActivity;
import com.cse6324.service.MyApplication;
import com.cse6324.util.UserUtil;
import com.wooplr.spotlight.SpotlightView;

import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by Jarvis on 2017/2/11.
 */

public class MedicineFragment extends Fragment {
    private ImageView ivEmpty, ivAdd, ivSearch;
    private MedicineListAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {

            if(headers.get("summary").equals("Token out of date")){
                new MaterialDialog
                        .Builder(getActivity())
                        .icon(getResources().getDrawable(R.mipmap.ic_warning_48dp))
                        .title("Login expired")
                        .content("It seems the account has been logged in on another device.")
                        .positiveText("OK")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                UserUtil.saveUserInfo(null);
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                MyApplication.getInstance().finishAllActivity();
                            }
                        })
                        .show();
            }

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

        ivAdd = (ImageView) v.findViewById(R.id.iv_add);
        ivAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AddMedicineDialog(getContext(), addMedicineListener).show();
                    }
                }
        );

        ivSearch = (ImageView) v.findViewById(R.id.iv_search);
        ivSearch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SearchActivity.class);
                        startActivity(intent);
                    }
                }
        );

        initData();
        initShowCase();

        return v;
    }

    public void initData() {
        new HttpUtil(HttpUtil.NORMAL_PARAMS)
                .add("uid", MyApplication.getPreferences(UserUtil.UID))
                .add("token", MyApplication.getPreferences(UserUtil.TOKEN))
                .get(Constant.URL_GETMEDICINE, callback);
    }

    public void initShowCase(){
        new SpotlightView.Builder(getActivity())
                .introAnimationDuration(400)
                .performClick(true)
                .fadeinTextDuration(400)
                .headingTvColor(Color.parseColor("#ffffff"))
                .headingTvSize(32)
                .headingTvText("Add medicine ")
                .subHeadingTvColor(Color.parseColor("#ffffff"))
                .subHeadingTvSize(16)
                .subHeadingTvText("Click to add a medicine and reminder for it")
                .maskColor(Color.parseColor("#dc000000"))
                .target(ivAdd)
                .lineAnimDuration(400)
                .lineAndArcColor(Color.parseColor("#ffffff"))
                .dismissOnTouch(true)
                .dismissOnBackPress(true)
                .enableDismissAfterShown(true)
                .usageId("med") //UNIQUE ID
                .enableRevealAnimation(true)
                .show();
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
