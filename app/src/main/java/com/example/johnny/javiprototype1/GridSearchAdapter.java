package com.example.johnny.javiprototype1;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by almamybouboucoulibaly on 2017-04-08.
 */

class GridSearchAdapter extends ArrayAdapter implements Filterable{
    private Context context;
    private int layoutResourceId;
    private Filter imageFilter;
    private ArrayList data = new ArrayList();
    private ArrayList filteredData = new ArrayList();


    public GridSearchAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.filteredData = data;
    }

    public int getCount() {
        return filteredData.size();
    }

    public ImageItem getItem(int position) {
        return (ImageItem) filteredData.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }
            System.out.println("size" + filteredData.size());
            ImageItem item = (ImageItem) filteredData.get(position);
            holder.imageTitle.setText(item.getTitle());
            holder.image.setImageBitmap(item.getImage());


        return row;
    }


    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (imageFilter == null) {
            imageFilter = new ImageFilter();
        }

        return imageFilter;
    }

    private class ImageFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<ImageItem> tempList=new ArrayList<ImageItem>();
            //constraint is the result from text you want to filter against.
            //objects is your data set you will filter from
            if(constraint == null || constraint.length() == 0) {
                filterResults.values = data;
                filterResults.count = data.size();
            }
            else{
                int length=data.size();
                int i=0;
                while(i<length){
                    ImageItem item =(ImageItem) data.get(i);

                    if(item.getTitle().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        tempList.add(item);
                    }
                    else{}
                    i++;
                }
                //following two lines is very important
                //as publish result can only take FilterResults objects
                filterResults.values = tempList;
                filterResults.count = tempList.size();
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence contraint, FilterResults results) {
            if (results.count == 0) {
                notifyDataSetInvalidated();
            } else {
                filteredData = (ArrayList<ImageItem>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}