package com.example.archer.mobliesafe.utils;

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


    }

