package com.vivintsolar.SmartStudio.Comm;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import com.vivintsolar.SmartStudio.Model.CurrentState;
import com.vivintsolar.SmartStudio.Model.Script;
import fi.iki.elonen.NanoHTTPD;

public class AndroidWebServer extends NanoHTTPD {

    private List<String> client_list;
    private Context context;

    public AndroidWebServer(int port, Context context) {
        super(port);
        client_list = new ArrayList<>();
        this.context = context;
    }

    public AndroidWebServer(String hostname, int port) {
        super(hostname, port);
        client_list = new ArrayList<>();
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        String ip_address = session.getRemoteIpAddress();

        if (uri.equals("/sync")){

            CurrentState.setScroll_state(0);
            CurrentState.setScroll_position(-1);
            // Send Volley Request to get position
            String sync_position_url = "http://" + client_list.get(0) + ":8080" + "/syncposition";
            Log.d("servesync", client_list.get(0));
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(context);
            // Request a string response from the provided URL.
            StringRequest position_request = new StringRequest(Request.Method.GET, sync_position_url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            CurrentState.setScroll_position(Integer.parseInt(response));
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("network error", error.toString());
                }
            });
            queue.add(position_request);
            while(CurrentState.getScroll_position() == -1){
                //wait
//                Log.d("servesync", "wait");
            }
            //Trigger send sync signal
            CurrentState.setScroll_state(50);
            return newFixedLengthResponse(Integer.toString(CurrentState.getScroll_position()));
        }
        else if (uri.equals("/syncposition")){
            return newFixedLengthResponse(Integer.toString(CurrentState.getScroll_position()));
        }
        else if (uri.equals("/scroll")){
            if (!client_list.contains(ip_address)){
                client_list.add(ip_address);
                Log.d("added_to_list", ip_address);
            }
            return newFixedLengthResponse(Integer.toString(CurrentState.getScroll_state()));
        }
        else if (uri.equals("/manscroll")){
            return newFixedLengthResponse(Float.toString(CurrentState.getMan_scroll()));
        }
        else if(uri.equals("/teleprompt")){
            return newFixedLengthResponse(Boolean.toString(CurrentState.isTeleprompter_active()));
        }
        else if(uri.equals("/script")){
            return newFixedLengthResponse(Script.getScript());
        }
        else if(uri.equals("/ping")){
            return newFixedLengthResponse("Ping back atcha");
        }
        return null;
    }
}