package com.vivintsolar.SmartStudio.GUI;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vivintsolar.SmartStudio.R;

import java.io.IOException;

import com.vivintsolar.SmartStudio.Comm.AndroidWebServer;
import com.vivintsolar.SmartStudio.Comm.getIPAddress;
import com.vivintsolar.SmartStudio.Model.CurrentState;
import com.vivintsolar.SmartStudio.Model.Script;

public class AddClientActivity extends AppCompatActivity {

    TextView ip_display;
    EditText script_container;
    Button save_script_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        ip_display = findViewById(R.id.ip_address);
        script_container = findViewById(R.id.script_text);
        save_script_button = findViewById(R.id.save_script_button);

        //Get IP Address
        getIPAddress ip_getter = new getIPAddress(ip_display);
        ip_getter.execute();

        //Start Server We'll Loop until we get a good port number
        boolean searching = true;
        int port = 8080;
        while (searching) {
            AndroidWebServer server = new AndroidWebServer(port, this);
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
                port++;
            } finally {
                searching = false;
            }
        }
        CurrentState.setScroll_state(60);

        //Set button click listener
        save_script_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newScript = script_container.getText().toString();
//                newScript = newScript.replaceAll("\n\n","\n");
                Script.setScript(newScript + "\n\n\n");
                CurrentState.setScroll_state(0);
                CurrentState.setTeleprompter_active(true);
                Intent intent = new Intent(v.getContext(), MainControlActivity.class);
                startActivity(intent);
            }
        });

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }
}
