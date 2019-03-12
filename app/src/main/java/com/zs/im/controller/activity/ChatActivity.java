package com.zs.im.controller.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.zs.im.R;

public class ChatActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initData();

    }

    private void initData() {
        //创建一个会话的Fragmnet
        EaseChatFragment easeChatFragment = new EaseChatFragment();

        String mHxid = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);

        easeChatFragment.setArguments(getIntent().getExtras());
        //替换fragmnet
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_chat,easeChatFragment).commit();

    }
}
