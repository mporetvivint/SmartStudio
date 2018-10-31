package com.vivintsolar.SmartStudio.GUI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.vivintsolar.SmartStudio.Model.CurrentState;
import com.vivintsolar.SmartStudio.R;

public class ModePickerActivity extends AppCompatActivity {

    ImageButton controlMode;
    ImageButton teleprompterMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_picker);

        controlMode = findViewById(R.id.control_mode);
        teleprompterMode = findViewById(R.id.teleprompter_mode);

        CurrentState.setApp_open(true);


        controlMode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddClientActivity.class);
                startActivity(intent);

            }
        });

        teleprompterMode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ClientAddress.class);
                startActivity(intent);

            }
        });
    }
}
