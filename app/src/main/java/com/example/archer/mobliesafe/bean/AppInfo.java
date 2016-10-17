package com.example.archer.mobliesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Archer on 2016/10/17.
 * <p>
 * 描述:
 * <p>
 * 作者
 */

public class AppInfo {

    public AppInfo() {
    }


    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public long getApkSize() {
        return apkSize;
    }

    public void setApkSize(long apkSize) {
        this.apkSize = apkSize;
    }

    public String getApkPackageName() {
        return apkPackageName;
    }

    public void setApkPackageName(String apkPackageName) {
        this.apkPackageName = apkPackageName;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    public boolean isRom() {
        return isRom;
    }

    public void setRom(boolean rom) {
        isRom = rom;
    }

    private Drawable icon;//定义图片

    @Override
    public String toString() {
        return "AppInfo{" +
                "icon=" + icon +
                ", apkName='" + apkName + '\'' +
                ", apkSize='" + apkSize + '\'' +
                ", apkPackageName='" + apkPackageName + '\'' +
                ", userApp=" + userApp +
                ", isRom=" + isRom +
                '}';
    }

    private String apkName;//程序的名称
    private long apkSize;//程序的大小

    private String apkPackageName;//程序的包名
    private boolean userApp;//是否是用户的程序还是系统的程序 true为用户
    private  boolean isRom;//程序存在的位置，true表示在用户里面

}
