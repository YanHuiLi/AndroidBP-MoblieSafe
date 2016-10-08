package com.example.archer.mobliesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.example.archer.mobliesafe.bean.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Archer on 2016/7/21.
 * <p/>
 * 描述:
 * <p/>
 * 作者
 */
public class BlackNumberDao {

    public BlackNumberOpenHelper helper;

    public BlackNumberDao(Context context) {

        helper = new BlackNumberOpenHelper(context);
    }


    /**
     *
     * @param number 黑名单
     * @param mode  拦截的模式
     */
    public boolean  add(String number,String mode){

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("number",number);
        contentValues.put("mode",mode);
        long rowId = db.insert("blacknumber", null, contentValues);
        if (rowId==-1){
            return false;
        }else {
            return true;
        }
    }

    /**
     *
     * @param number 电话号码
     */
    public boolean  delete(String number){

        SQLiteDatabase db = helper.getWritableDatabase();

        int rowNumber = db.delete("blacknumber", "number=?", new String[]{number});


        if (rowNumber==0){
            return  false;
        }else {
            return  true;
        }

    }

    /**
     *
     * @param number 通过电话号码去修改拦截的模式
     */
    public boolean changeNumberMode(String number,String mode){

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mode",mode);
        int rowNumber = db.update("blacknumber", contentValues, "number=?", new String[]{number});

        if (rowNumber==0){
            return false;
        }else {
            return  true;
        }

    }

    /**
     *
     * @return 通过电话号码查找拦截的模式
     */
    public String findNumber(String number){

        String mode="";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "number=?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()){
            mode = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return mode;
    }

    /**
     * 查询所有的黑名单
     * @return
     */

    public List<BlackNumberInfo> findAll(){

        SQLiteDatabase db = helper.getReadableDatabase();

        List<BlackNumberInfo> blackNumberInfos = new ArrayList<>();
        Cursor cursor = db.query("blacknumber", new String[]{"number", "mode"}, null, null, null, null, null);

        //查询所有
        while(cursor.moveToNext()){

            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setNumber(cursor.getString(0));
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfos.add(blackNumberInfo);
        }
        cursor.close();
        db.close();
        SystemClock.sleep(3000);
        return blackNumberInfos;
    }



    /**
     *分页加载数据
     * @param pageNumber 表示当前是那一页
     * @param pageSize  表示当前又多少数据
     * @return
     * limit 限制数据
     * offset 表示跳过从第几条开始
     */
    public List<BlackNumberInfo> findPar(int pageNumber,int pageSize){
        SQLiteDatabase db = helper.getReadableDatabase();
        //分页查询语句
        Cursor cursor = db.rawQuery("select number,mode from blacknumber limit ? offset ?",new String[]{String.valueOf(pageSize),String.valueOf(pageSize *pageNumber)});
      //新建一个集合返回数据
        ArrayList<BlackNumberInfo> blackNumberInfos = new ArrayList<>();

        while (cursor.moveToNext()){
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfo.setNumber(cursor.getString(0));
            blackNumberInfos.add(blackNumberInfo);

        }
        cursor.close();
        db.close();
        return  blackNumberInfos;
    }


    /**
     * 获取总的记录数字
     * @return
     */

    public int getTotalNumber(){
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);

        cursor.moveToNext();
        //获取总数
        int cursorInt = cursor.getInt(0);
        cursor.close();
        db.close();
        return cursorInt;
    }
}
