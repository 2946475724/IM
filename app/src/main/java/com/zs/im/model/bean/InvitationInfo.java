package com.zs.im.model.bean;

//邀请信息的bean类
public class InvitationInfo {
    private UserInfo user;   //联系人
    private GroupInfo group;   //群组
    private String reason;    //邀请原因
    private InvitationStatus status;   //邀请的状态

    public InvitationInfo() {
    }

    public InvitationInfo(UserInfo user, GroupInfo group, String reason, InvitationStatus status) {
        this.user = user;
        this.group = group;
        this.reason = reason;
        this.status = status;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public GroupInfo getGroup() {
        return group;
    }

    public void setGroup(GroupInfo group) {
        this.group = group;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "InvitationInfo{" +
                "user=" + user +
                ", group=" + group +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                '}';
    }

    public enum InvitationStatus{
        //联系人邀请信息状态
        NEW_INVITE, //新邀请
        INVITE_ACCEPT,  //接受邀请
        INVITE_ACCEPT_BY_PEER, //接受被邀请


        //群组邀请信息状态
        NEW_GROUP_INVITE,  //收到邀请去加群
        NEW_GROUP_APPLICATION,  //收到申请群加入
        NEW_INVITE_ACCEPTED,   //群邀请已经被对方接受
        GROUP_APPLICATION_ACCEPTED, //群申请已经被批准
        GROUP_ACCEPT_INVITE, //接受了群邀请
        GROUP_ACCEPT_APPLICATION, //批准的群加入申请
        GROUP_REJECT_APPLICATION,  //拒绝了群申请加入
        GROUP_INVITE_DECLINED,  //群邀请被对方拒绝
        GROUP_APPLICATION_DECLINED  //群申请被拒绝
    }

}
