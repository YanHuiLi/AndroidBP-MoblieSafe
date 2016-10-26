package com.example.archer.mobliesafe;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.archer.mobliesafe.bean.TaskInfo;
import com.example.archer.mobliesafe.engine.TaskInfos;
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
import static com.example.archer.mobliesafe.R.id.iv_icon;

public class TaskManagerActivity extends AppCompatActivity {

    @ViewInject(R.id.tv_task_process_count)
    private TextView tv_task_process_count;

    @ViewInject(R.id.tv_task_memory)
    private TextView tv_task_memory;

    @ViewInject(R.id.tv_task_user_process_count)
    private TextView tv_task_user_process_count;

    private ListView  listView_process;
    private List<TaskInfo> taskInfos;


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
        listView_process= (ListView) findViewById(R.id.list_view_process);


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

//    private Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            TaskManagerAdapter taskManagerAdapter = new TaskManagerAdapter();
//
//
//        }
//    };

    private class TaskManagerAdapter  extends BaseAdapter{
        @Override
        public int getCount() {
            return taskInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {



            View view = View.inflate(TaskManagerActivity.this, R.layout.item_task_manager, null);
            ViewHolder holder=new ViewHolder();
            //初始化组件
            holder.iv_icon  = (ImageView) view.findViewById(R.id.iv_task_icon);
            holder.tv_ProcessName= (TextView) view.findViewById(R.id.tv_ProcessName);
            holder.ck_clear= (CheckBox) view.findViewById(R.id.ck_process);
            holder.tv_memory_size= (TextView) view.findViewById(R.id.tv_task_memory_size);

            TaskInfo taskInfo = taskInfos.get(position);//得到的当前的bean对象

            holder.iv_icon.setImageDrawable(taskInfo.getIcon());
             holder.tv_ProcessName.setText(taskInfo.getAppName());
            holder.tv_memory_size.setText(Formatter.formatFileSize(TaskManagerActivity.this,taskInfo.getMemorySize()));
            return view;
        }

    }


    static  class  ViewHolder{

        ImageView iv_icon;
        TextView tv_ProcessName;
        TextView tv_memory_size;
        CheckBox ck_clear;

    }


    private void initData(){
  new Thread(){
      @Override
      public void run() {
          taskInfos = TaskInfos.getTaskInfos(TaskManagerActivity.this);

//          handler.sendEmpztyMessage(0);

          runOnUiThread(new Runnable() {
              @Override
              public void run() {
                              TaskManagerAdapter taskManagerAdapter = new TaskManagerAdapter();

                               listView_process.setAdapter(taskManagerAdapter);

              }
          });



      }
  }.start();
    }


}
