package com.example.archer.mobliesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.example.archer.mobliesafe.R;
import com.example.archer.mobliesafe.service.LocationService;

/**
 * 短信拦截
 * Created by Archer on 2016/6/16.
 */
public class SmsReceiver  extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        Object[] objects = (Object[]) intent.getExtras().get("pdus");

        assert objects != null;
        for (Object object:objects){
            SmsMessage message = SmsMessage.createFromPdu((byte[]) object);

            String originatingAddress = message.getOriginatingAddress();//短信来源
            String messageBody = message.getMessageBody();
            System.out.println(originatingAddress+";"+messageBody);

            if ("#*alarm*#".equals(messageBody)){
                //播放报警音乐
                MediaPlayer mediaPlayer = MediaPlayer.create(context,R.raw.ylzs);
                 mediaPlayer.setVolume(1f,1f);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                abortBroadcast();//中断系统短信收不到内容了
            }else if ("#*location*#".equals(messageBody)){
                abortBroadcast();//中断系统短信收不到内容了
                   context.startService(new Intent(context, LocationService.class));//开启定位服务

                SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                String location = sharedPreferences.getString("location", "getting Location");
                System.out.println("location:"+location);
                SmsManager smsManager = SmsManager.getDefault();
                String safe_phone = sp.getString("safe_phone", "");
                smsManager.sendTextMessage(safe_phone,null,location,null,null);
            }else if ("#*lockscreen*#".equals(messageBody)){
                System.out.println("锁屏");
            }else if ("#*wipedata*#".equals(messageBody)){
                System.out.println("清除数据");
            }
        }
    }
}
