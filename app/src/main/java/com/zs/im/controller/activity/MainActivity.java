package com.zs.im.controller.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.zs.im.R;
import com.zs.im.controller.fragment.ChatFragment;
import com.zs.im.controller.fragment.ContactListFragment;
import com.zs.im.controller.fragment.SettingFragment;

public class MainActivity extends FragmentActivity {

    private  RadioGroup rg_main;
    private  ChatFragment chatFragment;
    private  ContactListFragment contactListFragment;
    private  SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDate();
        initListener();
    }

    private void initView() {
        rg_main = findViewById(R.id.rg_main);
    }

    private void initDate() {
        //创建三个Fragment对象
        chatFragment = new ChatFragment();
        contactListFragment = new ContactListFragment();
        settingFragment = new SettingFragment();

    }

    private void initListener() {
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment fragment = null;
                switch (checkedId){
                    //会话列表页面
                    case R.id.rb_main_chat:
                        fragment = chatFragment;
                        break;
                    //联系人列表页面
                    case R.id.rb_main_contact:
                        fragment = contactListFragment;
                        break;
                    //设置页面
                    case R.id.rb_main_setting:
                        fragment = settingFragment;
                        break;
                }
                //实现fragment切换的方法
                switchFragment(fragment);
            }
        });
        //默认选择会话列表页面
        rg_main.check(R.id.rb_main_chat);
    }

    //实现fragment切换的方法
    private void switchFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_main,fragment).commit();
    }

}
