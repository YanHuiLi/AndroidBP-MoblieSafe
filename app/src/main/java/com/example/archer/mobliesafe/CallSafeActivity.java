package com.example.archer.mobliesafe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.ThemedSpinnerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
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
import com.lidroid.xutils.db.sqlite.CursorUtils;

import java.util.List;

/**
 * 黑名单的设计
 */

public class CallSafeActivity extends AppCompatActivity {

    private ListView list_view;
    private List<BlackNumberInfo> daoAll;
    private LinearLayout mLinearlayout;
    private BlackNumberDao dao;
    private  CallSafeAdapter adapter;

    private int mStartIndex=0 ;
    private int  maxCount=20;
    private int totalNumber;
    //    /**
//     * 当前页面
//     * 每一页展示20条数据
//     */
//    private int mCurrentPageNumber =0;
//    private int mPageSize=20;
//    private TextView tv_page_number;
//
//    /**
//     * 一共多少页面
//     */
//    private int totalPage;
//
//    private EditText et_page_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe2);

        initUI();

        initData();
    }

    //因为数据较少所以可以在主线程里面做初始化
    //但如果数据量太大的话，只能重新使用一个线程去做

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mLinearlayout.setVisibility(View.INVISIBLE);
            if (adapter==null){
                adapter = new CallSafeAdapter(daoAll,CallSafeActivity.this);

                list_view.setAdapter(adapter);

            }else {
                adapter.notifyDataSetChanged();
            }

//            if (mCurrentPageNumber>=0){
//                tv_page_number.setText((mCurrentPageNumber+1)+"/"+totalPage);
//            }

        }
    };

    private void initData() {
        dao = new BlackNumberDao(CallSafeActivity.this);
        //一共多少条数据
        totalNumber = dao.getTotalNumber();

        new  Thread(){
            @Override
            public void run() {

                //通过总的记录数字 除以 每一页多少数据


//                totalPage = dao.getTotalNumber()/mPageSize;

//                System.out.println(totalPage);
//                System.out.println(dao.getTotalNumber());
// daoAll = dao.findAll();
                //分批加载数据
                if (daoAll==null){
                    daoAll=dao.findPar2(mStartIndex,maxCount);
                }else {
//                    //把后面的数据追加到daoall集合里面
                    daoAll.addAll(dao.findPar2(mStartIndex,maxCount));
                }


                handler.sendEmptyMessage(0);
            }
        }.start();


    }

    /**
     * 添加黑名单
     * @param view
     */
    public void addBlackNumber(View view) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View Add_Black_Dialog = View.inflate(this, R.layout.view_dialog_add_black, null);
        final   AlertDialog alertDialog = builder.create();


        final EditText editText= (EditText) Add_Black_Dialog.findViewById(R.id.Abm);

        final Button buttonOK= (Button) Add_Black_Dialog.findViewById(R.id.btn_ok);
        final    Button buttonCancel= (Button) Add_Black_Dialog.findViewById(R.id.btn_cancel);
        final CheckBox ckPhone= (CheckBox) Add_Black_Dialog.findViewById(R.id.ck_phone);
        final CheckBox ck_message= (CheckBox) Add_Black_Dialog.findViewById(R.id.ck_message);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        buttonOK.setOnClickListener(new View.OnClickListener() {

            private CallSafeAdapter adapter;

            @Override
            public void onClick(View v) {
                String trim = editText.getText().toString().trim();

                if (TextUtils.isEmpty(trim)) {
                    ToastUtils.showToast(getApplicationContext(), "请输入黑名单号码");
                    return;
                }
                String mode="";

                if (ckPhone.isChecked()&&ck_message.isChecked()){
                    mode="1";
                }else if (ckPhone.isChecked()){
                    mode="2";
                }else if (ck_message.isChecked()){
                    mode="3";
                }else {
                    Toast.makeText(CallSafeActivity.this,"请勾选拦截模式",Toast.LENGTH_SHORT).show();
                    return;

                }

                BlackNumberInfo blackNumberInfo=new BlackNumberInfo();
                blackNumberInfo.setNumber(trim);
                blackNumberInfo.setMode(mode);

                daoAll.add(0,blackNumberInfo);
//把电话号码添加到数据库里面
                dao.add(trim,mode);

                //刷新界面
                if (adapter==null){
                    adapter = new CallSafeAdapter(daoAll, CallSafeActivity.this);
                    list_view.setAdapter(adapter);
                }else {
                    adapter.notifyDataSetChanged();//刷新界面
                }

                alertDialog.dismiss();

            }
        });
        alertDialog.setView(Add_Black_Dialog);
        alertDialog.show();

    }

    private void initUI() {
        list_view = (ListView) findViewById(R.id.list_view);
        mLinearlayout = (LinearLayout) findViewById(R.id.ll_pb);
        //展示圆圈
        assert mLinearlayout != null;
        mLinearlayout.setVisibility(View.VISIBLE);
//        tv_page_number = (TextView) findViewById(R.id.tv_page_number);

//        et_page_number = (EditText) findViewById(R.id.et_page_number);

        list_view.setOnScrollListener(new AbsListView.OnScrollListener() {

            /**
             * 当状态改变了，调用onScrollStateChanged方法
             * 在状态改变的过程之中，调用onScroll方法
             AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸状态
             AbsListView.OnScrollListener.SCROLL_STATE_FLING://惯性状态
             AbsListView.OnScrollListener.SCROLL_STATE_IDLE://静止状态
             */
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                System.out.println("onScrollStateChanged");

                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        //获取到最后一条显示的数据
                        int lastVisiblePosition = list_view.getLastVisiblePosition();
                        System.out.println("lastVisiblePosition==========" + lastVisiblePosition);
                        if(lastVisiblePosition == daoAll.size() - 1){
                            // 加载更多的数据。 更改加载数据的开始位置
                            mStartIndex += maxCount;
                            if (mStartIndex >= totalNumber) {
                                ToastUtils.showToast(CallSafeActivity.this,"没有更多数据了");
                                return;
                            }
                            initData();
                        }


                        break;
                }


            }

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




        private CallSafeAdapter(List lists, Context mContext) {
            super(lists, mContext);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView==null){
                convertView = View.inflate(CallSafeActivity.this, R.layout.item_call_safe, null);

                viewHolder = new ViewHolder();
                viewHolder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
                viewHolder.tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
                viewHolder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);

                convertView.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }

            viewHolder.tv_number.setText(lists.get(position).getNumber());
            String mode = lists.get(position).getMode();
            switch (mode) {
                case "1":
                    viewHolder.tv_mode.setText("来电和短信");
                    break;
                case "2":
                    viewHolder.tv_mode.setText("来电拦截");

                    break;
                case "3":
                    viewHolder.tv_mode.setText("短信拦截");
                    break;
            }

            final BlackNumberInfo Info = lists.get(position);

            viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = Info.getNumber();
                    boolean result = dao.delete(number);
                    if (result){
                        ToastUtils.showToast(CallSafeActivity.this,"删除成功");
                        lists.remove(Info);
                        //刷新界面
                        adapter.notifyDataSetChanged();
                    }else {
                        ToastUtils.showToast(CallSafeActivity.this,"删除失败");
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
