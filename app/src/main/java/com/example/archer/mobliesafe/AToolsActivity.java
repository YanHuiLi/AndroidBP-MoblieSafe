package com.example.archer.mobliesafe;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.archer.mobliesafe.utils.SmsUtils;


import com.example.archer.mobliesafe.utils.SmsUtils.BackUpCallBackSms;
import com.example.archer.mobliesafe.utils.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 *面试点一：
 *
 * 子线程并非不能更新主线程的UI，而是因为是不安全。
 * 主线程里面，会有一个类来初始化，检查更新的UI是否是主线程。
 * 当我们在主线程里面写了很多业务逻辑的时候，此类初始化完毕，再来子线程里面更新主UI，是无法运行的
 *
 *
 * 高级工具
 *
 *
 * Created by Archer on 2016/6/18.
 */
public class AToolsActivity  extends Activity {

    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
    private static final int READ_SMS = 2;
    private ProgressDialog progressDialog;
    @ViewInject(R.id.progressBar)
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_atools);
        ViewUtils.inject(this);
//        requestPermission();
    }

    /**
     * 点击事件
     * @param view
     */
    public void numberAddressQuery(View view) {

        startActivity(new Intent(this,AddressActivity.class));
    }

//    private Handler mHandler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//        }
//    };


    //备份短信
    public void backupSms(View view) {
        requestPermissionSms();
        requestPermission();

        progressDialog = new ProgressDialog(AToolsActivity.this);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("玩命加载中.....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//加一个样式

        progressDialog.show();
//如果短信太多容易引起线程阻塞，影响体验，所以应该把这个功能仍进子线程里面去
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                    boolean result = SmsUtils.backup(AToolsActivity.this,progressDialog);
//                if(result){
//                    //土司只能放在主线程里面
//                    //子线程要改变UI的话，必须调用looper.prepare(),和 looper.loop()方法。
////                    Message obtain = Message.obtain();
////                    mHandler.sendMessage(obtain);
//
//                    Looper.prepare();
//                    ToastUtils.showToast(AToolsActivity.this,"备份成功");
//                         Looper.loop();
//
//                }else {
//                    Looper.prepare();
//                    ToastUtils.showToast(AToolsActivity.this,"备份失败");
//                    Looper.loop();
//            }
//                progressDialog.dismiss();
//            }
//
//        }).start();

//        boolean result = SmsUtils.backup(AToolsActivity.this);

        new  Thread(){
            @Override
            public void run() {

                boolean result = SmsUtils.backup(AToolsActivity.this, new SmsUtils.BackUpCallBackSms() {
                    @Override
                    public void before(int count) {
progressDialog.setMax(count);
                        progressBar.setMax(count);
                    }

                    @Override
                    public void onBackUpSms(int progress) {

                        progressDialog.setProgress(progress);
                        progressBar.setProgress(progress);
                    }
                });
                if(result){
                    //土司只能放在主线程里面
                    //子线程要改变UI的话，必须调用looper.prepare(),和 looper.loop()方法。
//                    Message obtain = Message.obtain();
//                    mHandler.sendMessage(obtain);
//                    progressDialog.dismiss();
//                    progressBar
//                    Looper.prepare();
//                    ToastUtils.showToast(AToolsActivity.this,"备份成功");
//                    Looper.loop();

                    ToastUtils.showToast(AToolsActivity.this,"备份成功");

                }else {
                    ToastUtils.showToast(AToolsActivity.this,"备份失败");
//                    Looper.prepare();
//                    ToastUtils.showToast(AToolsActivity.this,"备份失败");
//                    Looper.loop();

                }
                progressDialog.dismiss();
            }

        }.start();
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
            // 向用户解释为什么需要这个权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this)
                        .setMessage("申请读取SD卡的权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请sd卡权限
                                ActivityCompat.requestPermissions(AToolsActivity.this,
                                        new String[]{Manifest.permission.CAMERA}, WRITE_EXTERNAL_STORAGE_CODE);
                            }
                        })
                        .show();
            } else {
                //申请SD卡权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
            }
        } else {
//            tvPermissionStatus.setTextColor(Color.GREEN);
//            tvPermissionStatus.setText("相机权限已申请");
            ToastUtils.showToast(AToolsActivity.this,"读取SD卡权限已经申请");
        }
    }

    private void requestPermissionSms() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
            // 向用户解释为什么需要这个权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this)
                        .setMessage("申请读取短信的权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请sd卡权限
                                ActivityCompat.requestPermissions(AToolsActivity.this,
                                        new String[]{Manifest.permission.CAMERA}, READ_SMS);
                            }
                        })
                        .show();
            } else {
                //申请SD卡权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SMS}, READ_SMS);
            }
        } else {
//            tvPermissionStatus.setTextColor(Color.GREEN);
//            tvPermissionStatus.setText("相机权限已申请");
            ToastUtils.showToast(AToolsActivity.this,"读取短信的权限已经申请");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                tvPermissionStatus.setTextColor(Color.GREEN);
////                tvPermissionStatus.setText("相机权限已申请");
//                ToastUtils.showToast(AToolsActivity.this,"读取SD卡权限已经申请");
//            } else {
//                //用户勾选了不再询问
//                //提示用户手动打开权限
//                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    Toast.makeText(this, "读取SD卡权限已经申请", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }

        switch (requestCode){
            case WRITE_EXTERNAL_STORAGE_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                tvPermissionStatus.setTextColor(Color.GREEN);
//                tvPermissionStatus.setText("相机权限已申请");
                ToastUtils.showToast(AToolsActivity.this,"读取SD卡权限已经申请");
            } else {
                //用户勾选了不再询问
                //提示用户手动打开权限
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "读取SD卡权限已经申请", Toast.LENGTH_SHORT).show();
                }
            }
                break;

            case READ_SMS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                tvPermissionStatus.setTextColor(Color.GREEN);
//                tvPermissionStatus.setText("相机权限已申请");
                    ToastUtils.showToast(AToolsActivity.this,"读取短信权限已经申请");
                } else {
                    //用户勾选了不再询问
                    //提示用户手动打开权限
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(this, "读取短信权限已经申请", Toast.LENGTH_SHORT).show();
                    }
                }

            default:
                break;
        }
    }

//    private void requestPermission1() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            // 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
//            // 向用户解释为什么需要这个权限
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
//                new AlertDialog.Builder(this)
//                        .setMessage("申请读取SD卡的权限")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //申请相机权限
//                                ActivityCompat.requestPermissions(AToolsActivity.this,
//                                        new String[]{Manifest.permission.CAMERA}, WRITE_EXTERNAL_STORAGE_CODE);
//                            }
//                        })
//                        .show();
//            } else {
//                //申请相机权限
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
//            }
//        } else {
////            tvPermissionStatus.setTextColor(Color.GREEN);
////            tvPermissionStatus.setText("相机权限已申请");
//            ToastUtils.showToast(AToolsActivity.this,"读取SD卡权限已经申请");
//        }
//    }







//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                tvPermissionStatus.setTextColor(Color.GREEN);
////                tvPermissionStatus.setText("相机权限已申请");
//                ToastUtils.showToast(AToolsActivity.this,"读取SD卡权限已经申请");
//            } else {
//                //用户勾选了不再询问
//                //提示用户手动打开权限
//                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    Toast.makeText(this, "读取SD卡权限已经申请", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }

}




