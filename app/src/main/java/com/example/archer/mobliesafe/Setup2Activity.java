package com.example.archer.mobliesafe;

import android.content.Intent;
import android.os.Bundle;

/**
 * 设置向导页面
 * Created by Archer on 2016/6/5.
 */
public class Setup2Activity extends BaseSetupActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);


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
