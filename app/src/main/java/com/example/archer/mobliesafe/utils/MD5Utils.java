package com.example.archer.mobliesafe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 加密工具类
 * Created by Archer on 2016/6/5.
 */
public class MD5Utils {

    public static String enCode(String password ){


        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            byte[] digest = instance.digest(password.getBytes());

            StringBuilder sb=new StringBuilder();
            for (byte b:digest){
                int i=b&0xff;
                String hexString = Integer.toHexString(i);
                //转为16进制，获取低八位
//           System.out.println(hexString);
                if(hexString.length()<2){

                    hexString="0"+hexString;

                }
                sb.append(hexString);
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取到文件的MD5（也称作病毒特征码）
     * @param sourceDir
     * @return
     */

    public static String getFileMd5(String sourceDir) {

        File file = new File(sourceDir);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[1024];

            int len = -1;

            MessageDigest md5 = MessageDigest.getInstance("md5");//获取到数字摘要


            while ((len = fileInputStream.read(bytes)) != -1) {

                md5.update(bytes, 0, len);

            }

            byte[] result = md5.digest();

            StringBuffer sb = new StringBuffer();
            for (byte b : result) {
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);
                //转为16进制，获取低八位
//           System.out.println(hexString);
                if (hexString.length() < 2) {

                    hexString = "0" + hexString;

                }
                sb.append(hexString);
            }

            return sb.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

