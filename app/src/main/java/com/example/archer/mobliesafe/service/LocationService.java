package com.example.archer.mobliesafe.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;


/**
 * Created by Archer on 2016/6/17.
 */
public class LocationService extends Service {

    private TextView textView;
    private LocationManager locationManager;
    private MyLocationListener listener;
    private SharedPreferences mPref;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mPref = getSharedPreferences("config", MODE_PRIVATE);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        List<String> allProviders = locationManager.getAllProviders();//所有定位的方式
//        System.out.println(allProviders);

        //第一个参数填写标准，第二个参数是返回。

        Criteria criteria=new Criteria();
        criteria.setCostAllowed(true);//是否允许付费，比如是否使用4G网络，涉及付费的问题
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = locationManager.getBestProvider(criteria, true);//获取最佳位置提供者

        listener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(bestProvider, 0, 0, listener);

    }

    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
//位置发生变化
            System.out.println("onLocationChanged");

//            String j = "经度" + location.getLongitude();
//            String w = "纬度" + location.getLatitude();

//            String accuracy = "精确度" + location.getAccuracy();
//            String altitude = "海拔" + location.getAltitude();
//            textView.setText(longitude + "\n" + latitude + "\n" + accuracy + "\n" + altitude);

            //获取的经纬度保存在sp中
          mPref.edit().putString("location","j:"+location.getLongitude()+
                  ";w:"+location.getLatitude()).commit();
            stopSelf();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
//状态发生变化
            System.out.println("onStatusChanged");

        }

        @Override
        public void onProviderEnabled(String provider) {
//提供者可用
            System.out.println("onProviderEnabled");

        }

        @Override
        public void onProviderDisabled(String provider) {
//提供者不可用，比如关闭gps
            System.out.println("onProviderDisabled");

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(listener);
    }
}
