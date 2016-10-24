package com.example.archer.mobliesafe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
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

    private ProgressDialog progressDialog;
    @ViewInject(R.id.progressBar)
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_atools);
        ViewUtils.inject(this);
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

}
