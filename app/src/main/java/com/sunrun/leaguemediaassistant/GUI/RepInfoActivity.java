package com.sunrun.leaguemediaassistant.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.sunrun.leaguemediaassistant.Comm.OnEventListener;
import com.sunrun.leaguemediaassistant.Comm.SendRep;
import com.sunrun.leaguemediaassistant.R;

public class RepInfoActivity extends AppCompatActivity {

    private EditText fn;
    private EditText ln;
    private EditText em;
    private Button btn_save;
    private ProgressBar progressBar;
    String ipaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_info);

        ipaddress = getIntent().getStringExtra("url");

        //Setup GUI
        fn = findViewById(R.id.editTextFN);
        ln = findViewById(R.id.editTextLN);
        em = findViewById(R.id.editTextEM);
        btn_save = findViewById(R.id.btn_save);
        progressBar = findViewById(R.id.progressBar);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String first = fn.getText().toString().trim();
                String last = ln.getText().toString().trim();
                String email = em.getText().toString().trim();

                //verify text fields
                boolean valid = !fn.getText().toString().equals("") &&
                        !ln.getText().toString().equals("") &&
                        !em.getText().toString().equals("");

                if(valid){//Send volley request
                    addRep();
                }
                else {
                    makeToast("Please fill out all of the information");
                }
            }
        });

    }

    private void submitRep(){
        //verify text fields
        boolean valid = fn.getText().toString() != "" &&
                ln.getText().toString() != "" &&
                em.getText().toString() != "";

        if(valid){//Send volley request

        }
    }

    private void addRep(){
        progressBar.setVisibility(View.VISIBLE);


        SendRep sendRep = new SendRep(ipaddress,fn.getText().toString(),
                ln.getText().toString(),em.getText().toString(), new OnEventListener() {
            @Override
            public void onSuccess(Object object) {
                progressBar.setVisibility(View.GONE);
                fn.setText("");
                ln.setText("");
                em.setText("");
            }

            @Override
            public void onFail(){
                progressBar.setVisibility(View.GONE);
            }
        }, this);
        sendRep.execute();
    }

    public void makeToast(String toastText){
        Toast.makeText(this,toastText,Toast.LENGTH_LONG).show();
    }
}