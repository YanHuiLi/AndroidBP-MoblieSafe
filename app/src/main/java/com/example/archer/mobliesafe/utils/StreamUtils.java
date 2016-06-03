package com.example.archer.mobliesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Archer on 2016/6/1.
 *输入流读取成string后返回
 * 读取流的工具
 */
public class StreamUtils {

    public static String readFromStream(InputStream inputStream) throws IOException {

        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();

       int len;
        byte[] buffer=new byte[1024];

        while ((len=inputStream.read())!=-1){
            outputStream.write(buffer,0,len);
        }

        String result=outputStream.toString();
        inputStream.close();
        outputStream.close();
        return  result;
    }
}
