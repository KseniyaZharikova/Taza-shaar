package com.example.kseniya.zerowaste.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.VectorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;



/**
 * Created by rustam on 17.02.16.
 */
public class BitmapUtil {

    public static Point getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static int dp2px(Context context, int dp) {
        if (context == null) {
            return dp;
        }
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap createBitmap(Context context, @DrawableRes int drawableId, int width, int height) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        return Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), dp2px(context, width), dp2px(context, height), false);
    }


    public static File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("BitmapUtil", "Directory not created");
        }
        return file;
    }

    static public Bitmap imageFromUri(Context context, Uri uri) throws IOException {
        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
    }

    static public Bitmap getImage(Context context, Uri uri) {
        Bitmap image = null;

        int downtime = 0;

        while (image == null && downtime <= 3000) {
            downtime += 200;

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return image;
    }

    static public Bitmap createPreview(Context context, Bitmap fullImage) {
        Bitmap preview = Bitmap.createBitmap(fullImage);

        double width = getDisplaySize(context).x;

        preview = scaleImage(preview, (int) (width * 0.7));

        return preview;
    }

    static public Bitmap scaleImage(Bitmap image, int maxSideSize) {
        int maxSide = Math.max(image.getWidth(), image.getHeight());
        double scale = (double) maxSide / (double) maxSideSize;
        if (scale > 1) {
            int newWidth = (int) (image.getWidth() / scale);
            int newHeigth = (int) (image.getHeight() / scale);
            image = Bitmap.createScaledBitmap(image, newWidth, newHeigth, false);
        }
        return image;
    }

    static public Matrix originalOrientationMatrix(Context context, Uri uri) {
        String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
        Cursor cur = context.getContentResolver().query(uri, orientationColumn, null, null, null);

        int rotate = 0;

        if (cur != null) {
            if (cur.moveToFirst() && cur.getColumnIndex(orientationColumn[0]) != -1) {
                rotate = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
            }
            cur.close();
        } else {
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                context.getContentResolver().notifyChange(uri, null);

            File imageFile = new File(uri.getPath());

            int orientation = 0;

            try {
                ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            } catch (IOException e) {
                e.printStackTrace();
            }

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);

        return matrix;
    }

    static public Bitmap squareImage(Bitmap image) {
        int x = 0;
        int y = 0;
        int a = image.getWidth();
        if (image.getWidth() > image.getHeight()) {
            x = (image.getWidth() - image.getHeight()) / 2;
            a = image.getHeight();
        } else if (image.getWidth() < image.getHeight()) {
            y = (image.getHeight() - image.getWidth()) / 2;
        }
        image = Bitmap.createBitmap(image, x, y, a, a);
        return image;
    }

    static public Bitmap compressedImage(Bitmap image, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return image;
    }

    static public byte[] imageData(Bitmap image, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }

    static public Uri imagePath(Context context, Bitmap image, String fileName) {
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), compressedImage(image, 100), fileName, null);
        return Uri.parse(path);
    }

//    static public TypedFile imageTypedFile(Context context, Bitmap image, String fileName) {
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = context.getContentResolver().query(imagePath(context, image, fileName), proj, null, null, null);
//        TypedFile typedFile = null;
//        if (cursor != null) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            typedFile = new TypedFile("multipart/form-data", new File(cursor.getString(column_index)));
//        }
//        return typedFile;
//    }

    public static void createCameraAndGalleryIntentList(Context context, List<Intent> appIntentList, List<ResolveInfo> appInfoList) {
        boolean addCameraIntent = true;
        boolean addGalleryIntent = true;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addCameraIntent = context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
            addGalleryIntent = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }

        if (addCameraIntent) {
            Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            camIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            addAppInfo(context, camIntent, appIntentList, appInfoList);
        }

        if (addGalleryIntent) {
            Intent gallIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            gallIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            addAppInfo(context, gallIntent, appIntentList, appInfoList);
        }
    }

    private static void addAppInfo(Context context, Intent intent, List<Intent> appIntentList, List<ResolveInfo> appInfoList) {
        List<ResolveInfo> appList = context.getPackageManager().queryIntentActivities(intent, 0);
        appInfoList.addAll(appList);
        for (final ResolveInfo res : appList) {
            Intent tempIntent = new Intent(intent);
            tempIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            appIntentList.add(tempIntent);
        }
    }

    //----ripple_utils---

    public static int getColor(Context context, @ColorRes int id) {
        int color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = context.getColor(id);
        } else {
            color = context.getResources().getColor(id);
        }
        return color;

    }
}
