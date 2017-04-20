package com.example.johnny.javiprototype1;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {
    private GridView gridView;
    private GridViewAdapter gridAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);

        gridView = (GridView) v.findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(getContext(), R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);
/*
        int nbImage = getData().size();
        nbImage = (nbImage/3) + 1;

        LinearLayout layout = (LinearLayout) v.findViewById(R.id.linear_grid);

        ViewGroup.LayoutParams params = layout.getLayoutParams();

        params.height = 2000;
        layout.setLayoutParams(params);
*/
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);

                //Create intent
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra("title", item.getTitle());

                // intent.putExtra("image", item.getImage());

                //Start details activity
                startActivity(intent);
            }
        });
        return v;
    }

    //A modifier lorsque database ready
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
        return imageItems;

    }
}
