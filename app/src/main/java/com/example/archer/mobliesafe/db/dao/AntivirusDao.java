package com.example.archer.mobliesafe.db.dao;

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



}
