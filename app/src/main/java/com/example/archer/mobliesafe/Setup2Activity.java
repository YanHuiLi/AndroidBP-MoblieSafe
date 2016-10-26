package com.example.archer.mobliesafe;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.archer.mobliesafe.utils.ToastUtils;
import com.example.archer.mobliesafe.view.SettingItemView;

/**
 * 设置向导页面
 * Created by Archer on 2016/6/5.
 */
public class Setup2Activity extends BaseSetupActivity {


    private static final int READ_PHONE_STATE = 1;
    private SettingItemView sivSIM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        requestPermissionSms();
        sivSIM = (SettingItemView) findViewById(R.id.siv_sim);

        String sim = mPref.getString("sim", null);
        if (!TextUtils.isEmpty(sim)) {
            sivSIM.setCheck(true);
        } else {
            sivSIM.setCheck(false);

        }

        sivSIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivSIM.isChecked()) {
                    sivSIM.setCheck(false);
                    mPref.edit().remove("sim").commit();//删除已经绑定的sim卡
                } else {
                    sivSIM.setCheck(true);
                    //保存sim卡的信息
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String simSerialNumber = telephonyManager.getSimSerialNumber();
                    System.out.println(simSerialNumber);

                    mPref.edit().putString("sim", simSerialNumber).commit();//将SIM卡序列号保存在sp中

                }
            }
        });
    }

    public void showNextPage() {

        //如果sim卡没有绑定就不能到下一个页面
        String sim = mPref.getString("sim", null);
        if (TextUtils.isEmpty(sim)) {

            ToastUtils.showToast(this, "必须绑定sim卡");
            return;
        }
        startActivity(new Intent(this, Setup3Activity.class));
        finish();

        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);

    }


    //展示上一页
    public void showPreviousPage() {
        startActivity(new Intent(this, Setup1Activity.class));
        finish();
        overridePendingTransition(R.anim.trans_previous_in, R.anim.trans_previous_out);

    }


    private void requestPermissionSms() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
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
                                ActivityCompat.requestPermissions(Setup2Activity.this,
                                        new String[]{Manifest.permission.CAMERA}, READ_PHONE_STATE);
                            }
                        })
                        .show();
            } else {
                //申请SD卡权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
            }
        } else {
//            tvPermissionStatus.setTextColor(Color.GREEN);
//            tvPermissionStatus.setText("相机权限已申请");
            ToastUtils.showToast(Setup2Activity.this, "读取短信的权限已经申请");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                tvPermissionStatus.setTextColor(Color.GREEN);
//                tvPermissionStatus.setText("相机权限已申请");
                    ToastUtils.showToast(Setup2Activity.this, "读取SD卡权限已经申请");
                } else {
                    //用户勾选了不再询问
                    //提示用户手动打开权限
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(this, "读取SD卡权限已经申请", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            default:
                break;


        }
    }
}
