package com.cse6324.phms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.cse6324.fragment.LoginFragment;
import com.cse6324.fragment.RegisterFragment;
import com.cse6324.fragment.ResetPasswordFragment;
import com.cse6324.fragment.SecurityQuestionFragment;
import com.cse6324.service.MyApplication;
import com.cse6324.util.UserUtil;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static cn.finalteam.toolsfinal.StringUtils.isEmpty;

public class LoginActivity extends AppCompatActivity {

    LoginFragment loginFragment;
    RegisterFragment registerFragment;
    SecurityQuestionFragment securityQuestionFragment;
    ResetPasswordFragment resetPasswordFragment;

    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!isEmpty(UserUtil.getUserInfo().getUid())) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();
        securityQuestionFragment = new SecurityQuestionFragment();
        resetPasswordFragment = new ResetPasswordFragment();

        changeFragment(0);
    }

    public void changeFragment(int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (currentFragment != null) {
            ft.hide(currentFragment);
        }

        Fragment fragment = null;

        switch (index) {
            case 0:
                fragment = loginFragment;
                break;
            case 1:
                fragment = registerFragment;
                break;
            case 2:
                fragment = securityQuestionFragment;
                break;
            case 3:
                fragment = resetPasswordFragment;
                break;
        }

        if (!fragment.isAdded()) {
            ft.add(R.id.frame_layout, fragment, fragment.getClass().getName());
        } else {
            ft.show(fragment);
        }

        currentFragment = fragment;

        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_none, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MyApplication.getInstance().AppExit();
        }
        return false;
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
