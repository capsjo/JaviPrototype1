package com.example.johnny.javiprototype1;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.drawable.BitmapDrawable;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.RatingBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Map;

        import static android.app.Activity.RESULT_OK;

/**
 * Created by Idole Koun on 2017-04-07.
 */

public class UploadFragment extends Fragment implements View.OnClickListener, DBRequestHandler {
    private static final int SELECT_PICTURE = 1;
    private ImageView inputPhotoView;
    private EditText inputTitleView, inputDescView;
    private RatingBar inputRatingView;
    private ImageButton captureButton, searchButton, uploadButton, cancelButton;
    private HashMap<String, String> params;
    private ArrayList<Bitmap> selectedImages;
    private PhotoAdapter photoAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View uploadView = inflater.inflate(R.layout.upload_fragment, container, false);

        TextView titleView = (TextView)uploadView.findViewById(R.id.titleView);
        TextView descView = (TextView)uploadView.findViewById(R.id.descView) ;

        inputTitleView = (EditText)uploadView.findViewById(R.id.inputTitleView);
        inputPhotoView = (ImageView)uploadView.findViewById(R.id.inputPhotoView);
        inputDescView = (EditText)uploadView.findViewById(R.id.inputDescView);
        inputRatingView = (RatingBar)uploadView.findViewById(R.id.inputRatingView);

        captureButton = (ImageButton)uploadView.findViewById(R.id.captureButton);
        searchButton = (ImageButton)uploadView.findViewById(R.id.searchButton);
        uploadButton = (ImageButton)uploadView.findViewById(R.id.uploadButton);
        cancelButton = (ImageButton)uploadView.findViewById(R.id.cancelButton);

        captureButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        return uploadView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.captureButton:
                break;
            case R.id.searchButton:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), SELECT_PICTURE);
                break;
            case R.id.uploadButton:
                String strTitle = inputTitleView.getText().toString();
                String strPhoto = BitmapConverter.bitmapToString(((BitmapDrawable)inputPhotoView.getDrawable()).getBitmap());
                String strDesc = inputDescView.getText().toString();
                String strRating = Float.toString(inputRatingView.getRating());
                params = new HashMap<>();
                params.put("photo_title", strTitle);
                params.put("image", strPhoto);
                params.put("rating", strRating);
               // sendRequest(); // Send request to insert a row in the table 'photos'
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
        }
    }

    @Override
    public void sendRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBCmdLink.INSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String jsonResponse = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1); // Fetch the json section from the string response
                    JSONObject jsonObject = new JSONObject(jsonResponse); // Convert string response to json response
                    boolean success = jsonObject.optBoolean("success"); // Fetch the boolean value from the 'success' key
                    Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                    Log.d("On response", response);
                    // Personalized response

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
                /*Map<String, String> params = new HashMap<>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    params.put(entry.getKey(), entry.getValue());
                }*/
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}