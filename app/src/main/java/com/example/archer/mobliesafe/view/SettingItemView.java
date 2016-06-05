package com.example.archer.mobliesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.archer.mobliesafe.R;

/**
 * 设置中心的自定义组合控件
 * Created by Archer on 2016/6/3.
 * 1.自定义一个view集成ViewGroup，比如RelativeLayout
 * 2.编写组合控件的布局文件，在自定义的View中加载
 * 3.自定义属性
 */
public class SettingItemView extends LinearLayout{

    private static final  String NAMESPACE="http://schemas.android.com/apk/res/com.example.archer.mobliesafe";

    private TextView tvTitle,tvdec;
    private CheckBox checkBox;
    /**
     * ctrl +alt +F 快捷键：成员变量转化为全局变量
     */
    private String mDescOff;
    private String mDescOn;
    private String mTitle;

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //注意代码的执行顺序
        //根据属性名称获得属性的值
        mTitle = attrs.getAttributeValue(NAMESPACE, "SetItemTitle");
        mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
        mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");
        initView();

//        int attributeCount = attrs.getAttributeCount();
//        for (int i=0;i<attributeCount;i++){
//            String attributeName = attrs.getAttributeName(i);
//            String attributeValue = attrs.getAttributeValue(i);
//            System.out.println(attributeName+"="+attributeValue);
//        }



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
        /**
         * 根据属性名称获取属性的值
         */
        View.inflate(getContext(), R.layout.view_setting_item,this);
         tvTitle= (TextView) findViewById(R.id.tv_title);
         tvdec= (TextView) findViewById(R.id.tv_dec);
        checkBox= (CheckBox) findViewById(R.id.cb_status);

        setTitle(mTitle);//设置标题

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

        //根据选择的状态更新文本
        checkBox.setChecked(check);
        if (check){
            setDec(mDescOn);
        }else {
            setDec(mDescOff);
        }
    }

}
