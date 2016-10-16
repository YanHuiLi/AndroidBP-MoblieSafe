package com.example.archer.mobliesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;

import com.example.archer.mobliesafe.bean.BlackNumberInfo;
import com.example.archer.mobliesafe.db.dao.BlackNumberDao;

public class CallSafeMyService extends Service {

    private BlackNumberDao dao;

    public CallSafeMyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化黑名单
        dao = new BlackNumberDao(this);

//初始化短信的广播
        InnerReceiver innerReceiver = new InnerReceiver();
//        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(innerReceiver, intentFilter);
    }

    private class InnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            System.out.println("短信来了");
            Object[] objects = (Object[]) intent.getExtras().get("pdus");

            assert objects != null;
            for (Object object : objects) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) object);

                String originatingAddress = message.getOriginatingAddress();//短信来源
                String messageBody = message.getMessageBody();
                System.out.println(originatingAddress + ";" + messageBody);

                String mode = dao.findNumber(originatingAddress);//通过短信的电话号码查询拦截的模式
                System.out.println("mode=========="+mode);
                /**
                 * 黑名单电话号码
                 *
                 * 黑名单拦截模式
                 * 1.全部拦截，电话拦截和短信拦截
                 * 2.电话拦截
                 * 3.短信拦截
                 *
                 * 自android4.4以上加强了短信拦截的功能
                 * 实用abortBroadcast()已经不能在拦截短信了。
                 * 需要另另辟蹊径
                 */

                if (mode.equals("1")){
                       abortBroadcast();//没用了
                }else if(mode.equals("3")){
                    abortBroadcast();//没用了

                }

            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
