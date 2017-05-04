package com.example.johnny.javiprototype1;

/////////////////////////////////////////IMPORTANT//////////////////////////////////////////
/////////////L'application ne s'auto launch pas sur emulateur(launch sur phone)/////////////
import android.Manifest;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DBRequestHandler{

    private TextInputLayout usernameInput;
    private TextInputLayout passwordInput;
    private Button signinButton;
    private Button createAccountButton;
    private RequestQueue requestQueue;
    private String user, password;
    //private Boolean success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameInput = (TextInputLayout) findViewById(R.id.user_layout);
        passwordInput = (TextInputLayout) findViewById(R.id.password_layout);

        signinButton = (Button) findViewById(R.id.signin_button);
        signinButton.setOnClickListener(this);

        createAccountButton = (Button) findViewById(R.id.create_button);
        createAccountButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_button:
                user = usernameInput.getEditText().getText().toString();
                password = passwordInput.getEditText().getText().toString();
                sendRequest();
                /*if (authent(user, password)) {
                    Intent intent = new Intent(this, MemberPage.class);
                    intent.putExtra("key", "info to put");
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
                }*/
                break;
            case R.id.create_button:
                Intent signinPage = new Intent(this, SignUpActivity.class);
                startActivity(signinPage);
                break;
        }
    }

    /*public Boolean authent(String user, String password){
        sendRequest();
        //To complete
        if(success){
            if(true){
                return true;
            }
            else return false;
        }
        else return false;

    }*/

    @Override
    public void sendRequest() {
        String u = "username="+user;
        String p = "password="+password;
        String link = DBCmdLink.FETCH+"?"+u+"&"+p;
        requestQueue = Volley.newRequestQueue(MainActivity.this.getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonResponse = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    Boolean success = jsonObject.optBoolean("success");
                    String message = jsonObject.optString("message");
                    if (success) {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, MemberPage.class);
                        intent.putExtra("username", user);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                Log.d("On response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
        };
        requestQueue.add(stringRequest);
    }
}
