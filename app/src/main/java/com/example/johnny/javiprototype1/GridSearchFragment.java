package com.example.johnny.javiprototype1;


import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.johnny.javiprototype1.Cheeses.CHEESES;

/**
 * Created by Johnny on 3/30/2017.
 */

public class GridSearchFragment extends Fragment implements SearchView.OnQueryTextListener, DBRequestHandler{

    private SearchView mSearchView;
    private GridView mGridView;
    //private final String[] mStrings = CHEESES ;
    private HashMap<String, String> restaurantImg = new HashMap<>();
    private HashMap<String, String> content = new HashMap<>();
    private GridSearchAdapter gridSearchAdapter;
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private double longitude, latitude;
    private Location currentLocation, lastLocation;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private String searchType = "Uploader Name";

    @Nullable
    @Override
    //@SuppressWarnings({"MissingPermission"})
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location GPSlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = GPSlocation.getLongitude();
        latitude = GPSlocation.getLatitude();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30*1000, 10);
        if (GPSlocation != null) {
            lastLocation = GPSlocation;
        } else {
            Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (networkLocation != null) {
                lastLocation = networkLocation;
            } else {
                lastLocation = new Location("FIXED");
                lastLocation.setAltitude(1);
                lastLocation.setLatitude(45.5796580);
                lastLocation.setLongitude(-73.5446840);
                Log.i("loc","fixed");
            }
        }
        onLocationChanged(lastLocation);
        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);*/
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(10);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }
            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
        View v = inflater.inflate(R.layout.grid_search_fragment, container, false);
        mSearchView= (SearchView)v.findViewById(R.id.search_view);
        mGridView = (GridView) v.findViewById(R.id.grid_view);
        gridSearchAdapter = new GridSearchAdapter(getContext(), R.layout.grid_item_layout, imageItems, imageLoader, searchType);
        mGridView.setAdapter(gridSearchAdapter);
        mGridView.setTextFilterEnabled(true);
        setupSearchView();
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);

                //Create intent
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                /*intent.putExtra("title", item.getTitle());
                intent.putExtra("url", "http://www-ens.iro.umontreal.ca/~kounidol/1.jpg");
                intent.putExtra("username", "username");
                intent.putExtra("city", "city");*/

                // intent.putExtra("image", item.getImage());

                //Start details activity
                startActivity(intent);
            }
        });

        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchType = parent.getItemAtPosition(position).toString();
                gridSearchAdapter = new GridSearchAdapter(getContext(), R.layout.grid_item_layout, imageItems, imageLoader, searchType);
                mGridView.setAdapter(gridSearchAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Uploader Name");
        categories.add("Restaurant");
        categories.add("City");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        sendRequest();
        return v;
    }

    /*@Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        geomagneticField = new GeomagneticField((float)lastLocation.getLatitude(),(float)lastLocation.getLongitude(),(float)lastLocation.getAltitude(),System.currentTimeMillis());
    }*/

    @Override
    public void sendRequest() {
        String n = "showSearch=20";
        String link = DBCmdLink.FETCH+"?"+n;
        /*JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, DBCmdLink.FETCH, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonImgArray = response.optJSONArray("userimg");
                        for (int i = 0; i < jsonImgArray.length(); i++) {
                            restaurantImg.put(jsonImgArray.optJSONObject(i).optString("id"), jsonImgArray.optJSONObject(i).optString("image"));
                        }
                        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("On response", response.toString());
                        getSearchImg();
                        gridSearchAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("On error response", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("user-key", DBCmdLink.USER_KEY);
                return headers;
            }
        };*/
        StringRequest stringRequest = new StringRequest(Request.Method.GET, link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String jsonResponse = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1); // Fetch the json section from the string response
                    JSONObject jsonObject = new JSONObject(jsonResponse); // Convert string response to json response
                    JSONArray jsonImgArray = jsonObject.optJSONArray("userimg");
                    //restaurantImg.clear();
                    for (int i = 0; i < jsonImgArray.length(); i++) {
                        restaurantImg.put(jsonImgArray.optJSONObject(i).optString("name"), jsonImgArray.optJSONObject(i).optString("image"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                Log.d("On response", response);
                getImg();
                gridSearchAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("On error response", error.toString());
            }
        }) {
            // Columns targeted by the requests
            /*@Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                for (Map.Entry<String, String> entry : content.entrySet()) {
                    params.put(entry.getKey(), entry.getValue());
                }
                return params;
            }*/
            /*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("user-key", DBCmdLink.USER_KEY);
                return headers;
            }*/
        };
        requestQueue.add(stringRequest);
        //requestQueue.add(jsonObjectRequest);
    }
/*
    public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.search_fragment);

        mSearchView= (SearchView) findViewById(R.id.search_view);
        mListView= (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mStrings));
        mListView.setTextFilterEnabled(true);
        setupSearchView();
        }

*/

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mGridView.clearTextFilter();
        } else {
            mGridView.setFilterText(newText.toString());
        }
        return true;
    }

    private void getImg() {
        //final ArrayList<ImageItem> imageItems = new ArrayList<>();
        /*TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }*/
        //BitmapFactory.Options options;
        for (Map.Entry<String, String> entry : restaurantImg.entrySet()) {
            //Toast.makeText(getActivity(), entry.getValue(), Toast.LENGTH_SHORT).show();
            //imageItems.clear();
            imageItems.add(new ImageItem(entry.getValue(), entry.getKey()));
            //bitmap = BitmapConverter.getBitmapFromUrl(path);
            /*try {
                InputStream inputStream = new java.net.URL(path).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageItems.add(new ImageItem(bitmap, entry.getKey()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            /*try {
                bitmap = BitmapFactory.decodeFile(path);
            }
            catch (OutOfMemoryError outOfMemoryError) {
                try {
                    options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    bitmap = BitmapFactory.decodeFile(path, options);
                }
                catch (Exception exception) {
                }
            }*/
        }
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP1
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
