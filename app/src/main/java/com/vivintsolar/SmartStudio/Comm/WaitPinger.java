package com.vivintsolar.SmartStudio.Comm;

import android.os.Handler;

import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vivintsolar.SmartStudio.GUI.TeleprompterWaitActivity;

import com.vivintsolar.SmartStudio.Model.CurrentState;
import com.vivintsolar.SmartStudio.Model.Script;

public class WaitPinger {
    private String url;
    private TeleprompterWaitActivity activity;
    private boolean activity_started;

    //Volley Resources
    private StringRequest statusRequest;
    private StringRequest scriptRequest;
    private RequestQueue queue;

    public WaitPinger(TeleprompterWaitActivity activity, String url) {
        this.activity = activity;
        this.url = url;
        activity_started = false;
    }


    public void start(){
        final Handler handler = new Handler();
        final int delay = 1000; //milliseconds

        initializeComm();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateScrollState();
                int state = CurrentState.getScroll_state();
                if(state == 60){
                    activity.setConnected();
                    handler.postDelayed(this, delay);
                }
                else if(state != 61) {
                    //Get script
                    getScript();
                    CurrentState.setTeleprompter_active(true);
                    CurrentState.setSync_active(false);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //Start Teleprompter activity
                    if (!activity_started) {
                        activity_started = true;
                        Log.d("activity_start", "got response starting teleprompter");
                        activity.startTeleprompter();
                    }
                }else {
                    handler.postDelayed(this, delay);
                    Log.d("activity_start", "no response - waiting");
                }
            }
        }, delay);

    }

    private void initializeComm(){
        queue =  Volley.newRequestQueue(activity.getApplicationContext());

        String statusURL = url + "/scroll";
        String scriptURL = url + "/script";

        statusRequest = new StringRequest(Request.Method.GET, statusURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("got_response", response);
                        CurrentState.setScroll_state(Integer.parseInt(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("network error", error.toString());
            }
        });

        scriptRequest = new StringRequest(Request.Method.GET, scriptURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("got_response", response);
                        //Parse Response
                        int i = 0;
                        boolean searching_width = true;
                        boolean searching_height = true;
                        StringBuilder width = new StringBuilder();
                        StringBuilder height = new StringBuilder();
                        char current;
                        while(searching_width){
                            current = response.charAt(i++);
                            if(current == '\t'){
                                searching_width = false;
                            }
                            else{
                                width.append(current);
                            }
                        }
                        while(searching_height){
                            current = response.charAt(i++);
                            if(current == '\t'){
                                searching_height = false;
                            }
                            else{
                                height.append(current);
                            }
                        }
                        response = response.substring(i);
                        Script.setWindow_size(Integer.parseInt(width.toString()), Integer.parseInt(height.toString()));
                        Script.setScript(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("network error", error.toString());
            }
        });
    }

    private void getScript(){

// Add the request to the RequestQueue.
        queue.add(scriptRequest);
    }

    private void updateScrollState(){

// Add the request to the RequestQueue.
        queue.add(statusRequest);
    }

    public void setActivity_started(boolean activity_started) {
        this.activity_started = activity_started;
    }
}
