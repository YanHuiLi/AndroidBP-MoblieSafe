package com.example.archer.mobliesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.archer.mobliesafe.EnterPwdActivity;
import com.example.archer.mobliesafe.db.dao.AppLockDao;

import java.util.List;



/**
 * Created by Archer on 2016/11/2.
 */

public class WatchDogService extends Service {

    private ActivityManager activityManager;
    private WatchDogReceiver receiver;



    private String tempStopProtectPackageName;// 临时停止保护的包名
    private List<String> appLockInfos;


    private class WatchDogReceiver extends BroadcastReceiver {



        @Override
        public void onReceive(Context context, Intent intent) {


            if(intent.getAction().equals("com.archer.mobilesafe.stopprotect")){
                //获取到停止保护的对象

                tempStopProtectPackageName = intent.getStringExtra("packageName");
            }else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                tempStopProtectPackageName = null;
                // 让狗休息
                flag = false;
            }else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
                //让狗继续干活
                if(!flag){
                    startWatchDog();
                }
            }


        }

    }


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
        AppLockDao appLockDao = new AppLockDao(this);

        appLockInfos = appLockDao.findAll();

        receiver = new WatchDogReceiver();



        //注册一个广播过滤器
        IntentFilter filter = new IntentFilter();
        //停止保护
        filter.addAction("com.archer.mobilesafe.stopprotect");

        //注册一个锁屏的广播
        /**
         * 当屏幕锁住的时候。狗就休息
         * 屏幕解锁的时候。让狗活过来
         */
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        filter.addAction(Intent.ACTION_SCREEN_ON);


        registerReceiver(receiver, filter);


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
                    List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);

                    //获取到最上面的进程
                    ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
                    //获取到最顶端应用程序的包名
                    String packageName = runningTaskInfo.topActivity.getPackageName();

                    //让狗休息一会
                    SystemClock.sleep(30);
                    //直接从数据库里面查找当前的数据
                    //这个可以优化。改成从内存当中寻找
                    System.out.println(packageName);

                    if(appLockInfos.contains(packageName)){
//					if(dao.find(packageName)){
//						System.out.println("在程序锁数据库里面");

                        //说明需要临时取消保护
                        //是因为用户输入了正确的密码
                        if(packageName.equals(tempStopProtectPackageName)){

                        }else{
                            Intent intent = new Intent(WatchDogService.this,EnterPwdActivity.class);
                            /**
                             * 需要注意：如果是在服务里面往activity界面跳的话。需要设置flag
                             */
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //停止保护的对象
                            intent.putExtra("packageName", packageName);

                            startActivity(intent);
                        }
                    }
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//做一个标记，当服务被destroy的时候，置flag为false，看门狗就不工作了
        flag=false;
        unregisterReceiver(receiver);
        receiver = null;
    }
}
