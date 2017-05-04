package com.example.johnny.javiprototype1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

/**
 * Created by almamybouboucoulibaly on 2017-04-12.
 */

public class DetailsActivity extends AppCompatActivity{
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(20);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }
            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");
        String username = getIntent().getStringExtra("username");
        String city = getIntent().getStringExtra("city");
        String restaurant = getIntent().getStringExtra("restaurant");
        Bitmap bitmap;
        BitmapFactory.Options options;
        bitmap = BitmapFactory.decodeFile(url);
        System.out.println(url);
        /*try {
            bitmap = BitmapFactory.decodeFile(url);
        }
        catch (OutOfMemoryError outOfMemoryError) {
            try {
                options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                bitmap = BitmapFactory.decodeFile(url, options);
            }
            catch (Exception exception) {
            }
        }*/


        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);

        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.image);
        imageView.setImageUrl(url, imageLoader);
    }
}
