package com.cse6324.service;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.coolerfall.daemon.Daemon;
import com.cse6324.phms.MainActivity;
import com.cse6324.phms.R;

import java.util.Stack;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;
import okhttp3.Headers;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Jarvis on 2017/2/6.
 */

public class MyApplication extends Application {

    public static final String APPLICATION_NAME = "PHMS";

    private static Context context;
    public static SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();

        Daemon.run(this,
                MyApplication.class, Daemon.INTERVAL_ONE_MINUTE);
        singleton=this;
        context = this;

        preferences = getSharedPreferences(
                APPLICATION_NAME, Context.MODE_PRIVATE);
        initHttpUtil();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/GothamRnd-Light.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public void initHttpUtil(){
        OkHttpFinalConfiguration.Builder builder =
                new OkHttpFinalConfiguration.Builder();

        //common http header
        Headers commonHeaders = new Headers.Builder().build();
        builder.setCommenHeaders(commonHeaders);

        OkHttpFinal.getInstance().init(builder.build());
    }

    public static SharedPreferences getPreferences(){
        return preferences;
    }

    public static String getPreferences(String key){
        return preferences.getString(key,"");
    }

    public static void setStringPref(String key, String value){
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString(key, value);
        ed.commit();
    }

    public static Context getContext(){
        return context;
    }


    private static Stack<Activity> activityStack;
    private static MyApplication singleton;

    // Returns the application instance
    public static MyApplication getInstance() {
        return singleton;
    }

    public void addActivity(Activity activity){
        if(activityStack ==null){
            activityStack =new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public void AppExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
        }
    }

    public void finishMainActivity(){
        for(int i = 0; i < activityStack.size(); i++){
            Activity activity = activityStack.elementAt(i);
            if(activity instanceof MainActivity){
                activity.finish();
            }
        }
    }
}
