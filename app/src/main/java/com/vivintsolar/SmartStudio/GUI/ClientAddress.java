package com.vivintsolar.SmartStudio.GUI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vivintsolar.SmartStudio.R;

public class ClientAddress extends AppCompatActivity {

    EditText vmix_ip;
    EditText vmix_port;
    TextView vmix_ip_label;
    TextView vmix_port_label;
    EditText tab_ip;
    EditText tab_port;
    TextView tab_ip_label;
    TextView tab_port_label;
    Button start_button;
    CheckBox tally_checkbox;
    CheckBox prompter_checkbox;
    boolean tally_light;
    boolean teleprompter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_address);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Store state of check boxes
        tally_light = false;
        teleprompter = true;


        //Set up widgets
        vmix_ip = findViewById(R.id.vmix_ip);
        vmix_port = findViewById(R.id.vmix_port);
        tab_ip = findViewById(R.id.tablet_ip);
        tab_port = findViewById(R.id.tablet_port);
        start_button = findViewById(R.id.start_button);
        vmix_ip_label = findViewById(R.id.vmix_ip_title);
        vmix_port_label = findViewById(R.id.vmix_port_title);
        tab_ip_label = findViewById(R.id.tablet_ip_title);
        tab_port_label= findViewById(R.id.tablet_port_title);
        tally_checkbox = findViewById(R.id.tally_cheeck);
        prompter_checkbox = findViewById(R.id.prompter_check);

        start_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(tally_light && teleprompter) {
                    String vmix_ip_address = vmix_ip.getText().toString();
                    String vmix_port = ClientAddress.this.vmix_port.getText().toString();
                    String tab_ip_address = tab_ip.getText().toString();
                    String tab_port = ClientAddress.this.tab_port.getText().toString();

                    Intent intent = new Intent(v.getContext(), TeleprompterWaitActivity.class);
                    intent.putExtra("vmix_address", vmix_ip_address + ":" + vmix_port);
                    intent.putExtra("tab_address", tab_ip_address + ":" + tab_port);
                    startActivity(intent);
                }
                else if (tally_light){
                    String vmix_ip_address = vmix_ip.getText().toString();
                    String vmix_port = ClientAddress.this.vmix_port.getText().toString();

                    Intent intent = new Intent(v.getContext(), TallyOnlyActivity.class);
                    intent.putExtra("address",vmix_ip_address+":"+vmix_port);
                    startActivity(intent);
                }
                else if (teleprompter){
                    String vmix_ip_address = vmix_ip.getText().toString();
                    String vmix_port = ClientAddress.this.vmix_port.getText().toString();
                    String tab_ip_address = tab_ip.getText().toString();
                    String tab_port = ClientAddress.this.tab_port.getText().toString();

                    Intent intent = new Intent(v.getContext(), TeleprompterWaitActivity.class);
                    intent.putExtra("vmix_address", "none");
                    intent.putExtra("tab_address", tab_ip_address + ":" + tab_port);
                    startActivity(intent);
                }
                else {
                    Toast error_toast = Toast.makeText(v.getContext(),"Please choose something for me to do! I can't do nothing!", Toast.LENGTH_LONG);
                    error_toast.show();
                }

            }
        });

        //Disable tally by default
        vmix_ip.setEnabled(false);
        vmix_port.setEnabled(false);
        vmix_ip_label.setTextColor(getColor(R.color.disabledControls));
        vmix_port_label.setTextColor(getColor(R.color.disabledControls));
        vmix_ip.setTextColor(getColor(R.color.disabledControls));
        vmix_port.setTextColor(getColor(R.color.disabledControls));

        tally_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                tally_light = b;
                if (tally_light){
                    vmix_ip.setEnabled(true);
                    vmix_port.setEnabled(true);
                    vmix_ip_label.setTextColor(getColor(R.color.enabledControls));
                    vmix_port_label.setTextColor(getColor(R.color.enabledControls));
                    vmix_ip.setTextColor(getColor(R.color.enabledControls));
                    vmix_port.setTextColor(getColor(R.color.enabledControls));
                }
                else{
                    vmix_ip.setEnabled(false);
                    vmix_port.setEnabled(false);
                    vmix_ip_label.setTextColor(getColor(R.color.disabledControls));
                    vmix_port_label.setTextColor(getColor(R.color.disabledControls));
                    vmix_ip.setTextColor(getColor(R.color.disabledControls));
                    vmix_port.setTextColor(getColor(R.color.disabledControls));
                }

            }
        });

        prompter_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                teleprompter = b;
                if (teleprompter){
                    tab_ip.setEnabled(true);
                    tab_port.setEnabled(true);
                    tab_ip_label.setTextColor(getColor(R.color.enabledControls));
                    tab_port_label.setTextColor(getColor(R.color.enabledControls));
                    tab_ip.setTextColor(getColor(R.color.enabledControls));
                    tab_port.setTextColor(getColor(R.color.enabledControls));
                }
                else{
                    tab_ip.setEnabled(false);
                    tab_port.setEnabled(false);
                    tab_ip_label.setTextColor(getColor(R.color.disabledControls));
                    tab_port_label.setTextColor(getColor(R.color.disabledControls));
                    tab_ip.setTextColor(getColor(R.color.disabledControls));
                    tab_port.setTextColor(getColor(R.color.disabledControls));
                }

            }
        });

    }

}
