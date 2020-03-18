package com.vivintsolar.SmartStudio.GUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.vivintsolar.SmartStudio.Comm.OnEventListener;
import com.vivintsolar.SmartStudio.Comm.getIPAddress;
import com.vivintsolar.SmartStudio.R;

import com.vivintsolar.SmartStudio.Comm.Scroller;
import com.vivintsolar.SmartStudio.Model.CurrentState;
import com.vivintsolar.SmartStudio.Model.Script;

public class MainControlActivity extends AppCompatActivity {

    private Button speed_1;
    private Button speed_2;
    private Button speed_3;
    private Button edit_button;
    private ProgressBar editProgressBar;
    private ImageButton play_pause;
    private ImageView reverse_area;
    private ImageView foward_area;
    private ImageView qr_display;
    private TextView script;
    private ScrollView script_container;
    private Scroller scroller;
    private RelativeLayout hold_to_edit;
    private EditScriptDelay scriptDelay;

    private int speed;
    private boolean scrolling;
    public boolean fling_process;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_control);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        speed = 3;
        scrolling = false;
        fling_process = false;


        speed_1 = findViewById(R.id.speed_1);
        speed_2 = findViewById(R.id.speed_2);
        speed_3 = findViewById(R.id.speed_3);
        edit_button = findViewById(R.id.edit_script_button);
        editProgressBar = findViewById(R.id.edit_progress);
        play_pause = findViewById(R.id.play_pause);
        reverse_area = findViewById(R.id.reverse_area);
        foward_area = findViewById(R.id.forward_area);
        script = findViewById(R.id.control_script);
        script_container = findViewById(R.id.control_script_container);
        hold_to_edit = findViewById(R.id.hold_to_edit_container);
        scroller = new Scroller(script_container);
        qr_display = findViewById(R.id.mn_cntrl_qr);

        //Initialize script
        script.setText(Script.getScript());

        //set script window size
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density  = metrics.density;
        Script.setWindow_size((int) (metrics.widthPixels/density), 384);

        //Set onclick listeners
        speed_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                speed = 1;
                clearSpeedColors();
                speed_1.setBackgroundTintList(ContextCompat.getColorStateList(v.getContext(), R.color.speedOrange));
                if (scrolling) {
                    CurrentState.setScroll_state(speed);
                }
            }
        });

        speed_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                speed = 2;
                clearSpeedColors();
                speed_2.setBackgroundTintList(ContextCompat.getColorStateList(v.getContext(), R.color.speedOrange));
                if (scrolling) {
                    CurrentState.setScroll_state(speed);
                }
            }
        });

        speed_3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                speed = 3;
                clearSpeedColors();
                speed_3.setBackgroundTintList(ContextCompat.getColorStateList(v.getContext(), R.color.speedOrange));
                if (scrolling) {
                    CurrentState.setScroll_state(speed);
                }
            }
        });

        edit_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        TransitionManager.beginDelayedTransition(hold_to_edit);
                        hold_to_edit.setVisibility(View.VISIBLE);
                        scriptDelay = new EditScriptDelay(new OnEventListener() {
                            @Override
                            public void onSuccess(Object object) {
                                finish();
                            }

                            @Override
                            public void onFail() {

                            }
                        });
                        scriptDelay.execute();
                        return true;
                    case MotionEvent.ACTION_UP:
                        TransitionManager.beginDelayedTransition(hold_to_edit);
                        hold_to_edit.setVisibility(View.GONE);
                        scriptDelay.cancel(true);
                        return true;
                }
                return false;
            }
        });

        play_pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startStopScroll();
            }
        });

        script_container.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = script_container.getScrollY(); // For ScrollView
                CurrentState.setScroll_position(scrollY);
            }
        });

        foward_area.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        startScroll();
                        break;
                    case MotionEvent.ACTION_UP:
                        stopScroll();
                        break;
                }
                return true;
            }
        });

        reverse_area.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        play_pause.setImageDrawable(getDrawable(R.drawable.pause_icon));
                        scrolling = true;
                        CurrentState.setScroll_state(-speed);
                        scroller.start();
                        break;
                    case MotionEvent.ACTION_UP:
                        stopScroll();
                        break;
                }
                return true;
            }
        });



    }

    public void startStopScroll(){
        if (scrolling) { //Pause scroll
            play_pause.setImageDrawable(getDrawable(R.drawable.play_icon));
            scrolling = false;
            CurrentState.setScroll_state(0);
        } else { //Play scroll
            play_pause.setImageDrawable(getDrawable(R.drawable.pause_icon));
            scrolling = true;
            CurrentState.setScroll_state(speed);
            scroller.start();
        }
    }

    public void startScroll(){
        play_pause.setImageDrawable(getDrawable(R.drawable.pause_icon));
        scrolling = true;
        CurrentState.setScroll_state(speed);
        scroller.start();
    }
    public void stopScroll(){
        play_pause.setImageDrawable(getDrawable(R.drawable.play_icon));
        scrolling = false;
        CurrentState.setScroll_state(0);
    }

    //Utility Functions
    private void clearSpeedColors(){
        speed_1.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.speedButtonDefault));
        speed_2.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.speedButtonDefault));
        speed_3.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.speedButtonDefault));
    }

    //Delay for script edit
    private class EditScriptDelay extends AsyncTask<Void, Integer, Void> {


        private OnEventListener<String> callBack;

        public EditScriptDelay(OnEventListener callBack) {
            this.callBack = callBack;
        }

        @Override
        protected void onPostExecute(Void s) {
            if(callBack != null){
                callBack.onSuccess("success");
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values[0]);
            editProgressBar.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... a) {
            for (int i = 0; i < 100; i++){
                if(isCancelled()){
                    return null;
                }
                publishProgress(i);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Get IP Address
        getIPAddress ip_getter = new getIPAddress(this){
            @Override
            public void onCompletion(Bitmap s){
                if(s != null) {
                    qr_display.setImageBitmap(s);
                }
            }
        };
        ip_getter.execute();

    }

}
