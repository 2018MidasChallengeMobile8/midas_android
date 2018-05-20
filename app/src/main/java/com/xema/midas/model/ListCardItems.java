package com.xema.midas.model;

public class ListCardItems {
    String title;
    String content;
    String date;
    String im_url;


    public ListCardItems(String title, String content, String date, String im_url) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.im_url = im_url;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getIm_url() {
        return im_url;
    }

    public String getDate() {
        return date;
    }
}
