package com.example.archer.mobliesafe.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static android.R.attr.x;

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

public class SmsUtils {


    public static  boolean  backup(Context context){

/**
 * 1.先判断用户是否有SD卡
 * 2.权限 --- 使用内容观察者
 *
 *3.写短信 写到SD卡
 *
 */

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //如果能进来说就明用户有SD卡
            ContentResolver resolver = context.getContentResolver();

            //获取短信的路径
            Uri uri = Uri.parse("content://sms/");


            Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type","body"}, null,null,null);

            //cursor是游标的意思
            assert cursor != null;


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


                }


             xmlSerializer.endTag(null,"smss");


                xmlSerializer.endDocument();
            } catch (Exception e) {
                e.printStackTrace();
            }





        }

        return  false;

    }




}
