package com.example.archer.mobliesafe.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.example.archer.mobliesafe.R;
import com.example.archer.mobliesafe.bean.TaskInfo;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * Created by Archer on 2016/10/26.
 *
 *
 *
 */

public class TaskInfos {

    /**
     * 获取到所有的进程信息
     * @return
     */

    public  static List<TaskInfo>  getTaskInfos(Context context){

        PackageManager packageManager = context.getPackageManager();

        List<TaskInfo> taskInfos = new ArrayList<>();

        //获取到进程管理器
        ActivityManager MActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

//获取到手机上面所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = MActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {

            //获取到进程的名字
            TaskInfo taskInfo = new TaskInfo();
            String processName = runningAppProcess.processName;//获取到进程的名字 -》包名
            taskInfo.setPackageName(processName);
            try {
//获取到内存基本信息,数组里面只有一个数据
                Debug.MemoryInfo[] memoryInfo = MActivityManager.getProcessMemoryInfo(new int[]{runningAppProcess.pid});
                Debug.MemoryInfo memoryInfo1 = memoryInfo[0];
                int  totalPrivateDirty = memoryInfo1.getTotalPrivateDirty()*1024;//当前应用程序占用了多少内存

                taskInfo.setMemorySize(totalPrivateDirty);

                //获取到进程的图片
                PackageInfo packageInfo = packageManager.getPackageInfo(processName, 0);
                Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
                taskInfo.setIcon(icon);


                //获取到应用的包名
                String apkName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                taskInfo.setAppName(apkName);


                System.out.println( "进程名字和包名=================  "+apkName+"/"+processName+"/"+totalPrivateDirty);

                //获取到当前应用程序的标记
                //packageInfo.applicationInfo.flags 我们写的答案
                //ApplicationInfo.FLAG_SYSTEM表示老师的该卷器
                int flags = packageInfo.applicationInfo.flags;
                //ApplicationInfo.FLAG_SYSTEM 表示系统应用程序
                if((flags & ApplicationInfo.FLAG_SYSTEM) != 0 ){
                    //系统应用
                    taskInfo.setUserApp(false);
                }else{
//					/用户应用
                    taskInfo.setUserApp(true);

                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();

                //有些系统进程没有图标，给一个默认的图标
                //并不是说所有进程都有包。
                taskInfo.setIcon(context.getResources().getDrawable(R.drawable.lock));
                taskInfo.setAppName(processName);

            }
            taskInfos.add(taskInfo);


        }


        return taskInfos;
    }
}
