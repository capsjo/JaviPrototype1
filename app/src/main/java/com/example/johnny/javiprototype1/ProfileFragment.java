package com.example.johnny.javiprototype1;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment implements DBRequestHandler {
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private Button editButton;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private ArrayList<String> restaurantImg = new ArrayList<>();
    private ArrayList<String> restaurantCity = new ArrayList<>();
    private ArrayList<String> restaurantName = new ArrayList<>();
    private ArrayList<String> restaurantUser = new ArrayList<>();
    private ArrayList<String> imgTitle = new ArrayList<>();
    private ArrayList<String> imgId = new ArrayList<>();
    private ArrayList<ImageItem> imageItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
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
        gridView = (GridView) v.findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(getContext(), R.layout.grid_item_layout, imageItems, imageLoader);
        gridView.setAdapter(gridAdapter);
        editButton = (Button) v.findViewById(R.id.button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userInfo = new Intent(getContext(), UserInfoActivity.class);
                startActivity(userInfo);
            }
        });

        TextView user_name = (TextView)v.findViewById(R.id.user_name);
        Intent intent = getActivity().getIntent();
        user_name.setText(intent.getStringExtra("username"));
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
                intent.putExtra("url", item.getUrl());
                intent.putExtra("restaurant", item.getRestaurant());
                intent.putExtra("city", item.getCity());
                intent.putExtra("username", item.getUsername());
                // intent.putExtra("image", item.getImage());

                //Start details activity
                startActivity(intent);
            }
        });
        sendRequest();
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

    private void getImg() {
        for (int i = 0; i < restaurantImg.size(); i++) {
            imageItems.add(new ImageItem(restaurantImg.get(i),
                    imgTitle.get(i)+imgId.get(i),
                    restaurantUser.get(i),
                    restaurantName.get(i),
                    restaurantCity.get(i)));
        }
    }

    @Override
    public void sendRequest() {
        Intent intent = getActivity().getIntent();
        String n = "showMostRecent=20";
        String user = "username="+intent.getStringExtra("username");
        String link = DBCmdLink.FETCH+"?"+n+"&"+user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonResponse = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                Log.d("JsonResponse", jsonResponse);
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse); // Convert string response to json response
                    JSONArray jsonImgArray = jsonObject.optJSONArray("recentImg");
                    for (int i = 0; i < jsonImgArray.length(); i++) {
                        restaurantImg.add(jsonImgArray.optJSONObject(i).optString("image"));
                        restaurantCity.add(jsonImgArray.optJSONObject(i).optString("city"));
                        restaurantName.add(jsonImgArray.optJSONObject(i).optString("restaurant"));
                        restaurantUser.add(jsonImgArray.optJSONObject(i).optString("username"));
                        imgTitle.add(jsonImgArray.optJSONObject(i).optString("name"));
                        imgId.add(jsonImgArray.optJSONObject(i).optString("id"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                Log.d("On response", response);
                getImg();
                gridAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("On error response", error.toString());
            }
        })
            /*@Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d("Sending", "content");
                Map<String, String> params = new HashMap<>();
                for (Map.Entry<String, String> entry : content.entrySet()) {
                    params.put(entry.getKey(), entry.getValue());
                }
                return params;
            }*/
                ;
        requestQueue.add(stringRequest);
    }
}
