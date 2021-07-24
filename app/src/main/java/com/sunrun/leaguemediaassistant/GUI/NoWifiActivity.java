package com.sunrun.leaguemediaassistant.GUI;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.sunrun.leaguemediaassistant.Model.CurrentState;
import com.sunrun.leaguemediaassistant.R;

import java.util.Observable;
import java.util.Observer;

public class NoWifiActivity extends AppCompatActivity implements Observer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_wifi);

        //Set up observer
        CurrentState.putObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(!CurrentState.isNo_wifi_activity_up()){
            finish();
        }
    }
}
