package com.vivintsolar.SmartStudio.Comm;

import android.os.AsyncTask;

public class EditScriptDelay extends AsyncTask<Void, Integer, Void> {


    private OnEventListener<String> callBack;

    public EditScriptDelay(OnEventListener callBack) {
        this.callBack = callBack;
    }

    @Override
    protected void onPostExecute(Void s) {
        if(callBack != null){
            callBack.onSuccess("success");
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values[0]);
    }

    @Override
    protected Void doInBackground(Void... a) {
        for (int i = 0; i < 20; i++){
            publishProgress(i*5);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
