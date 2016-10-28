package com.example.archer.mobliesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Archer on 2016/10/28.
 */

public class SharedPreferenceUtils {

    public static final  String SP_NAME= "config";

     public static void saveBoolean(Context context,String key,boolean value){

         SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
         sp.edit().putBoolean(key,value).commit();//不要忘记了提交


     }


    public static boolean getBoolean(Context context,String key,boolean  value){

        //1.第一步一定是初始化 sp
        //2.读取数据
        //3.注意这个value得意思是 默认得值，一般为false
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return  sp.getBoolean(key,value);





    }
}
