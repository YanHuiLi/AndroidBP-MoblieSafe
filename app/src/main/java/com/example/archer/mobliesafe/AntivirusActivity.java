package com.example.archer.mobliesafe;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.archer.mobliesafe.db.dao.AntivirusDao;
import com.example.archer.mobliesafe.utils.MD5Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AntivirusActivity extends AppCompatActivity {


    private static final int SCANNING = 2;
    private static final int FINISH = 3;
    private static final int BEGING = 1;

    @Bind(R.id.iv_act_scanning_03)
    ImageView iv_act_scanning_03;

    private Message message;

    @Bind(R.id.tv_antivirus)
    TextView tv_antivirus;

    @Bind(R.id.pb_Antivirus)
    ProgressBar pb_Antivirus;

    @Bind(R.id.ll_content)
    LinearLayout ll_content;

    @Bind(R.id.scrollView)
    ScrollView scrollView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();

    }


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){

                case  BEGING:
                    tv_antivirus.setText("初始化八核查杀");
                    break;

                case  SCANNING:
                    // 病毒扫描中：
                    TextView child = new TextView(AntivirusActivity.this);

                    ScanInfo scanInfo = (ScanInfo) msg.obj;
                    // 如果为true表示有病毒
                    if (scanInfo.desc) {
                        child.setTextColor(Color.RED);

                        child.setText(scanInfo.appName + "有病毒");

                    } else {

                        child.setTextColor(Color.BLACK);
//					// 为false表示没有病毒
                        child.setText(scanInfo.appName + "------>扫描安全");

                    }

                    ll_content.addView(child,0);
                    //自动滚动
//                    scrollView.post(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            //一直往下面进行滚动
//                            scrollView.fullScroll(View.FOCUS_DOWN);
//
//                        }
//                    });


                    System.out.println(scanInfo.appName + "扫描安全");

                    break;
                case  FINISH:

                    //当扫描结束的时候，停止动画
                    iv_act_scanning_03.clearAnimation();

                    break;

                default:


                    break;
            }


        }
    };

    private void initData() {



        //进行查毒是耗时的操作
        new Thread(){

            @Override
            public void run() {

                message = Message.obtain();

                message.what = BEGING;

                handler.handleMessage(message);

                PackageManager packageManager = getPackageManager();
                // 获取到所有安装的应用程序
                List<PackageInfo> installedPackages = packageManager
                        .getInstalledPackages(0);
                // 返回手机上面安装了多少个应用程序
                int size = installedPackages.size();
                // 设置进度条的最大值
                pb_Antivirus.setMax(size);

                int progress = 0;

                for (PackageInfo packageInfo : installedPackages) {

                    ScanInfo scanInfo = new ScanInfo();

                    // 获取到当前手机上面的app的名字
                    String appName = packageInfo.applicationInfo.loadLabel(
                            packageManager).toString();

                    scanInfo.appName = appName;

                    /**
                     * 如果写成 scanInfo.appName =packageInfo.applicationInfo.loadLabel(
                     packageManager).toString();这种形式 会报空指针异常。

                     */



                    scanInfo.packageName = packageInfo.applicationInfo.packageName;

                    // 首先需要获取到每个应用程序的目录

                    String sourceDir = packageInfo.applicationInfo.sourceDir;
                    // 获取到文件的md5
                    String md5 = MD5Utils.getFileMd5(sourceDir);
                    // 判断当前的文件是否是病毒数据库里面
                    String desc = AntivirusDao.CheckFileVirus(md5);

                    System.out.println("-------------------------");

                    System.out.println(appName);

                    System.out.println(md5);


//					03-06 07:37:32.505: I/System.out(23660): 垃圾
//					03-06 07:37:32.505: I/System.out(23660): 51dc6ba54cbfbcff299eb72e79e03668

//					["md5":"51dc6ba54cbfbcff299eb72e79e03668","desc":"蝗虫病毒赶快卸载","desc":"蝗虫病毒赶快卸载","desc":"蝗虫病毒赶快卸载"]
//                    b2d53b898d472160347af79d06c723d8  模拟一个病毒

//					B7DA3864FD19C0B2390C9719E812E649
                    // 如果当前的描述信息等于null说明没有病毒
                    scanInfo.desc = desc != null;



                    progress++;

                    SystemClock.sleep(100);

                    pb_Antivirus.setProgress(progress);

                    message = Message.obtain();

                    message.what = SCANNING;

                    message.obj = scanInfo;

                    handler.sendMessage(message);

                }

                message = Message.obtain();

                message.what = FINISH;

                handler.sendMessage(message);
            }
        }.start();
    }


    //定义一个bean对象

    static  class ScanInfo {

        boolean desc;

        String appName;

        String  packageName;


    }

    private void initUI() {
        setContentView(R.layout.activity_antivirus);
        ButterKnife.bind(this);

        //1.第一个参数表示开始的角度
        //2.第二个参数是结束的角度
        //3.参照类型，参照自己
        //4.参照
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        // 设置动画的时间
        rotateAnimation.setDuration(5000);
        // 设置动画无限循环
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        // 开始动画
        iv_act_scanning_03.startAnimation(rotateAnimation);



    }
}
