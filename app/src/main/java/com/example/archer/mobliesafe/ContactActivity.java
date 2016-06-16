package com.example.archer.mobliesafe;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Archer on 2016/6/16.
 */
public class ContactActivity extends Activity {

    private ArrayList<HashMap<String, String>> readContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        ListView mListView = (ListView) findViewById(R.id.lv_readContact);
        readContact = readContact();
//        System.out.println(readContact);
        assert mListView != null;
        mListView.setAdapter(new SimpleAdapter(this, readContact,R.layout.contact_list_item,
                new String[]{"name","phone"},new int[]{R.id.tv_name,R.id.tv_phone}));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String phone = readContact.get(position).get("phone");//读取当前item的电话号码
                Intent intent=new Intent();
                intent.putExtra("phone",phone);
                setResult(Activity.RESULT_OK,intent);
                /**
                 * 在setResult方法中的第一个参数即是resultCode的数值，resultCode如果为ok就表示确定选中
                 * 如果为cancel即为取消，同时需要在接收页面复写onActivityResult方法来判断接下来的逻辑
                 *
                 */

                finish();//将数据返回给上一个页面
            }
        });
    }
    private ArrayList<HashMap<String,String>> readContact(){
        //首先从raw_contacts读取联系人的Id（"contact_id"）
        //根据（"contact_id"）从data表中查出相应的数据，电话号码和联系人名称
        //然后根据mintape来区分哪个是联系人，哪个是电话号码

        Uri rawContactUri=Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri=Uri.parse("content://com.android.contacts/data");

        ArrayList<HashMap<String,String>>list=new ArrayList<>();


        Cursor rawContactCusor = getContentResolver().query(rawContactUri,
                new String[]{"contact_id"}, null, null, null);

        if (rawContactCusor!=null){
            while(rawContactCusor.moveToNext()){
                String rawContactId = rawContactCusor.getString(0);
//                System.out.println(rawContactId);

                //根据查询到的contact_id去查询出相应的联系人的号码和名称,实际上查询是view_data里面去查，
                //我考虑的应该是因为安全性和高效性的方式去考虑，为什么要从view_data去查询

                Cursor DataCursor = getContentResolver().query(dataUri, new String[]{"data1", "mimetype"},
                        "contact_id=?", new String[]{rawContactId}, null);

                if (DataCursor!=null){
                    HashMap<String,String>map= new HashMap<>();
                    while (DataCursor.moveToNext()){
                        String data1 = DataCursor.getString(0);
                        String  mimetype=DataCursor.getString(1);
//                        System.out.println(rawContactId+";"+data1+";"+mimetype);

                        if ("vnd.android.cursor.item/phone_v2".equals(mimetype)){
                            map.put("phone",data1);
                        }else if ("vnd.android.cursor.item/name".equals(mimetype)){
                            map.put("name",data1);
                        }

                    }
                    list.add(map);
                    DataCursor.close();
                }

            }
            rawContactCusor.close();
        }
        return list;

    }
}
