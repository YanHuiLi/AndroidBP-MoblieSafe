package com.example.archer.mobliesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 设置向导页面
 * Created by Archer on 2016/6/5.
 */
public class Setup2Activity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
    }

    public void next(View view){
        startActivity(new Intent(this,Setup3Activity.class));
        finish();
    }
    //返回上一页
    public void previous(View view){
        startActivity(new Intent(this,Setup1Activity.class));
        finish();
    }


}
