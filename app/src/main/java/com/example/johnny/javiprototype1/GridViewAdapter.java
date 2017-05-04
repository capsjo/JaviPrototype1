package com.example.johnny.javiprototype1;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by almamybouboucoulibaly on 2017-04-08.
 */

class GridViewAdapter extends ArrayAdapter implements Filterable{
    private Context context;
    private int layoutResourceId;
    private ArrayList data = new ArrayList();
    private ImageLoader imageLoader;


    public GridViewAdapter(Context context, int layoutResourceId, ArrayList data, ImageLoader imageLoader) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.imageLoader = imageLoader;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (NetworkImageView) row.findViewById(R.id.image);
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }
        ImageItem item = (ImageItem) data.get(position);
        holder.imageTitle.setText(item.getTitle());
        holder.image.setImageUrl(item.getUrl(), imageLoader);
        return row;
    }


    static class ViewHolder {
        TextView imageTitle;
        //ImageView image;
        NetworkImageView image;
    }
}