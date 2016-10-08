package com.example.archer.mobliesafe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.ThemedSpinnerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
    /**
     * 当前页面
     * 每一页展示20条数据
     */
    private int mCurrentPageNumber =0;
    private int mPageSize=20;
    private TextView tv_page_number;

    /**
     * 一共多少页面
     */
    private int totalPage;
    private CallSafeAdapter adapter;
    private EditText et_page_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe);

        initUI();

        initData();
    }

    //因为数据较少所以可以在主线程里面做初始化
    //但如果数据量太大的话，只能重新使用一个线程去做

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mLinearlayout.setVisibility(View.INVISIBLE);
            adapter = new CallSafeAdapter(daoAll,CallSafeActivity.this);
            list_view.setAdapter(adapter);
            if (mCurrentPageNumber>=0){
                tv_page_number.setText((mCurrentPageNumber+1)+"/"+totalPage);
            }

        }
    };

    private void initData() {

        new  Thread(){
            @Override
            public void run() {

                //通过总的记录数字 除以 每一页多少数据


                dao = new BlackNumberDao(CallSafeActivity.this);
                totalPage = dao.getTotalNumber()/mPageSize;

                System.out.println(totalPage);
                System.out.println(dao.getTotalNumber());
// daoAll = dao.findAll();
                daoAll=  dao.findPar(mCurrentPageNumber,mPageSize);
                handler.sendEmptyMessage(0);
            }
        }.start();


    }

    private void initUI() {
        list_view = (ListView) findViewById(R.id.list_view);
        mLinearlayout = (LinearLayout) findViewById(R.id.ll_pb);
        tv_page_number = (TextView) findViewById(R.id.tv_page_number);
        //展示圆圈
        assert mLinearlayout != null;
        mLinearlayout.setVisibility(View.VISIBLE);

        et_page_number = (EditText) findViewById(R.id.et_page_number);
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

    /**
     * 上一页
     * @param view
     */
    public void prePage(View view) {

        if (mCurrentPageNumber<=0){
            ToastUtils.showToast(this,"已经是第一页了");

        }
        mCurrentPageNumber--;
        initData();
    }

    /**
     * 下一页
     * @param view
     */
    public void nextPage(View view) {
        //判断当前的页码不能大于总的页数
        if (mCurrentPageNumber>=(totalPage-1)){
            ToastUtils.showToast(this,"最后一页");
            return;
        }
        mCurrentPageNumber++;
        initData();
    }

    /**
     * 跳转
     * @param view
     */
    public void jump(View view) {

        String trim_page_number = et_page_number.getText().toString().trim();
        if (TextUtils.isEmpty(trim_page_number)){
            ToastUtils.showToast(this,"请输入正确的页码");
        }else{
            int parseInt = Integer.parseInt(trim_page_number);
            if (parseInt>=0&&parseInt<=(totalPage-1)){
                mCurrentPageNumber=parseInt-1;
                initData();

            }else {
                ToastUtils.showToast(this,"请输入正确的页码");
            }

        }

    }


}
