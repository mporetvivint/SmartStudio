package Comm;

import android.content.Intent;
import android.os.Handler;

import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vivintsolar.SmartStudio.GUI.TeleprompterActivity;
import com.vivintsolar.SmartStudio.GUI.TeleprompterWaitActivity;

import Model.CurrentState;
import Model.Script;

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
                if(state != 60) {
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
                        Log.d("activity_start", "starting teleprompter");
                        activity.startTeleprompter();
                    }
                }else {
                    handler.postDelayed(this, delay);
                }
            }
        }, delay);

    }

    private void initializeComm(){
        queue =  Volley.newRequestQueue(activity.getApplicationContext());

        String statusURL = "http://" + url + "/scroll";
        String scriptURL = "http://" + url + "/script";

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
