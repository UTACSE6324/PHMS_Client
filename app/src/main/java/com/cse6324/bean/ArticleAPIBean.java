package com.cse6324.bean;

import java.util.List;

/**
 * Created by Jarvis on 2017/4/20.
 */

public class ArticleAPIBean {
    Response response;

    public class Response{
        List<ArticleBean> docs;

        public List<ArticleBean> getDocs() {
            return docs;
        }

        public void setDocs(List<ArticleBean> docs) {
            this.docs = docs;
        }
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
