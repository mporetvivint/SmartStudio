package com.vivintsolar.SmartStudio.GUI;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Determine which layout we need
        String tally_url = getIntent().getStringExtra("vmix_address");
        if (tally_url.equals("none")){
            //No tally light
            setContentView(R.layout.activity_teleprompter_only);
            //Script
            script_container = findViewById(R.id.prompter_only_scroll_container);
            script_view = findViewById(R.id.prompter_only_script_container);
            script_container.setScaleX(1.78f);
            script_container.setScaleY(1.78f);
        }
        else{
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
            script_container.setScaleX(1.78f);
            script_container.setScaleY(1.78f);


        }

        //Common Setup to both layouts
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Set up scrolling
        scrolling = false;

        scroller = new Scroller(script_container);

        //Get script
        script_view.setText(Script.getScript());




        //Set up pinger
        String tab_url = getIntent().getStringExtra("tab_address");
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
}
