package com.example.johnny.javiprototype1;

import android.graphics.Bitmap;

/**
 * Created by almamybouboucoulibaly on 2017-04-11.
 */

class ImageItem {

    private Bitmap image;
    private String title;
    private String description;
    private String rating;

    public ImageItem(Bitmap image, String title, String description, String rating) {
        super();
        this.image = image;
        this.title = title;
        this.description = description;
        this.rating = rating;
    }
    public ImageItem(Bitmap image, String title) {
        super();
        this.image = image;
        this.title = title;
        this.description = "";
        this.rating = "";
    }
    public String getDescription(){
        return description;
    }
    public String getRating(){
        return rating;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
