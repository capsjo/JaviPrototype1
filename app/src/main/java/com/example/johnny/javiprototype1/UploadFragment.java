package com.example.johnny.javiprototype1;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.Matrix;
        import android.graphics.drawable.BitmapDrawable;
        import android.net.Uri;
        import android.os.Bundle;
        import android.provider.MediaStore;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.RatingBar;
        import android.widget.SearchView;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

        import static android.app.Activity.RESULT_OK;
        import static com.example.johnny.javiprototype1.Cheeses.CHEESES;

/**
 * Created by Idole Koun on 2017-04-07.
 */

public class UploadFragment extends Fragment implements View.OnClickListener, DBRequestHandler {
    private static final int SELECT_PICTURE = 1;
    private ImageView inputPhotoView;
    private EditText inputTitleView, inputDescView;
    private RatingBar inputRatingView;
    private ImageButton captureButton, searchButton, cancelButton;
    private Button uploadButton;
    private ArrayList<Bitmap> selectedImages;
    private PhotoAdapter photoAdapter;
    private HashMap<String, String> content;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    private SearchView mSearchView1, mSearchView2;
    private ListView mListView1, mListView2;
    private ArrayList<String> restaurants;
    private ArrayList<String> cities;
    private List<String> categories = new ArrayList<String>();
    private List<String> categories2 = new ArrayList<String>();
    private Spinner spinner, spinner2;
    private String restaurant, city;
    private ArrayAdapter<String> dataAdapter, dataAdapter2;
    private RequestQueue requestQueue;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        restaurants = new ArrayList<>();
        cities = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, DBCmdLink.SEARCH, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = response.optJSONArray("restaurants");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            restaurants.add(i, jsonArray.optJSONObject(i).optJSONObject("restaurant").optString("name"));
                        }
                        //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("On response", response.toString());
                        dataAdapter.notifyDataSetChanged();
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
        requestQueue.add(jsonObjectRequest);

        View uploadView = inflater.inflate(R.layout.upload_fragment, container, false);

        TextView titleView = (TextView)uploadView.findViewById(R.id.titleView);
        TextView descView = (TextView)uploadView.findViewById(R.id.descView) ;

        inputTitleView = (EditText)uploadView.findViewById(R.id.inputTitleView);
        inputPhotoView = (ImageView)uploadView.findViewById(R.id.inputPhotoView);
        inputDescView = (EditText)uploadView.findViewById(R.id.inputDescView);
        //inputRatingView = (RatingBar)uploadView.findViewById(R.id.inputRatingView);

        captureButton = (ImageButton)uploadView.findViewById(R.id.captureButton);
        searchButton = (ImageButton)uploadView.findViewById(R.id.searchButton);
        uploadButton = (Button)uploadView.findViewById(R.id.uploadButton);
        cancelButton = (ImageButton)uploadView.findViewById(R.id.cancelButton);

        captureButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        mSearchView1= (SearchView)uploadView.findViewById(R.id.search_view1);
        //mListView1= (ListView) uploadView.findViewById(R.id.list_view1);
        //     mListView1.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,mStrings));

        //  mListView1.setTextFilterEnabled(true);

        mSearchView2= (SearchView)uploadView.findViewById(R.id.search_view2);
        //mListView2= (ListView) uploadView.findViewById(R.id.list_view2);
        // mListView2.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,mStrings));

        // mListView2.setTextFilterEnabled(true);
        setupSearchView();

        spinner = (Spinner) uploadView.findViewById(R.id.spinner);
        spinner2 = (Spinner) uploadView.findViewById(R.id.spinner2);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                restaurant = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);
        dataAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories2);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner2.setAdapter(dataAdapter2);

        return uploadView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.captureButton:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                break;
            case R.id.searchButton:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), SELECT_PICTURE);
                break;
            case R.id.uploadButton:
                Intent mIntent = getActivity().getIntent();
                String strTitle = inputTitleView.getText().toString();
                String strPhoto = BitmapConverter.bitmapToString(((BitmapDrawable)inputPhotoView.getDrawable()).getBitmap());
                //strPhoto = strPhoto.replace("\n","");
                String strDesc = inputDescView.getText().toString();
                //String strRating = Float.toString(inputRatingView.getRating());
                content = new HashMap<>();
                /*content.put("txtdescription", strDesc);
                content.put("txtnomrestau", strTitle);
                content.put("txtnomville", "Montreal");*/
                content.put("name", strTitle);
                content.put("image", strPhoto);
                content.put("username", mIntent.getStringExtra("username"));
                content.put("description", strDesc);
                content.put("restaurant_name", restaurant);
                content.put("city", city);
                sendRequest(); // Send request to insert a row in the table 'photos'
                break;
            case R.id.cancelButton:
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PICTURE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    inputPhotoView.setImageURI(selectedImageUri);
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK)
                {
                    // Dans le cas de la caméra, on sait qu'il existe une image
                    // de type Bitmap dans l'extra "data"
                    Bundle extras = data.getExtras();
                    Bitmap img = (Bitmap)extras.get("data");
                    img = getResizedBitmap(img, 650, 500);
                    inputPhotoView.setImageBitmap(img);
                }
                break;

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

    @Override
    public void sendRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBCmdLink.INSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /*try {
                    String jsonResponse = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1); // Fetch the json section from the string response
                    JSONObject jsonObject = new JSONObject(jsonResponse); // Convert string response to json response
                    //boolean success = jsonObject.optBoolean("success"); // Fetch the boolean value from the 'success' key
                    // Personalized response
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                Log.d("On response", response);
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
        };
        requestQueue.add(stringRequest);
    }

    private void setupSearchView() {
        mSearchView1.setIconifiedByDefault(false);
        mSearchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                categories = new ArrayList<String>();
                for(int i = 0; i < restaurants.size(); i++){
                    if (restaurants.get(i).toUpperCase().contains(query.toUpperCase())) {
                        categories.add(restaurants.get(i));
                    } else {
                    }
                }
                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spinner.setAdapter(dataAdapter);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        mSearchView1.setSubmitButtonEnabled(true);
        mSearchView1.setQueryHint("Search For Restaurant");

        mSearchView2.setIconifiedByDefault(false);
        mSearchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                categories2 = new ArrayList<String>();
                for(int i = 0; i < cities.size(); i++){
                    if (cities.get(i).toUpperCase().contains(query.toUpperCase())) {
                        categories2.add(cities.get(i));
                    } else {
                    }
                }
                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories2);

                // Drop down layout style - list view with radio button
                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spinner2.setAdapter(dataAdapter2);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        mSearchView2.setSubmitButtonEnabled(true);
        mSearchView2.setQueryHint("Search For City");
    }

}