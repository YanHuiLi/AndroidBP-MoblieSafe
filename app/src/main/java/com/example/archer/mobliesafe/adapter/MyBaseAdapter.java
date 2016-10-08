package com.example.archer.mobliesafe.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Archer on 2016/8/14.
 * <p/>
 * 描述:
 * <p/>
 * 作者
 */
public  abstract class MyBaseAdapter<T>  extends BaseAdapter{

    public MyBaseAdapter(List<T> lists, Context mContext) {
        this.lists = lists;
        this.mContext = mContext;
    }

    public MyBaseAdapter() {
    }

    public List<T> lists;

public Context mContext;
    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
