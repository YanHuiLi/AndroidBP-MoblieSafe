package com.example.archer.mobliesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

/**
 * 设置第三个向导向导页面
 * Created by Archer on 2016/6/5.
 */
public class Setup4Activity extends Activity{

    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        mPref = getSharedPreferences("config", MODE_PRIVATE);

    }

    public void next(View view){
        startActivity(new Intent(this,LostFoundActivity.class));
        finish();
        //更新configed表示已经展示过设置向导，下次进来就不展示了
        mPref.edit().putBoolean("configed",true).commit();

    }
    //返回上一页
    public void previous(View view){
        startActivity(new Intent(this,Setup3Activity.class));
        finish();
    }

}
