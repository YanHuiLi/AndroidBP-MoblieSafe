package com.example.archer.mobliesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Archer on 2016/6/5.
 */
public class LostFoundActivity  extends Activity{

    private ImageView imageViewProcect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mPref = getSharedPreferences("config", MODE_PRIVATE);
        //设置是否设置过向导
        boolean configed = mPref.getBoolean("configed",false);
        if(configed){
            setContentView(R.layout.activity_lost_found);
            TextView safePhone = (TextView) findViewById(R.id.tv_safe_phone);

            String safe_phone = mPref.getString("safe_phone", "");
            safePhone.setText(safe_phone);//更新安全号码

            //根据sp去更新保护锁
            imageViewProcect= (ImageView) findViewById(R.id.iv_protect);
            boolean protect = mPref.getBoolean("protect", false);
            if (protect){
                imageViewProcect.setImageResource(R.drawable.lock);
            }else {
                imageViewProcect.setImageResource(R.drawable.unlock);

            }

        }else {
            //跳转设置向导
            startActivity(new Intent(this,Setup1Activity.class));
            finish();
        }
    }

    //重新进入设置向导
    public void reEnter(View view) {
        startActivity(new Intent(this,Setup1Activity.class));
        finish();
        overridePendingTransition(R.anim.trans_in,R.anim.trans_out);


    }
}
