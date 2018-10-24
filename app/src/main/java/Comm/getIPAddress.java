package Comm;

import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.widget.TextView;


import java.net.InetAddress;
import java.net.UnknownHostException;

import static android.content.Context.WIFI_SERVICE;

public class getIPAddress extends AsyncTask<Void,Void,String> {

    private TextView ip_display;

    public getIPAddress(TextView ip_display) {
        this.ip_display = ip_display;
    }

    @Override
    protected void onPostExecute(String s) {
        ip_display.setText(s);
    }

    @Override
    protected String doInBackground(Void... addClientActivities) {
//        InetAddress inetAddress = null;
//        try {
//            inetAddress = InetAddress.getLocalHost();
//            return inetAddress.getHostAddress();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//            return "error";
//        }

        WifiManager wm = (WifiManager) ip_display.getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }
}
