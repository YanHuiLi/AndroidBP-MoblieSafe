package com.example.archer.mobliesafe;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.tv_version);

        assert textView != null;
        textView.setText("版本号："+getVersionName());

    }


    private String getVersionName(){

        PackageManager packageManager=getPackageManager();

        try {
             PackageInfo packageInfo=packageManager.getPackageInfo(getPackageName(),PackageManager.GET_ACTIVITIES);

            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;

            System.out.println("versionName="+versionName+";versionCode="+versionCode);

             return  versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //没找到包名的时候处理异常
            e.printStackTrace();
        }

        return "";
    }


}
