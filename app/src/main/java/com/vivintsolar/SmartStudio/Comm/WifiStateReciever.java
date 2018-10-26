package com.vivintsolar.SmartStudio.Comm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.vivintsolar.SmartStudio.GUI.NoWifiActivity;
import com.vivintsolar.SmartStudio.Model.CurrentState;

public class WifiStateReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

        if(networkInfo!= null && (!networkInfo.isConnected())){
            if(!CurrentState.isNo_wifi_activity_up()) {
                CurrentState.setNo_wifi_activity_up(true);
                Intent noWifiIntent = new Intent(context, NoWifiActivity.class);
                context.startActivity(noWifiIntent);
            }
        }
        else if(networkInfo!= null && (networkInfo.isConnected())){
            CurrentState.setNo_wifi_activity_up(false);
        }
    }
}
