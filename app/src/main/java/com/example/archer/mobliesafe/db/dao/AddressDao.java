package com.example.archer.mobliesafe.db.dao;

import android.database.sqlite.SQLiteDatabase;

/**
 * 归属地查询工具
 * Created by Archer on 2016/6/18.
 */
public class AddressDao {
    //该路径必须是data/data目录下的数据库文件，否则访问不到
    private static final String PATH="data/data/com.example.archer.mobliesafe/files/address.db";


    public static  String getAddress(String number){


        //获得数据库对象
        SQLiteDatabase database=SQLiteDatabase.openDatabase(PATH,null, SQLiteDatabase.OPEN_READONLY);

        return null;
    }
}
