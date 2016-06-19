package com.example.archer.mobliesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * 服务状态的工具类
 * Created by Archer on 2016/6/19.
 */
public class ServiceStatusUtils {

    /**
     * 检测服务是否运行
     * @return
     */

    public static boolean siServiceRunning(Context context,String serviceName){

        ActivityManager activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(100);//返回100个服务数

        for (ActivityManager.RunningServiceInfo runningServiceInfo :runningServices) {
            String className = runningServiceInfo.service.getClassName();
            System.out.println(className);//获取服务名称
            if (className.equals(serviceName)){
                return true;
            }
        }

        return false;
    }
}
