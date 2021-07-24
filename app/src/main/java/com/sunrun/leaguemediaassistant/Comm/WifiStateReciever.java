package com.sunrun.leaguemediaassistant.Comm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.sunrun.leaguemediaassistant.GUI.NoWifiActivity;
import com.sunrun.leaguemediaassistant.Model.CurrentState;

public class WifiStateReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (!CurrentState.isApp_open()){
            return;
        }

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
