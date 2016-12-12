package com.congnt.androidbasecomponent.utility;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by congnt24 on 23/10/2016.
 */

public class CameraUtil {
    private static final String TAG = "CameraUtil";

    /**
     * Checking device has camera hardware or not
     */
    public static boolean isDeviceSupportCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    public static Camera.Parameters getCameraParam(Camera cam){
        Camera.Parameters params = cam.getParameters();
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        Camera.Size mSize = null;

        Collections.sort(sizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size t1, Camera.Size t2) {
                return t1.width >= t2.width ? 1 : -1;
            }
        });
        for (Camera.Size size : sizes) {
            if (size.width >= 600) {
                mSize = size;
                break;
            }
        }
        Log.i("CAMERA", "Available resolution: " + mSize.width + " " + mSize.height);
        params.setPictureSize(mSize.width, mSize.height);
        return params;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void openCamera2(Context context, CameraDevice.StateCallback stateCallback) {
        if (isDeviceSupportCamera(context)) {
            CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            Log.d(TAG, "is camera open");
            try {
                String cameraId = manager.getCameraIdList()[0];
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                assert map != null;
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                manager.openCamera(cameraId, stateCallback, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static Camera openCamera1(int cameraId) {
        Camera camera;
        if (cameraId == -1) {
            camera = Camera.open();
        } else {
            camera = Camera.open(cameraId);
        }
        return camera;
    }

    public static int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }
}
