package com.example.archer.mobliesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.archer.mobliesafe.view.SettingItemView;

/**
 * @fuction 设置中心
 * Created by Archer on 2016/6/3.
 */
public class SettingActivity extends Activity{

    private SettingItemView settingItemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        final SharedPreferences mPref = getSharedPreferences("config", MODE_PRIVATE);

        settingItemView= (SettingItemView) findViewById(R.id.siv_update);
//        settingItemView.setTitle("自动更新设置");
        boolean autoUpdate = mPref.getBoolean("auto_update",true);

           if (autoUpdate){
               settingItemView.setCheck(true);
//               settingItemView.setDec("自动更新已经开启");
           }else {
               settingItemView.setCheck(false);
//               settingItemView.setDec("自动更新已经关闭");


           }
        settingItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前的勾选状态


                if (settingItemView.isChecked()){
                    settingItemView.setCheck(false);

//                    settingItemView.setDec("自动更新已经关闭");
                    //更新sp，保存记录
                    mPref.edit().putBoolean("auto_update",false).commit();

                }else {
                    settingItemView.setCheck(true);
//                    settingItemView.setDec("自动更新已经开启");
                    //更新sp
                    mPref.edit().putBoolean("auto_update",true).commit();

                }

            }
        });
    }
}
