package com.jnu.student;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jnu.student.MainFragments.HomeFragment;
import com.jnu.student.MainFragments.RewardFragment;
import com.jnu.student.MainFragments.TaskFragment;
import com.jnu.student.MainFragments.WalletFragment;

public class MainActivity extends AppCompatActivity {



    private BottomNavigationView mNavigationView;

    private FragmentManager mFragmentManager;

    private Fragment[] fragments;
    private int lastFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationView = findViewById(R.id.main_navigation_bar);
        initFragment();
        initListener();
    }


    private void initFragment() {
        TaskFragment taskFragment = new TaskFragment();
        RewardFragment rewardFragment = new RewardFragment();
        WalletFragment walletFragment = new WalletFragment();
        HomeFragment homeFragment = new HomeFragment();
        fragments = new Fragment[]{taskFragment, rewardFragment,walletFragment, homeFragment};
        mFragmentManager = getSupportFragmentManager();
        //默认显示taskFragment
        mFragmentManager.beginTransaction()
                .replace(R.id.main_page_controller, taskFragment)
                .show(taskFragment)
                .commit();
    }
    private void initListener() {
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 处理项目的点击事件
                if (item.getItemId() == R.id.menu_task) {
                    // 执行菜单任务的操作
                    if (lastFragment != 0) {
                        MainActivity.this.switchFragment(lastFragment, 0);
                        lastFragment = 0;
                    }
                    return true;
                } else if (item.getItemId() == R.id.menu_reward) {
                    // 执行奖励菜单的操作
                    if (lastFragment != 1) {
                        MainActivity.this.switchFragment(lastFragment, 1);
                        lastFragment = 1;
                    }
                    return true;
                } else if (item.getItemId() == R.id.menu_wallet) {
                    // 执行统计菜单的操作
                    if (lastFragment != 2) {
                        MainActivity.this.switchFragment(lastFragment, 2);
                        lastFragment = 2;
                    }
                    return true;
                } else if (item.getItemId() == R.id.menu_home) {
                    // 执行我的菜单的操作
                    if (lastFragment != 3) {
                        MainActivity.this.switchFragment(lastFragment, 3);
                        lastFragment = 3;
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void switchFragment(int lastFragment, int index) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.hide(fragments[lastFragment]);
        if (!fragments[index].isAdded()){
            transaction.add(R.id.main_page_controller,fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

}