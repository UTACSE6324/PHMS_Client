package com.cse6324.bean;

import java.io.Serializable;

/**
 * Created by Jarvis on 2017/2/25.
 */

public class DietBean implements Serializable{
    int dietid;
    int type;
    String date;
    String name;
    String quantity;
    String unit;
    String calorie;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public int getDietid() {
        return dietid;
    }

    public void setDietid(int dietid) {
        this.dietid = dietid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
