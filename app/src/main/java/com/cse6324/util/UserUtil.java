package com.cse6324.util;

import com.cse6324.bean.UserBean;
import com.cse6324.service.MyApplication;

import static cn.finalteam.toolsfinal.StringUtils.isEmpty;

/**
 * Created by Jarvis on 2017/2/11.
 */

public class UserUtil {

    public final static String UID = "uid";
    public final static String TOKEN = "token";
    public final static String UNAME = "username";
    public final static String EMAIL = "email";
    public final static String PASSWORD = "password";
    public final static String GENDER = "gender";
    public final static String AGE = "age";
    public final static String WEIGHT = "weight";
    public final static String HEIGHT = "height";
    public final static String BP = "bp";
    public final static String BSL = "bsl";
    public final static String CHOL = "chol";
    public final static String CAL = "calorie";

    public static  UserBean getUserInfo(){
        UserBean bean = new UserBean();
        bean.setUid(MyApplication.getPreferences(UID));
        bean.setToken(MyApplication.getPreferences(TOKEN));
        bean.setName(MyApplication.getPreferences(UNAME));
        bean.setEmail(MyApplication.getPreferences(EMAIL));
        bean.setPassword(MyApplication.getPreferences(PASSWORD));
        bean.setGender(MyApplication.getPreferences(GENDER));
        bean.setAge(MyApplication.getPreferences(AGE));
        bean.setWeight(MyApplication.getPreferences(WEIGHT));
        bean.setHeight(MyApplication.getPreferences(HEIGHT));
        bean.setBp(MyApplication.getPreferences(BP));
        bean.setBsl(MyApplication.getPreferences(BSL));
        bean.setChol(MyApplication.getPreferences(CHOL));
        return bean;
    }

    public static void saveUserInfo(UserBean bean){
        if(bean!=null){
            if(!isEmpty(bean.getUid()))
                MyApplication.setStringPref(UID,bean.getUid());

            if(!isEmpty(bean.getToken()))
                MyApplication.setStringPref(TOKEN,bean.getToken());

            if(!isEmpty(bean.getName()))
                MyApplication.setStringPref(UNAME,bean.getName());

            if(!isEmpty(bean.getEmail()))
                MyApplication.setStringPref(EMAIL,bean.getEmail());

            if(!isEmpty(bean.getPassword()))
                MyApplication.setStringPref(PASSWORD,bean.getPassword());

            if(!isEmpty(bean.getGender()))
                MyApplication.setStringPref(GENDER,bean.getGender());

            if(!isEmpty(bean.getAge()))
                MyApplication.setStringPref(AGE,bean.getAge());

            if(!isEmpty(bean.getWeight()))
                MyApplication.setStringPref(WEIGHT,bean.getWeight());

            if(!isEmpty(bean.getHeight()))
                MyApplication.setStringPref(HEIGHT,bean.getHeight());

            if(!isEmpty(bean.getBp()))
                MyApplication.setStringPref(BP,bean.getBp());

            if(!isEmpty(bean.getBsl()))
                MyApplication.setStringPref(BSL,bean.getBsl());

            if(!isEmpty(bean.getChol()))
                MyApplication.setStringPref(CHOL,bean.getChol());

            if(!isEmpty(bean.getCalorie()))
                MyApplication.setStringPref(CAL, bean.getCalorie());

        }else{
            //Clear Cache
            MyApplication.setStringPref(UID,null);
            MyApplication.setStringPref(TOKEN,null);
            MyApplication.setStringPref(UNAME,null);
            MyApplication.setStringPref(PASSWORD,null);
            MyApplication.setStringPref(GENDER,null);
            MyApplication.setStringPref(AGE,null);
            MyApplication.setStringPref(WEIGHT,null);
            MyApplication.setStringPref(HEIGHT,null);
            MyApplication.setStringPref(BP,null);
            MyApplication.setStringPref(BSL,null);
            MyApplication.setStringPref(CHOL,null);
            MyApplication.setStringPref(CAL,null);
        }
    }
}

