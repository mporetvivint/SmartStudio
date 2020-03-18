package com.vivintsolar.SmartStudio.Comm;

import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vivintsolar.SmartStudio.GUI.TeleprompterActivity;

import com.vivintsolar.SmartStudio.Model.CurrentState;

public class ActivePinger {
    private String url;
    private TeleprompterActivity activity;

    //Volley Resources
    private StringRequest stateRequest;
    private StringRequest manScrollRequest;
    private StringRequest syncPosition;
    private RequestQueue queue;
    final private float conversion = 1.0f; //The factor of conversion between control and teleprompter scroll location

    public ActivePinger(TeleprompterActivity activity, String url) {
        this.activity = activity;
        this.url = url;
    }


    public void start(){
        final Handler handler = new Handler();
        final int delay = 50; //milliseconds

        initializeComm();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateScrollPosition();
                handler.postDelayed(this,delay);
            }
        }, delay);

    }

    private void initializeComm(){
        String state_url = url + "/scroll";
        String manscroll_url = url + "/manscroll";
        String sync_position_url = url + "/syncposition";
        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(activity.getApplicationContext());
        // Request a string response from the provided URL.
        stateRequest = new StringRequest(Request.Method.GET, state_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CurrentState.setScroll_state(Integer.parseInt(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("network error", error.toString());
            }
        });
        manScrollRequest = new StringRequest(Request.Method.GET, manscroll_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CurrentState.setMan_scroll(Float.parseFloat(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("network error", error.toString());
            }
        });
        syncPosition = new StringRequest(Request.Method.GET, sync_position_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        activity.syncTo((int)(Integer.parseInt(response) * conversion));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("network error", error.toString());
            }
        });
    }


    private void updateScrollState(){

        // Add the request to the RequestQueue.
        queue.add(stateRequest);
    }

    private void updateScrollPosition(){
        queue.add(syncPosition);
    }
}
