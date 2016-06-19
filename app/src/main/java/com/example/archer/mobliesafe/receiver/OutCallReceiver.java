package com.example.archer.mobliesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.archer.mobliesafe.db.dao.AddressDao;
import com.example.archer.mobliesafe.utils.ToastUtils;

/**
 * 需要权限
 * 去电的广播接收者
 * Created by Archer on 2016/6/19.
 */
public class OutCallReceiver  extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        String resultData = getResultData();//返回去电的号码

        String address = AddressDao.getAddress(resultData);
        ToastUtils.showToast(context,address);

    }
}
