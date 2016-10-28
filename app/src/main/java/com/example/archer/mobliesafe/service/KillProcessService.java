package com.example.archer.mobliesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class KillProcessService extends Service {

    private LockScreenReceiver lockScreenReceiver;

    public KillProcessService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private  class LockScreenReceiver  extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            //得到进程管理器
            ActivityManager activimanager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
           //遍历整个增在运行得进程
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activimanager.getRunningAppProcesses();

            for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {

                activimanager.killBackgroundProcesses(runningAppProcess.processName);
            }


        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        lockScreenReceiver = new LockScreenReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);//intentFilter过滤器

        registerReceiver(lockScreenReceiver,intentFilter);//注册一个锁屏得广播


        Timer timer = new Timer();

   TimerTask timerTask=   new TimerTask() {
            @Override
            public void run() {

            }
        };

        //第一个参数，传入一个timetask,第二个参数设定delay，第三个参数 每隔多少后调用一次
        timer.schedule(timerTask,1000,1000);


    }




    @Override
    public void onDestroy() {
        super.onDestroy();

        //当应用程序退出得时候，反注册广播
        unregisterReceiver(lockScreenReceiver);
        lockScreenReceiver=null;
    }
}
