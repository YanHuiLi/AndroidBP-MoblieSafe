package com.example.archer.mobliesafe;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.archer.mobliesafe.bean.AppInfo;
import com.example.archer.mobliesafe.engine.AppInfos;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class AppManageActivity extends AppCompatActivity {

    @ViewInject(R.id.list_view)
    private ListView list_view;
    @ViewInject(R.id.tv_rom)
    private TextView tv_rom;
    @ViewInject(R.id.tv_sd)
    private TextView tv_sd;
    private List<AppInfo> appinfos;
    private ArrayList<AppInfo> userAppInfos;
    private ArrayList<AppInfo> systemInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();

        initData();

    }

    private class AppManageAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return appinfos.size();
        }

        @Override
        public Object getItem(int position) {

if (position==0){//如果是等于特殊的条目，就直接返回空，不用取关
    return null;
}else if (position==userAppInfos.size()+1){//第二个特殊条目的位置
    return  null;
}
            AppInfo appinfo ;

            if (position<userAppInfos.size()+1){

//                System.out.println("userapp的个数是多少"+userAppInfos.size());

                appinfo=userAppInfos.get(position-1);

            }else {
                int location =userAppInfos.size()+2;
                appinfo=systemInfos.get(position-location);
            }


            return appinfo;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(position==0){//如果是position等于0的话，说明是第一个textView的展示

                //新建一个特殊的展示的textView
                TextView textView= new TextView(AppManageActivity.this);

                textView.setTextColor(Color.WHITE);//字体会白色
                textView.setTextSize(18);
                textView.setBackgroundColor(Color.GRAY);//背景颜色为灰色

                textView.setText("用户程序（"+userAppInfos.size()+")");
                return  textView;

            }else  if (position==userAppInfos.size()+1){//第二个系统程序的textView

                TextView textView=new TextView(AppManageActivity.this);
                textView.setTextSize(18);
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.GRAY);

                textView.setText("系统程序（"+systemInfos.size()+")");

                return  textView;

            }

            AppInfo appinfo ;

            if (position<userAppInfos.size()+1){

//                System.out.println("userapp的个数是多少"+userAppInfos.size());

                appinfo=userAppInfos.get(position-1);

            }else {
                int location =userAppInfos.size()+2;
                appinfo=systemInfos.get(position-location);
            }



            View view=null;
            ViewHolder hodler;

            if (convertView!=null&&convertView instanceof LinearLayout){
                view= convertView;
                hodler  = (ViewHolder) convertView.getTag();
            }else {
                hodler = new ViewHolder();
                view = View.inflate(AppManageActivity.this, R.layout.item_app_manager, null);
                hodler. iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                hodler.tv_apkName= (TextView) view.findViewById(R.id.tv_apkName);
                hodler.tv_apkLocation= (TextView)view.findViewById(R.id.tv_location);
                hodler.tv_apkSize= (TextView) view.findViewById(R.id.tv_size);


                view.setTag(hodler);
            }


            hodler.iv_icon.setBackground(appinfo.getIcon());
            hodler.tv_apkName.setText(appinfo.getApkName());
            hodler.tv_apkSize.setText(Formatter.formatFileSize(AppManageActivity.this,appinfo.getApkSize()));


            if (appinfo.isRom()){
                hodler.tv_apkLocation.setText("手机内存");
            }else {
                hodler.tv_apkLocation.setText("SD卡");

            }

            return view;
        }
    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            AppManageAdapter mAdapter=new AppManageAdapter();
            list_view.setAdapter(mAdapter);
        }
    };

    private void initData() {

        //因为是从手机访问是一个耗时的操作，所以使用异步的做法

        new Thread(){
            @Override
            public void run() {
                //获取所有的安装到手机上的应用程序

                appinfos = AppInfos.getAppinfos(AppManageActivity.this);

                //拆分成 用户程序集合 +系统程序集合
                userAppInfos = new ArrayList<>();
                systemInfos = new ArrayList<>();
                for (AppInfo appinfo : appinfos) {
                    if (appinfo.isUserApp()){
                        userAppInfos.add(appinfo);
                    }else {
                        systemInfos.add(appinfo);
                    }
                }
//                Message obtain = Message.obtain();//拿到一个消息
//                mHandler.sendMessage(obtain);//发送 消息



                mHandler.sendEmptyMessage(0);


            }
        }.start();

    }

    private void initUI() {
        setContentView(R.layout.activity_app_manage);
        com.lidroid.xutils.ViewUtils.inject(this);

        long Rom_freeSpace = Environment.getDataDirectory().getFreeSpace();//获取到rom内存的大小

        long SD_freeSpace = Environment.getExternalStorageDirectory().getFreeSpace();//获取sd卡剩余空间


        tv_rom.setText("内存可用："+Formatter.formatFileSize(this,Rom_freeSpace));//格式化
        tv_sd.setText("SD卡可用："+Formatter.formatFileSize(this,SD_freeSpace));//格式化

    }

    private  static class ViewHolder {
        ImageView iv_icon;
        TextView tv_apkName;
        TextView tv_apkLocation;
        TextView tv_apkSize;
    }
}
