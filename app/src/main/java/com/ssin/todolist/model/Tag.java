package com.ssin.todolist.model;

/**
 * Created by SS-In on 2018-07-11.
 */

public class Tag {
    private String name;
    private int bgColor;
    private int txtColor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getTxtColor() {
        return txtColor;
    }

    public void setTxtColor(int txtColor) {
        this.txtColor = txtColor;
    }

    public Tag(){
        txtColor = -1;
        bgColor = -16777216;
    }
}
