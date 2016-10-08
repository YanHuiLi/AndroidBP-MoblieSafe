package com.example.archer.mobliesafe;

import android.content.Context;
import android.test.AndroidTestCase;

import com.example.archer.mobliesafe.bean.BlackNumberInfo;
import com.example.archer.mobliesafe.db.dao.BlackNumberDao;

import java.util.List;
import java.util.Random;

/**
 * Created by Archer on 2016/7/23.
 * <p>
 * 描述:
 * <p>
 * 作者
 */
public class TestBlackNumberDao extends AndroidTestCase{

    public Context  mContext;

    @Override
    protected void setUp() throws Exception {
        this.mContext=getContext();
        super.setUp();
    }

    public void testAdd(){
        BlackNumberDao dao = new BlackNumberDao(mContext);
        Random random = new Random();
        for (int i = 0; i <200; i++) {
            Long number = 13300000000L +i;
            dao.add(number +"",String.valueOf(random.nextInt(3) + 1));
        }
    }

//    //测试删除
//    public void  testDelete(){
//        BlackNumberDao deleteDao = new BlackNumberDao(mContext);
//
//        boolean delete = deleteDao.delete("13300000000");
//        assertEquals(true,delete);
//
//    }
//
//    public void testFind(){
//
//        BlackNumberDao findDao = new BlackNumberDao(mContext);
//
//        String number = findDao.findNumber("13300000001");
//
//        System.out.println(number);
//    }
//
//    public  void  testFindAll(){
//        BlackNumberDao dao = new BlackNumberDao(mContext);
//        List<BlackNumberInfo> blackNumberInfos=dao.findAll();
//
//        for (BlackNumberInfo blackNumberInfo:blackNumberInfos){
//            System.out.println(blackNumberInfo.getMode()+"   "+blackNumberInfo.getNumber());
//        }
//    }



}
