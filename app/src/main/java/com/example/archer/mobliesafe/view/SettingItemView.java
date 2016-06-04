package com.example.archer.mobliesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.archer.mobliesafe.R;

/**
 * 设置中心的自定义控件
 * Created by Archer on 2016/6/3.
 */
public class SettingItemView extends LinearLayout{

    private TextView tvTitle,tvdec;
    private CheckBox checkBox;
    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    /**
     * 初始化方法。
     */
    private void  initView(){

        //将自定义的好的布局文件设置给当前的settingViewItem
        //已经塞过去了，就不需要再view了，注意this和null的区别
        View.inflate(getContext(), R.layout.view_setting_item,this);
         tvTitle= (TextView) findViewById(R.id.tv_title);
         tvdec= (TextView) findViewById(R.id.tv_dec);
        checkBox= (CheckBox) findViewById(R.id.cb_status);

    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }

    public void setDec(String  dec){
        tvdec.setText(dec);
    }

    //判断返回勾选状态
    public boolean isChecked(){

        return checkBox.isChecked();
    }

    public void setCheck(boolean check){
        checkBox.setChecked(check);
    }

}
