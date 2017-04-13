package com.example.johnny.javiprototype1;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputLayout usernameInput;
    private TextInputLayout passwordInput;
    private TextInputLayout passwordConfirmInput;
    private Button signupButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
/*
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                1);
*/

        usernameInput = (TextInputLayout) findViewById(R.id.username_selection);
        passwordInput = (TextInputLayout) findViewById(R.id.password_selection);
        passwordConfirmInput = (TextInputLayout) findViewById(R.id.password_comfirm);

        signupButton = (Button) findViewById(R.id.signup_button);
        signupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String user = usernameInput.getEditText().getText().toString();
        String password = passwordInput.getEditText().getText().toString();
        String passConfirm = passwordConfirmInput.getEditText().getText().toString();
        if(usernameAvailable(user)){
            if(password.equals(passConfirm)){
                //Enregirstrer user et password dans la base de donnee
                Toast.makeText(getApplicationContext(), "Account creation complete", Toast.LENGTH_LONG).show();
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(), "Password and password confirmation does not match", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Username already taken", Toast.LENGTH_LONG).show();
        }

    }

    public Boolean usernameAvailable(String user){
        //To complete
        /*
        for(int i = 0; i < usernameList.length; i++){
            if(user.equals(usernameList[i])){
                return false;
            }
        }
        */
        return true;
    }
}

