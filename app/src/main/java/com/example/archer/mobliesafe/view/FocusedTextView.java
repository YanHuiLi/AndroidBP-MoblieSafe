package com.example.archer.mobliesafe.view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * function：获取焦点的textview
 * Created by Archer on 2016/6/3.
 */
public class FocusedTextView extends TextView{


    //直接使用textview，用代码去new的时候
    public FocusedTextView(Context context) {
        super(context);
    }

    /**
     * 表示有没有获取焦点
     * @return
     * 跑马灯要运行，首先调用此函数，判断是否有焦点
     * 是true的话，跑马灯才会有效果
     * 强制返回true，不管实际上textview，强制返回true，认为有焦点
     */
    @Override
    public boolean isFocused() {

        return true;
    }

    //有属性时候，用这个方法
    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //有style的话，就用这个方法
    public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
