package com.cse6324.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cse6324.phms.LoginActivity;
import com.cse6324.phms.R;
import com.cse6324.util.UserUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import com.cse6324.bean.UserBean;

import com.cse6324.util.FormatUtil;

/**
 * Created by Jarvis on 2017/2/10.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener{

    TextView cancel,submit;
    MaterialEditText etEmail,etPassword,confPassword,etUsername;

    UserBean bean;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, null);
        initViews(v);
        return v;
    }

    public void initViews(View v){
        cancel = (TextView) v.findViewById(R.id.btn_cancel);
        submit = (TextView) v.findViewById(R.id.btn_submit);

        etEmail = (MaterialEditText) v.findViewById(R.id.email) ;
        etUsername = (MaterialEditText) v.findViewById(R.id.username);
        etPassword = (MaterialEditText) v.findViewById(R.id.password);
        confPassword = (MaterialEditText) v.findViewById(R.id.password2);
        etPassword.setTypeface(Typeface.DEFAULT);
        etPassword.setTransformationMethod(new PasswordTransformationMethod());
        confPassword.setTypeface(Typeface.DEFAULT);
        confPassword.setTransformationMethod(new PasswordTransformationMethod());

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
                    UserBean bean = new UserBean();
                    bean.setEmail(etEmail.getText().toString());
                    bean.setName(etUsername.getText().toString());
                    bean.setPassword(etPassword.getText().toString());

                    UserUtil.saveUserInfo(bean);

                    ((LoginActivity) getActivity()).changeFragment(2);
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

        if(etUsername.getText().toString().isEmpty()) {
            bool = false;
            Toast.makeText(getContext(), "Username cannot be empty!", Toast.LENGTH_LONG).show();
        }

        if(etPassword.getText().toString().isEmpty()||etPassword.getText().toString().length()<8) {
            bool = false;
            Toast.makeText(getContext(), "Password cannot be empty!", Toast.LENGTH_LONG).show();
        }

        if(!etPassword.getText().toString().equals(confPassword.getText().toString())) {
            bool = false;
            Toast.makeText(getContext(), "Enter the password twice inconsistent!", Toast.LENGTH_LONG).show();
        }

        return bool;
    }
}
