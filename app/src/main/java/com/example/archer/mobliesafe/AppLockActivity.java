package com.example.archer.mobliesafe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.archer.mobliesafe.fragment.LockFragment;
import com.example.archer.mobliesafe.fragment.UnLockFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 程序锁
 * Created by Archer on 2016/10/31.
 */
public class AppLockActivity extends FragmentActivity {


    @Bind(R.id.tv_lock)
    TextView tvLock;
    @Bind(R.id.tv_unlock)
    TextView tvUnlock;
    @Bind(R.id.applock_framelaout)
    FrameLayout applockFramelaout;
    private FragmentManager fragmentManager;
    private UnLockFragment unLockFragment;
    private LockFragment lockFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();


    }

    private void initUI() {
        setContentView(R.layout.activity_app_lock);
        ButterKnife.bind(this);
        //要想使用fragment就必须得到fragment的管理者

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();//开启事务
        unLockFragment = new UnLockFragment();
        lockFragment = new LockFragment();

        /**
         * 1.替换界面
         * 2.两个参数，替换界面的ID和需要替换的fragment，注意一定要commit，否则没用。
         *
         */
        fragmentTransaction.replace(R.id.applock_framelaout, unLockFragment).commit();
    }


    @OnClick({R.id.tv_lock, R.id.tv_unlock})
    public void onClick(View view) {

        FragmentTransaction ft = fragmentManager.beginTransaction();


        switch (view.getId()) {
            case R.id.tv_lock:

                tvLock.setBackgroundResource(R.drawable.tab_left_pressed);
                tvUnlock.setBackgroundResource(R.drawable.tab_right_default);

                ft.replace(R.id.applock_framelaout, lockFragment);



                break;
            case R.id.tv_unlock:


                tvLock.setBackgroundResource(R.drawable.tab_left_default);
                tvUnlock.setBackgroundResource(R.drawable.tab_right_pressed);

                ft.replace(R.id.applock_framelaout, unLockFragment);



                break;
        }

        ft.commit();
    }
}
