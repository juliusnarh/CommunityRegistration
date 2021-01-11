package com.ecomtrading.android.util;

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
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.ecomtrading.android.data.AddressPojo;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;

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
import java.util.Queue;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 * Created by Junior on 11/24/2017.
 */

public class AndroidUtils {

    static OkHttpClient client = new OkHttpClient();
    static String TAG = AndroidUtils.class.getSimpleName();
    static List<File> fileList;


    public static String uploadDirectoryPath() {
        return Environment.getExternalStorageDirectory() + File.separator + "communityRegistration/uploadfolder";
    }

    public static String downloadDirectoryPath() {
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "csvdatafolder/serverfiles");
        if (!folder.exists()) {
            folder.mkdirs();
            return folder.getAbsolutePath();
        } else {
            return folder.getAbsolutePath();
        }
    }

    public static String uploadTempDirectoryPath() {
        return Environment.getExternalStorageDirectory() + File.separator + "uploadtempfolder";
    }

    public static JSONObject getLocationInfo(double lat, double lng) {
        Response response = null;
        try {
            Request request = new Request.Builder()
                    .url("http://maps.googleapis.com/maps/api/geocode/json?latlng="
                            + lat + "," + lng + "&sensor=true")
                    .build();
            response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(response.body().string());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return jsonObject;

    }

    public boolean checkInternetConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static AddressPojo getCurrentLocationViaJSON(double lat, double lng) {
        AddressPojo addressPojo = new AddressPojo();
        JSONObject jsonObj = getLocationInfo(lat, lng);
        //Log.i("JSON string =>", jsonObj.toString());
        try {
            String status = jsonObj.getString("status");
            Log.i("status", status);

            if (status.equalsIgnoreCase("OK")) {
                JSONArray Results = jsonObj.getJSONArray("results");
                JSONObject zero = Results.getJSONObject(0);
                JSONArray address_components = zero.getJSONArray("address_components");

                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject zero2 = address_components.getJSONObject(i);
                    JSONArray typeObj = (JSONArray) zero2.get("types");
                    if (typeObj.get(0).equals("route")) {
                        addressPojo.setStreetaddress("" + zero2.get("long_name"));
                    } else if (typeObj.get(0).equals("locality")) {
                        addressPojo.setCity("" + zero2.get("long_name"));
                    } else if (typeObj.get(0).equals("administrative_area_level_2")) {
                        addressPojo.setAssembly("" + zero2.get("long_name"));
                    } else if (typeObj.get(0).equals("administrative_area_level_1")) {
                        addressPojo.setRegion("" + zero2.get("long_name"));
                    } else if (typeObj.get(0).equals("country")) {
                        addressPojo.setCountry("" + zero2.get("long_name"));
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return addressPojo;
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

    public static String saveImageToDirectory(byte[] data, String filename) {
        boolean result = false;
        String path = "";
        try {
            Bitmap pic = byteArrayToBitmap(data);
            File photo = new File(getAlbumStorageDir("ucl"), filename + ".jpg"  /*String.format
             (filename+"_%d.jpg", System.currentTimeMillis())*/);
            saveBitmapToJPG(pic, photo);
            //scanMediaFile(photo);
            path = photo.getAbsolutePath();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static String saveImageToDirectory(Bitmap pic, String filename) {
        boolean result = false;
        String path = "";
        try {
            File photo = new File(getAlbumStorageDir("ucl"), String.format(filename + "_%d.jpg",
                    System.currentTimeMillis()));
            saveBitmapToJPG(pic, photo);
            //scanMediaFile(photo);
            path = photo.getAbsolutePath();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("ucl", "Directory not created");
        }
        return file;
    }

    public static void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap
                .Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

//    Uri contentUri;
//    private void scanMediaFile(File photo) {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        contentUri = Uri.fromFile(photo);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }

    public static String byteArrayToBase64(byte[] barray) {
        return Base64.encodeToString(barray, Base64.DEFAULT);
    }

    public static byte[] base64ToByteArray(String base64string) {
        if (base64string != null){
            if (!base64string.isEmpty())
                return Base64.decode(base64string.getBytes(), Base64.DEFAULT);
        }
        return null;
    }

    /**
     * method to find the center of a polygon
     *
     * @param points
     * @return
     */
    public static double[] centroid(List<LatLng> points) {
        double[] centroid = {0.0, 0.0};

        for (int i = 0; i < points.size(); i++) {
            centroid[0] += points.get(i).latitude;
            centroid[1] += points.get(i).longitude;
        }

        int totalPoints = points.size();
        centroid[0] = centroid[0] / totalPoints;
        centroid[1] = centroid[1] / totalPoints;

        return centroid;
    }

    /**
     * method to calculate center of polygon
     *
     * @param polygonPointsList
     * @return
     */
    public static LatLng getPolygonCenterPoint(List<LatLng> polygonPointsList) {
        LatLng centerLatLng = null;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < polygonPointsList.size(); i++) {
            builder.include(polygonPointsList.get(i));
        }
        LatLngBounds bounds = builder.build();
        centerLatLng = bounds.getCenter();

        return centerLatLng;
    }

    public static <T> List<T> getGsonList(String json, Class<T> typeClass) {
        return new Gson().fromJson(json, new ListOfGson<T>(typeClass));
    }

    /**
     * method to set image in imageview
     *
     * @param imageView
     * @param imagebyte
     */

    /**
     * method to convert drawable to bitmap
     */
    public static Bitmap drawableToBitmap(int drawable, Context context) {
        return BitmapFactory.decodeResource(context.getResources(), drawable);
    }

    /**
     * method to convert drawable to bitmap
     */
    public static Drawable arraytodrawable(byte[] imgbyte, Context context) {
        try {
            return new BitmapDrawable(context.getResources(), BitmapFactory.decodeByteArray(imgbyte, 0, imgbyte.length));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
     * Convert date string into java.util.Date
     */
    public static Date dateStringToDate(String datestring, String pattern) {
        Date date = null;
        try {
            date = new SimpleDateFormat(pattern).parse(datestring);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }
        return date;
    }

    public static String dateToFormattedString(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String datestring = "";
        try {
            datestring = formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datestring;
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

    public static String writeToscalelog(String data, String filename) {
        String dirpath = Environment.getExternalStorageDirectory() + File.separator +
                "ams/scalelogs";
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

    public static void hideKeyboard(Activity ctx) {
        try {
            InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(ctx.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception ex) {
        }

    }

    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "communityRegistration//Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static void sendMessage(Context mContext, String contact, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            PendingIntent sentPI, deliveredPI;
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            sentPI = PendingIntent.getBroadcast(mContext, 0, new Intent(SENT), 0);
            deliveredPI = PendingIntent.getBroadcast(mContext, 0, new Intent(DELIVERED), 0);

            ArrayList<String> parts = smsManager.divideMessage(message);

            ArrayList<PendingIntent> sendList = new ArrayList<>();
            sendList.add(sentPI);

            ArrayList<PendingIntent> deliverList = new ArrayList<>();
            deliverList.add(deliveredPI);


            smsManager.sendMultipartTextMessage(contact, null, parts, sendList, deliverList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(new Date());
    }

    public static String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }
        return "";
    }

    public static byte[] drawableToArray(int drawable, Resources resources) {
        Bitmap bitmap = BitmapFactory.decodeResource(resources, drawable);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);

        return outputStream.toByteArray();
    }
}
