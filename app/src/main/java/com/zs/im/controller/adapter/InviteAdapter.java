package com.zs.im.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zs.im.R;
import com.zs.im.model.bean.InvitationInfo;
import com.zs.im.model.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

//邀请信息列表页面的适配器
public class InviteAdapter extends BaseAdapter {
    private Context mContext;
    private List<InvitationInfo> mInvitationInfos = new ArrayList<>();
    private OnInviteListener mOnInviteListener;

    public InviteAdapter(Context context, OnInviteListener OnInviteListener) {
        mContext = context;
        mOnInviteListener = OnInviteListener;
    }

    //刷新数据的方法
    public void refresh(List<InvitationInfo> invitationInfos) {

        if (invitationInfos != null && invitationInfos.size() >= 0) {

            mInvitationInfos.clear();

            mInvitationInfos.addAll(invitationInfos);

            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mInvitationInfos == null ? 0 : mInvitationInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mInvitationInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //1.获取或创建ViewHolder
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_invite, null);

            holder.name = convertView.findViewById(R.id.tv_invite_name);
            holder.reason = convertView.findViewById(R.id.tv_invite_reason);
            holder.accept = convertView.findViewById(R.id.bt_invite_accept);
            holder.reject = convertView.findViewById(R.id.bt_invite_reject);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        //2.获取当前item数据
        final InvitationInfo invitationInfo = mInvitationInfos.get(position);

        //3.显示当前item数据
        UserInfo user = invitationInfo.getUser();
        if (user != null) {//联系人
            //名称的展示
            holder.name.setText(invitationInfo.getUser().getName());
            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);

            //原因
            if (invitationInfo.getStatus() == InvitationInfo.InvitationStatus.NEW_INVITE) {//新的邀请
                if (invitationInfo.getReason() == null) {
                    holder.reason.setText("添加好友");
                } else {
                    holder.reason.setText(invitationInfo.getReason());
                }
                holder.accept.setVisibility(View.VISIBLE);
                holder.reject.setVisibility(View.VISIBLE);
            }
            else if (invitationInfo.getStatus() == InvitationInfo.InvitationStatus.INVITE_ACCEPT) {//接受邀请
                if (invitationInfo.getReason() == null) {
                    holder.reason.setText("接受邀请");
                } else {
                    holder.reason.setText(invitationInfo.getReason());
                }
            }
            else if (invitationInfo.getStatus() == InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER) {//邀请被接受
                if (invitationInfo.getReason() == null) {
                    holder.reason.setText("邀请被接受");
                } else {
                    holder.reason.setText(invitationInfo.getReason());
                }
            }

            //按钮的处理
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnInviteListener.onAccept(invitationInfo);
                }
            });

            //拒绝按钮的点击事件处理
            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnInviteListener.onReject(invitationInfo);
                }
            });

        } else {//群组
            //显示名称
            holder.name.setText(invitationInfo.getGroup().getInvitePerson());
            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);

            //显示原因
            switch (invitationInfo.getStatus()){
                //你的群申请已经被接受
                case GROUP_APPLICATION_ACCEPTED:
                    holder.reason.setText("你的群申请已经被接受");
                    break;
                //你的群邀请已经被接受
                case GROUP_INVITE_ACCEPTED:
                    holder.reason.setText("你的群邀请已经被接受");
                    break;
                //你的群申请已经被拒绝
                case GROUP_APPLICATION_DECLINED:
                    holder.reason.setText("你的群申请已经被拒绝");
                    break;
                //你的群邀请已经被拒绝
                case GROUP_INVITE_DECLINED:
                    holder.reason.setText("你的群邀请已经被拒绝");
                    break;
                //你收到了群邀请
                case NEW_GROUP_INVITE:
                    holder.accept.setVisibility(View.VISIBLE);
                    holder.reject.setVisibility(View.VISIBLE);
                    //接受邀请
                    holder.accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnInviteListener.onInviteAccept(invitationInfo);
                        }
                    });
                    //拒绝邀请
                    holder.reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnInviteListener.onInviteReject(invitationInfo);
                        }
                    });
                    holder.reason.setText("你收到了群邀请");
                    break;
                //你收到了群申请
                case NEW_GROUP_APPLICATION:
                    holder.accept.setVisibility(View.VISIBLE);
                    holder.reject.setVisibility(View.VISIBLE);
                    //接受申请
                    holder.accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnInviteListener.onApplicationAccept(invitationInfo);
                        }
                    });
                    //拒绝申请
                    holder.reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnInviteListener.onApplicationReject(invitationInfo);
                        }
                    });
                    holder.reason.setText("你收到了群申请");
                    break;
                //你接受了群邀请
                case GROUP_ACCEPT_INVITE:
                    holder.reason.setText("你接受了群邀请");
                    break;
                //你批准了群申请
                case GROUP_ACCEPT_APPLICATION:
                    holder.reason.setText("你批准了群申请");
                    break;
//                //你拒绝了群邀请
//                case GROUP_REJECT_INVITE:
//                    holder.reason.setText("你拒绝了群邀请");
//                    break;
//                //你拒绝了群申请
//                case GROUP_REJECT_APPLICATION:
//                    holder.reason.setText("你拒绝了群申请");
//                    break;
            }
        }

        //4.返回view
        return convertView;
    }

    private class ViewHolder {
        private TextView name;
        private TextView reason;
        private Button accept;
        private Button reject;
    }

    public interface OnInviteListener {
        //联系人接受按钮的点击事件
        void onAccept(InvitationInfo invitationInfo);

        //联系人拒绝按钮的点击事件
        void onReject(InvitationInfo invitationInfo);

        //接受邀请按钮处理
        void onInviteAccept(InvitationInfo invitationInfo);

        //拒绝邀请按钮处理
        void onInviteReject(InvitationInfo invitationInfo);

        //接受申请按钮处理
        void onApplicationAccept(InvitationInfo invitationInfo);

        //拒绝申请按钮处理
        void onApplicationReject(InvitationInfo invitationInfo);
    }
}
