package com.example.archer.mobliesafe;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.archer.mobliesafe.adapter.MyBaseAdapter;
import com.example.archer.mobliesafe.bean.BlackNumberInfo;
import com.example.archer.mobliesafe.db.dao.BlackNumberDao;
import com.example.archer.mobliesafe.utils.ToastUtils;

import java.util.List;


/**
 * 黑名单的设计
 */

public class CallSafeActivity2 extends AppCompatActivity {

    private ListView list_view;
    private List<BlackNumberInfo> daoAll;
    private LinearLayout mLinearlayout;
    private BlackNumberDao dao;
    private CallSafeAdapter adapter;

    /**
     * 每页加载多少数据
     */
    private int startIndex =0;
    private int maxCount= 20;
    /**
     * 一共多少页面
     */
    private int totalPage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_call_safe2);

        initUI();

        initData();
    }

    //因为数据较少所以可以在主线程里面做初始化
    //但如果数据量太大的话，只能重新使用一个线程去做

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mLinearlayout.setVisibility(View.INVISIBLE);
            adapter = new CallSafeAdapter(daoAll,CallSafeActivity2.this);
            list_view.setAdapter(adapter);


        }
    };

    private void initData() {
        dao = new BlackNumberDao(CallSafeActivity2.this);
        new  Thread(){
            @Override
            public void run() {

                //通过总的记录数字 除以 每一页多少数据




                System.out.println(totalPage);
                System.out.println(dao.getTotalNumber());

                //分批加载数据
                daoAll=  dao.findPar2(startIndex,maxCount);
                handler.sendEmptyMessage(0);
            }
        }.start();


    }

    private void initUI() {
        list_view = (ListView) findViewById(R.id.list_view);
        mLinearlayout = (LinearLayout) findViewById(R.id.ll_pb);
        //展示圆圈
        assert mLinearlayout != null;
        mLinearlayout.setVisibility(View.VISIBLE);

        //设置listview滚动监听器
list_view.setOnScrollListener(new AbsListView.OnScrollListener() {

  //状态改变回调的方法
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        System.out.println("onScrollStateChanged");
    }

    //状态滚动回调的方法
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        System.out.println("onScroll");
    }
});
    }



    /**
     * 自己使用了一个抽象类MyAdapter继承BaseAdapter，面向getview方法编程
     */
    private class CallSafeAdapter extends MyBaseAdapter<BlackNumberInfo>{


        public CallSafeAdapter(List lists, Context mContext) {
            super(lists, mContext);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView==null){
                convertView = View.inflate(CallSafeActivity2.this, R.layout.item_call_safe, null);

                viewHolder = new ViewHolder();
                viewHolder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
                viewHolder.tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
                viewHolder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);

                convertView.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }

            viewHolder.tv_number.setText(lists.get(position).getNumber());
            String mode= lists.get(position).getMode();
            if(mode.equals("1")){
                viewHolder.tv_mode.setText("来电和短信");
            }else if(mode.equals("2")){
                viewHolder.tv_mode.setText("来电拦截");

            }else if (mode.equals("3")){
                viewHolder.tv_mode.setText("短信拦截");
            }

            final BlackNumberInfo Info = lists.get(position);

            viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = Info.getNumber();
                    boolean result = dao.delete(number);
                    if (result){
                        ToastUtils.showToast(CallSafeActivity2.this,"删除成功");
                   lists.remove(Info);
                        //刷新界面
                        adapter.notifyDataSetChanged();
                    }else {
                        ToastUtils.showToast(CallSafeActivity2.this,"删除失败");
                    }

                }
            });

            return convertView;
        }
    }

    static class ViewHolder{

        TextView tv_number;
        TextView tv_mode;
        ImageView iv_delete;
    }




}
