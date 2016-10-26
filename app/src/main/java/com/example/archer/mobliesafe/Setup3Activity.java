package com.example.archer.mobliesafe;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.archer.mobliesafe.utils.ToastUtils;

/**
 * 设置第三个向导向导页面
 * Created by Archer on 2016/6/5.
 */
public class Setup3Activity extends BaseSetupActivity{
    private static final int ReadContact = 1;
    private EditText etPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        requestPermission();
        etPhone= (EditText) findViewById(R.id.et_phone);
        String safe_phone = mPref.getString("safe_phone", "");
        etPhone.setText(safe_phone);

    }

    @Override
    public void showNextPage() {
        String phone = etPhone.getText().toString().trim();//trim方法过滤空格
        if (TextUtils.isEmpty(phone)){
            ToastUtils.showToast(this,"安全号码不能为空");
            return;
        }

        mPref.edit().putString("safe_phone",phone).commit();//保存安全号码
        startActivity(new Intent(this,Setup4Activity.class));
        finish();
        overridePendingTransition(R.anim.trans_in,R.anim.trans_out);

    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this,Setup2Activity.class));
        finish();

        overridePendingTransition(R.anim.trans_previous_in,R.anim.trans_previous_out);

    }

    /**
     * 选择联系人
     * @param view
     */

    public void selectContact(View view) {

        Intent intent=new Intent(this,ContactActivity.class);
        startActivityForResult(intent,0);
        /**
         * 这个0是要求传入一个requestCode，标定为一个唯一的值即可
         * 但是如果有很多逻辑，则需要在onActivityResult里面对requestCode的值进行判断
         * 在该项目中，只有一个读取联系人的意图，因此只需要判断一个即可。
         */


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case 0:
                if (resultCode== Activity.RESULT_OK){
                    String phone = data.getStringExtra("phone");

                    phone = phone.replaceAll("-", "").replaceAll(" ", "");
                    etPhone.setText(phone);//把电话号码设置给输入框
                }
                break;
            case 1:{
         //当requestCode为1的时候，比如说执行调取相册的逻辑
                //以此类推
            }
            default:

        }


        super.onActivityResult(requestCode, resultCode, data);
    }
    //这个next用来相应下一步的点击事件，其实是使用到了，因此不能删除
//    public void next(View view){
//        startActivity(new Intent(this,Setup4Activity.class));
//        finish();
//
//        overridePendingTransition(R.anim.trans_in,R.anim.trans_out);
//
//    }
    //返回上一页
    //这个previous用来相应下一步的点击事件，其实是使用到了，因此不能删除

//    public void previous(View view){
//        startActivity(new Intent(this,Setup2Activity.class));
//        finish();
//
//        overridePendingTransition(R.anim.trans_previous_in,R.anim.trans_previous_out);
//
//    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
            // 向用户解释为什么需要这个权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                new AlertDialog.Builder(this)
                        .setMessage("申请读取联系人的权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请相机权限
                                ActivityCompat.requestPermissions(Setup3Activity.this,
                                        new String[]{Manifest.permission.READ_CONTACTS}, ReadContact);
                            }
                        })
                        .show();
            } else {
                //申请读取联系人权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS}, ReadContact);
            }
        } else {
//            tvPermissionStatus.setTextColor(Color.GREEN);
//            tvPermissionStatus.setText("相机权限已申请");
            ToastUtils.showToast(Setup3Activity.this,"读取联系人权限");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ReadContact) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                tvPermissionStatus.setTextColor(Color.GREEN);
//                tvPermissionStatus.setText("相机权限已申请");
                ToastUtils.showToast(Setup3Activity.this,"联系人权限已申请");
            } else {
                //用户勾选了不再询问
                //提示用户手动打开权限
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "读取联系人权限", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
