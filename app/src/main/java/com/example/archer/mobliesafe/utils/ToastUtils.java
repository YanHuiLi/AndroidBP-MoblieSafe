package com.example.archer.mobliesafe.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Archer on 2016/6/16.
 */
public class ToastUtils {

    public static void showToast(Context context,String text){

        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();

    }

    public static void showToast(final Activity context, final String msg){
        if("main".equals(Thread.currentThread().getName())){
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }else{
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
