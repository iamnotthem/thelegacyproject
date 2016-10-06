package ns804.bigpiph.shitcode;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Luke747 on 5/19/16.
 */
public class Async extends AsyncTask {
    Context context;
    List<HashMap<String,Object>> paramsList;
    String tag,stringUrl;
    ResponseInterface mInterface;
    ProgressDialog progressDialog;
    String mStringResponse,progressMessage;

    public Async(Context context, List<HashMap<String,Object>> paramsList, String stringUrl, String tag, String progressMessage, ResponseInterface mInterface) {
        this.context = context;
        this.paramsList = paramsList;
        this.tag = tag;
        this.mInterface = mInterface;
        this.progressMessage = progressMessage;
        this.stringUrl = stringUrl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressMessage != null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(progressMessage + "\nPlease wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //mStringResponse = new Utility().hitServer2(stringUrl, paramsList);
            JSONObject object = new JSONObject();
            if (paramsList != null && !stringUrl.contains("token")) {
                String mainKey = "";
                for (String key : paramsList.get(0).keySet()) {
                    mainKey = key;
                    object.put(mainKey, paramsList.get(0).get(mainKey));

                    Log.d("URLENCODED","wrong-code:"+ mainKey.toString());
                }
            }else if(stringUrl.contains("token")){
                String mainKey = "";
                Log.d("URLENCODED","fired+created");
                Log.d("URLENCODED",paramsList.toString());
                Log.d("URLENCODED",": " + stringUrl);

                JSONObject jObject = new JSONObject(paramsList.remove(0));
                JSONObject nObject = jObject.getJSONObject("user");
                String email = nObject.optString("email");
                String password = nObject.optString("password");
                String grant_type = nObject.optString("grant_type");
                Log.d("URLENCODED",jObject.toString());

                Log.d("URLENCODED","correct-code:"+ stringUrl);

                params.add(new BasicNameValuePair("grant_type", "password"));

                params.add(new BasicNameValuePair("username", email));

                params.add(new BasicNameValuePair("password",password));







                    Log.d("URLENCODED" + tag + " Sending...", mainKey.toString());






            }else {
                objects = null;
            }
            Log.d("URLENCODED","fired");
            Log.d(""+tag+ " Sending...",object.toString());
            if(stringUrl.contains("token")){
                Log.d("URLENCODED","fired");
                mStringResponse = new Utility().hitServer(stringUrl,params );
            }else {
                Log.d("URLENCODED","fired_WRONG_CODE");
                mStringResponse = new Utility().Hit_Server(object, stringUrl);
            }
            Log.i(tag + " Receving...", mStringResponse);
            //System.out.print(tag+"===== " + mStringResponse);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        if (mInterface != null) {
            mInterface.getResponse(mStringResponse, tag);
        }
    }


}
