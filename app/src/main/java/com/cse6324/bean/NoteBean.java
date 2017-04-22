package com.cse6324.bean;

import java.io.Serializable;

/**
 * Created by Jarvis on 2017/4/9.
 */

public class NoteBean implements Serializable{
    String noteid;
    String type;
    String name;
    String date;
    String summary;

    public String getNoteid() {
        return noteid;
    }

    public void setNoteid(String noteid) {
        this.noteid = noteid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
