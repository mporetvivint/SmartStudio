package Comm;

import android.os.Handler;
import android.util.Log;
import android.widget.ScrollView;

import Model.CurrentState;

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
            Log.d("scrolltest","scrolltest: " + Boolean.toString(scrolling));
            scrollView.smoothScrollBy(0,CurrentState.getScroll_state() * 2);
            if (CurrentState.getScroll_state() != 0) {
                handler.postDelayed(this, delay);
            }
        }
    };
    public void start(){


        Log.d("scrolltest","start scroll");
        scrolling = true;
        handler.postDelayed(runnable, delay);




    }
    public void stop(){

        scrolling = false;

        Log.d("scrolltest","stop scroll: " + Boolean.toString(scrolling));
        handler.removeCallbacks(runnable);
    }
}

