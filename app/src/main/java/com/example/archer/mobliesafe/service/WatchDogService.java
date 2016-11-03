package com.example.archer.mobliesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.archer.mobliesafe.db.dao.AppLockDao;

import java.util.List;

import static android.R.attr.start;

/**
 * Created by Archer on 2016/11/2.
 */

public class WatchDogService extends Service {

    private ActivityManager activityManager;
    private AppLockDao appLockDao;

    /**
     * Return the communication channel to the service.  May return null if
     * clients can not bind to the service.  The returned
     * {@link IBinder} is usually for a complex interface
     * that has been <a href="{@docRoot}guide/components/aidl.html">described using
     * aidl</a>.
     * <p>
     * <p><em>Note that unlike other application components, calls on to the
     * IBinder interface returned here may not happen on the main thread
     * of the process</em>.  More information about the main thread can be found in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html">Processes and
     * Threads</a>.</p>
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appLockDao = new AppLockDao(this);

        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);//得到进程管理器
//        1.首先获得当前的任务栈
//        2.得任务栈上的最上面的任务。

        startWatchDog();

    }

    //标记当前的看门狗是否停下来

    private   boolean flag =false;


    private void startWatchDog() {
        //如果再主线程里面开着 dog一直运行的话，会阻塞主线程所以开启一个新的线程

        new Thread(){

            @Override
            public void run() {

                flag=true;

                while (flag){

                    //狗一直再后台运行，避免程序堵塞

                    //获取到所有的tasks
                    List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(100);

                    //获取到最上面的进程
                    ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
                    //获取到最顶端应用程序的包名
                    String packageName = runningTaskInfo.topActivity.getPackageName();

                    if (appLockDao.find(packageName)){//如果找到了，就说明再程序锁数据库里面

                        Log.d("appLock",packageName);
                        System.out.println("在程序锁里面"+packageName);

                    }else {
                        System.out.println("没在程序锁里面");
                        Log.d("NotappLock",packageName);


                    }

                }



            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        flag=false;
    }
}
