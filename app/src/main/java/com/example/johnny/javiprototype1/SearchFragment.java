package com.example.johnny.javiprototype1;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.johnny.javiprototype1.Cheeses.CHEESES;

/**
 * Created by Johnny on 3/30/2017.
 */

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener, DBRequestHandler{

    private SearchView mSearchView;
    private ListView mListView;
    private ArrayList<String> restaurantName;
    private ArrayAdapter arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_fragment, container, false);
        mSearchView= (SearchView)v.findViewById(R.id.search_view);
        mListView= (ListView) v.findViewById(R.id.list_view);
        restaurantName = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,restaurantName);
        mListView.setAdapter(arrayAdapter);
        mListView.setTextFilterEnabled(true);
        setupSearchView();
        return v;
    }

    @Override
    public void sendRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, DBCmdLink.SEARCH, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonRestaurants = response.optJSONArray("restaurants");
                            for (int i = 0; i < jsonRestaurants.length(); i++) {
                                restaurantName.add(i, jsonRestaurants.optJSONObject(i).optJSONObject("restaurant").getString("name"));
                            }
                            //Toast.makeText(getActivity(), "Connected to zomato API", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("On response", response.toString());
                        arrayAdapter.notifyDataSetChanged();
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
        };
        /*StringRequest stringRequest = new StringRequest(Request.Method.GET, DBCmdLink.SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String jsonResponse = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1); // Fetch the json section from the string response
                    JSONObject jsonObject = new JSONObject(response); // Convert string response to json response
                    JSONArray jsonRestaurants = jsonObject.getJSONArray("restaurants");
                    restaurantsNames = new String[jsonRestaurants.length()];
                    for (int i = 0; i < jsonRestaurants.length(); i++) {
                        restaurantsNames[i] = (jsonRestaurants.getJSONObject(i)).getString("name");
                    }
                    Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                    Log.d("On response", response);
                    Toast.makeText(getActivity(), "Connected to zomato API", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("On error response", error.toString());
            }
        }) {
            // Columns targeted by the requests
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                for (Map.Entry<String, String> entry : content.entrySet()) {
                    params.put(entry.getKey(), entry.getValue());
                }
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("user-key", DBCmdLink.USER_KEY);
                return headers;
            }
        };*/
        //requestQueue.add(stringRequest);
        requestQueue.add(jsonObjectRequest);
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
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText.toString());
        }
        return true;
    }
}
