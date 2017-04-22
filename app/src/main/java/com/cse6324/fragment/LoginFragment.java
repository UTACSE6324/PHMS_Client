package com.cse6324.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cse6324.bean.UserBean;
import com.cse6324.phms.LoginActivity;
import com.cse6324.phms.MainActivity;
import com.cse6324.phms.R;
import com.cse6324.util.UserUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import okhttp3.Headers;
import okhttp3.Response;
import com.cse6324.util.FormatUtil;

/**
 * Created by Jarvis on 2017/2/10.
 */

public class LoginFragment extends Fragment implements View.OnClickListener{

    TextView register,login,resetpassword;
    MaterialEditText etEmail,etPassword;

    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {

            if(response.isEmpty()){
                Toast.makeText(getContext(),"Login fail",Toast.LENGTH_SHORT).show();
            }else{
                UserBean bean = JSON.parseObject(response, UserBean.class);
                UserUtil.saveUserInfo(bean);

                Toast.makeText(getContext(),"Login success",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                getContext().startActivity(intent);
                getActivity().finish();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, null);

        initViews(v);

        return v;
    }

    public void initViews(View v){
        register = (TextView) v.findViewById(R.id.btn_register);
        login = (TextView) v.findViewById(R.id.btn_login);
        resetpassword = (TextView) v.findViewById(R.id.btn_forgotpass);

        etEmail = (MaterialEditText) v.findViewById(R.id.email);
        etPassword = (MaterialEditText) v.findViewById(R.id.password);
        etPassword.setTypeface(Typeface.DEFAULT);
        etPassword.setTransformationMethod(new PasswordTransformationMethod());

        register.setOnClickListener(this);
        login.setOnClickListener(this);
        resetpassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_register:
                ((LoginActivity)getActivity()).changeFragment(1);
                break;
            case R.id.btn_login:
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if(!FormatUtil.checkEmail(email)){
                    Toast.makeText(getContext(),"Email invalid",Toast.LENGTH_SHORT).show();
                }else if(password.length() < 8){
                    Toast.makeText(getContext(),"Password invalid",Toast.LENGTH_SHORT).show();
                }else {
                    new HttpUtil(HttpUtil.NORMAL_PARAMS)
                            .add("email", email)
                            .add("password", password)
                            .get(Constant.URL_LOGIN, callback);
                }
                break;
            case R.id.btn_forgotpass:
                ((LoginActivity)getActivity()).changeFragment(3);
                break;
        }
    }
}
