package com.example.archer.mobliesafe;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.TextView;

import com.example.archer.mobliesafe.utils.SystemInfoUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import com.example.archer.mobliesafe.utils.SystemInfoUtils.*;

import static android.content.Context.ACTIVITY_SERVICE;

public class TaskManagerActivity extends AppCompatActivity {

    @ViewInject(R.id.tv_task_process_count)
    private TextView tv_task_process_count;

    @ViewInject(R.id.tv_task_memory)
    private TextView tv_task_memory;

    @ViewInject(R.id.tv_task_user_process_count)
    private TextView tv_task_user_process_count;

    @ViewInject(R.id.list_view_process)
    private TextView list_view_process;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();


    }

    /**
     * ActivityManager 活动管理器(进程管理器)
     *
     * packageManager  包管理器
     *
     * 注意ViewUtils的注入必须在初始化布局之后，不然会报空指针异常。
     */

    private void initUI() {
        setContentView(R.layout.activity_task_manager);
        ViewUtils.inject(this);


//        ActivityManager MacitiviManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
         //获取到手机里面得到的所有运行的任务
//        List<ActivityManager.RunningServiceInfo> runningServices = MacitiviManager.getRunningServices(100);
//        for (ActivityManager.RunningServiceInfo runningService : runningServices) {
//            ComponentName service = runningService.service;
//            System.out.println("=="+service);
//        }

        /**
         * android 6.0 的系统 用户进程无法读出来，只会显示一个自己的包名。其他的读不出来
         */
//        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = MacitiviManager.getRunningAppProcesses();//得到当前的进程数
//        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
//            String processName = runningAppProcess.processName;
//            System.out.println("==================="+processName);
//
//        }
//        int size1 = runningServices.size();

//        int size = runningAppProcesses.size();//获取手机上面的进程数
//        System.out.println("所有的应用用多少"+size);

     tv_task_process_count.setText("运行中的进程："+ SystemInfoUtils.getProcessCount(TaskManagerActivity.this)+" 个");

       //获取到剩余内存
//        long totalMem = memoryInfo.totalMem;//获取到总内存
        /**
         *
         /proc/meminfo/
        **/

        long totalMem = SystemInfoUtils.getTotalMemory(TaskManagerActivity.this);

        tv_task_memory.setText("剩余/总内存："+Formatter.formatFileSize(this,SystemInfoUtils.getAvaliMem(TaskManagerActivity.this))
                +"/"+Formatter.formatFileSize(this, totalMem));

    }

    private void initData(){
    }
}
