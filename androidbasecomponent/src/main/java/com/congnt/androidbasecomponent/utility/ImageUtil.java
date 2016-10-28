package com.congnt.androidbasecomponent.utility;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by congn_000 on 9/20/2016.
 */

public class ImageUtil {
    public static String filePath;


    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
    /**
     * add a picture to a gallary
     *
     * @param context
     * @param mCurrentPhotoPath
     */
    public static void galleryAddPic(Context context, String mCurrentPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static boolean createFileFromData(byte[] data) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            return false;
        }
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(pictureFile);//String.format("/sdcard/%d.jpg", System.currentTimeMillis())
            outStream.write(data);
            outStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    /**
     * Save bitmap to file
     *
     * @param bitmap
     * @param folder   Parent Dir
     * @param fileName
     * @return
     */
    public static void createFileFromBitmap(final Context context, final Bitmap bitmap, final File folder, final String fileName) {
        if (PermissionUtil.getInstance(context).checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            filePath = createBitmapFile(bitmap, folder, fileName);
        }
    }

    private static String createBitmapFile(Bitmap bmp, File dest, String fileName) {
        String result = null;
        File f = new File(dest, fileName);
        if (f.exists()) {
            return f.getAbsolutePath();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your
            // Bitmap
            // instance
            // PNG is a loss less format, the compression factor (100) is ignored
            result = f.getAbsolutePath();
        } catch (Exception e) {
            LogUtil.e("Error save bitmap", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                LogUtil.e("Error close stream", e);
            }
        }
        return result;
    }

    public static Bitmap scaleBitmap(Context ctx, Bitmap source, float scale) {
        int w = (int) (scale * source.getWidth());
        int h = (int) (scale * source.getHeight());
        Bitmap photo = Bitmap.createScaledBitmap(source, w, h, true);
        return photo;
    }

    public Bitmap rotate(Bitmap bm, int rotateAngle) {
        if (rotateAngle % 360 == 0) {
            return bm;
        }
        Matrix mtx = new Matrix();
        mtx.postRotate(rotateAngle);
        bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), mtx, true);
        return bm;
    }
}
