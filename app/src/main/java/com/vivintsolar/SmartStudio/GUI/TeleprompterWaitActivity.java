package com.vivintsolar.SmartStudio.GUI;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.vivintsolar.SmartStudio.R;

import com.vivintsolar.SmartStudio.Comm.WaitPinger;
import com.vivintsolar.SmartStudio.Model.CurrentState;

public class TeleprompterWaitActivity extends AppCompatActivity {

    private WaitPinger waitPinger;
    private TextView waitLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teleprompter_wait);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        waitLabel = findViewById(R.id.waiting_label);

        CurrentState.setScroll_state(61);

        //Start WaitPinger
        String url = getIntent().getStringExtra("tab_address");
        waitPinger = new WaitPinger(this ,url);
    }

    @Override
    protected void onStart() {
        super.onStart();
        waitPinger.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        waitPinger.setActivity_started(false);
        waitPinger.start();
        waitLabel.setText("Connecting to Controller");
    }

    public void startTeleprompter(){
        waitLabel.setText("Launching");
        Intent current_intent = getIntent();
        String tab_url = current_intent.getStringExtra("tab_address");
        String vmix_url = current_intent.getStringExtra("vmix_address");
        Intent intent = new Intent(this, TeleprompterActivity.class);
        intent.putExtra("tab_address",tab_url);
        intent.putExtra("vmix_address", vmix_url);
        startActivity(intent);
    }

    public void setConnected(){
        waitLabel.setText("Ready");
    }
}
