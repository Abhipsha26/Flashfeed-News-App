package com.example.newsapp;

public class NewsItem {
    private String newslink;
    private String imagelink;
    private String head;
    private String title;
    private String desc;

    public NewsItem() {
        // Default constructor required for Firebase
    }

    public NewsItem(String newslink, String imagelink, String head, String title, String desc) {
        this.newslink = newslink;
        this.imagelink = imagelink;
        this.head = head;
        this.title = title;
        this.desc = desc;
    }

    public String getNewslink() {
        return newslink;
    }

    public void setNewslink(String newslink) {
        this.newslink = newslink;
    }

    public String getImagelink() {
        return imagelink;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}