package com.example.archer.mobliesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.example.archer.mobliesafe.bean.BlackNumberInfo;
import com.example.archer.mobliesafe.db.dao.BlackNumberDao;

import java.lang.reflect.Method;

import static android.R.attr.id;
import static android.R.attr.mode;

public class CallSafeMyService extends Service {

    private BlackNumberDao dao;
    private TelephonyManager tm;

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

        //获取系统电话服务
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        MyPhoneStateListener myPhoneStateListener = new MyPhoneStateListener();
        tm.listen(myPhoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);


//初始化短信的广播
        InnerReceiver innerReceiver = new InnerReceiver();
//        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(innerReceiver, intentFilter);
    }


    private class MyPhoneStateListener extends PhoneStateListener{

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            /**
             * Callback invoked when device call state changes.
             * @param state call state
             * @param incomingNumber incoming call phone number. If application does not have
             * {@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE} permission, an empty
             * string will be passed as an argument.
             *
             * @see TelephonyManager#CALL_STATE_IDLE    电话闲置
             * @see TelephonyManager#CALL_STATE_RINGING  电话响
             * @see TelephonyManager#CALL_STATE_OFFHOOK    电话接通
             *
             *
             *   /**
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

            switch (state){

                case TelephonyManager.CALL_STATE_RINGING:

                    String mode = dao.findNumber(incomingNumber);


                    if (mode.equals("1")||mode.equals("2")){
                        System.out.println("挂断黑名单");

                         endCall();

                    }

                    break;

            }

            super.onCallStateChanged(state, incomingNumber);
        }

        /**
         * 挂断电话
         */

        private void endCall() {

            try {
                //通过类加载器得到ServiceManager类
                Class<?> aClass = getClassLoader().loadClass("android.os.ServiceManager");
//通过反射得到当前的方法
                Method method= aClass.getDeclaredMethod("getSystemService", String.class);

                IBinder ibinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);





            } catch (Exception e) {
                e.printStackTrace();
            }


        }
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
