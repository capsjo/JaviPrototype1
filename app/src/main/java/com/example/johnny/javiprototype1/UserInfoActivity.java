package com.example.johnny.javiprototype1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int SELECT_PICTURE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    private ImageView inputPhotoView;
    private EditText name;
    private EditText description;
    private Button select;
    private Button takePic;
    private Button ok;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        name = (EditText)findViewById(R.id.input_name);
        description = (EditText)findViewById(R.id.input_des);
        select = (Button)findViewById(R.id.gallery_select);
        takePic = (Button)findViewById(R.id.take_photo);
        ok = (Button)findViewById(R.id.ok_button);
        cancel = (Button)findViewById(R.id.cancel_button);
        inputPhotoView = (ImageView)findViewById(R.id.inputPhotoView);

        select.setOnClickListener(this);
        takePic.setOnClickListener(this);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);

        name.setText("this user's name");
        description.setText("a description of the user");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.take_photo:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                break;
            case R.id.gallery_select:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), SELECT_PICTURE);
                break;
            case R.id.ok_button:
                break;
            case R.id.cancel_button:
                finish();
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
}
