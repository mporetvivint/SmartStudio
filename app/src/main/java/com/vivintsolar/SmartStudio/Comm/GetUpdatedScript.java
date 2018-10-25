package com.vivintsolar.SmartStudio.Comm;

import android.content.ContentProvider;
import android.content.Context;
import android.media.MediaDrm;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vivintsolar.SmartStudio.Model.Script;

import static android.content.Context.WIFI_SERVICE;

public class GetUpdatedScript extends AsyncTask<Void,Void,String> {

    String ip_address;

    private OnEventListener<String> callBack;
    private Context context;

    public GetUpdatedScript(String ip_address, OnEventListener callBack, Context context) {
        this.ip_address = ip_address;
        this.callBack = callBack;
        this.context = context;
    }

    @Override
    protected void onPostExecute(String s) {
        if(callBack != null){
            callBack.onSuccess(s);
        }
    }

    @Override
    protected String doInBackground(Void... addClientActivities) {
        //Set up request
        StringRequest request = createRequest();
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "script has been updated successfully";
    }

    private StringRequest createRequest(){

        String scriptURL = "http://" + ip_address + "/script";


        return new StringRequest(Request.Method.GET, scriptURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("got_response", response);
                        Script.setScript(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onFail();
                Log.d("network error", error.toString());
            }
        });
    }
}
