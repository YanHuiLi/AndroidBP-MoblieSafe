package com.example.archer.mobliesafe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.example.archer.mobliesafe.db.dao.AddressDao;

/**
 * Created by Archer on 2016/6/18.
 * 归属地查询页面
 */
public class AddressActivity extends Activity{


    private EditText etNumber;

    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        etNumber= (EditText) findViewById(R.id.et_number);
        tvResult= (TextView) findViewById(R.id.tv_result);


       //监听ettext的变化
        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //文字变化前的回调
                String address = AddressDao.getAddress(s.toString());
                tvResult.setText(address);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //文字发生变化的回调
            }

            @Override
            public void afterTextChanged(Editable s) {
                String address = AddressDao.getAddress(s.toString());
                tvResult.setText(address);
            }
        });

    }

    public void Query(View view) {

        String number = etNumber.getText().toString().trim();

        if (!TextUtils.isEmpty(number)){
            String address = AddressDao.getAddress(number);
            tvResult.setText(address);
        }else{
            Animation shake= AnimationUtils.loadAnimation(this,R.anim.shake);

// 插补器的实现
// shake.setInterpolator(new Interpolator() {
//                @Override
//                public float getInterpolation(float input) {
//
//
//                  int y=0;
//
//                    return y;
//                }
//            });
            etNumber.startAnimation(shake);
            vibrate();
        }

    }

    private void vibrate(){
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(500);//震动一秒
//等1s震动2s，再等待1s震动2s，再等1s震动3s,
// 第二个参数-1等于不循环，震动一次，
// 0代表重头循环
        //1代表从第一个开始循环
        //第二个参数表示从第几个开始循环
//        vibrator.vibrate(new long[]{1000,2000,1000,3000},-1);
//        vibrator.cancel();//取消震动
    }
}
