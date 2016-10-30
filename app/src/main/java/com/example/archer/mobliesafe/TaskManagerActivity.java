package com.example.archer.mobliesafe;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.archer.mobliesafe.bean.TaskInfo;
import com.example.archer.mobliesafe.engine.TaskInfos;
import com.example.archer.mobliesafe.utils.SharedPreferenceUtils;
import com.example.archer.mobliesafe.utils.SystemInfoUtils;
import com.example.archer.mobliesafe.utils.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;



import java.util.ArrayList;
import java.util.List;


public class TaskManagerActivity extends AppCompatActivity {



//    @ViewInject(R.id.tv_task_process_count)
//    private TextView tv_task_process_count;

    @ViewInject(R.id.tv_task_process_count)
    private TextView tv_task_process_count;;

    @ViewInject(R.id.tv_task_memory)
    private TextView tv_task_memory;

//    @ViewInject(R.id.tv_task_user_process_count)
//    private TextView tv_task_user_process_count;

    private ListView  listView_process;
    private List<TaskInfo> taskInfos;
    private ArrayList<TaskInfo> userTaskInfos;
    private ArrayList<TaskInfo> systemTaskInfos;
    private View view;
    private ViewHolder holder;
    private TaskManagerAdapter taskManagerAdapter;
    private int processCount;
    private long totalMem;
    private long avaliMem;
//    private SharedPreferences config;
    ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //1.得到一个SP SP得主要用途在于数据量小得临时标记和保存
        //2.得到点击checkbox时候，保存再config里面得value
//        config = getSharedPreferences("config", 0);



        initUI();
        initData();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (taskManagerAdapter!=null){
            taskManagerAdapter.notifyDataSetChanged();
        }

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

        processCount = SystemInfoUtils.getProcessCount(TaskManagerActivity.this);
        tv_task_process_count.setTextSize(14);
        tv_task_process_count.setText("运行中的进程："+ processCount +" 个");

       //获取到剩余内存
//        long totalMem = memoryInfo.totalMem;//获取到总内存
        /**
         *
         /proc/meminfo/
        **/

        totalMem = SystemInfoUtils.getTotalMemory(TaskManagerActivity.this);
        avaliMem = SystemInfoUtils.getAvaliMem(TaskManagerActivity.this);

        tv_task_memory.setTextSize(14);
        tv_task_memory.setText("剩余/总内存："+Formatter.formatFileSize(this, avaliMem)
                +"/"+Formatter.formatFileSize(this, totalMem));

        listView_process.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //当点击item的时候先得到该
                Object itemAtPosition = listView_process.getItemAtPosition(position);

                //如果当前的实例不为空而且是taskinfo实例的话
                if (itemAtPosition!=null&&itemAtPosition instanceof TaskInfo){

                     TaskInfo  taskInfo= (TaskInfo) itemAtPosition;

                    if (taskInfo.getPackageName().equals(getPackageName())){
                        return;
                    }

                    //判断是否被勾选上
                    /**
                     * 如果勾选上了，就改成勾选上
                     * 反之亦然
                     */

                     ViewHolder holder = (ViewHolder) view.getTag();//拿到整个Item对象


                    if (taskInfo.isChecked()){
                    taskInfo.setChecked(false);
                        holder.ck_clear.setChecked(false);
                }else{
                    taskInfo.setChecked(true);
                        holder.ck_clear.setChecked(true);

                    }

                }
            }
        });

    }



    public void selectAllTask(View view) {
        for (TaskInfo taskInfo : userTaskInfos) {

            //判断当前得程序 是不是自己，是的话，隐藏自己得checkbox

            if (taskInfo.getPackageName().equals(getPackageName())){
                continue;
            }

           taskInfo.setChecked(true);
       }

       for (TaskInfo taskInfo : systemTaskInfos) {
           taskInfo.setChecked(true);
        }


        //一旦数据发生改变一定要刷新
        taskManagerAdapter.notifyDataSetChanged();

    }

    //完成反选的功能
    public void OpSelect(View view) {
        for (TaskInfo taskInfo:userTaskInfos){

            if (taskInfo.getPackageName().equals(getPackageName())){
                continue;
            }
          taskInfo.setChecked(!taskInfo.isChecked());

        }

        for (TaskInfo taskInfo:systemTaskInfos){
            taskInfo.setChecked(!taskInfo.isChecked());
        }

        //一旦数据发生改变一定要刷新
        taskManagerAdapter.notifyDataSetChanged();
    }


//清理进程


    public void ClearProcess(View view) {


//        PackageManager packageManager=getPackageManager().getInstallerPackageName();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        //设置一个集合来加入killed  process
        List<TaskInfo> killTaskInfo = new ArrayList<>();



        int total=0;//记录清理的总数
        int avalMem=0;//清理的进程的大小
        for (TaskInfo userTaskInfo : userTaskInfos) {
            if (userTaskInfo.isChecked()) {
                activityManager.killBackgroundProcesses(userTaskInfo.getPackageName());//传一个包名字
//                userTaskInfos.remove(userTaskInfo);
                killTaskInfo.add(userTaskInfo);
                total++;
                avalMem+=userTaskInfo.getMemorySize();
            }
        }
        for (TaskInfo systemTaskInfo : systemTaskInfos) {
            if (systemTaskInfo.isChecked()) {
                activityManager.killBackgroundProcesses(systemTaskInfo.getPackageName());//传一个包名字
// 当集合在迭代得时候，不能立马移除，通常得做法就新建一个集合装进去，等集合迭代完毕再删除
// systemTaskInfos.remove(systemTaskInfo);
                killTaskInfo.add(systemTaskInfo);

                total++;
                avalMem+=systemTaskInfo.getMemorySize();
            }
        }
        for (TaskInfo taskInfo : killTaskInfo) {
           //判断是不是用户得APP需要删除

            if (taskInfo.isUserApp()){
                userTaskInfos.remove(taskInfo);
            }else {
                systemTaskInfos.remove(taskInfo);
            }
        }


        ToastUtils.showToast(TaskManagerActivity.this,"清理的进程"+total
                +"个，释放的内存总数"+Formatter.formatFileSize(TaskManagerActivity.this,avalMem));
//表示当前还剩多少个进程
        processCount-=total;
        tv_task_process_count.setTextSize(13);
        tv_task_process_count.setText("运行中的进程："+ processCount+" 个");

//清理掉得内存应该加回去
        tv_task_memory.setTextSize(13);
        tv_task_memory.setText("剩余/总内存："+Formatter.formatFileSize(this,avaliMem+avalMem)
                +"/"+Formatter.formatFileSize(this, totalMem));
        taskManagerAdapter.notifyDataSetChanged();
    }

    //点击进程设置按钮

    public void TaskSetting(View view) {

        //点击跳转到进程设置管理界面
        startActivity(new Intent(TaskManagerActivity.this,TaskManagerActivitySetting.class));
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

            //根据checkbox拿到标记
//            boolean result = config.getBoolean("is_system_process", false);//默认为false

            boolean  result= SharedPreferenceUtils.getBoolean(TaskManagerActivity.this, "is_system_process", false);

            if (result){

                return taskInfos.size()+2;

            }else {
                return userTaskInfos.size()+1;
            }

//            userTaskInfos.size()+1+systemTaskInfos+1
        }

        @Override
        public Object getItem(int position) {
            TaskInfo taskInfo ;

            if (position==0){
                return null;
            }else if (position==userTaskInfos.size()+1){
                return null;
            }

            if (position<userTaskInfos.size()+1){

//                System.out.println("userapp的个数是多少"+userAppInfos.size());

                taskInfo=userTaskInfos.get(position-1);

            }else {
                int location =userTaskInfos.size()+2;
                taskInfo=systemTaskInfos.get(position-location);
            }
            return taskInfo;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(position==0){//如果是position等于0的话，说明是第一个textView的展示

                //新建一个特殊的展示的textView
                TextView textView= new TextView(TaskManagerActivity.this);
                textView.setTextColor(Color.WHITE);//字体会白色
                textView.setTextSize(13);
                textView.setBackgroundColor(Color.GRAY);//背景颜色为灰色

                textView.setText("用户进程（"+userTaskInfos.size()+")个");
                return  textView;

            }else  if (position==userTaskInfos.size()+1){//第二个系统程序的textView

                TextView textView=new TextView(TaskManagerActivity.this);
                textView.setTextSize(13);
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.GRAY);

                textView.setText("系统进程("+systemTaskInfos.size()+")个");

                return  textView;

            }

            TaskInfo taskInfo ;

            if (position<userTaskInfos.size()+1){

//                System.out.println("userapp的个数是多少"+userAppInfos.size());

                taskInfo=userTaskInfos.get(position-1);

            }else {
                int location =userTaskInfos.size()+2;
                taskInfo=systemTaskInfos.get(position-location);
            }

            View view=null;
//            ViewHolder hodler;

            if (convertView!=null&&convertView instanceof LinearLayout){


                view= convertView;

                holder =(TaskManagerActivity.ViewHolder) convertView.getTag();
            }else{

                view = View.inflate(TaskManagerActivity.this, R.layout.item_task_manager, null);
                holder = new ViewHolder();
                //初始化组件
                holder.iv_icon  = (ImageView) view.findViewById(R.id.iv_task_icon);
                holder.tv_ProcessName= (TextView) view.findViewById(R.id.tv_ProcessName);
                holder.ck_clear= (CheckBox) view.findViewById(R.id.ck_process);
                holder.tv_memory_size= (TextView) view.findViewById(R.id.tv_task_memory_size);

                view.setTag(holder);
            }

//            if (taskInfo.isUserApp()){
//                holder.tv_apkLocation.setText("手机内存");
//            }else {
//                holder.tv_apkLocation.setText("SD卡");
//
//            }

//            TaskInfo taskInfo = taskInfos.get(position);//得到的当前的bean对象

            holder.iv_icon.setImageDrawable(taskInfo.getIcon());
             holder.tv_ProcessName.setText(taskInfo.getAppName());
            holder.tv_memory_size.setText("内存占用"+Formatter.formatFileSize(TaskManagerActivity.this,taskInfo.getMemorySize()));



            if (taskInfo.isChecked()){
                holder.ck_clear.setChecked(true);
            }else{
                holder.ck_clear.setChecked(false);

            }
            //判断当前展示得item是不是自己得程序
            //如果是就把checkbox隐藏起来
            if (taskInfo.getPackageName().equals(getPackageName())){
                //优先考虑invisible。
                //gone不占位置，会影响应用性能，系统会重绘
                //INVISIBLE 占位置的。
                holder.ck_clear.setVisibility(View.INVISIBLE);
            }else {
                     //显示checkbox
                holder.ck_clear.setVisibility(View.VISIBLE);

            }


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
          userTaskInfos = new ArrayList<>();
          systemTaskInfos = new ArrayList<>();

          //遍历整个集合
          for (TaskInfo taskInfo : taskInfos) {
              if (taskInfo.isUserApp()){
                   userTaskInfos.add(taskInfo);
              }else {
                  systemTaskInfos.add(taskInfo);
              }
          }

//          handler.sendEmpztyMessage(0);

          runOnUiThread(new Runnable() {
              @Override
              public void run() {
                  taskManagerAdapter = new TaskManagerAdapter();
                  listView_process.setAdapter(taskManagerAdapter);

              }
          });



      }
  }.start();
    }


}
