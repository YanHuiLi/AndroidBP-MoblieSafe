package com.example.archer.mobliesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 设置第三个向导向导页面
 * Created by Archer on 2016/6/5.
 */
public class Setup3Activity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
    }

    public void next(View view){
        startActivity(new Intent(this,Setup4Activity.class));
        finish();
    }
    //返回上一页
    public void previous(View view){
        startActivity(new Intent(this,Setup2Activity.class));
        finish();
    }

}
