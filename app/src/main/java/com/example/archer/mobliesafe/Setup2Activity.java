package com.example.archer.mobliesafe;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.example.archer.mobliesafe.view.SettingItemView;

/**
 * 设置向导页面
 * Created by Archer on 2016/6/5.
 */
public class Setup2Activity extends BaseSetupActivity{


    private SettingItemView sivSIM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        sivSIM = (SettingItemView) findViewById(R.id.siv_sim);

        String sim = mPref.getString("sim", null);
        if (!TextUtils.isEmpty(sim)){
            sivSIM.setCheck(true);
        }else {
            sivSIM.setCheck(false);

        }

        sivSIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivSIM.isChecked()){
                    sivSIM.setCheck(false);
                    mPref.edit().remove("sim").commit();//删除已经绑定的sim卡
                }else {
                    sivSIM.setCheck(true);
                    //保存sim卡的信息
                    TelephonyManager telephonyManager= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String simSerialNumber = telephonyManager.getSimSerialNumber();
                    System.out.println(simSerialNumber);

                    mPref.edit().putString("sim",simSerialNumber).commit();//将SIM卡序列号保存在sp中

                }
            }
        });
    }
    public  void showNextPage() {
        startActivity(new Intent(this,Setup3Activity.class));
        finish();

        overridePendingTransition(R.anim.trans_in,R.anim.trans_out);

    }



    //展示上一页
    public void showPreviousPage(){
        startActivity(new Intent(this,Setup1Activity.class));
        finish();
        overridePendingTransition(R.anim.trans_previous_in,R.anim.trans_previous_out);

    }


}
