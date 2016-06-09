package com.example.archer.mobliesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 *
 * 设置引导页的基类。父类
 * Created by Archer on 2016/6/7.
 *
 * 没必要注册，因为不向用户展示
 *
 */
public abstract class BaseSetupActivity extends Activity {

    private GestureDetector mDetector;
    public SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPref = getSharedPreferences("config", MODE_PRIVATE);

        //监听手势滑动
        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            //监听手势滑动
            /**
             * e1表示起始点。e2表示滑动的终点
             * velocityx表示水平速度
             * velocityY表示垂直速度
             */
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                /**
                 * getX拿到的坐标是该组件父控件的坐标，也就是说，在屏幕中，当有一个View的时候，
                 * 拿到的X坐标是以View为标准的。
                 *
                 * getRawX拿到的是以屏幕的左上角为定点的坐标体系。以屏幕为父控件。
                 *
                 * 注意区别和使用
                 */

                //判断纵向滑动是否过大，过大不允许滑动
                if (Math.abs(e2.getRawY()-e1.getRawY())>100){
                    Toast.makeText(BaseSetupActivity.this,"不能这样滑动",Toast.LENGTH_SHORT).show();

                    return true;
                }

                //判断滑动是否过慢
                if (Math.abs(velocityX)<100){
                    Toast.makeText(BaseSetupActivity.this,"你滑动的太慢了",Toast.LENGTH_SHORT).show();

                }

                if (e2.getRawX()-e1.getRawX()>200){

                    showPreviousPage();
                    return true;
                }

                //向左滑动，下一页
                if (e1.getRawX()-e2.getRawX()>200){

                    showNextPage();
                    return true;
                }


                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }
    public abstract void showNextPage() ;



    //展示上一页
    public  abstract void showPreviousPage();

    public void next(View view){
        showNextPage();

    }
    //返回上一页
    public void previous(View view){
        showPreviousPage();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);//委托手势识别器去处理
        return super.onTouchEvent(event);
    }
}
