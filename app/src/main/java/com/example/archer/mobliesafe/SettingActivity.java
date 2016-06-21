package com.example.archer.mobliesafe;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.example.archer.mobliesafe.service.AddressService;
import com.example.archer.mobliesafe.utils.ServiceStatusUtils;
import com.example.archer.mobliesafe.view.SettingClickView;
import com.example.archer.mobliesafe.view.SettingItemView;

/**
 * @fuction 设置中心
 * Created by Archer on 2016/6/3.
 */
public class SettingActivity extends Activity{

    private SettingItemView settingItemView;
    private SettingItemView sivAddress;
    private SettingClickView scvAddressStyle;//修改风格
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        initUpdateView();
        initAddressView();
        initAddressStyle();
    }


    private  void initUpdateView(){


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


                if (settingItemView.isChecked()) {
                    settingItemView.setCheck(false);

//                    settingItemView.setDec("自动更新已经关闭");
                    //更新sp，保存记录
                    mPref.edit().putBoolean("auto_update", false).commit();

                } else {
                    settingItemView.setCheck(true);
//                    settingItemView.setDec("自动更新已经开启");
                    //更新sp
                    mPref.edit().putBoolean("auto_update", true).commit();

                }

            }
        });
    }

    /**
     * 初始化归属地开关
     */

    private void initAddressView(){



        sivAddress= (SettingItemView) findViewById(R.id.siv_address);

        //注意代码摆放位置，否则会造成空指针异常。
        //根据归属地服务是否运行来更新checkbox。
        boolean serviceRunning = ServiceStatusUtils.siServiceRunning(this,"com.example.archer.mobliesafe.service.AddressService");

        if (serviceRunning){
            sivAddress.setCheck(true);
        }else {

            sivAddress.setCheck(false);
        }

        sivAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivAddress.isChecked()){

                    sivAddress.setCheck(false);
                    stopService(new Intent(SettingActivity.this, AddressService.class));//停止归属地服务
                }else {
                    sivAddress.setCheck(true);
                    startService(new Intent(SettingActivity.this, AddressService.class));//开始归属地服务
                }
            }
        });

    }

    final String[] items=new String[]{"半透明","活力橙","卫士蓝","金属灰","苹果绿"};


    /**
     * 修改提示框的显示风格
     */
    private void initAddressStyle(){

        scvAddressStyle= (SettingClickView) findViewById(R.id.scv_address_style);

        scvAddressStyle.setTitle("归属地提示框风格");
        scvAddressStyle.setDec("活力橙");
        int address_style = mPref.getInt("address_style", 0);//读取默认保存的style

        scvAddressStyle.setDec(items[address_style]);
        scvAddressStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChooseDailog();
            }


        });
    }

    /**
     * 弹出选择风格的单选框
     */
    private void showSingleChooseDailog() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("归属地提示款风格");
        int address_style = mPref.getInt("address_style", 0);//读取默认保存的style


        builder.setSingleChoiceItems(items, address_style, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPref.edit().putInt("address_style",which).commit();//保存选择的风格

                dialog.dismiss();//让dialog消失
                scvAddressStyle.setDec(items[which]);//更新组合控件的描述
            }
        });

        builder.setNegativeButton("取消",null);
        builder.show();
    }

}
