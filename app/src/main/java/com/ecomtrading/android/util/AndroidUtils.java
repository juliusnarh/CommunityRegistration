package com.ecomtrading.android.util;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.ecomtrading.android.data.AddressPojo;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 * Created by Junior on 11/24/2017.
 */

public class AndroidUtils {
    static String TAG = AndroidUtils.class.getSimpleName();
    static List<File> fileList;


    public static String uploadDirectoryPath() {
        return Environment.getExternalStorageDirectory() + File.separator + "communityRegistration/uploadfolder";
    }

    public boolean checkInternetConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Convert Bitmap image to byte array
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap byteArrayToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public static String bitmapToBase64(Bitmap bitmap){
        return byteArrayToBase64(bitmapToByteArray(bitmap));
    }

    public static String byteArrayToBase64(byte[] barray) {
        return Base64.encodeToString(barray, Base64.DEFAULT);
    }

    public static Bitmap base64ToBitmap(String photo) {
        return byteArrayToBitmap(base64ToByteArray(photo));
    }

    public static byte[] base64ToByteArray(String base64string) {
        if (base64string != null){
            if (!base64string.isEmpty())
                return Base64.decode(base64string.getBytes(), Base64.DEFAULT);
        }
        return null;
    }

    public static <T> List<T> getGsonList(String json, Class<T> typeClass) {
        return new Gson().fromJson(json, new ListOfGson<T>(typeClass));
    }

    /**
     * Generate uniqueuid random number
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * method to get list of files
     *
     * @param parentDir
     * @return
     */
    public static List<File> getListOfFiles(File parentDir, String fileType) {

        List<File> inFiles = new ArrayList<>();
        Queue<File> files = new LinkedList<>();

        files.addAll(Arrays.asList(parentDir.listFiles()));
        while (!files.isEmpty()) {
            File file = files.remove();
            if (file.isDirectory()) {
                files.addAll(Arrays.asList(file.listFiles()));
            } else if (file.getName().endsWith(fileType)) {
                inFiles.add(file);
            }
        }
        return inFiles;
    }

    public static void checkPermissions(Activity activity) {
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.CAMERA

                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
    }

    public static String writeToFile(String data, String filename) {
        String dirpath = Environment.getExternalStorageDirectory() + File.separator +
                "uploadfolder";
        final File dir = new File(dirpath);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        final File file = new File(dir, filename);

        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.append(data);

            osw.close();
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        return file.getAbsolutePath();
    }


    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "communityRegistration//Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

}
