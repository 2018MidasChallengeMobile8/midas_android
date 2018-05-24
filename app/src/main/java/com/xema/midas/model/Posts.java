package com.xema.midas.model;

public class Posts {
    String title;
    String text;
    String image;

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }

    public Posts(String title, String text, String image) {

        this.title = title;
        this.text = text;
        this.image = image;
    }
}
