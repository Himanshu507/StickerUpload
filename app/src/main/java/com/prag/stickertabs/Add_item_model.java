package com.prag.stickertabs;

public class Add_item_model {

    String title, imagepath;

    public Add_item_model() {

    }

    public Add_item_model(String title,String imagepath) {
        this.title = title;
        this.imagepath = imagepath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
}
