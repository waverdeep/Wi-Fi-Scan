package com.tistory.coderush.signalcollect;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

public class ConnectionAsyncTask extends AsyncTask<String,String,String> {

    private String url;
    private ContentValues contentValues;
    private MainActivity mainActivity;

    public ConnectionAsyncTask(String url, ContentValues contentValues, MainActivity mainActivity){
        this.contentValues=contentValues;
        this.mainActivity=mainActivity;
        this.url=url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String result;
        RequestHttpURLConnection requestHttpURLConnection=new RequestHttpURLConnection();

        result=requestHttpURLConnection.request(url,contentValues);
        return result;
    }


    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


        Log.i("result",""+s);

        //mainActivity.setResult(s);
    }


}
