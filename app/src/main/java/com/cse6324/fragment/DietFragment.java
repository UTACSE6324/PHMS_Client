package com.cse6324.fragment;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cse6324.adapter.ContactListAdapter;
import com.cse6324.adapter.DietListAdapter;
import com.cse6324.bean.ContactBean;
import com.cse6324.bean.DietBean;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.phms.R;
import com.cse6324.service.MyApplication;
import com.cse6324.util.FormatUtil;
import com.cse6324.util.UserUtil;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by Jarvis on 2017/2/11.
 */

public class DietFragment extends Fragment {
    private RecyclerView recyclerView;
    private DietListAdapter adapter;

    private Calendar cal;
    private TextView tvDate;
    private ImageView left,right;

    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {

            if (response == null || response.length() == 0) {
                Toast.makeText(getContext(), "Connect fail", Toast.LENGTH_SHORT).show();
            } else{
                if(adapter == null){
                    adapter = new DietListAdapter(DietFragment.this.getContext(),
                            DietFragment.this);
                    recyclerView.setAdapter(adapter);
                }

                try {
                    List<DietBean> dietList = JSON.parseArray(response, DietBean.class);
                    adapter.setDietList(dietList);
                    adapter.setCalendar(cal);
                }catch(Exception e){

                }
            }

            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_diet, null);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle("Diet");

        cal = Calendar.getInstance();

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        tvDate = (TextView) v.findViewById(R.id.date);
        left = (ImageView) v.findViewById(R.id.left);
        right = (ImageView) v.findViewById(R.id.right);

        tvDate.setText(FormatUtil.getDate(cal));

        left.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cal.add(Calendar.DAY_OF_MONTH, -1);
                        tvDate.setText(FormatUtil.getDate(cal));
                        initData();
                    }
                }
        );

        right.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        tvDate.setText(FormatUtil.getDate(cal));
                        initData();
                    }
                }
        );

        tvDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker timePicker, int year, int month, int day) {
                                cal.set(year, month, day);
                                tvDate.setText(FormatUtil.getDate(cal));
                                initData();
                            }
                        },
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH),
                                cal.get(Calendar.DATE)).show();
                    }
                }
        );

        initData();

        return v;
    }

    public void initData(){
        new HttpUtil(HttpUtil.NORMAL_PARAMS)
                .add("uid", MyApplication.getPreferences(UserUtil.UID))
                .add("token",MyApplication.getPreferences(UserUtil.TOKEN))
                .add("startdate", FormatUtil.getDate(cal))
                .add("enddate", FormatUtil.getDate(cal))
                .get(Constant.URL_GETDIETHISTORY,callback);
    }
}
