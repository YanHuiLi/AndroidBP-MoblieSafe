package com.example.archer.mobliesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;

import com.example.archer.mobliesafe.utils.SmsUtils;
import com.example.archer.mobliesafe.utils.ToastUtils;

/**
 * 高级工具
 *
 *
 * Created by Archer on 2016/6/18.
 */
public class AToolsActivity  extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_atools);
    }

    /**
     * 点击事件
     * @param view
     */
    public void numberAddressQuery(View view) {

        startActivity(new Intent(this,AddressActivity.class));
    }


    //备份短信
    public void backupSms(View view) {
//如果短信太多容易引起线程阻塞，影响体验，所以应该把这个功能仍进子线程里面去

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = SmsUtils.backup(AToolsActivity.this);
                if(result){
                    Looper.prepare();
                    ToastUtils.showToast(AToolsActivity.this,"备份成功");
                         Looper.loop();
                }else {
                    Looper.prepare();
                    ToastUtils.showToast(AToolsActivity.this,"备份失败");
                    Looper.loop();
                }
            }
        }).start();

//        boolean result = SmsUtils.backup(AToolsActivity.this);


    }

}
