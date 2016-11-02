package com.example.archer.mobliesafe.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Archer on 2016/10/29.
 */

public class AntivirusDao {

    private static final String PATH ="data/data/com.example.archer.mobliesafe/files/antivirus.db";

    /**
     * 检查当前的md5值是否再病毒数据库里面
     * @param md5
     * @return
     */
    public static  String CheckFileVirus(String md5){

        SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);

        String desc = null;

        Cursor cursor = database.rawQuery("select desc from datable where md5 = ?", new String[]{md5});//查询当前传过来的md5是否在病毒数据库里面
        if (cursor.moveToNext()){
            desc = cursor.getString(0);
        }
        cursor.close();
        return  desc;

    }


    /**
     * 添加病毒数据库
     * @param md5  特征码
     * @param desc  描述信息
     */
    public  static  void addVirus(String md5,String desc){

        SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READWRITE);


        ContentValues contentValues=new ContentValues();
        contentValues.put("md5",md5);
        contentValues.put("desc",desc);
        contentValues.put("type",6);
        contentValues.put("name","this is my virus");


        database.insert("datable",null, contentValues);


        database.close();
    }



}
