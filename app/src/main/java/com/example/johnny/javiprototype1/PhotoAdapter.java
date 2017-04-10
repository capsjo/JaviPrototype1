package com.example.johnny.javiprototype1;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class PhotoAdapter extends ArrayAdapter<Bitmap> {
    private Context context;
    private ArrayList<Bitmap> photosList;

    public PhotoAdapter(Context context, int textViewResourceId, ArrayList<Bitmap> images) {
        super(context, textViewResourceId, images);
        this.context = context;
        photosList = images;
    }

    public int getCount() { return photosList.size(); }

    public Bitmap getItem(int position) {
        return photosList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
        }
        else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(photosList.get(position));
        imageView.setId(position);
        return imageView;
    }
}