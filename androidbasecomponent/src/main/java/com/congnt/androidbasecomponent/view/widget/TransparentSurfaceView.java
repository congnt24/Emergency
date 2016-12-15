package com.congnt.androidbasecomponent.view.widget;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TransparentSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "CameraPreview";
    public List<Camera.Size> mSupportedPreviewSizes;
    private Camera camera;
    private SurfaceHolder surfaceHolder;

    public TransparentSurfaceView(Context context) {
        super(context);
        this.camera = Camera.open();
        mSupportedPreviewSizes = camera.getParameters().getSupportedPreviewSizes();
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public TransparentSurfaceView(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        mSupportedPreviewSizes = camera.getParameters().getSupportedPreviewSizes();
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public Camera getCamera() {
        return camera;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            this.camera.setPreviewDisplay(surfaceHolder);
            Camera.Parameters params = this.camera.getParameters();
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
            this.camera.setParameters(params);
            this.camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int w, int h) {
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        try {https://youtu.be/Y2GC6P5hPeA
            camera.stopPreview();
        } catch (Exception e) {

        }

        // set preview size and make any resize, rotate or reformatting changes here
        // startUpdate preview with new settings
        try {
            Camera.Parameters parameters = camera.getParameters();
            camera.setParameters(parameters);
//            camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();

        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        try {
            getHolder().removeCallback(this);
            this.camera.stopPreview();
            this.camera.release();
        } catch (Exception e){}
    }

}