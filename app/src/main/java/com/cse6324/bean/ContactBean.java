package com.cse6324.bean;

import java.io.Serializable;

/**
 * Created by Jarvis on 2017/2/15.
 */

public class ContactBean implements Serializable {
    private String cid;
    private String name;
    private String phone;
    private String email;
    private String defaultc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDefaultc() {
        return defaultc;
    }

    public void setDefaultc(String defaultc) {
        this.defaultc = defaultc;
    }
}
