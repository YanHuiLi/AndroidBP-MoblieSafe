package com.example.archer.mobliesafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.archer.mobliesafe.bean.AppInfo;
import com.example.archer.mobliesafe.engine.AppInfos;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class AppManageActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.list_view)
    private ListView list_view;
    @ViewInject(R.id.tv_rom)
    private TextView tv_rom;
    @ViewInject(R.id.tv_sd)
    private TextView tv_sd;
    private List<AppInfo> appinfos;
    private ArrayList<AppInfo> userAppInfos;
    private ArrayList<AppInfo> systemInfos;

    @ViewInject(R.id.tv_appNumber)
    private TextView tv_app;
    private PopupWindow popupWindow;
    private LinearLayout ll_uninstall;
    private LinearLayout ll_start;
    private LinearLayout ll_share;


    private LinearLayout ll_detail;

    private AppInfo checkAppInfo;
    private AppManageAdapter mAdapter;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
        initData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.ll_uninstall:
                Intent uninstall_localIntent = new Intent("android.intent.action.DELETE", Uri.parse("package:" + checkAppInfo.getApkPackageName()));
                startActivity(uninstall_localIntent);
                popupWindowDismiss();
                mAdapter.notifyDataSetChanged();
                initData();
                break;

            case R.id.ll_start:
                Intent start_localIntent = this.getPackageManager().getLaunchIntentForPackage(checkAppInfo.getApkPackageName());
                this.startActivity(start_localIntent);
                popupWindowDismiss();
                break;

            case  R.id.ll_share:
                Intent share_localIntent = new Intent("android.intent.action.SEND");
                share_localIntent.setType("text/plain");
                share_localIntent.putExtra("android.intent.extra.SUBJECT", "f分享");
                share_localIntent.putExtra("android.intent.extra.TEXT",
                        "Hi！推荐您使用软件：" + checkAppInfo.getApkName()+" 下载地址:"+"https://play.google.com/store/apps/details?id="+checkAppInfo.getApkPackageName());
                this.startActivity(Intent.createChooser(share_localIntent, "分享"));
                popupWindowDismiss();

                break;

            case R.id.ll_detail:
                Intent detail_intent = new Intent();
                detail_intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                detail_intent.addCategory(Intent.CATEGORY_DEFAULT);
                detail_intent.setData(Uri.parse("package:" + checkAppInfo.getApkPackageName()));
                startActivity(detail_intent);
                break;
        }

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

                textView.setText("用户应用（"+userAppInfos.size()+")");
                return  textView;

            }else  if (position==userAppInfos.size()+1){//第二个系统程序的textView

                TextView textView=new TextView(AppManageActivity.this);
                textView.setTextSize(18);
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.GRAY);

                textView.setText("系统应用（"+systemInfos.size()+")");

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

            mAdapter = new AppManageAdapter();
            list_view.setAdapter(mAdapter);
            dialog.dismiss();

        }
    };

    private void initData() {

        dialog.show();//dialog的使用

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
        dialog = new ProgressDialog(this);
        dialog.setTitle("玩命加载中");

        setContentView(R.layout.activity_app_manage);
        com.lidroid.xutils.ViewUtils.inject(this);


        long Rom_freeSpace = Environment.getDataDirectory().getFreeSpace();//获取到rom内存的大小

        long SD_freeSpace = Environment.getExternalStorageDirectory().getFreeSpace();//获取sd卡剩余空间


        tv_rom.setText("内存可用："+Formatter.formatFileSize(this,Rom_freeSpace));//格式化
        tv_sd.setText("SD卡可用："+Formatter.formatFileSize(this,SD_freeSpace));//格式化
        list_view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            /**
             *
             * @param view
             * @param firstVisibleItem 第一个可见的条目的位置
             * @param visibleItemCount  一页可以展示多少条目
             * @param totalItemCount    总共的条目数字
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                popupWindowDismiss();
                if (userAppInfos!=null&&systemInfos!=null){
                    if (firstVisibleItem>=userAppInfos.size()+1){
                        tv_app.setText("系统应用（"+systemInfos.size()+")");
                    }else {
                        tv_app.setText("用户应用（"+userAppInfos.size()+")");
                    }

                }
            }
        });

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = list_view.getItemAtPosition(position);

                checkAppInfo = (AppInfo) obj;


                if (obj!=null&& obj instanceof AppInfo){

                    View contentView = View.inflate(AppManageActivity.this, R.layout.item_popup, null);

                    ll_uninstall = (LinearLayout) contentView.findViewById(R.id.ll_uninstall);
                    ll_start = (LinearLayout) contentView.findViewById(R.id.ll_start);
                    ll_share = (LinearLayout) contentView.findViewById(R.id.ll_share);
                    ll_detail= (LinearLayout)contentView.findViewById(R.id.ll_detail);


//                 ViewUtils.inject(AppManageActivity.this);
                    ll_uninstall.setOnClickListener(AppManageActivity.this);
                    ll_start.setOnClickListener(AppManageActivity.this);
                    ll_share.setOnClickListener(AppManageActivity.this);
                    ll_detail.setOnClickListener(AppManageActivity.this);

                    popupWindowDismiss();

                    //传入三个参数，第一个是要弹出的页面，第二，第三个是该控件的大小，设置为包裹内容，返回-2亦可
                    popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    //使用popupwindow做动画的时候要注意给一个背景图片
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    int[] location=new int[2];
                    //获取要展示在窗口的Y距离的位置
                    view.getLocationInWindow(location);
                    popupWindow.showAtLocation(parent, Gravity.LEFT+Gravity.TOP,200,location[1]-50);



                    //缩放的动画
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f,1.0f,0.5f,1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);

                    scaleAnimation.setDuration(500);
                    contentView.startAnimation(scaleAnimation);

                }
            }
        });
    }

    private void popupWindowDismiss() {
        if (popupWindow != null && popupWindow.isShowing()){
            popupWindow.dismiss();
            popupWindow=null;
        }
    }

    private  static class ViewHolder {
        ImageView iv_icon;
        TextView tv_apkName;
        TextView tv_apkLocation;
        TextView tv_apkSize;
    }

    //当出现popupWindow没有消除就后退的话 会出现一个异常

    @Override
    protected void onDestroy() {
        popupWindowDismiss();
        super.onDestroy();
    }
}
