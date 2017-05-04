package com.example.johnny.javiprototype1;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, DBRequestHandler {

    private TextInputLayout usernameInput;
    private TextInputLayout passwordInput;
    private TextInputLayout passwordConfirmInput;
    private TextInputLayout firstName;
    private TextInputLayout lastName;
    private TextInputLayout country;
    private TextInputLayout province;
    private TextInputLayout city;
    private HashMap<String, String> content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        /*ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                1);*/

        usernameInput = (TextInputLayout) findViewById(R.id.username_selection);
        passwordInput = (TextInputLayout) findViewById(R.id.password_selection);
        passwordConfirmInput = (TextInputLayout) findViewById(R.id.password_comfirm);

        firstName = (TextInputLayout) findViewById(R.id.first_name);
        lastName = (TextInputLayout) findViewById(R.id.last_name);
        country = (TextInputLayout) findViewById(R.id.country);
        province = (TextInputLayout) findViewById(R.id.province);
        city = (TextInputLayout) findViewById(R.id.city);

        Button signupButton = (Button) findViewById(R.id.signup_button);
        signupButton.setOnClickListener(this);
        ImageButton cancel = (ImageButton) findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        String user = usernameInput.getEditText().getText().toString();
        String password = passwordInput.getEditText().getText().toString();
        String passConfirm = passwordConfirmInput.getEditText().getText().toString();

        String UserFirstName = firstName.getEditText().getText().toString();
        String UserLastName = lastName.getEditText().getText().toString();
        String UserCountry = country.getEditText().getText().toString();
        String UserProvince = province.getEditText().getText().toString();
        String UserCity = city.getEditText().getText().toString();

        // User's information to submit.
        content = new HashMap<>();
        content.put("username", user);
        content.put("password", password);
        content.put("last_name", UserLastName);
        content.put("first_name", UserFirstName);
        content.put("city", UserCity);
        content.put("province", UserProvince);
        content.put("country", UserCountry);
        if (password.equals(passConfirm)) { // If the password match the password confirmation, send a request to the database.
            sendRequest();
        }
        else { // Else send a warning to the user that the password does not match the password confirmation.
            Toast.makeText(getApplicationContext(), "Password does not match with the password confirmation", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void sendRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBCmdLink.INSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonResponse = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    Boolean success = jsonObject.optBoolean("success");
                    String message = jsonObject.optString("message");
                    if (success) { // If the account was successfully created, send a message to the user.
                        /*LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view = layoutInflater.inflate(R.layout.profile_fragment, null);
                        TextView user_name = (TextView)view.findViewById(R.id.user_name);
                        user_name.setText(user);*/
                        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("On response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("On error response", error.toString());
            }
        }) {
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
}



