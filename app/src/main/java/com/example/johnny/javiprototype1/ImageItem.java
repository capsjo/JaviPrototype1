package com.example.johnny.javiprototype1;

import android.graphics.Bitmap;

/**
 * Created by almamybouboucoulibaly on 2017-04-11.
 */

class ImageItem {

    private Bitmap image;
    private String title;
    private String url;
    private String restaurant;
    private String city;
    private String username;


    public ImageItem(Bitmap image, String title) {
        super();
        this.image = image;
        this.title = title;
    }
    public ImageItem(String url, String title) {
        super();
        this.title = title;
        this.url = url;
    }
    public ImageItem(String url, String title, String username, String restaurant, String city) {
        super();
        this.url = url;
        this.title = title;
        this.username = username;
        this.restaurant = restaurant;
        this.city = city;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getUrl() {
        return url;
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

    public String getRestaurant(){
        return restaurant;
    }

    public String getCity(){
        return city;
    }

    public String getUsername(){return username;}


}
