package com.example.archer.mobliesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.archer.mobliesafe.utils.ToastUtils;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * 监听开机启动的广播
 * Created by Archer on 2016/6/9.
 */
public class BootCompleteReceiver  extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        boolean protect = sp.getBoolean("protect", false);

        //只有在防盗保护开启的前提下才进行sim卡判断
        if (protect){

            ToastUtils.showToast(context,"boot已经启动");

            String sim = sp.getString("sim", null);//获取绑定的sim卡


            if (!TextUtils.isEmpty(sim)){
                //获取本地的sim卡
                TelephonyManager telephonyManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                //当前的sim卡
                String CurrentsimSerialNumber = telephonyManager.getSimSerialNumber()+"123";
//                System.out.println(CurrentsimSerialNumber);
                if (sim.equals(CurrentsimSerialNumber)){
                    System.out.println("手机安全");
                }else{
                    System.out.println("SIM已经变化，发送报警短信");

                    String safe_phone = sp.getString("safe_phone", "");

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(safe_phone,null,"sim card changed",null,null);

                }

            }

        }


    }
}
