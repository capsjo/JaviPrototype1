package com.example.johnny.javiprototype1;


import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import static android.hardware.Camera.getNumberOfCameras;

public class CameraFragment extends Fragment {

    private Camera camInstance;
    private CameraPreview camPreview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.camera_fragment, container, false);

        camInstance = getCameraInstance();
        camPreview = new CameraPreview(getActivity(), camInstance);

        FrameLayout preview = (FrameLayout) v.findViewById(R.id.camera_preview);
        preview.addView(camPreview);

        return v;
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(0); // attempt to get a Camera instance
        }
        catch (Exception e){
            if(c == null) {
                int nb = getNumberOfCameras();
                System.out.println("no cam" + nb);
            }
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

}
