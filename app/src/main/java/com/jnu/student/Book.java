package com.jnu.student;

import java.io.Serializable;

public class Book implements Serializable {
    private int coverResourceId;
    private String title;

    public Book(String title, int coverResourceId) {
        this.title = title;
        this.coverResourceId = coverResourceId;
    }

    public int getCoverResourceId() {
        return coverResourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setCoverResourceId(int coverResourceId) {
        this.coverResourceId = coverResourceId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

