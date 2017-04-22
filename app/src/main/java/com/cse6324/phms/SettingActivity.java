package com.cse6324.phms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.service.MyApplication;
import com.cse6324.util.UserUtil;
import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;

import java.io.Serializable;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Jarvis on 2017/2/12.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout rlLogout;
    Switch pinNotify;

    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            Toast.makeText(SettingActivity.this, headers.get("summary"), Toast.LENGTH_SHORT).show();
            MyApplication.setStringPref(UserUtil.NOTIFY, (pinNotify.isChecked() ? 1 : 0) + "");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.back);

        rlLogout = (RelativeLayout) findViewById(R.id.rl_logout);
        rlLogout.setOnClickListener(this);

        pinNotify = (Switch) findViewById(R.id.pin_notify);

        if (UserUtil.getUserInfo().getNotify().equals("1")) {
            pinNotify.setChecked(true);
        }

        pinNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                String notify;
                if (isChecked)
                    notify = "1";
                else
                    notify = "0";

                new HttpUtil(HttpUtil.NORMAL_PARAMS)
                        .add("uid", MyApplication.getPreferences(UserUtil.UID))
                        .add("token", MyApplication.getPreferences(UserUtil.TOKEN))
                        .add("notify", notify)
                        .get(Constant.URL_SETNOTIFY, callback);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_none, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_logout:
                UserUtil.saveUserInfo(null);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                MyApplication.getInstance().finishMainActivity();
                finish();
                break;
        }
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
