package com.example.archer.mobliesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.archer.mobliesafe.utils.MD5Utils;

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
    private SharedPreferences login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gvHome= (GridView) findViewById(R.id.gv_home);

        //初始化sharedPreferences
        login = getSharedPreferences("login", MODE_PRIVATE);


        assert gvHome != null;
        gvHome.setAdapter(new HomeAdapter());
        //设置主页面的监听
        gvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //响应点击事件，position 0到8

                switch (position){

                    case 0:
                        //手机防盗
                        showPassWordDialog();
                        break;
                    case 1:
                        //通讯卫士
                        startActivity(new Intent(HomeActivity.this,CallSafeActivity.class));

                        break;
                    case 2:
                        //通讯卫士
                        startActivity(new Intent(HomeActivity.this,AppManageActivity.class));

                        break;

                    case 7:
                        //高级工具开发
                        startActivity(new Intent(HomeActivity.this,AToolsActivity.class));

                        break;
                    case 8:
                        //点击设置中心的时候，跳转过去

                        startActivity(new Intent(HomeActivity.this,SettingActivity.class));
                        break;



                }
            }
        });

    }

    /**
     * 显示密码弹窗
     *
     */
    private void showPassWordDialog() {
        //判断如果没有设置密码
        //如果没有设置过，弹出设置密码的弹窗
        String savePassWord = login.getString("PassWord", null);
        if (!TextUtils.isEmpty(savePassWord)){

            showPassWordInputDialog();

        }else {
            //没设置过密码，则设置
            showPassWordSetDialog();
        }


    }

    /**
     * 输入密码弹出窗
     */
    private void showPassWordInputDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view=View.inflate(this,R.layout.dailog_input_password,null);
//        dialog.setView(view);//将自定义的布局文件设置个dialog
        //保证低版本上运行没问题
        dialog.setView(view,0,0,0,0);
        Button btnOK= (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel= (Button) view.findViewById(R.id.btn_cancel);

        final EditText etPassword= (EditText) view.findViewById(R.id.et_password);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String passWord = etPassword.getText().toString();

                if (!TextUtils.isEmpty(passWord)){
                    String savedPassWord = login.getString("PassWord", null);
                    if (MD5Utils.enCode(passWord).equals(savedPassWord)){
                        dialog.dismiss();

                        //跳转到防盗页面
                        startActivity(new Intent(HomeActivity.this,LostFoundActivity.class));
                    }else {

                        Toast.makeText(HomeActivity.this,"密码错误",Toast.LENGTH_SHORT).show();

                    }

                }else {
                    Toast.makeText(HomeActivity.this,"输入内容不能为空",Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    /**
     * 设置密码的弹窗
     */
    private void showPassWordSetDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view=View.inflate(this,R.layout.dailog_set_password,null);
//        dialog.setView(view);//将自定义的布局文件设置个dialog
        //保证低版本上运行没问题
        dialog.setView(view,0,0,0,0);
        Button btnOK= (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel= (Button) view.findViewById(R.id.btn_cancel);

        final EditText etPassword= (EditText) view.findViewById(R.id.et_password);
        final EditText etPasswordConfirm= (EditText) view.findViewById(R.id.et_password_confirm);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进行密码比对

                String passWord = etPassword.getText().toString();
                String passWordConfirm=etPasswordConfirm.getText().toString();

                //使用textutil进行判断，已经封装好了
                if (!TextUtils.isEmpty(passWord)&&!passWordConfirm.isEmpty()){


                    if (passWord.equals(passWordConfirm)){

                        Toast.makeText(HomeActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                        //保存密码
                        login.edit().putString("PassWord", MD5Utils.enCode(passWord)).commit();

                        dialog.dismiss();
                        //跳转到防盗页面
                        startActivity(new Intent(HomeActivity.this,LostFoundActivity.class));


                    }else {
                        Toast.makeText(HomeActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(HomeActivity.this,"输入内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

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
