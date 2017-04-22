package com.cse6324.http;

/**
 * Name: Constant
 * Description: Used for Http request.
 * Created by Jarvis on 2017/2/5.
 */


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.FileWrapper;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class HttpUtil {

    /*
    * decide the header of request
    * NORMAL_PARAMS: normal one
    * FILE_PARAMS: from request
    * NON_TOKEN_PARAMS; request without user token
    * */
    public final static int NORMAL_PARAMS = 0;
    public final static int FILE_PARAMS = 1;
    public final static int NON_TOKEN_PARAMS = 2;

    private RequestParams params;

    public HttpUtil(int type){
        params = new RequestParams();
        switch(type){
            case FILE_PARAMS:
                params.addHeader("Content-Type", "multipart/form-data");
                break;
            case NON_TOKEN_PARAMS:
                params.addHeader("Content-Type", "application/x-www-form-urlencoded");
                break;
            default:
                params.addHeader("Content-Type", "application/json");
                break;
        }
    }

    public HttpUtil addHead(String key, String value){
        params.addHeader(key , value);
        return this;
    }

    public HttpUtil add(String key, String value){
        params.addFormDataPart(key,value);
        return this;
    }

    public HttpUtil addJSON(JSONObject value){
        params.applicationJson(value);
        return this;
    }

    /*
    * addBody used for post file
    * */
    public HttpUtil addBody(File file){
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MediaType type = MediaType.parse("multipart/form-data");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(type)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"Filedata\"; filename=\""+file.getName()+"\""), fileBody)
                .build();

        params.setCustomRequestBody(requestBody);
        return this;
    }

    public void post(String url, BaseHttpRequestCallback callback){
        HttpRequest.post(url,params,callback);
    }

    public void get(String url, BaseHttpRequestCallback callback){
        HttpRequest.get(url,params,callback);
    }
}
