package com.example.archer.mobliesafe;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import butterknife.Bind;

public class TaskManagerActivitySetting extends AppCompatActivity {


    @ViewInject(R.id.ck_stats)
    private CheckBox  ck_stats;
    private SharedPreferences config;

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
        config = getSharedPreferences("config", MODE_PRIVATE);



        //读取用户选中得状态，很关键得一行代码，默认是false。
        ck_stats.setChecked(config.getBoolean("is_system_process",false));


//初始化完成了checkBox 判断用户是否点击了checkBox;
        ck_stats.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
//如果你点击了
                    //1.得到sp得editor编辑器得对象
                    //2.进行编辑
                    //3.进行提交
                    SharedPreferences.Editor edit = config.edit();
                    edit.putBoolean("is_system_process",true);//存入得标记为"is_system_process",返回值为true;
                    edit.commit();

                }else {
                    //1.得到sp得editor编辑器得对象
                    //2.进行编辑
                    //3.进行提交
                    SharedPreferences.Editor edit = config.edit();
                    edit.putBoolean("is_system_process",false);//存入得标记为"is_system_process",返回值为true;
                    edit.commit();

                }
            }
        });

    }
}
