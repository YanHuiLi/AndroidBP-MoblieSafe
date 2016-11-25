package com.example.archer.mobliesafe;

import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.reflect.Method;
import java.util.List;


public class CleanCache extends AppCompatActivity {

    private PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
    }

    private void initUI() {

        setContentView(R.layout.activity_clean_cache);


        packageManager = getPackageManager();


//        packageManager.getPackageSizeInfo();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);

        for (PackageInfo packageInfo : installedPackages) {
            getCacheSize(packageInfo);
        }


    }


    private void getCacheSize(PackageInfo  packageInfo) {

        try {
//            Class<?> clazz = getClassLoader().loadClass("packageManager");
            //通过反射获取到当前的方法
            Method method = PackageManager.class.getDeclaredMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);

     method.invoke(packageManager,packageInfo.applicationInfo.packageName,new  MyIPackageStatsObserver(packageInfo));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class  MyIPackageStatsObserver extends  IPackageStatsObserver.Stub{


        private  PackageInfo packageInfo;

        public MyIPackageStatsObserver(PackageInfo packageInfo) {
              this.packageInfo=packageInfo;
        }

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {

            //获取到当前手机应用大小
            long cacheSize = pStats.cacheSize;
             //判断是否有缓存
            if (cacheSize>0){

                System.out.println("当前应用的名字："+packageInfo.applicationInfo.loadLabel(packageManager)+
                        " 缓存的大小： "+cacheSize);
                System.out.println("world freeline");

            }

        }
    }
}
