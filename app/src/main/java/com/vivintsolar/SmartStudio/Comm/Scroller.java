package com.vivintsolar.SmartStudio.Comm;

import android.os.Handler;
import android.util.Log;
import android.widget.ScrollView;

import com.vivintsolar.SmartStudio.Model.CurrentState;

public class Scroller {
    private ScrollView scrollView;

    final private Handler handler = new Handler();
    final private int delay = 50; //milliseconds
    private boolean scrolling;

    public Scroller(ScrollView scrollView) {
        this.scrollView = scrollView;
        scrolling = false;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            scrollView.smoothScrollBy(0,CurrentState.getScroll_state() * 2);
            if (CurrentState.getScroll_state() != 0) {
                handler.postDelayed(this, delay);
            }
        }
    };
    public void start(){


        scrolling = true;
        handler.postDelayed(runnable, delay);




    }
    public void stop(){

        scrolling = false;

        handler.removeCallbacks(runnable);
    }
}

