package com.example.archer.mobliesafe;

import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;


public class CleanCache extends AppCompatActivity {


    private static final int UPDATE_CACHE_APP = 100;
    private static final int CHECK_CACHE_APP = 101;
    private static final int CHECK_FINISH = 102;
    private PackageManager packageManager;
    private Button bt_clear_cache;
    private ProgressBar pb_bar;
    private TextView tv_name;
    private LinearLayout ll_parent;

    private int mIndex=0;


private Handler  handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {

          switch (msg.what){
              case  UPDATE_CACHE_APP:
                  //8.在线性布局种添加有缓存应用的app

                  View view = View.inflate(getApplicationContext(),R.layout.item_clean_cache, null);

                  ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                  TextView tv_Item_name = (TextView) view.findViewById(R.id.tv_name);
                  TextView tv_cache_size = (TextView)view.findViewById(R.id.tv_cache_size);
//                  ImageView iv_delete = (ImageView)view.findViewById(R.id.iv_delete_cache);


                  CacheInfo cacheInfo = (CacheInfo) msg.obj;
//                  iv_icon.setBackgroundDrawable(cacheInfo.icon);
                  iv_icon.setImageDrawable(cacheInfo.icon);
                  tv_Item_name.setText(cacheInfo.packagename);
                  tv_cache_size.setText("缓存大小:"+Formatter.formatFileSize(getApplicationContext(),cacheInfo.cacheSize));

                  ll_parent.addView(view,0);

                  break;

              case  CHECK_CACHE_APP:

                  tv_name.setText((String)msg.obj);

                  break;

              case  CHECK_FINISH:

                  tv_name.setText("扫描完成");
break;


          }
    }
};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();

        initData();
    }

    /**
     * 遍历手机所有应用，获取有缓存的应用
     */
    private void initData() {

        //开启一个子线程来处理耗时数据操作
        new  Thread(){
            @Override
            public void run() {
                //1.获取包的的管理对象
                packageManager =getPackageManager();
                //2.获取安装在手机上的所有应用
                List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
                //3.设置进度条的总数
                pb_bar.setMax(installedPackages.size());
                //4.遍历每个应用，获取有缓存的应用信息。（应用名称，缓存大小，包名）
                for (PackageInfo packageInfo : installedPackages) {

                    //包名作为获取信息的条件
                    getPackagerCache(packageInfo);

                    try {
                        Thread.sleep(100+new Random().nextInt(50));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    mIndex++;
                    pb_bar.setProgress(mIndex);

                    //每循环一次就将检测应用的名称发送给主线程
                    Message msg = Message.obtain();
                    msg.what=CHECK_CACHE_APP;

                    msg.obj= packageInfo.applicationInfo.loadLabel(packageManager).toString();
                    handler.sendMessage(msg);

                }

                Message msg = Message.obtain();
                msg.what=CHECK_FINISH;
                handler.sendMessage(msg);
            }


        }.start();

    }

    /**
     *通过包名获取应用的缓存信息
     * @param packageInfo 应用包名
     *
     */
    private void getPackagerCache( PackageInfo packageInfo) {
//
//        IPackageStatsObserver.Stub mStatsObserver=new IPackageStatsObserver.Stub(){
//
//
//            @Override
//            public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)  {
//              //使用消息机制
//
//                //4.获取指定包名的缓存大小
//                long cacheSize = pStats.cacheSize;
//                //5.判断缓存大小是否大于零
//                if (cacheSize>0){
//
//                    System.out.println("当前应用的名字+++" + packageInfo.applicationInfo.loadLabel(packageManager));
//
//                     //6.告知主线程更新UI
//                    Message  msg=Message.obtain();
//                    msg.what=UPDATE_CACHE_APP;//设置一个常量标记
//                    CacheInfo cacheInfo=null;
//                    try {
//
//                    //7. 维护有缓存应用的javabean.
//                    //有异常是非常正常的，因为并不是所有的有缓存的APP都有图标活着是名称
//                        cacheInfo = new CacheInfo();
//                        cacheInfo.cacheSize=cacheSize;
//                        cacheInfo.packagename=pStats.packageName;
//                        cacheInfo.name=packageManager.getApplicationInfo(pStats.packageName,0).loadLabel(packageManager).toString();
//                        cacheInfo.icon=packageManager.getApplicationInfo(pStats.packageName,0).loadIcon(packageManager);
//
//                    } catch (PackageManager.NameNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    msg.obj=cacheInfo;
//                    handler.sendMessage(msg);
//
//                }
////                String cache = Formatter.formatFileSize(getApplicationContext(), cacheSize);
//            }
//        };
//



        //1.获取制定类的字节码文件
        try {

            Method method = PackageManager.class.getDeclaredMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);

            method.invoke(packageManager,packageInfo.applicationInfo.packageName,new MyIPackegeStatsObserver(packageInfo));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private  class MyIPackegeStatsObserver extends IPackageStatsObserver.Stub{

        private   PackageInfo packageInfo;
        public MyIPackegeStatsObserver(PackageInfo packageInfo) {

            this.packageInfo=packageInfo;
        }

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {

            //获取到缓存大小
            long cacheSize = pStats.cacheSize;

            if (cacheSize>0){
                //说明有缓存
                Message  msg=Message.obtain();
                msg.what=UPDATE_CACHE_APP;//设置一个常量标记

                System.out.println("当前应用的名字+"+ packageInfo.applicationInfo.loadLabel(packageManager));
                System.out.println("缓存大小"+cacheSize);
                CacheInfo  cacheInfo=null;
                cacheInfo=new CacheInfo();
                Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
                cacheInfo.icon=icon;
                String appName=  packageInfo.applicationInfo.loadLabel(packageManager).toString();

                cacheInfo.packagename=appName;
                cacheInfo.cacheSize=cacheSize;


                msg.obj=cacheInfo;
                handler.sendMessage(msg);
//                handler.sendEmptyMessage(0);
            }


        }
    }


    /**
     * 定义javaBean对象
     */
      static    class CacheInfo{
         Drawable icon;
         String  packagename;
         long   cacheSize;
    }

    private void initUI() {

        setContentView(R.layout.activity_clean_cache);
        bt_clear_cache = (Button) findViewById(R.id.bt_clear);
        pb_bar = (ProgressBar) findViewById(R.id.pb_bar);
        tv_name = (TextView) findViewById(R.id.tv_name);
        ll_parent = (LinearLayout) findViewById(R.id.ll_parent);

    }

}
