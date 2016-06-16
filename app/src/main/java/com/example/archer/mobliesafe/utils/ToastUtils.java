package com.example.archer.mobliesafe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Archer on 2016/6/16.
 */
public class ToastUtils {

    public static void showToast(Context context,String text){

        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();

    }
}
