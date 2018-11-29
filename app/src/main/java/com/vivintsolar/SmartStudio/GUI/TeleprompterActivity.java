package com.vivintsolar.SmartStudio.GUI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.vivintsolar.SmartStudio.Comm.GetUpdatedScript;
import com.vivintsolar.SmartStudio.Comm.OnEventListener;
import com.vivintsolar.SmartStudio.Model.CurrentState;
import com.vivintsolar.SmartStudio.R;

import com.vivintsolar.SmartStudio.Comm.ActivePinger;
import com.vivintsolar.SmartStudio.Comm.Scroller;
import com.vivintsolar.SmartStudio.Model.Script;

public class TeleprompterActivity extends AppCompatActivity {

    //Instantiate Views
    private WebView tally_light;
    private TextView script_view;
    public ScrollView script_container;
    private boolean scrolling;
    private Scroller scroller;
    private ActivePinger pinger;
    private RelativeLayout loading_script_container;
    private RelativeLayout script_failed_container;

    //Fields
    private String tab_url;
    private WifiManager wifiManager;
    private boolean mirrored;
    //Factor to scale script to fill screen
    float scaleX = 1.78f;
    float scaleY = 1.78f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mirrored = false;

        //Determine which layout we need
        String tally_url = getIntent().getStringExtra("vmix_address");
        if (tally_url.equals("none")) {
            //No tally light
            setContentView(R.layout.activity_teleprompter_only);
            //Script
            script_container = findViewById(R.id.prompter_only_scroll_container);
            script_view = findViewById(R.id.prompter_only_script_container);
            script_container.setScaleX(scaleX);
            script_container.setScaleY(scaleY);

            //Loading Script
            loading_script_container = findViewById(R.id.loading_script_container);
            script_failed_container = findViewById(R.id.updated_failed_container);
        } else {
            //There is a tally light
            setContentView(R.layout.activity_teleprompter);


            //Setup tally light view
            tally_url = "http://" + tally_url + "/tally";
            tally_light = findViewById(R.id.tally_light);
            tally_light.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(request.getUrl().toString());
                    return false;
                }
            });
            tally_light.loadUrl(tally_url);
            tally_light.setBackgroundColor(Color.TRANSPARENT);
            tally_light.getSettings().setJavaScriptEnabled(true); //Required to show correctly

            //Script
            script_container = findViewById(R.id.script_scroll_container);
            script_view = findViewById(R.id.script_container);
            script_container.setScaleX(scaleX);
            script_container.setScaleY(scaleY);

            //Loading Script
            loading_script_container = findViewById(R.id.loading_script_container_tally);
            script_failed_container = findViewById(R.id.updated_failed_container_tally);

        }

        //Common Setup to both layouts
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Set up scrolling
        scrolling = false;

        scroller = new Scroller(script_container);

        //Get script
        script_view.setText(Script.getScript());

        //Setup on touch listener
        script_failed_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(script_failed_container);
                script_failed_container.setVisibility(View.INVISIBLE);
            }
        });


        //Set up pinger
        tab_url = getIntent().getStringExtra("tab_address");
        pinger = new ActivePinger(this, tab_url);


        script_container.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = script_container.getScrollY(); // For ScrollView
                Log.d("dynamicScroll", Integer.toString(scrollY));
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        pinger.start();
    }

    public void syncTo(int position){
        script_container.smoothScrollTo(0,position);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP://Update Script
                if (action == KeyEvent.ACTION_DOWN) {
                    updateScript();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN://Mirror Script
                if (action == KeyEvent.ACTION_DOWN) {
                    if (mirrored){
                        script_container.setScaleX(scaleX);
                        script_container.setScaleY(scaleY);
                        mirrored = false;
                    }
                    else{
                        script_container.setScaleX(-1.4f);
                        script_container.setScaleY(1.4f);
                        mirrored = true;
                    }

                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    private void updateScript(){
        TransitionManager.beginDelayedTransition(loading_script_container);
        loading_script_container.setVisibility(View.VISIBLE);


        GetUpdatedScript getUpdatedScript = new GetUpdatedScript(tab_url, new OnEventListener() {
            @Override
            public void onSuccess(Object object) {
                doneUpdating((String) object);
            }

            @Override
            public void onFail(){
                TransitionManager.beginDelayedTransition(loading_script_container);
                loading_script_container.setVisibility(View.GONE);
                TransitionManager.beginDelayedTransition(script_failed_container);
                script_failed_container.setVisibility(View.VISIBLE);
            }
        }, this);
        getUpdatedScript.execute();
    }

    public void doneUpdating(String script){
        script_view.setText(Script.getScript());
        TransitionManager.beginDelayedTransition(loading_script_container);
        loading_script_container.setVisibility(View.GONE);
    }
}
