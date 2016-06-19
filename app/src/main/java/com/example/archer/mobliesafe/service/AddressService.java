package com.example.archer.mobliesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.example.archer.mobliesafe.db.dao.AddressDao;

/**
 * 来电提醒的服务
 * <p/>
 * Created by Archer on 2016/6/19.
 */
public class AddressService extends Service {

    private TelephonyManager tmService;
    private MyListener mylistener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        //监听来电
        tmService = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        mylistener = new MyListener();
        tmService.listen(mylistener, PhoneStateListener.LISTEN_CALL_STATE);//监听打电话的状态
    }

    class MyListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://电话铃声响了
                    System.out.println("电话铃响");
//根据来电号码，查询归属地
                    String address = AddressDao.getAddress(incomingNumber);
                    Toast.makeText(AddressService.this, address, Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }

            super.onCallStateChanged(state, incomingNumber);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tmService.listen(mylistener,PhoneStateListener.LISTEN_NONE);
    }
}
