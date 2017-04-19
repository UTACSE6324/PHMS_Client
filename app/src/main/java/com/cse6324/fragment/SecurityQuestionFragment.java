package com.cse6324.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cse6324.phms.LoginActivity;
import com.cse6324.phms.MainActivity;
import com.cse6324.phms.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import com.cse6324.bean.UserBean;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import okhttp3.Headers;
import okhttp3.Response;
import com.cse6324.service.MyApplication;
import com.cse6324.util.UserUtil;

/**
 * Created by Jarvis on 2017/2/10.
 */

public class SecurityQuestionFragment extends Fragment implements View.OnClickListener {

    TextView cancel, submit;
    MaterialEditText etAnswer;
    Spinner spinner;
    UserBean userBean;

    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            if(response.isEmpty()){
                Toast.makeText(MyApplication.getContext(),headers.get("summary"),Toast.LENGTH_LONG).show();
            }else {
                UserBean bean = JSON.parseObject(response, UserBean.class);
                UserUtil.saveUserInfo(bean);

                Intent intent = new Intent(getActivity(), MainActivity.class);
                getContext().startActivity(intent);
                getActivity().finish();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_security_question, null);
        initViews(v);
        return v;
    }

    public void initViews(View v) {
        cancel = (TextView) v.findViewById(R.id.btn_cancel);
        submit = (TextView) v.findViewById(R.id.btn_submit);

        etAnswer = (MaterialEditText) v.findViewById(R.id.answer);

        spinner = (Spinner) v.findViewById(R.id.spinner);

        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_cancel:
                ((LoginActivity) getActivity()).changeFragment(1);
                break;
            case R.id.btn_submit:
                if (etAnswer.getText().toString().isEmpty()) {
                    Toast.makeText(MyApplication.getContext(), "answer cannot be empty!", Toast.LENGTH_LONG).show();
                } else {

                    String security_question = spinner.getSelectedItem().toString();
                    String sq_answer = etAnswer.getText().toString();

                    new HttpUtil(HttpUtil.NON_TOKEN_PARAMS)
                            .add("email", MyApplication.getPreferences(UserUtil.EMAIL))
                            .add("name", MyApplication.getPreferences(UserUtil.UNAME))
                            .add("password", MyApplication.getPreferences(UserUtil.PASSWORD))
                            .add("sq", security_question)
                            .add("sqanswer", sq_answer)
                            .get(Constant.URL_REGISTER, callback);
                    break;
                }
        }
    }
}

