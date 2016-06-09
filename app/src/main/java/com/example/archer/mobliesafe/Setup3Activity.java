package com.example.archer.mobliesafe;

import android.content.Intent;
import android.os.Bundle;

/**
 * 设置第三个向导向导页面
 * Created by Archer on 2016/6/5.
 */
public class Setup3Activity extends BaseSetupActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
    }

    @Override
    public void showNextPage() {
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

}
