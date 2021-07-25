package com.sunrun.leaguemediaassistant.Comm;

import android.content.Context;
import android.os.AsyncTask;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sunrun.leaguemediaassistant.GUI.RepInfoActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static android.content.Context.WIFI_SERVICE;

public class SendRep extends AsyncTask<Void,Void,String> {

    String ip_address;
    String fn;
    String ln;
    String em;

    private OnEventListener<String> callBack;
    private Context context;

    public SendRep(String ip_address, String fn, String ln, String em, OnEventListener callBack, Context context) {
        this.ip_address = ip_address;
        this.fn = fn;
        this.ln = ln;
        this.em = em;
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

        //fix strings
        fn = fn.trim();
        ln = ln.trim();
        em = em.trim();

        try {
            fn = URLEncoder.encode(fn, String.valueOf(StandardCharsets.UTF_8));
            ln = URLEncoder.encode(ln, String.valueOf(StandardCharsets.UTF_8));
            em = URLEncoder.encode(em, String.valueOf(StandardCharsets.UTF_8));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String scriptURL = ip_address + "/addrep?fn=" + fn +
                "&ln="+ln+"&em="+em;


        return new StringRequest(Request.Method.GET, scriptURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        RepInfoActivity activity = (RepInfoActivity) context;
                        activity.makeToast("You're all Checked in!");
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

