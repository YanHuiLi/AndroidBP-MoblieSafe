package com.example.archer.mobliesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Archer on 2016/10/26.
 */

public class TaskInfo {


    /**
     * 判断当前的item条目是否被勾上
     * @return true为勾上了
     */

    private boolean isChecked ;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    private Drawable icon;

    private String packageName;

    private String appName;

    private int memorySize;


    /**
     * 是否是用户进程
     */
    private boolean userApp;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(int  memorySize) {
        this.memorySize = memorySize;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "packageName='" + packageName + '\'' +
                ", userApp=" + userApp +
                ", memorySize=" + memorySize +
                ", appName='" + appName + '\'' +
                '}';
    }
}
