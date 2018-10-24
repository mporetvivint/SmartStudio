package com.vivintsolar.SmartStudio.GUI;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
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
    private ProgressBar progressSpinner;
    private ImageView updateDoneImage;

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

            //Loading Script
            loading_script_container = findViewById(R.id.loading_script_container);
            progressSpinner = findViewById(R.id.update_script_progress);
            updateDoneImage = findViewById(R.id.update_done_image);
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    updateScript();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    updateScript();
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    private void updateScript(){
        //TODO get the animation working to show check mark on completion
//        progressSpinner.setAlpha(1.0f);
//        updateDoneImage.setAlpha(0.0f);
        updateDoneImage.setVisibility(View.INVISIBLE);
        TransitionManager.beginDelayedTransition((ViewGroup) loading_script_container);
        loading_script_container.setVisibility(View.VISIBLE);

        GetUpdatedScript getUpdatedScript = new GetUpdatedScript("1.1.1.1", new OnEventListener() {
            @Override
            public void onSuccess(Object object) {
                doneUpdating((String) object);
            }
        });
        getUpdatedScript.execute();
    }

    public void doneUpdating(String script){
        updateDoneImage.setVisibility(View.VISIBLE);
        Script.setScript(script);
        script_view.setText(script);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TransitionManager.beginDelayedTransition((ViewGroup) loading_script_container);
        loading_script_container.setVisibility(View.GONE);
    }

    public void hideUpdateOverlay(){
        loading_script_container.setAlpha(0.0f);
    }
}
