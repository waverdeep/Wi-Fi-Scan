package com.tistory.coderush.signalcollect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

public class CheckPermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permission);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

            //사용자의 단말의 권한 중 위치 가져오기 권한이 허가되었는지 확인
            int permissionResult=checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);

            //패키지는 안드로이드 어플리케이션의 아이디이다.
            //현재 어플리케이션이 권한에 대해 거부되었는지 확인
            if(permissionResult== PackageManager.PERMISSION_DENIED){

                //사용자가 권한을 거부한 적이 있는지 확인
                //거부한적이 있으면 true를 리턴
                //거부한 적이 없으면 false를 리턴
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)){
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1000);

                }else{
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1000);
                }
            }else{

                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }


        }else{

            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //권한요청에 대한 응답 가져옴
    //요청코드 @ param requestCode
    //사용자가 요청한 권한들
    //권한에 대한 응답들(인덱스별로 매칭)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1000){

            //허용 했다면
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);

                //addpermission check
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                    startActivity(intent);
                    finish();
                }
            }else{
                Toast.makeText(this, "권한 요청을 거부하였습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
