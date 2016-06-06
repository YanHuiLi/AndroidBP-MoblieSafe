package com.example.archer.mobliesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by Archer on 2016/6/5.
 */
public class LostFoundActivity  extends Activity{

    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        //设置是否设置过向导
        boolean configed = mPref.getBoolean("configed",false);
        if(configed){
            setContentView(R.layout.activity_lost_found);

        }else {
            //跳转设置向导
            startActivity(new Intent(this,Setup1Activity.class));
            finish();
        }
    }
}
