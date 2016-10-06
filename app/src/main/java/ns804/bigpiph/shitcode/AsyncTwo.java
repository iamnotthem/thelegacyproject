package ns804.bigpiph.shitcode;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Luke747 on 6/9/16.
 */
public class AsyncTwo extends AsyncTask<String, String, String>{

    Context context;
    List<HashMap> paramsList;
    String tag,stringUrl;
    ResponseInterface mInterface;
    ProgressDialog progressDialog;
    String mStringResponse,progressMessage;

    public AsyncTwo(Context context, List<HashMap> paramsList, String tag, ResponseInterface mInterface) {
        this.context = context;
        this.paramsList = paramsList;
        this.tag = tag;
        this.mInterface = mInterface;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        String responseString = null;
        Log.d("Add_token_here", "fired here");

        Log.d("PARAMSLIST", paramsList + "");

        try {
            String url = uri[0].replace(" ","%20");
            //String bear = paramsList.remove(0).toString();
            HashMap params = paramsList.remove(0);
            String bearer = params.get("Authorization").toString();



            Log.d("Add_token_here", ":  "+bearer);

            HttpGet req = new HttpGet(url);
            req.addHeader("Authorization", bearer);

            response = httpclient.execute(req);

            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        if (responseString != null) {
            Log.d("RESPONSEE", responseString);
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mStringResponse = result;
        if (mInterface != null) {
            mInterface.getResponse(mStringResponse, tag);
        }

    }

}


