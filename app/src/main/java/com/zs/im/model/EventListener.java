package com.zs.im.model;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.zs.im.model.bean.InvitationInfo;
import com.zs.im.model.bean.UserInfo;
import com.zs.im.utils.Constant;
import com.zs.im.utils.SpUtils;

//全局事件监听类
public class EventListener {

    private Context mContext;
    private LocalBroadcastManager mLBM;


    public EventListener(Context context) {
        mContext = context;

        //创建一个发送广播的管理者对象
        mLBM = LocalBroadcastManager.getInstance(mContext);

        //注册一个联系人变化的监听
        EMClient.getInstance().contactManager().setContactListener(emContactListener);
    }

    //注册一个联系人变化的监听
    private final EMContactListener emContactListener = new EMContactListener() {

        //添加联系人方法
        @Override
        public void onContactAdded(String hxid) {
            //数据库更新
            Model.getInstance().getDBManager().getContactTableDao().saveContact(new UserInfo(hxid), true);

            //发送联系人变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        //联系人删除后执行的方法
        @Override
        public void onContactDeleted(String hxid) {
            //数据库更新
            Model.getInstance().getDBManager().getContactTableDao().deleteContactByHxId(hxid);
            Model.getInstance().getDBManager().getInviteTableDao().removeInvitation(hxid);
            //发送联系人变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        //接受到联系人的新邀请
        @Override
        public void onContactInvited(String hxid, String reason) {
            //数据库更新
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setUser(new UserInfo(hxid));
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_INVITE);  //新邀请

            Model.getInstance().getDBManager().getInviteTableDao().addInvitation(invitationInfo);

            //红点的处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            //发送邀请信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));

        }

        //别人同意了你的好友邀请
        @Override
        public void onFriendRequestAccepted(String hxid) {
            //数据库更新
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setUser(new UserInfo(hxid));
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER);  //别人同意了你的邀请
            Model.getInstance().getDBManager().getInviteTableDao().addInvitation(invitationInfo);

            //红点的处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);


            //发送邀请信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }

        //别人拒绝了你的好友邀请
        @Override
        public void onFriendRequestDeclined(String s) {
            //红点的处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            //发送邀请信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }
    };
}
