package ns804.bigpiph.shitcode;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoTools;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ns804.bigpiph.R;

/**
 * Created by Luke747 on 5/19/16.
 */
public class Utility {
    /**--------------------------- FONT SETTING ------------------------------**/

    public static void fontCollegeBold(Context context,View view){
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/collegeb.ttf");
        if (view instanceof TextView){
            ((TextView)view).setTypeface(face);
        }
        if (view instanceof Button){
            ((Button)view).setTypeface(face);
        }
        if (view instanceof EditText){
            ((EditText)view).setTypeface(face);
        }
    }

    public static void fontCalibri(Context context,View view){
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/Calibri.ttf");
        if (view instanceof TextView){
            ((TextView)view).setTypeface(face);
        }
        if (view instanceof Button){
            ((Button)view).setTypeface(face);
        }
        if (view instanceof EditText){
            ((EditText)view).setTypeface(face);
        }
    }

    public static void fontSerifaStd(Context context,View view){
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/SerifaStd-Roman.otf");
        if (view instanceof TextView){
            ((TextView)view).setTypeface(face);
        }
        if (view instanceof Button){
            ((Button)view).setTypeface(face);
        }
        if (view instanceof EditText){
            ((EditText)view).setTypeface(face);
        }
    }

    /**--------------------------- TOAST METHODS ------------------------------**/

    public static void toastMessage(Context context,String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastMessageLong(Context context,String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void toastMessageCenter(Context context,String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    public static String getLocationFromLatLng(Context context,double lat,double lng){
        Log.d("LOAT", lat + " -- " + lng);
        StringBuilder location = new StringBuilder();
        Geocoder geocoder;
        List<Address> addresses=null;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        //String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        //location.append(address+" ").append(city+" ").append(state+" ").append(country);
        location.append(city+" ").append(state);
        return location.toString();
    }

    /**------------------- INTERNET CONNECTION CHECK METHOD --------------------**/

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
        }
        Utility.toastMessage(context, "No internet connection");
        return false;
    }

    public static boolean hasGPSDevice(Context context)
    {
        final LocationManager mgr = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        if ( mgr == null ) return false;
        final List<String> providers = mgr.getAllProviders();
        if ( providers == null ) return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    /**--------------------------- PREFRENCES METHODS ------------------------------**/

    public static void savePreferences(Context context,String key,String value) {
        SharedPreferences mPreferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        mPreferences.edit().putString(key, value).commit();
    }

    public static void savePreferences(Context context,String key,int value) {
        SharedPreferences mPreferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        mPreferences.edit().putInt(key, value).commit();
    }

    public static void savePreferences(Context context,String key,boolean value) {
        SharedPreferences mPreferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        mPreferences.edit().putBoolean(key, value).commit();
    }

    public static void removePreferences(Context context,String key) {
        SharedPreferences mPreferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        mPreferences.edit().remove(key).commit();
    }

    public static String getPrefrences(Context context,String key){
        SharedPreferences mPreferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        String result = mPreferences.getString(key, "");
        return result;
    }

    public static int getPrefrencesInt(Context context,String key){
        SharedPreferences mPreferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        int result = mPreferences.getInt(key, -1);
        return result;
    }

    public static boolean getPrefrencesBool(Context context,String key){
        SharedPreferences mPreferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        boolean result = mPreferences.getBoolean(key, false);
        return result;
    }

    public static void saveArrayList(Context context,ArrayList<Integer> mArrayList,
                                     String arrayListName) {
        SharedPreferences mPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        mPreferences.edit().putInt(arrayListName + "_size", mArrayList.size())
                .commit();
        for (int i = 0; i < mArrayList.size(); i++)
            mPreferences.edit()
                    .putInt(arrayListName + "_" + i, mArrayList.get(i))
                    .commit();
    }

    public static ArrayList<Integer> loadArrayList(Context context,String arrayListName) {
        SharedPreferences mPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        int size = mPreferences.getInt(arrayListName + "_size", 0);
        ArrayList<Integer> array = new ArrayList<Integer>();
        for (int i = 0; i < size; i++)
            array.add(mPreferences.getInt(arrayListName + "_" + i, 0));
        return array;
    }

    public static void deleteArrayList(Context context,String arrayListName) {
        ArrayList<Integer> mList = loadArrayList(context, arrayListName);
        SharedPreferences mPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        mPreferences.edit().remove(arrayListName + "_size").commit();
        for (int i = 0; i < mList.size(); i++)
            mPreferences.edit().remove(arrayListName + "_" + i).commit();
    }

    public static void deleteItemArrayList(Context context,String arrayListName,int index) {
        ArrayList<Integer> mList = loadArrayList(context, arrayListName);
        mList.remove(index);
        saveArrayList(context, mList, arrayListName);
    }


    /**--------------------------------------------- Email Validation -----------------------------------------**/
    public static boolean isEmailValid(String email) {
        Log.e("CHK IF EMAIL IS VALID", "" + email);
        boolean isValid;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            isValid = true;
        } else {

            Log.i(".....Email", "Not valid");

            isValid = false;

        }
        return isValid;
    }

    /**----------------------------------------- CHECK GPS ----------------------------------**/
    public static boolean isGPSEnabled (Context mContext){
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**--------------------------------------------- Bitmap to Base64 String -----------------------------------------**/
    public static String bitmapToBase64(Bitmap bitmap){
        String encodedBase64 = null;
        int bitmapSize;
        //	convert bitmap to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        //	encode base64 from byte array
        encodedBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encodedBase64;
    }

    /**--------------------------------------------- Bitmap to Base64 String -----------------------------------------**/
    public static Bitmap base64ToBitmap(String base64Encode){
        Bitmap bitmap = null;
        //	convert base64 to byte array
        byte[] decodedString = Base64.decode(base64Encode, Base64.DEFAULT);
        //	convert byte array to bitmap
        bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bitmap;
    }

    /**--------------------------------------------- Check bitmap size -----------------------------------------**/
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public static int sizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else {
            return data.getByteCount();
        }
    }

    /**
     * Hides the soft keyboard
     */
    public static void hideSoftKeyboard(Context context) {
        if(((Activity)context).getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((Activity)context).getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public static void showSoftKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    /**--------------------------------------------- HIT SERVER METHODS -----------------------------------------**/

    public String hitServer(String url,List<NameValuePair> params) throws MalformedURLException, IOException, JSONException
    {
        ArrayList< String> mArrayList=new ArrayList<String>();
        HttpURLConnection conn=null;
        InputStream iStream=null;
        String result;
        Log.d("URLENCODED","fired-hitServer");
        String urlParameters = getQuery(params);
        try
        {
            Log.d("URLENCODED","fired-afterquery");
            conn = (HttpURLConnection) ( new URL(url)).openConnection();
            conn.setRequestMethod("POST");
            //	conn.setChunkedStreamingMode(0);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            conn.setRequestProperty("Content-Language", "en-US");
            Log.d("messager of wtf", "output:" + urlParameters);

            conn.setDoInput(true);
            conn.setDoOutput(true);
            Log.d("messager of wtf", "conn-output:" + conn);

            // conn.getOutputStream().write(urlParameters.getBytes());
            //Send request
            OutputStream wr = conn.getOutputStream ();
            //wr.writeBytes (urlParameters);
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(wr, "UTF-8"));
            writer.write(getQuery(params));
            Log.d("messager of wtf", "output:" + wr);
            writer.flush ();
            writer.close ();

            //System.out.println("the response is "+conn.getResponseCode());
            //        OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            //        out.write(("cid = "+categoryid).getBytes());
            Log.v("messager of god", "" + conn.getResponseCode());
            if(conn.getResponseCode()==200)
            {
                iStream = conn.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while( ( line = br.readLine()) != null){
                    sb.append(line);
                    mArrayList.add(line);
                }

                result = sb.toString();
                for (int j = 0; j < mArrayList.size(); j++) {
                    Log.v("contacts are -->", "" + mArrayList.get(j));
                }

                iStream.close();
                //   System.out.println("The string data is "+str);
            }
            else
            {
                throw new IOException("Post failed with error code " + conn.getResponseCode());
            }
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }//end of finally//


        return result;
    }


    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }
        Log.d("URLENCODED-Pairs",": "+result.toString());
        return result.toString();

    }


//	private String getQuery2(List<HashMap> params) throws UnsupportedEncodingException
//	{
//		StringBuilder result = new StringBuilder();
//		boolean first = true;
//
//		for (HashMap<String,Object> pair : params)
//		{
//			if (first)
//				first = false;
//			else
//				result.append("&");
//
//			for ( String key : pair.keySet() ) {
//				System.out.println( key );
//			result.append(URLEncoder.encode(key, "UTF-8"));
//			result.append("=");
//			result.append(URLEncoder.encode(pair.get(key).toString(), "UTF-8"));
//			}
//		}
//
//		return result.toString();
//	}

    /**--------------------------------------------- Hit Server code -----------------------------------------**/
    public String Hit_Server(JSONObject mJsonToken, String string) {
        String status = "";

        HttpPost httppost = null;
        try {
            httppost = new HttpPost(string);
            HttpParams myParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(myParams, 10000);
            HttpConnectionParams.setSoTimeout(myParams, 30 * 10000);
            httppost.setHeader("Content-type", "application/json");
            HttpClient httpclient = new DefaultHttpClient(myParams);
            // JSONObject jsonObject = new JSONObject();
            // jsonObject.put("body_dict", mJsonToken);
            // StringEntity se;
            // if (jsonObject.toString() == null) {
            // se = new StringEntity("", HTTP.UTF_8);
            // } else {
            // se = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
            // }
            StringEntity se;
            if (mJsonToken.toString() == null) {
                se = new StringEntity("", HTTP.UTF_8);
            } else {
                se = new StringEntity(mJsonToken.toString(), HTTP.UTF_8);
            }
//			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
//					"application/json"));
            httppost.setEntity(se);
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String getResult = EntityUtils.toString(entity);
            status = getResult;

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
        }
        return status;

    }

    //////////////////////////////	HIT SERVER GET API	////////////////////////////////
    public static JSONObject hitGetServer(String urlString){
        HttpURLConnection urlConnection = null;
        URL url = null;
        JSONObject object = null;
        InputStream inStream = null;
        try {

            url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
            String temp, response = "";
            while ((temp = bReader.readLine()) != null) {
                response += temp;
            }
            object = (JSONObject) new JSONTokener(response).nextValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    // this will close the bReader as well
                    inStream.close();
                } catch (IOException ignored) {
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return object;
    }

    public static String loadJSONFromAsset(String filename,Context context)
    {
        String json=null;
        try {
            InputStream is =context.getAssets().open(filename);
            int size = is.available();
            if(size>0)
            {
                byte[] buffer = new byte[size];

                is.read(buffer);

                is.close();

                json = new String(buffer, "UTF-8");
            }
            else
            {
                json=null;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return json;
    }

    public static String encode(String input)  {
        try {
            return URLEncoder.encode(input, "UTF-8").replace("%2F", "/").replace("%3A", ":").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String addtoken(String url, String token){
        if(!url.endsWith("?")) {
            url += "?";
        }


        List<NameValuePair> params = new LinkedList<NameValuePair>();

        if ( token.length() > 0){
            params.add(new BasicNameValuePair("Authorization", token));

        }
        Log.d("Add_Token_Here","url: "+url+params);
        String paramString = URLEncodedUtils.format(params, "utf-8");

        Log.d("Add_Token_Here","url: "+url+paramString);

        url += paramString;
        return url;
    }



    // Returns the URI path to the Bitmap displayed in specified ImageView
    public static Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
    public static void getDeviceName(Context context){
        String name = android.os.Build.MODEL;

        if(name.contains("LG")) {
            Log.d("PicassoDelete","fired:"+name);
            Log.d("PicassoDelete","fired:"+ Runtime.getRuntime().maxMemory());
            PicassoTools.clearCache(Picasso.with(context));
        }

        
    }
    public static boolean clearImageDiskCache(Context mContext) {
        File cache = new File(mContext.getApplicationContext().getCacheDir(), "picasso-cache");
        if (cache.exists() && cache.isDirectory()) {
            Log.d("PicassoDelete","fired:");
            Log.d("PicassoDelete","fired:"+ Runtime.getRuntime().maxMemory());
            return deleteDir(cache);
        }
        return false;
    }
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                Log.d("PicassoDelete","fired:"+i);
                Log.d("PicassoDelete","fired:"+ Runtime.getRuntime().maxMemory());
                if (!success) {
                    return false;
                }
            }
            Log.d("Calendar","after: "+ Runtime.getRuntime().freeMemory());

        }
        // The directory is now empty so delete it
        return dir.delete();

    }
    public static Boolean updateDateTime(Context context){
        Calendar calendar = new GregorianCalendar();
        Date now = calendar.getTime();

        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        System.out.println("hour: " + hour);
        Log.d("Calendar","time now: "+ hour);
        int old = getPrefrencesInt(context,"Time");
        Log.d("Calendar","time old: "+ old);
        if((hour - old)>= 1 || (hour - old) <= -1){
            Log.d("Calendar","before: "+ Runtime.getRuntime().freeMemory());
            savePreferences(context,"Time", hour);
            clearImageDiskCache(context);
            return true;




        }
        return false;

    }


}
