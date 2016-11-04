package com.example.archer.mobliesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * 程序锁的数据库
 *
 * Created by Archer on 2016/11/2.
 */

public class AppLockDao {

    private final AppLockOpenHelper helper;

    public AppLockDao(Context context) {
        helper = new AppLockOpenHelper(context);
    }

    /**
     * 添加到程序所里面
     * @param packageName  包名
     */
     public void   add(String  packageName){

         SQLiteDatabase db = helper.getWritableDatabase();
         ContentValues contentValues = new ContentValues();
         contentValues.put("packagename",packageName);

         db.insert("appLockInfo",null,contentValues);
         db.close();
     }

    /**
     * 从程序所里面删除当前的包名
     * @param packageName  包名
     */
    public void  delete(String packageName){

        SQLiteDatabase db = helper.getWritableDatabase();

        db.delete("appLockInfo","packagename=?",new String[]{packageName});


      db.close();

    }

    /**
     * 查询当前的包 是否再程序锁里面
     * @param packageName
     * @return
     */

    public  boolean find(String  packageName){
        boolean result =false;
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query("appLockInfo", null, "packagename=?", new String[]{packageName}, null, null, null, null);

        if (cursor.moveToNext()){

             result=true;
        }


        cursor.close();
        db.close();
        return result;

    }

    /**
     * 查询全部的锁定的包名
     * @return
     */
    public List<String> findAll(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("appLockInfo", new String[]{"packagename"}, null, null, null, null, null);
        List<String> packnames = new ArrayList<String>();
        while(cursor.moveToNext()){
            packnames.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return packnames;
    }
}
