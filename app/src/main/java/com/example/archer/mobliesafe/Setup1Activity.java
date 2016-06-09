package com.example.archer.mobliesafe;

import android.content.Intent;
import android.os.Bundle;

/**
 * 设置向导页面
 * Created by Archer on 2016/6/5.
 */
public class Setup1Activity extends BaseSetupActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);

    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(this,Setup2Activity.class));
        finish();

        //调转的动画
        overridePendingTransition(R.anim.trans_in,R.anim.trans_out);
    }

    @Override
    public void showPreviousPage() {

    }

//    public void next(View view){
//        startActivity(new Intent(this,Setup2Activity.class));
//        finish();
//
//        //调转的动画
//        overridePendingTransition(R.anim.trans_in,R.anim.trans_out);
//    }

}
