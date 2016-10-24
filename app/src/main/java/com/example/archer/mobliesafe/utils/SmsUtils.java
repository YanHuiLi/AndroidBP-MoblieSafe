package com.example.archer.mobliesafe.utils;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Xml;
import android.widget.ProgressBar;

import com.example.archer.mobliesafe.AToolsActivity;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

import static android.R.attr.x;
import static com.example.archer.mobliesafe.R.id.progressBar;

/**
 * Created by Archer on 2016/10/23.
 * <p>
 * 描述:备份短信
 * <p>
 * 作者
 *
 *
 * type=2是发出的短信
 * type=1是收到的短信
 *
 *
 */

/**
 * 备份短信的接口
 */
public class SmsUtils {



    public  interface   BackUpCallBackSms{
        void before(int count);
        void onBackUpSms(int progress);

    }

//    public static final int EXTERNAL_STORAGE_REQ_CODE = 10 ;

    public static  boolean  backup(Context context,BackUpCallBackSms callBack){

/**
 * 1.先判断用户是否有SD卡
 * 2.权限 --- 使用内容观察者
 *
 *3.写短信 写到SD卡
 *
 */

        //注意动态获取权限。android 6.0 以后 我采取的做法是 从应用里面打开 存储 即可。

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //如果能进来说就明用户有SD卡
            ContentResolver resolver = context.getContentResolver();

            //获取短信的路径
            Uri uri = Uri.parse("content://sms/");


            Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type","body"}, null,null,null);

            assert cursor != null;
            int count = cursor.getCount();//获取有多少短信
int progress=0;//进度条默认是0
//            progressDialog.setMax(count);//设置PD的最大值
//progressDialog.setMax(count);
//            progressBar.setMax(count);
callBack.before(count);

            //cursor是游标的意思

            //写文件
            try {
                //把短信备份到SD卡,第一个参数是路径，第二个参数是名称
                File file = new File(Environment.getExternalStorageDirectory(),"backup.xml");

                FileOutputStream outputStream = new FileOutputStream(file);

                //在android系统里面所有关于xml的解析 都是pull解析
                XmlSerializer xmlSerializer = Xml.newSerializer();//
                xmlSerializer.setOutput(outputStream,"utf-8");//把短信序列化到SD卡，然后设置编码格式

//true表示文件独立
                xmlSerializer.startDocument("utf-8",true);//表示当前的xml是否是独立文件

                xmlSerializer.startTag(null,"smss");//设置开始的节点
                xmlSerializer.attribute(null,"size",String.valueOf(count));
                while(cursor.moveToNext()){
                    System.out.println("============================");
                    System.out.println( "address="+cursor.getString(0));
                    System.out.println("date="+cursor.getString(1));
                    System.out.println("type="+cursor.getString(2));
                    System.out.println("body="+cursor.getString(3));

                    xmlSerializer.startTag(null,"sms");

                    xmlSerializer.startTag(null,"address");
                    xmlSerializer.text(cursor.getString(0));
                    xmlSerializer.endTag(null,"address");


                    xmlSerializer.startTag(null,"data");
                    xmlSerializer.text(cursor.getString(1));
                    xmlSerializer.endTag(null,"data");


                    xmlSerializer.startTag(null,"type");
                    xmlSerializer.text(cursor.getString(2));
                    xmlSerializer.endTag(null,"type");


                    xmlSerializer.startTag(null,"body");
                    xmlSerializer.text(cursor.getString(3));
                    xmlSerializer.endTag(null,"body");


                    xmlSerializer.endTag(null,"sms");


                    progress++;//备份完一条短信以后，进度条++
//                    progressDialog.setProgress(progress);
//                    progressDialog.setProgress(progress);
//                    progressBar.setProgress(progress);
callBack.onBackUpSms(progress);

                    SystemClock.sleep(1000);
                }

                cursor.close();


                xmlSerializer.endTag(null,"smss");
                xmlSerializer.endDocument();

                outputStream.flush();
                outputStream.close();

                return true;


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        return  false;
}



}
