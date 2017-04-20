package com.example.johnny.javiprototype1;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private ViewPager imagePager;
    private ImageSlideAdapter imagePagerAdapter;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);

        gridView = (GridView) v.findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(getContext(), R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);

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

        imagePager = (ViewPager) v.findViewById(R.id.image_pager);
        imagePagerAdapter = new ImageSlideAdapter(getContext());
        imagePager.setAdapter(imagePagerAdapter);


        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (imagePager.getCurrentItem() == 4) {
                    imagePager.setCurrentItem(0, true);
                }
                else {
                    imagePager.setCurrentItem(imagePager.getCurrentItem() + 1, true);
                }
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 6000, 6000);
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