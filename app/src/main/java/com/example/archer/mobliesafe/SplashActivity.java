package com.example.archer.mobliesafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Xutils框架的使用
 *
 */


public class SplashActivity extends AppCompatActivity {

    private static final  int CODE_UPDATE_DIAOG=0;
    private static final int CODE_URL_ERROR =1 ;
    private static final int CODE_NET_ERROR =2 ;
    private static final int CODE_JSON_ERROR =3 ;
    private static final int CODE_ENTER_HOME=4;//进入主页面

    private TextView tvPrograss;

    //服务器的信息
    private String mVersionName;
    private int mVersionCode;
    private String mDownloadUrl;
    private String mDescription;

    private Handler mhandler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case CODE_UPDATE_DIAOG:
                    showUpdateDailog();
                    break;
                case CODE_URL_ERROR:
                    Toast.makeText(SplashActivity.this,"url错误",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_NET_ERROR:
                    Toast.makeText(SplashActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(SplashActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_ENTER_HOME:
                    enterHome();
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView textView = (TextView) findViewById(R.id.tv_version);

        assert textView != null;
        textView.setText("版本名称：" + getVersionName());

        tvPrograss= (TextView) findViewById(R.id.tv_downProcess);


        checkVersion();
    }

    //获取版本名称
    private String getVersionName() {

        PackageManager packageManager = getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);

            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;

//            System.out.println("versionName=" + versionName + ";versionCode=" + versionCode);

            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //没找到包名的时候处理异常
            e.printStackTrace();
        }

        return "";
    }
    private int getVersionCode() {

        PackageManager packageManager = getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);

            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;

//            System.out.println("versionName=" + versionName + ";versionCode=" + versionCode);

            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            //没找到包名的时候处理异常
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 获取版本信息进行校验
     */
    private void checkVersion() {

        //控制一下时间
        final long currentTimeMillis = System.currentTimeMillis();

        //启动子线程异步加载
        new Thread() {

            @Override
            public void run() {

                //用handler发消息
                Message msg=Message.obtain();

                HttpURLConnection conn=null ;
                try {
                    /**
                     * 注意调试的时候：
                     * 1.tomcat服务是否已经启动完毕，localhost:8080端口能否访问到
                     * 2.设备是否已经连上网络。
                     * 3.要是模拟器ip地址应为10.0.2.2
                     * 4.若是genymotion或者是真机，IP地址应该为PC的IP地址
                     * 5.android权限记得添加
                     * 6.若使用真机调试，保证PC和真机在同一局域网，并关闭防火墙
                     * 7.当重启电脑的时候，记得更改IP地址，部属到tomcat和java代码中的
                     */
                    URL url = new URL("http://113.55.42.79:8080/update.json");
//                    URL url = new URL("http://169.254.163.216:8080/update.json");
//                    URL url = new URL("http://192.168.1.201:8080/update.json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");// 设置请求方法
                    conn.setConnectTimeout(5000);// 设置连接超时
                    conn.setReadTimeout(5000);// 设置响应超时, 连接上了,但服务器迟迟不给响应
                    conn.connect();// 连接服务器

                    int responseCode = conn.getResponseCode();// 获取响应码
                    if (responseCode == 200) {
                        InputStream inputStream = conn.getInputStream();
                        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder result=new StringBuilder();
                        String line;
                        while((line=reader.readLine())!=null){
                            result.append(line);

                        }
                        System.out.println("网络返回:" + result);

                        JSONObject jsonObject= new JSONObject(String.valueOf(result));
                        mDescription = jsonObject.getString("description");
                        mVersionCode=jsonObject.getInt("versionCode");
                        mVersionName=jsonObject.getString("versionName");
                        mDownloadUrl=jsonObject.getString("downloadUrl");


// System.out.println(mDescription);
                        System.out.println(mDownloadUrl);
// System.out.println(mVersionName);
// System.out.println(mVersionCode);


                        if (mVersionCode>getVersionCode()){
                            /**
                             * 说明有更新
                             * 更新，弹出升级对话框
                             */
                            msg.what=CODE_UPDATE_DIAOG;

                        }else{
                            //没有版本更新
                            msg.what=CODE_ENTER_HOME;
                        }

                    }
                } catch (MalformedURLException e) {

                    System.out.println("URL异常");
                    msg.what=CODE_URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("网络异常");
                    msg.what=CODE_NET_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    System.out.println("json解析失败");
                    msg.what=CODE_JSON_ERROR;
                    e.printStackTrace();
                }finally {

                    long endTime=System.currentTimeMillis();
                    //访问网络花费的时间
                    long timeUsed=endTime-currentTimeMillis;
                    if (timeUsed<2000){

                        //强制休眠2s，保证闪屏页展示两秒
                        try {
                            Thread.sleep(2000-timeUsed);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    //消息发送出去
                    mhandler.sendMessage(msg);
                    if (conn!=null){
                        conn.disconnect();
                    }
                }


            }



        }.start();
    }

    /**
     * 弹出对话框
     */
    private void showUpdateDailog() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("最新版本"+mVersionName);
        builder.setMessage(mDescription);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("立即更新");

                //写一个下载的方法
                downLoad();
            }
        });

        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//点击跳准
                enterHome();
                System.out.println("12345678");
            }
        });
        builder.show();
    }

    /**
     * 下载APK文件
     */
    private void downLoad() {
        //判断是够有sd卡
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            /**
             * 三个参数，第一个是传入下载的地址，第二个传入文件将要存放的路径
             * 第三个参数是回调函数。
             */

            //显示出进度框的
            tvPrograss.setVisibility(View.VISIBLE);



            final String target= Environment.getExternalStorageDirectory()+"/update.apk";

            //XUtils开源框架
            HttpUtils utils=new HttpUtils();
            utils.download(mDownloadUrl, target, new RequestCallBack<File>() {

                //下载成功
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {

                    System.out.println("下载成功");

                    //跳转到系统下载页面

                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(responseInfo.result)
                            ,"application/vnd.android.package-archive");
                    //一定要注意的是启动intent
                    startActivity(intent);


                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    System.out.println(error);

                    Toast.makeText(SplashActivity.this,"下载失败",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    /**
                     * 文件的下载进度
                     */
                    System.out.println("下载进度"+current+"/"+total);

                    super.onLoading(total, current, isUploading);

                    tvPrograss.setText("下载进度"+current*100/total+"%");
                    System.out.println("current="+current);

                }
            });
        }else {
            Toast.makeText(SplashActivity.this,"没有SD卡，无法下载",Toast.LENGTH_SHORT).show();
        }



    }

    /**
     * 进入主界面
     */
    private  void  enterHome(){

        Intent intent=new Intent(SplashActivity.this,HomeActivity.class);
        //不要忘记startactivity
        startActivity(intent);
        finish();
    }


}