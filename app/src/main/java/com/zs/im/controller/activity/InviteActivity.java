package com.zs.im.controller.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zs.im.R;
import com.zs.im.controller.adapter.InviteAdapter;
import com.zs.im.model.Model;
import com.zs.im.model.bean.InvitationInfo;
import com.zs.im.utils.Constant;

import java.util.List;

//邀请信息列表页面
public class InviteActivity extends Activity {

    private ListView lv_invite;
    private InviteAdapter inviteAdapter;
    private LocalBroadcastManager mLBM;

    private BroadcastReceiver ContactInviteChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //刷新页面
            refresh();
        }
    };

    private InviteAdapter.OnInviteListener mOninviteListener = new InviteAdapter.OnInviteListener() {
        @Override
        public void onAccept(final InvitationInfo invitationInfo) {
            //通知环信服务器，点击了接受按钮
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().acceptInvitation(invitationInfo.getUser().getHxid());

                        //数据库更新
                        Model.getInstance().getDBManager().getInviteTableDao().updateInvitationStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT,invitationInfo.getUser().getHxid());


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面发生变化
                                Toast.makeText(InviteActivity.this,"接受了邀请",Toast.LENGTH_SHORT).show();

                                //刷新页面
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this,"接受邀请失败",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            });
        }

        @Override
        public void onReject(final InvitationInfo invitationInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().declineInvitation(invitationInfo.getUser().getHxid());
                        //数据库更新
                        Model.getInstance().getDBManager().getInviteTableDao().removeInvitation(invitationInfo.getUser().getHxid());

                        //页面变化
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this,"拒绝成功",Toast.LENGTH_SHORT).show();

                                //刷新页面
                                refresh();
                            }
                        });

                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this,"拒绝失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        initView();

        initDate();
    }

    private void initDate() {
        //初始化Listview

        inviteAdapter = new InviteAdapter(this,mOninviteListener);
        lv_invite.setAdapter(inviteAdapter);

        //刷新方法
        refresh();

        //注册邀请信息变化的广播
        mLBM = LocalBroadcastManager.getInstance(this);
        mLBM.registerReceiver(ContactInviteChangedReceiver,new IntentFilter(Constant.CONTACT_INVITE_CHANGED));

    }

    private void refresh(){
        //获取数据库中的所有邀请信息
        List<InvitationInfo> invitations = Model.getInstance().getDBManager().getInviteTableDao().getInvitations();
        //刷新适配器
        inviteAdapter.refresh(invitations);
    }

    private void initView() {
        lv_invite = findViewById(R.id.lv_invite);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLBM.unregisterReceiver(ContactInviteChangedReceiver);
    }
}
