package com.example.johnny.javiprototype1;

import android.Manifest;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputLayout usernameInput;
    private TextInputLayout passwordInput;
    private Button signinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                1);
*/

        usernameInput = (TextInputLayout) findViewById(R.id.user_layout);
        passwordInput = (TextInputLayout) findViewById(R.id.password_layout);

        signinButton = (Button) findViewById(R.id.signin_button);
        signinButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String user = usernameInput.getEditText().getText().toString();
        String password = passwordInput.getEditText().getText().toString();
        if(authent(user, password)){
            Intent intent = new Intent(this, MemberPage.class);
            intent.putExtra("key", "info to put");
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
        }

    }

    public Boolean authent(String user, String password){
        //To complete
        if(true){
            if(true){
                return true;
            }
            else return false;
        }
        else return false;

    }
}
