package com.example.archer.mobliesafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.archer.mobliesafe.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EnterPwdActivity extends AppCompatActivity {


    @Bind(R.id.bt_1)
    Button bt1;
    @Bind(R.id.bt_2)
    Button bt2;
    @Bind(R.id.bt_3)
    Button bt3;
    @Bind(R.id.bt_4)
    Button bt4;
    @Bind(R.id.bt_5)
    Button bt5;
    @Bind(R.id.bt_6)
    Button bt6;
    @Bind(R.id.bt_7)
    Button bt7;
    @Bind(R.id.bt_8)
    Button bt8;
    @Bind(R.id.bt_9)
    Button bt9;
    @Bind(R.id.bt_clean_all)
    Button btCleanAll;
    @Bind(R.id.bt_0)
    Button bt0;
    @Bind(R.id.bt_delete)
    Button btDelete;
    @Bind(R.id.et_pwd)
    EditText etPwd;
    @Bind(R.id.btn_selectOK)
    Button btnSelectOK;

    private String packageName;

    //输入密码的界面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set_pwd);
        ButterKnife.bind(this);

        initUI();
    }

    private void initUI() {

        Intent intent = getIntent();

        if (intent != null) {
            packageName = intent.getStringExtra("packageName");
        }

        etPwd.setInputType(InputType.TYPE_NULL);
    }


    @OnClick({R.id.bt_1, R.id.bt_2, R.id.bt_3, R.id.bt_4, R.id.bt_5, R.id.bt_6, R.id.bt_7, R.id.bt_8, R.id.bt_9, R.id.bt_clean_all, R.id.bt_0, R.id.bt_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_1:
                String string1 = etPwd.getText().toString();
                etPwd.setText(string1 + bt1.getText().toString());

                break;
            case R.id.bt_2:
                String string2 = etPwd.getText().toString();
                etPwd.setText(string2 + bt2.getText().toString());

                break;
            case R.id.bt_3:
                String string3 = etPwd.getText().toString();
                etPwd.setText(string3 + bt3.getText().toString());

                break;
            case R.id.bt_4:
                String string4 = etPwd.getText().toString();
                etPwd.setText(string4 + bt4.getText().toString());

                break;
            case R.id.bt_5:
                String string5 = etPwd.getText().toString();
                etPwd.setText(string5 + bt5.getText().toString());

                break;
            case R.id.bt_6:
                String string6 = etPwd.getText().toString();
                etPwd.setText(string6 + bt6.getText().toString());

                break;
            case R.id.bt_7:
                String string7 = etPwd.getText().toString();
                etPwd.setText(string7 + bt7.getText().toString());

                break;
            case R.id.bt_8:
                String string8 = etPwd.getText().toString();
                etPwd.setText(string8 + bt8.getText().toString());

                break;
            case R.id.bt_9:
                String string9 = etPwd.getText().toString();
                etPwd.setText(string9 + bt9.getText().toString());

                break;
            case R.id.bt_clean_all:
                etPwd.setText("");


                break;
            case R.id.bt_0:

                String string0 = etPwd.getText().toString();
                etPwd.setText(string0 + bt0.getText().toString());

                break;
            case R.id.bt_delete:

                String string11 = etPwd.getText().toString();

                if (string11.length() == 0) {
                    return;
                }
                etPwd.setText(string11.substring(0, string11.length() - 1));

                break;
        }
    }

    @OnClick(R.id.btn_selectOK)
    public void onClick() {//密码写成123，和首页的一样
        String trim = etPwd.getText().toString().trim();
        if (trim.equals("123")){

            ToastUtils.showToast(EnterPwdActivity.this,"输入密码正确");

            Intent intent = new Intent();
            // 发送广播。停止保护
            intent.setAction("com.archer.mobilesafe.stopprotect");
            // 跟狗说。现在停止保护短信
            intent.putExtra("packageName", packageName);

            sendBroadcast(intent);

            finish();


        }else {
            ToastUtils.showToast(EnterPwdActivity.this,"输入密码错误");

        }
    }

    @Override
    public void onBackPressed() {
// 当用户输入后退健 的时候。我们进入到桌面
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.MONKEY");
        startActivity(intent);
    }
}
