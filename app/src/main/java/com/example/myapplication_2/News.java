package com.example.myapplication_2;

public class News {
    public String title;
    public int  picture;

    public News(String book_name, int book_picture ) {
        this.title = book_name;
        this.picture = book_picture;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
