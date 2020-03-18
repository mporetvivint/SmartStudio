package com.vivintsolar.SmartStudio.GUI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.QRCode;
import com.vivintsolar.SmartStudio.R;

import java.io.IOException;

import com.vivintsolar.SmartStudio.Comm.AndroidWebServer;
import com.vivintsolar.SmartStudio.Comm.getIPAddress;
import com.vivintsolar.SmartStudio.Model.CurrentState;
import com.vivintsolar.SmartStudio.Model.Script;

public class AddClientActivity extends AppCompatActivity {

    EditText script_container;
    Button save_script_button;
    ImageView qr_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        script_container = findViewById(R.id.script_text);
        save_script_button = findViewById(R.id.save_script_button);
        qr_view = findViewById(R.id.qr_display);




        //Start Server We'll Loop until we get a good port number
        boolean searching = true;
        CurrentState.setPort_number(8080);
        while (searching) {
            AndroidWebServer server = new AndroidWebServer(CurrentState.getPort_number(), this);
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
                CurrentState.increment_port();
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

    @Override
    protected void onResume() {
        super.onResume();
        //Get IP Address
        getIPAddress ip_getter = new getIPAddress(this){
            @Override
            public void onCompletion(Bitmap s){
                if(s != null) {
                    qr_view.setImageBitmap(s);
                }
            }
        };
        ip_getter.execute();

    }
}
