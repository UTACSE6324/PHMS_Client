package com.cse6324.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jarvis on 2017/4/20.
 */

public class ArticleBean implements Serializable{
    String web_url;
    String lead_paragraph;
    String pub_date;
    String source;
    Headline headline;
    List<ImgBean> multimedia;

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getLead_paragraph() {
        return lead_paragraph;
    }

    public void setLead_paragraph(String lead_paragraph) {
        this.lead_paragraph = lead_paragraph;
    }

    public String getPub_date() {
        return pub_date;
    }

    public void setPub_date(String pub_date) {
        this.pub_date = pub_date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Headline getHeadline() {
        return headline;
    }

    public void setHeadline(Headline headline) {
        this.headline = headline;
    }

    public List<ImgBean> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<ImgBean> multimedia) {
        this.multimedia = multimedia;
    }

    public class Headline implements Serializable{
        String main;

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }
    }

    public class ImgBean implements Serializable{
        String url;
        String subtype;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSubtype() {
            return subtype;
        }

        public void setSubtype(String subtype) {
            this.subtype = subtype;
        }
    }
}
