package com.example.myapplication_2;

import java.io.Serializable;

public class dataList implements Serializable {
    private final String name;
    private final double price;
    private String title;

    public dataList(String name_, double price_) {
        this.name = name_;
        this.price = price_;
    }

    public void setTitle(String title_) {
        this.title = title_;
    }

    public String getTitle() {
        return title;

    }

    public int getResourceId() {
        return 0;
    }
}
