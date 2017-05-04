package com.example.johnny.javiprototype1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;

public class BitmapConverter {
    /*public static Bitmap getBitmapFromUrl(String path) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new java.net.URL(path).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return bitmap;
        } catch (OutOfMemoryError memerror) {
            getResizedBitmap(bitmap, 200, 200);
            return bitmap;
        } catch (MalformedURLException urlerror) {
            urlerror.printStackTrace();
            return null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }*/

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

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
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