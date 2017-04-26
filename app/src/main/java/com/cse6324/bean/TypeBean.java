package com.cse6324.bean;

/**
 * Created by Jarvis on 2017/4/23.
 */

public class TypeBean {
    int type;
    int position;

    public TypeBean(int position, int type){
        this.type = type;
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
