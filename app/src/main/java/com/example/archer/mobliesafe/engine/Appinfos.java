package com.example.archer.mobliesafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.archer.mobliesafe.bean.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Archer on 2016/10/17.
 * <p>
 * 描述:
 * <p>
 * 作者
 */

public class AppInfos {

    public static List<AppInfo> getAppinfos(Context context){

        List<AppInfo> PackageAppInfos = new ArrayList<>();
        //获取包的管理者
        PackageManager packageManager = context.getPackageManager();

        //或者系统中存在的app的集合
        List<PackageInfo> installedApplications = packageManager.getInstalledPackages(0);


        for (PackageInfo installedApplication : installedApplications) {
            AppInfo appInfos=new AppInfo();

            //获取每个app的图片
            Drawable drawable = installedApplication.applicationInfo.loadIcon(packageManager);

            appInfos.setIcon(drawable);//使用appinfos装载数字过去

            //获取应用程序的名字
            String apkName = installedApplication.applicationInfo.loadLabel(packageManager).toString();

            appInfos.setApkName(apkName);
            //获取到应用程序的包名
            String apkPackageName = installedApplication.packageName;

            appInfos.setApkPackageName(apkPackageName);
            //获得程序的大小
            String sourceDir = installedApplication.applicationInfo.sourceDir;
            File file=new File(sourceDir);
            long apkSize=file.length();
            appInfos.setApkSize(apkSize);

            System.out.println("=========================================");
            System.out.println("程序的名字是"+apkName);
            System.out.println("程序的包名是"+apkPackageName);
            System.out.println("程序的大小是"+apkSize);

            //log的用法
            Log.d("程序的名字是",apkName);

//判断app在哪的位置，可以有两种，一种是在data/app system/app里面
            //或者是判断拿到的installedApplication.flag的值
            int flags = installedApplication.applicationInfo.flags;

            if ((flags& ApplicationInfo.FLAG_SYSTEM)!=0){
                //说明这个APP是系统app
                appInfos.setUserApp(false);
            }else {
                //第三方app
                appInfos.setUserApp(true);
            }


            if ((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0){
                //sd卡中
                appInfos.setRom(false);
            }else {
                //内存中
                appInfos.setRom(true);
            }


            PackageAppInfos.add(appInfos);//数据加入到list集合中去
        }

        return PackageAppInfos;
    }

}
