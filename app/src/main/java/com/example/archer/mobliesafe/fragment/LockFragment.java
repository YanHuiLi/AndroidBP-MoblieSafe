package com.example.archer.mobliesafe.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.archer.mobliesafe.R;
import com.example.archer.mobliesafe.bean.AppInfo;
import com.example.archer.mobliesafe.db.dao.AppLockDao;
import com.example.archer.mobliesafe.engine.AppInfos;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Archer on 2016/10/31.
 *
 * 已经加锁的界面
 */

public class LockFragment extends Fragment {

    @Bind(R.id.tv_lockApp)
    TextView tvLockApp;
    @Bind(R.id.lock_listview)
    ListView lockListview;
    private AppLockDao appLockDao;
    private List<AppInfo> appinfos;
    private List<AppInfo> lockAppInfoList;
    private LockAdapter lockAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_lock_fragment, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        appinfos = AppInfos.getAppinfos(getActivity());
        lockAppInfoList = new ArrayList<>();
        appLockDao = new AppLockDao(getActivity());
        for (AppInfo appinfo : appinfos) {
            boolean result = appLockDao.find(appinfo.getApkPackageName());

            if (result){
              lockAppInfoList.add(appinfo);
            }
        }

        lockAdapter = new LockAdapter();
        lockListview.setAdapter(lockAdapter);
    }

    private class LockAdapter extends BaseAdapter {

        private AppInfo appInfo;

        /**
         * How many items are in the data set represented by this Adapter.
         *
         * @return Count of items.
         */
        @Override
        public int getCount() {

            tvLockApp.setText("已经加锁（"+lockAppInfoList.size()+"）个");
            return lockAppInfoList.size();
        }

        /**
         * Get the data item associated with the specified position in the data set.
         *
         * @param position Position of the item whose data we want within the adapter's
         *                 data set.
         * @return The data at the specified position.
         */
        @Override
        public Object getItem(int position) {
            return lockAppInfoList.get(position);
        }

        /**
         * Get the row id associated with the specified position in the list.
         *
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return The id of the item at the specified position.
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * Get a View that displays the data at the specified position in the data set. You can either
         * create a View manually or inflate it from an XML layout file. When the View is inflated, the
         * parent View (GridView, ListView...) will apply default layout parameters unless you use
         * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
         * to specify a root view and to prevent attachment to the root.
         *
         * @param position    The position of the item within the adapter's data set of the item whose view
         *                    we want.
         * @param convertView The old view to reuse, if possible. Note: You should check that this view
         *                    is non-null and of an appropriate type before using. If it is not possible to convert
         *                    this view to display the correct data, this method can create a new view.
         *                    Heterogeneous lists can specify their number of view types, so that this View is
         *                    always of the right type (see {@link #getViewTypeCount()} and
         *                    {@link #getItemViewType(int)}).
         * @param parent      The parent that this view will eventually be attached to
         * @return A View corresponding to the data at the specified position.
         */
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

          ViewHolder  MyViewHolder=null;

            if (convertView==null){

                MyViewHolder=new ViewHolder();

                convertView=View.inflate(getActivity(),R.layout.item_lock_view,null);

                MyViewHolder.iv_imageView= (ImageView) convertView.findViewById(R.id.iv_Lock_imageView);
                MyViewHolder.tv_name= (TextView) convertView.findViewById(R.id.tv_name_lock);
                MyViewHolder.iv_unlock= (ImageView) convertView.findViewById(R.id.iv_unlock);

                convertView.setTag(MyViewHolder);
            }else {

                MyViewHolder= (ViewHolder) convertView.getTag();

            }
//            AppInfo appInfo = lockAppInfoList.get(position);
            appInfo = lockAppInfoList.get(position);

            MyViewHolder.iv_imageView.setImageDrawable(appInfo.getIcon());
            MyViewHolder.tv_name.setText(appInfo.getApkName());
            MyViewHolder.iv_unlock.setBackgroundResource(R.drawable.list_button_unlock_default);
            final View finalConvertView = convertView;

             MyViewHolder.iv_unlock.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {


                     //初始化一个位移动画
                     TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.0f,
                             Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                     translateAnimation.setDuration(500);


                     finalConvertView.startAnimation(translateAnimation);



                     new Thread(){
                         @Override
                         public void run() {
                             SystemClock.sleep(500);
                             getActivity().runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {

                                     //添加到数据库里面
                                     appLockDao.delete(appInfo.getApkPackageName());
                                     //从当前页面移除对象
                                     lockAppInfoList.remove(position);
                                     lockAdapter.notifyDataSetChanged();//刷新界面


                                 }
                             });
                         }
                     }.start();


                 }
             });


            return convertView;
        }
    }

    static class  ViewHolder{

        ImageView iv_imageView;
        TextView  tv_name;
        ImageView iv_unlock;


    }

}
