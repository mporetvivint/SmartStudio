package com.vivintsolar.SmartStudio.Comm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.widget.TextView;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vivintsolar.SmartStudio.Model.CurrentState;

import java.lang.ref.WeakReference;

import static android.content.Context.WIFI_SERVICE;

/*
* Class that provides a QR code with the device IP address, to be used for device pairing
*/

public abstract class getIPAddress extends AsyncTask<Void,Void,Bitmap> {

    //Context is needed to obtain the IP
    private WeakReference<Context> context;

    public getIPAddress(Context context) {
        this.context = new WeakReference<>(context);
    }

    //After execution the bitmap image is returned via an abstract function in the calling class
    @Override
    protected void onPostExecute(Bitmap m) {
        onCompletion(m);
    }

    //Background task to get IP Address and generate QR code bitmap
    @Override
    protected Bitmap doInBackground(Void... addClientActivities) {

        WifiManager wm;
        wm = (WifiManager) context.get().getApplicationContext().getSystemService(WIFI_SERVICE);

        String address;
        address = "http://" + Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress()) + ":" + CurrentState.getPort_number();
        return generateQR(address);
    }

    //Abstract method to return QR code bitmap to calling class
    public abstract void onCompletion(Bitmap m);

    /*QR Code Generator
    *@Param: String containing IP address
    * @Return: Bitmap of QR code containing the IP address
     */
    private Bitmap generateQR(String s){
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix matrix = writer.encode(s, BarcodeFormat.QR_CODE, 300, 300);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
