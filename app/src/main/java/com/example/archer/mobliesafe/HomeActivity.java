package com.example.archer.mobliesafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 主页面
 * Created by Archer on 2016/6/2.
 */
public class HomeActivity extends AppCompatActivity{


     private GridView gvHome;
//用数组来保存九大模块
    private String[] mItem=new String[]{"手机防盗","通讯卫士",
            "软件管理","进程管理","流量统计",
            "手机杀毒","缓存管理","高级工具",
            "设置中心"};
    //用数组来保存图片
    private int[] mPic=new int[]{R.drawable.home_safe,R.drawable.home_callmsgsafe,R.drawable.home_apps,
    R.drawable.home_taskmanager,R.drawable.home_netmanager,R.drawable.home_trojan,
    R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gvHome= (GridView) findViewById(R.id.gv_home);

        assert gvHome != null;
        gvHome.setAdapter(new HomeAdapter());
        //设置主页面的监听
        gvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //响应点击事件，position 0到8

                switch (position){

                    case 8:
                        //点击设置中心的时候，跳转过去

                        startActivity(new Intent(HomeActivity.this,SettingActivity.class));
                        break;



                }
            }
        });

    }

    //gvHome写一个适配器

    class HomeAdapter extends BaseAdapter{

        //展示几个
        @Override
        public int getCount() {
            return mItem.length;
        }


        @Override
        public Object getItem(int position) {
            return mItem[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //被调9次
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(HomeActivity.this,R.layout.home_list_item,null);

            ImageView iv_item= (ImageView) view.findViewById(R.id.iv_item);
            TextView tv_item= (TextView) view.findViewById(R.id.tv_item);
           //赋值
            tv_item.setText(mItem[position]);
            iv_item.setImageResource(mPic[position]);
            return view;
        }
    }
}
