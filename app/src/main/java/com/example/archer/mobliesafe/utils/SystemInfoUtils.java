package com.example.archer.mobliesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.example.archer.mobliesafe.TaskManagerActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * 服务状态的工具类
 * Created by Archer on 2016/6/19.
 */
public class SystemInfoUtils {

    /**
     * 检测服务是否运行
     * @return
     */

    public static boolean siServiceRunning(Context context,String serviceName){

        ActivityManager activityManager= (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

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


    /**
     * 得到当前进程的个数
     * @param context
     * @return
     */
    public static int  getProcessCount(Context context){
        ActivityManager MActivityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = MActivityManager.getRunningAppProcesses();//得到当前的进程数

        return runningAppProcesses.size();

    }

    public static long  getAvaliMem(Context context){
        ActivityManager MActivityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);


        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        MActivityManager.getMemoryInfo(memoryInfo);//获取到内存的基本信息

        return memoryInfo.availMem;

    }

    public static  long getTotalMemory(Context context){

        try {

            //配置文件的路径 /proc/meminfo/
            FileInputStream fileInputStream = new FileInputStream(new File("/proc/meminfo"));

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            String s = bufferedReader.readLine();
            StringBuilder stringBuffer = new StringBuilder();
            for (char c:s.toCharArray()){
                if (c>='0'&&c<='9'){
                    stringBuffer.append(c);
                }
            }
            /**
             * 注意 乘以1024
             */
           return  Long.parseLong(stringBuffer.toString())*1024;


        } catch (IOException e) {
            e.printStackTrace();
        }
return  0;
    }
}
