package com.example.johnny.javiprototype1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class BitmapConverter {
    // Convert bitmap to string
    public static String bitmapToString(Bitmap bitmap) {
        //final int COMPRESSION_QUALITY = 100;
        byte[] bytesImg = bitmapToBytes(bitmap); // See below
        String strImg = Base64.encodeToString(bytesImg, Base64.DEFAULT);
        return strImg;
    }

    // Convert string to bitmap
    public static Bitmap stringToBitmap(String string) {
        byte[] bytesImg = Base64.decode(string, Base64.DEFAULT);
        Bitmap bitmapImg = bytesToBitmap(bytesImg); // See below
        return bitmapImg;
    }

    // Convert bitmap to bytes
    public static byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        byte[] bytesImg = byteArrayOutputStream.toByteArray();
        return bytesImg;
    }

    // Convert bytes to bitmap
    public static Bitmap bytesToBitmap(byte[] bytes) {
        Bitmap bitmapImg = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmapImg;
    }
}