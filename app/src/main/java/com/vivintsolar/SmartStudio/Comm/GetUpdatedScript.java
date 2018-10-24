package com.vivintsolar.SmartStudio.Comm;

import android.media.MediaDrm;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.widget.TextView;

import static android.content.Context.WIFI_SERVICE;

public class GetUpdatedScript extends AsyncTask<Void,Void,String> {

    String ip_address;

    private OnEventListener<String> callBack;

    public GetUpdatedScript(String ip_address, OnEventListener callBack) {
        this.ip_address = ip_address;
        this.callBack = callBack;
    }

    @Override
    protected void onPostExecute(String s) {
        if(callBack != null){
            callBack.onSuccess(s);
        }
    }

    @Override
    protected String doInBackground(Void... addClientActivities) {
        return "script has been updated successfully";
    }
}
