package com.tistory.coderush.signalcollect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tistory.coderush.signalcollect.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private WifiManager wifiManager;
    private IntentFilter intentFilter;
    private String action;

    private int loopCount=0;
    private JSONArray searchData;

    String address;
    String buildName;
    String spaceName;
    int floor;
    double inputX;
    double inputY;
    String deviceName;

    Date date;
    SimpleDateFormat simpleDateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.setActivity(this);

        binding.edtDevice.setText(Build.MODEL);
        Log.i("deviceName",Build.MODEL);

        date = new Date();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N){

            wifiManager=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        }else{
            wifiManager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
        }

        binding.btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String address = binding.edtAddress.getText().toString().replaceAll(" ", "");
                    String buildName = binding.edtBuildName.getText().toString().replaceAll(" ", "");
                    String spaceName = binding.edtSpaceName.getText().toString().replaceAll(" ", "");
                    int floor = Integer.parseInt(binding.edtFloor.getText().toString());
                    double inputX = Double.parseDouble(binding.edtInputX.getText().toString());
                    double inputY = Double.parseDouble(binding.edtInputY.getText().toString());
                    String deviceName = binding.edtDevice.getText().toString().replaceAll(" ", "");

                    MainActivity.this.address = address;
                    MainActivity.this.buildName = buildName;
                    MainActivity.this.spaceName = spaceName;
                    MainActivity.this.floor = floor;
                    MainActivity.this.inputX = inputX;
                    MainActivity.this.inputY = inputY;
                    MainActivity.this.deviceName = deviceName;

                    if (address.equals("")) {
                        Toast.makeText(MainActivity.this, "내용을 모두 입력해 주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        binding.btnCollect.setEnabled(false);
                        searchData = new JSONArray();
                        searchAP();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "내용을 모두 입력해 주세요", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void searchAP(){
        if(!wifiManager.isWifiEnabled()){
            Toast.makeText(this, "와이파이가 꺼져있습니다", Toast.LENGTH_SHORT).show();
            binding.btnCollect.setEnabled(true);
        }else{
            //와이파이가 정상적으로 켜져있다면

                    //An Access point has completed,
                    //and results are available.
                    //Call getScanResults() to obtain the result.
                    //The broadcast intent may contain an extra field with the key.
                    //EXTRA_RESULT_UPDATES and a boolean value indicating if the scan was successful.
                    intentFilter=new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

            //Broadcast intent action indicating that the state of WIFI connectivity has changed.
            //An extra provides the new state int the form of a NetworkInfo object.
            intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            //intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
            //intentFilter.addAction(WifiManager.EXTRA_RESULTS_UPDATED);
            registerReceiver(wifiScanReceiver,intentFilter);//메니페스트로 등록하는 것이 아닌 자바로 reciver 등록
            wifiManager.startScan();//와이파이 스캔 시작
            Log.i("wifiManager : ","startAction");
        }
    }

    private BroadcastReceiver wifiScanReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            action=intent.getAction();
            if(action!=null){
                if(action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
                    getWiFiScanResult();
                    if(!(loopCount>9)){
                        //만약에 아직 10번 시도가 완료되지 않았다면
                        Log.i("loopCount : ",""+loopCount);
                        wifiManager.startScan();
                    }else{
                        //10번 시도가 완료되었을 경우
                        binding.btnCollect.setEnabled(true);
                        loopCount=0;
                        Log.i("loopCountReset : ","finish resetCount : "+loopCount);
                        return;//해당 메서드 탈출
                    }
                }
            }
        }
    };

    private void getWiFiScanResult(){
        //Toast.makeText(this, "와이파이 스캔중.ㄴ..", Toast.LENGTH_SHORT).show();
        List<ScanResult> scanResults=wifiManager.getScanResults();
        Log.i("wifiCount",scanResults.size()+"");

        binding.tvResult.setText("스캔 와이파이 갯수"+scanResults.size()+"\n");
        String strResult="";

        JSONObject eachData=new JSONObject();

        JSONArray datas=new JSONArray();

        for(ScanResult result:scanResults){
            JSONObject temp=new JSONObject();
            Log.i("time",""+System.currentTimeMillis());
            Log.d("SSID : "+result.SSID,"level : "+result.level+", BSSID : "+result.BSSID);
            strResult+="SSID:"+result.SSID+" level:"+result.level+" BSSID:"+result.BSSID+"\n";
            try {
                temp.put(""+result.BSSID,""+result.level);
                datas.put(temp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            eachData.put("datas",datas);
            eachData.put("timeStamp",""+simpleDateFormat.format(date));
            eachData.put("address",""+address);
            eachData.put("buildName",""+buildName);
            eachData.put("spaceName",""+spaceName);
            eachData.put("floor",""+floor);
            eachData.put("inputX",""+inputX);
            eachData.put("inputY",""+inputY);
            eachData.put("deviceName",""+deviceName);
            searchData.put(eachData);
        } catch (JSONException e) {
            e.printStackTrace();
        }




        binding.tvResult.setText(binding.tvResult.getText().toString()+strResult);



        if(loopCount==9){

            Toast.makeText(this, "와이파이 스캔 완료", Toast.LENGTH_SHORT).show();


            binding.tvResult.setText(searchData.length()+"\n"+searchData+"");

            sendDatas(searchData);
            System.out.println(searchData);

            searchData=null;
            searchData=new JSONArray();
            //unregisterReceiver(wifiScanReceiver);

            //loopCount=0; //-> 무한으로 돌리고 싶을때


        }

        loopCount++;
        //tvCount.setText(loopCount+"");
    }


    @Override
    protected void onPause() {
        super.onPause();
        try{
            unregisterReceiver(wifiScanReceiver);
        }catch (Exception e){
            Log.i("register","이미 종료햇음");
        }

    }

    private void sendDatas(JSONArray temp){
        ConnectionAsyncTask connectionAsyncTask;
        ContentValues contentValues=new ContentValues();
        contentValues.put("action","INPUT");
        contentValues.put("addData",""+temp);

        connectionAsyncTask=new ConnectionAsyncTask(SignalConst.URL,contentValues,this);
        connectionAsyncTask.execute();
    }
}
