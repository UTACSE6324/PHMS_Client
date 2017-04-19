package com.cse6324.fragment;

//import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cse6324.phms.LoginActivity;
//import com.cse6324.phms.MainActivity;
import com.cse6324.phms.R;
import com.cse6324.util.FormatUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

//import com.cse6324.bean.UserBean;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import okhttp3.Headers;
import okhttp3.Response;
import com.cse6324.service.MyApplication;
//import com.cse6324.util.UserUtil;

/**
 * Created by gaolin on 2/13/17.
 */

public class ResetPasswordFragment extends Fragment implements View.OnClickListener{

    TextView cancel, submit;
    MaterialEditText etAnswer,etEmail,etNewPassword,etConfNewPassword;
    Spinner spinner;

    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            if(response.isEmpty()) {
                Toast.makeText(MyApplication.getContext(), headers.get("summary"), Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(MyApplication.getContext(), "Success", Toast.LENGTH_LONG).show();
                ((LoginActivity) getActivity()).changeFragment(0);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reset_password, null);
        initViews(v);
        return v;
    }

    public void initViews(View v) {
        cancel = (TextView) v.findViewById(R.id.btn_cancel);
        submit = (TextView) v.findViewById(R.id.btn_submit);
        etEmail = (MaterialEditText) v.findViewById(R.id.email);
        etAnswer = (MaterialEditText) v.findViewById(R.id.answer);

        spinner = (Spinner) v.findViewById(R.id.spinner);
        etNewPassword = (MaterialEditText) v.findViewById(R.id.password);
        etConfNewPassword = (MaterialEditText) v.findViewById(R.id.password2);

        etNewPassword.setTypeface(Typeface.DEFAULT);
        etNewPassword.setTransformationMethod(new PasswordTransformationMethod());
        etConfNewPassword.setTypeface(Typeface.DEFAULT);
        etConfNewPassword.setTransformationMethod(new PasswordTransformationMethod());

        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_cancel:
                ((LoginActivity)getActivity()).changeFragment(0);
                break;
            case R.id.btn_submit:
                if(isValid()) {
                    String security_question = spinner.getSelectedItem().toString();
                    String sq_answer = etAnswer.getText().toString();

                    new HttpUtil(HttpUtil.NORMAL_PARAMS)
                            .add("email", etEmail.getText().toString())
                            .add("password", etNewPassword.getText().toString())
                            .add("sq",security_question)
                            .add("sqanswer",sq_answer)
                            .get(Constant.URL_RESETPASSWORD, callback);
                }
                break;
        }
    }

    public boolean isValid(){

        boolean bool = true;
        if(etEmail.getText().toString().isEmpty()) {
            bool = false;
            Toast.makeText(getContext(), "Email cannot be empty!", Toast.LENGTH_LONG).show();
        }


        if(!FormatUtil.checkEmail(etEmail.getText().toString())) {
            bool = false;
            Toast.makeText(getContext(), "Please enter valid email!", Toast.LENGTH_LONG).show();
        }

        if(etAnswer.getText().toString().isEmpty()) {
            bool = false;
            Toast.makeText(getContext(), "Answer cannot be empty!", Toast.LENGTH_LONG).show();
        }

        if(etNewPassword.getText().toString().isEmpty()||etConfNewPassword.getText().toString().isEmpty()) {
            bool = false;
            Toast.makeText(getContext(), "Password cannot be empty!", Toast.LENGTH_LONG).show();
        }

        if(etNewPassword.getText().toString().length()<8||etConfNewPassword.getText().toString().length()<8) {
            bool = false;
            Toast.makeText(getContext(), "The password length must be greater than 8 digits! ", Toast.LENGTH_LONG).show();
        }

        if(!etNewPassword.getText().toString().equals(etConfNewPassword.getText().toString())) {
            bool = false;
            Toast.makeText(getContext(), "Enter the password twice inconsistent!", Toast.LENGTH_LONG).show();
        }

        return bool;
    }

}
