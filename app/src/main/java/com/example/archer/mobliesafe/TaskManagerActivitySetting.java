package com.example.archer.mobliesafe;

import com.example.archer.mobliesafe.R;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.archer.mobliesafe.R;
import com.example.archer.mobliesafe.service.KillProcessService;
import com.example.archer.mobliesafe.utils.SharedPreferenceUtils;
import com.example.archer.mobliesafe.utils.SystemInfoUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import butterknife.Bind;

public class TaskManagerActivitySetting extends AppCompatActivity {


    @ViewInject(R.id.ck_stats)
    private CheckBox  ck_stats;

    @ViewInject(R.id.ck_Lock)
    private CheckBox  ck_Lock;
//    private SharedPreferences config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_task_manager_setting);
        ViewUtils.inject(this);
//当判断用户为点击得时候，采用SP保存起来用户得点击记录，然后去更新对应进程得返回数.
        //mode_private  私有得
//        config = getSharedPreferences("config", MODE_PRIVATE);



        //读取用户选中得状态，很关键得一行代码，默认是false。
//        ck_stats.setChecked(config.getBoolean("is_system_process",false));

        ck_stats.setChecked(SharedPreferenceUtils.getBoolean(TaskManagerActivitySetting.this,"is_system_process",false));


//初始化完成了checkBox 判断用户是否点击了checkBox;
        ck_stats.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
////如果你点击了
//                    //1.得到sp得editor编辑器得对象
//                    //2.进行编辑
//                    //3.进行提交
////                    SharedPreferences.Editor edit = config.edit();
////                    edit.putBoolean("is_system_process",true);//存入得标记为"is_system_process",返回值为true;
////                    edit.commit();
//
//                    SharedPreferenceUtils.saveBoolean(TaskManagerActivitySetting.this,"is_system_process",true);
//
//                }else {
//                    //1.得到sp得editor编辑器得对象
//                    //2.进行编辑
//                    //3.进行提交
////                    SharedPreferences.Editor edit = config.edit();
////                    edit.putBoolean("is_system_process",false);//存入得标记为"is_system_process",返回值为true;
////                    edit.commit();
//
//
//
//
//                }
                SharedPreferenceUtils.saveBoolean(TaskManagerActivitySetting.this,"is_system_process",isChecked);

            }
        });

        //定时清理进程
        //新建一个服务，进行清理服务

        final Intent intent= new Intent(this, KillProcessService.class);

        ck_Lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (ck_Lock.isChecked()){
                    startService(intent);
                }else {
                    stopService(intent);
                }

            }
        });

    }

    //判断service是否开启


    @Override
    protected void onStart() {
        boolean siServiceRunning = SystemInfoUtils.siServiceRunning(TaskManagerActivitySetting.this, "com.example.archer.mobliesafe.service.KillProcessService");

        if (siServiceRunning){
            ck_Lock.setChecked(true);

        }else {
            ck_Lock.setChecked(false);

        }
        super.onStart();
    }
}
