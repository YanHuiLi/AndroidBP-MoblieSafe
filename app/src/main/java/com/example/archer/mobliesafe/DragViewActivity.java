package com.example.archer.mobliesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 修改归属地显示位置
 * Created by Archer on 2016/6/21.
 */
public class DragViewActivity extends Activity {

    private TextView tvTop,tvBootom;
    private ImageView ivDrag;

    private int startY;
    private int startX;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_view);
        mPref = getSharedPreferences("config", MODE_PRIVATE);

        tvTop= (TextView) findViewById(R.id.tv_top);
        tvBootom= (TextView) findViewById(R.id.tv_bottom);
        ivDrag= (ImageView) findViewById(R.id.iv_drag);



        int lastX = mPref.getInt("lastX", 0);
        int lastY = mPref.getInt("lastY", 0);
         //OnMeasure测量,onLayout放位置,onDraw绘制
//        ivDrag.layout(lastX,lastY,lastX+ivDrag.getWidth(),lastY+ivDrag.getHeight());不能使用这个方法,因为还没有测量完成，就不能安放位置

        //屏幕宽高
        final int winwidth = getWindowManager().getDefaultDisplay().getWidth();
        final int winheight = getWindowManager().getDefaultDisplay().getHeight();

        if (lastY>winheight/2){//大于屏幕高度的一半，上边显示，下边隐藏

            tvTop.setVisibility(View.VISIBLE);
            tvBootom.setVisibility(View.INVISIBLE);
        }else {
            tvTop.setVisibility(View.INVISIBLE);
            tvBootom.setVisibility(View.VISIBLE);
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivDrag.getLayoutParams();//布局参数

        layoutParams.leftMargin=lastX;//设置左边距
        layoutParams.topMargin=lastY;//设置top边距

        ivDrag.setLayoutParams(layoutParams);


        //触摸监听
        ivDrag.setOnTouchListener(new View.OnTouchListener() {



            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        //获取起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();


                        break;

                    case MotionEvent.ACTION_MOVE:

                        int endX=(int) event.getRawX();
                        int endY= (int) event.getRawY();

                        int dX=endX-startX;
                        int dY=endY-startY;//计算移动偏移量

                        int l=ivDrag.getLeft()+dX;
                        int r=ivDrag.getRight()+dX;
                        int t=ivDrag.getTop()+dY;
                        int b=ivDrag.getBottom()+dY;//更新坐标点举例



                        //判断是否超出屏幕的边界
                        if (l<0||r>winwidth||t<0||b>winheight-50){
                            break;
                        }

                        if (t>winheight/2){//大于屏幕高度的一半，上边显示，下边隐藏

                            tvTop.setVisibility(View.VISIBLE);
                            tvBootom.setVisibility(View.INVISIBLE);
                        }else {
                            tvTop.setVisibility(View.INVISIBLE);
                            tvBootom.setVisibility(View.VISIBLE);
                        }

                        ivDrag.layout(l,t,r,b);//更新界面


                        //重新初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;


                    case  MotionEvent.ACTION_UP:

                        //记录坐标点
                        SharedPreferences.Editor edit=mPref.edit();
                        edit.putInt("lastX",ivDrag.getLeft());
                        edit.putInt("lastY",ivDrag.getTop());

                        edit.commit();


                        break;



                }

                return true;
            }
        });

    }
}
