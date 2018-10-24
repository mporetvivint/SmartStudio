package com.vivintsolar.SmartStudio.GUI;

import android.annotation.SuppressLint;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vivintsolar.SmartStudio.R;

import Comm.Scroller;
import Model.CurrentState;
import Model.Script;

public class MainControlActivity extends AppCompatActivity {

    private Button speed_1;
    private Button speed_2;
    private Button speed_3;
    private ImageButton play_pause;
    private ImageView reverse_area;
    private ImageView foward_area;
    private TextView script;
    private ScrollView script_container;
    private Scroller scroller;

    private int speed;
    private boolean scrolling;
    public boolean fling_process;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_control);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        speed = 1;
        scrolling = false;
        fling_process = false;


        speed_1 = findViewById(R.id.speed_1);
        speed_2 = findViewById(R.id.speed_2);
        speed_3 = findViewById(R.id.speed_3);
        play_pause = findViewById(R.id.play_pause);
        reverse_area = findViewById(R.id.reverse_area);
        foward_area = findViewById(R.id.forward_area);
        script = findViewById(R.id.control_script);
        script_container = findViewById(R.id.control_script_container);
        scroller = new Scroller(script_container);

        //Initialize script
        script.setText(Script.getScript());

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

        play_pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startStopScroll();
            }
        });

        script_container.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = script_container.getScrollY(); // For ScrollView
                Log.d("dynamicScroll", Integer.toString(scrollY));
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
}
