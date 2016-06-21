package com.example.archer.mobliesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.archer.mobliesafe.R;
import com.example.archer.mobliesafe.db.dao.AddressDao;
import com.example.archer.mobliesafe.utils.ToastUtils;

/**
 * 来电提醒的服务
 * <p/>
 * Created by Archer on 2016/6/19.
 */
public class AddressService extends Service {

    private TelephonyManager tmService;
    private MyListener mylistener;
    private OutCallReceiver receiver;
    private WindowManager mWM;
    private View view;

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

    //动态注册广播
        receiver = new OutCallReceiver();
        IntentFilter filter=new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(receiver,filter);


    }

    class MyListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://电话铃声响了
                    System.out.println("电话铃响");
//根据来电号码，查询归属地
                    String address = AddressDao.getAddress(incomingNumber);
//                    Toast.makeText(AddressService.this, address, Toast.LENGTH_LONG).show();
                   showToast(address);
                    break;

                case TelephonyManager.CALL_STATE_IDLE://电话闲置状态
                    if (mWM!=null&&view!=null){
                    mWM.removeView(view);//windows中移除界面
                        view=null;
                }
                    break;
                default:
                    break;
            }

            super.onCallStateChanged(state, incomingNumber);
        }
    }

    class OutCallReceiver  extends BroadcastReceiver{

        /**
         * 需要权限
         * 去电的广播接收者
         * Created by Archer on 2016/6/19.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            String resultData = getResultData();//返回去电的号码

            String address = AddressDao.getAddress(resultData);
//            ToastUtils.showToast(context,address);
            showToast(address);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tmService.listen(mylistener,PhoneStateListener.LISTEN_NONE);

     unregisterReceiver(receiver);//注销广播


    }
/**
 * 自定义归属地的浮窗
 *
 */

    private void showToast(String text){
        //使用windowManager可以在其他第三方APP中运用上弹出浮窗
        mWM = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);

        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;

        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.setTitle("Toast");
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;


//        view = new TextView(this);
        view=  View.inflate(this, R.layout.toast_address,null);
        TextView textView= (TextView) view.findViewById(R.id.tvNumber);




        textView.setText(text);
//        view.setTextColor(Color.RED);
        mWM.addView(view, mParams);//将view添加到屏幕
    }

}
