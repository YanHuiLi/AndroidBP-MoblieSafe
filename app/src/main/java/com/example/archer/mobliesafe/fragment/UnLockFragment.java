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
 */

public class UnLockFragment extends Fragment {


    @Bind(R.id.tv_UnlockApp)
    TextView tvUnlockApp;
    @Bind(R.id.unlock_listview)
    ListView unlockListview;

    private View view;
    private List<AppInfo> appinfos;
    private AppLockDao appLockDao;
    private List<AppInfo> unLockList;
    private UnLockAdapter unLockAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item_unlock_fragment, null);
        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * 在fragment里面，  onAttach,oncreate,oncreateView, ONactivitycreateed 方法 只会调用一次，
     *
     * 因此 当每次都要初始化的数据的时候，写在onstart 方法里面
     */

    @Override
    public void onStart() {
        super.onStart();

        appinfos = AppInfos.getAppinfos(getActivity());

        //获取到程序锁的
        appLockDao = new AppLockDao(getActivity());

        //初始化一个没有加锁的集合
        unLockList = new ArrayList<>();
        for (AppInfo appinfo : appinfos) {

            //判断当前的应用是否再程序所的数据库里面
            if (appLockDao.find(appinfo.getApkPackageName())){

            }else {
                //如果查询不到就说明没在数据库里面
                unLockList.add(appinfo);
            }
        }


        unLockAdapter = new UnLockAdapter();
          unlockListview.setAdapter(unLockAdapter);

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class UnLockAdapter  extends BaseAdapter{

        /**
         * How many items are in the data set represented by this Adapter.
         *
         * @return Count of items.
         */
        @Override
        public int getCount() {
            //特别注意方法的执行
            tvUnlockApp.setText("未加锁的软件（"+unLockList.size()+"）个");
            return  unLockList.size();
        }

        /**
         * Get the data item associated with the specified position in the data set.
         *
         * @param position Position of the item whose data we want within the adapter's
         *                 data set.
         * @return The data at the specified position.
         * 得到单个子对像的具体位置
         */
        @Override
        public Object getItem(int position) {
            return  unLockList.get(position);
        }

        /**
         * Get the row id associated with the specified position in the list.
         *
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return The id of the item at the specified position.
         *
         * 得到index
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

            ViewHolder viewHolder=null;
            final AppInfo appInfo;

            //判断是否缓存
            if (convertView==null){

                viewHolder=new ViewHolder();

                convertView=View.inflate(getActivity(),R.layout.item_unlock_view,null);

                viewHolder.iv_imageView= (ImageView) convertView.findViewById(R.id.iv_imageView);
                viewHolder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.iv_lock= (ImageView) convertView.findViewById(R.id.imageView1);

                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }

            //获取到当前的对象

              appInfo = unLockList.get(position);

            viewHolder.iv_imageView.setImageDrawable(appInfo.getIcon());
            viewHolder.tv_name.setText(appInfo.getApkName());
            viewHolder.iv_lock.setBackgroundResource(R.drawable.list_button_lock_default);

            final View finalConvertView = convertView;
            viewHolder.iv_lock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                    //初始化一个位移动画
                    TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f,
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
                                    appLockDao.add(appInfo.getApkPackageName());
                                    //从当前页面移除对象
                                    unLockList.remove(position);
                                    unLockAdapter.notifyDataSetChanged();//刷新界面


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
        ImageView iv_lock;


    }
}
