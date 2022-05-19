package com.example.work_4;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class item {
    private String text;
    private String color;

    public item() {
    }

    public item(String text, String color) {
        this.text = text;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
