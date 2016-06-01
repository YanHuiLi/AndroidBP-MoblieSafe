package com.example.archer.mobliesafe;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.tv_version);

        assert textView != null;
        textView.setText("版本名称：" + getVersionName());

        checkVersion();
    }


    private String getVersionName() {

        PackageManager packageManager = getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);

            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;

            System.out.println("versionName=" + versionName + ";versionCode=" + versionCode);

            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //没找到包名的时候处理异常
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获取版本信息进行校验
     */
    private void checkVersion() {

        //启动子线程异步加载

        new Thread() {

            @Override
            public void run() {
                super.run();

                HttpURLConnection conn ;
                try {
                    /**
                     * 注意调试的时候：
                     * 1.tomcat服务是否已经启动完毕，localhost:8080端口能否访问到
                     * 2.设备是否已经连上网络。
                     * 3.要是模拟器ip地址应为10.0.2.2
                     * 4.若是genymotion或者是真机，IP地址应该为PC的IP地址
                     * 5.android权限记得添加
                     * 6.若使用真机调试，保证PC和真机在同一局域网，并关闭防火墙
                     */
                    URL url = new URL("http://113.55.67.100:8080/update.json");
//                    URL url = new URL("http://169.254.163.216:8080/update.json");
//                    URL url = new URL("http://192.168.1.201:8080/update.json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");// 设置请求方法
                    conn.setConnectTimeout(5000);// 设置连接超时
                    conn.setReadTimeout(5000);// 设置响应超时, 连接上了,但服务器迟迟不给响应
                    conn.connect();// 连接服务器

                    int responseCode = conn.getResponseCode();// 获取响应码
                    if (responseCode == 200) {
                        InputStream inputStream = conn.getInputStream();
                        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder result=new StringBuilder();
                        String line;
                        while((line=reader.readLine())!=null){
                            result.append(line);

                        }
                        System.out.println("网络返回:" + result);

                    }
                } catch (MalformedURLException e) {

                    System.out.println("连接异常");
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("url异常");
                    e.printStackTrace();
                }


            }

        }.start();
    }
}