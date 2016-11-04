package com.example.archer.mobliesafe;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.example.archer.mobliesafe.service.AddressService;
import com.example.archer.mobliesafe.service.CallSafeMyService;
import com.example.archer.mobliesafe.service.WatchDogService;
import com.example.archer.mobliesafe.utils.SystemInfoUtils;
import com.example.archer.mobliesafe.utils.ToastUtils;
import com.example.archer.mobliesafe.view.SettingClickView;
import com.example.archer.mobliesafe.view.SettingItemView;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * @fuction 设置中心
 * Created by Archer on 2016/6/3.
 */
@RuntimePermissions
public class SettingActivity extends Activity{

    private SettingItemView settingItemView;
    private SettingItemView sivAddress;
    private SettingItemView siv_CallSafe;//黑名单
    private SettingClickView scvAddressStyle;//修改风格
    private  SettingClickView scvAddressLocation;
    private SharedPreferences mPref;

    private  SettingItemView siv_watch_dog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mPref = getSharedPreferences("config", MODE_PRIVATE);
        requestPermission();
        initUpdateView();
        initBlackView();
        initAddressView();
        initAddressStyle();
        initAddressLocation();
        initWatchDog();
    }

    //初始化黑名单
    private void initBlackView() {

        siv_CallSafe= (SettingItemView) findViewById(R.id.siv_CallSafe);
        boolean autoUpdate = mPref.getBoolean("auto_update",true);

        //注意代码摆放位置，否则会造成空指针异常。
        //根据归属地服务是否运行来更新checkbox。
        boolean serviceRunning = SystemInfoUtils.siServiceRunning(this,"com.example.archer.mobliesafe.service.CallSafeMyService");

        if (serviceRunning){
            siv_CallSafe.setCheck(true);
        }else {

            siv_CallSafe.setCheck(false);
        }

        siv_CallSafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (siv_CallSafe.isChecked()){

                    siv_CallSafe.setCheck(false);
                    stopService(new Intent(SettingActivity.this, CallSafeMyService.class));//停止归属地服务
                }else {
                    siv_CallSafe.setCheck(true);
                    startService(new Intent(SettingActivity.this, CallSafeMyService.class));//开始归属地服务
                }
            }
        });
    }


    //初始化看门狗
    private void initWatchDog() {

        siv_watch_dog= (SettingItemView) findViewById(R.id.siv_watch_dog);
        boolean autoUpdate = mPref.getBoolean("auto_update",true);

        //注意代码摆放位置，否则会造成空指针异常。
        //根据归属地服务是否运行来更新checkbox。
        boolean serviceRunning = SystemInfoUtils.siServiceRunning(this,"com.example.archer.mobliesafe.service.WatchDogService");

        if (serviceRunning){
            siv_watch_dog.setCheck(true);

        }else {

            siv_watch_dog.setCheck(false);
        }

        siv_watch_dog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (siv_watch_dog.isChecked()){

                    siv_watch_dog.setCheck(false);
                    stopService(new Intent(SettingActivity.this, WatchDogService.class));//停止归属地服务
//                    mPref.edit().putBoolean("auto_update", false).commit();

                }else {
                    siv_watch_dog.setCheck(true);
                    startService(new Intent(SettingActivity.this, WatchDogService.class));//开始归属地服务
//                    mPref.edit().putBoolean("auto_update", true).commit();

                }
            }
        });
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
        boolean serviceRunning = SystemInfoUtils.siServiceRunning(this,"com.example.archer.mobliesafe.service.AddressService");

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


    /**
     * 初始化归属地位置
     */

    private void initAddressLocation(){

        scvAddressLocation= (SettingClickView) findViewById(R.id.scv_address_location);
        scvAddressLocation.setTitle("归属地提示框显示位置");
        scvAddressLocation.setDec("设置归属地提示框的显示位置");

        scvAddressLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent( SettingActivity.this,DragViewActivity.class));
            }
        });

    }

    private void  requestPermission(){
        SettingActivityPermissionsDispatcher.AlertWindowWithCheck(this);

    }

    @NeedsPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void AlertWindow() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SettingActivityPermissionsDispatcher.onActivityResult(this, requestCode);
    }

    @OnShowRationale(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void ShowRationale(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("请求读取在其他屏幕上显示的权限，否则无法完成悬浮框的功能")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //再次执行请求
                        request.proceed();
                    }
                })
                .show();
    }

    @OnPermissionDenied(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void PermissonDenied() {

        ToastUtils.showToast(SettingActivity.this,"此权限已经被禁止");

    }

    @OnNeverAskAgain(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void NeverAskAgain() {

        ToastUtils.showToast(SettingActivity.this,"不再询问");

    }
}
