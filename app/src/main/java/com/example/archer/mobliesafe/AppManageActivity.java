package com.example.archer.mobliesafe;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.text.format.Formatter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.archer.mobliesafe.bean.AppInfo;
import com.example.archer.mobliesafe.engine.AppInfos;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

public class AppManageActivity extends AppCompatActivity {

    @ViewInject(R.id.list_view)
    private ListView list_view;
    @ViewInject(R.id.tv_rom)
    private TextView tv_rom;
    @ViewInject(R.id.tv_sd)
    private TextView tv_sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initUI();

    }

    private void initData() {
        List<AppInfo> appinfos = AppInfos.getAppinfos(this);
    }

    private void initUI() {
        setContentView(R.layout.activity_app_manage);
        com.lidroid.xutils.ViewUtils.inject(this);

        long Rom_freeSpace = Environment.getDataDirectory().getFreeSpace();//获取到rom内存的大小

        long SD_freeSpace = Environment.getExternalStorageDirectory().getFreeSpace();//获取sd卡剩余空间


        tv_rom.setText("内存可用："+Formatter.formatFileSize(this,Rom_freeSpace));//格式化
        tv_sd.setText("SD卡可用："+Formatter.formatFileSize(this,SD_freeSpace));//格式化

    }
}
