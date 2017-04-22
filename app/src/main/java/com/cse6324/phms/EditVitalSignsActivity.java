package com.cse6324.phms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import com.cse6324.bean.UserBean;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.service.MyApplication;
import com.cse6324.util.UserUtil;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by tusha on 2/15/2017.
 */

public class EditVitalSignsActivity extends AppCompatActivity{
    Spinner spinner;
    EditText txtBloodPressure, txtBloodSugar, txtCholesterol, txtAge, txtHeight, txtWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_edit_vital_signs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Vital Signs");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView tvName = (TextView) findViewById(R.id.tv_name);
        tvName.setText(MyApplication.getPreferences(UserUtil.UNAME));

        txtBloodPressure = (EditText) findViewById(R.id.txtBloodPressure);
        txtBloodPressure.setText(MyApplication.getPreferences(UserUtil.BP));

        txtBloodSugar = (EditText) findViewById(R.id.txtBloodSugar);
        txtBloodSugar.setText(MyApplication.getPreferences(UserUtil.BSL));

        txtCholesterol = (EditText) findViewById(R.id.txtCholesterol);
        txtCholesterol.setText(MyApplication.getPreferences(UserUtil.CHOL));

        txtAge = (EditText) findViewById(R.id.txtAge);
        txtAge.setText(MyApplication.getPreferences(UserUtil.AGE));

        spinner = (Spinner) findViewById(R.id.spinner);

        txtHeight = (EditText) findViewById(R.id.txtHeight);
        txtHeight.setText(MyApplication.getPreferences(UserUtil.HEIGHT));

        txtWeight = (EditText) findViewById(R.id.txtWeight);
        txtWeight.setText(MyApplication.getPreferences(UserUtil.WEIGHT));
    }

    protected void saveVitalSignsInfo()
    {

        String bp = txtBloodPressure.getText().toString();
        String bsl = txtBloodSugar.getText().toString();
        String chol = txtCholesterol.getText().toString();
        String age = txtAge.getText().toString();
        String weight = txtWeight.getText().toString();
        String height = txtHeight.getText().toString();


        String g = spinner.getSelectedItem().toString();
        String gender;
        if(g.equals("Male")){
            gender = "1";
        }else
            gender = "0";

        new HttpUtil(HttpUtil.NORMAL_PARAMS)
                .add("uid",MyApplication.getPreferences(UserUtil.UID))
                .add("token",MyApplication.getPreferences(UserUtil.TOKEN))
                .add("gender",gender)
                .add("age",age)
                .add("weight",weight)
                .add("height",height)
                .add("bp",bp)
                .add("bsl",bsl)
                .add("chol",chol)
                .get(Constant.URL_VITALSIGNS, callback);
    }

    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            if(!response.isEmpty()) {
                String g = spinner.getSelectedItem().toString();
                String gender;
                if(g.equals("Male")){
                    gender = "1";
                }else
                    gender = "0";

                UserBean usrBean = UserUtil.getUserInfo();
                usrBean.setAge(txtAge.getText().toString());
                usrBean.setGender(gender);
                usrBean.setHeight(txtHeight.getText().toString());
                usrBean.setWeight(txtWeight.getText().toString());
                usrBean.setBp(txtBloodPressure.getText().toString());
                usrBean.setBsl(txtBloodSugar.getText().toString());
                usrBean.setChol(txtCholesterol.getText().toString());
                UserUtil.saveUserInfo(usrBean);
                Toast.makeText(MyApplication.getContext(),"Information updated successfully",Toast.LENGTH_LONG).show();

                //数据是使用Intent返回
                Intent intent = new Intent();
                //设置返回数据
                EditVitalSignsActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                EditVitalSignsActivity.this.finish();
            }
            else
                Toast.makeText(MyApplication.getContext(),headers.get("summary"),Toast.LENGTH_LONG).show();
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_save:
                saveVitalSignsInfo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束Activity&从栈中移除该Activity
        MyApplication.getInstance().finishActivity(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
