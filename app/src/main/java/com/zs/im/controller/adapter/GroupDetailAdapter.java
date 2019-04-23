package com.zs.im.controller.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zs.im.R;
import com.zs.im.model.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailAdapter extends BaseAdapter {

    private Context mContext;
    private boolean mIsCanModify;  //是否允许添加和删除群成员
    private List<UserInfo> mUsers = new ArrayList<>();
    private boolean mIsDeleteModel; //删除模式 true表示可以删除 false 表示不可以删除
    private OnGroupDetailListener mOnGroupDetailListener;

    public GroupDetailAdapter(Context Context, boolean isCanModify,OnGroupDetailListener onGroupDetailListener) {
        this.mContext = Context;
        this.mIsCanModify = isCanModify;
        mOnGroupDetailListener = onGroupDetailListener;
    }

    //获取当前的删除模式
    public boolean ismIsDeleteModel(){
        return mIsDeleteModel;
    }

    //设置当前的删除模式
    public void setmIsDeleteModel(boolean mIsDeleteModel){
        this.mIsDeleteModel = mIsDeleteModel;
    }

    //刷新数据
    public void refresh(List<UserInfo> users){
        if(users != null && users.size() >= 0){
            mUsers.clear();

            //添加加号和减号
            initUsers();

            mUsers.addAll(0,users);
        }
        notifyDataSetChanged();
    }

    //添加加号和减号(把加号和减号也当成用户头像来操作)
    private void initUsers() {
        UserInfo add = new UserInfo("add");
        UserInfo delete = new UserInfo("delete");

        mUsers.add(delete);
        mUsers.add(0,add);
    }

    @Override
    public int getCount() {
        return mUsers == null ? 0 : mUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取或创建viewholder
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_groupdetail,null);

            holder.photo = (ImageView) convertView.findViewById(R.id.iv_group_detail_photo);
            holder.delete = (ImageView) convertView.findViewById(R.id.iv_group_detail_delete);
            holder.name = (TextView) convertView.findViewById(R.id.tv_group_detail_name);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //获取当前item数据
        final UserInfo userInfo = mUsers.get(position);

        //显示数据
        if(mIsCanModify){//群主或开放了群权限
            if(position == getCount() - 1){//减号的处理
                //删除模式判断
                if(mIsDeleteModel){
                    convertView.setVisibility(View.INVISIBLE);
                }else{
                    convertView.setVisibility(View.VISIBLE);
                    holder.photo.setImageResource(R.drawable.em_smiley_minus_btn_pressed);
                    holder.delete.setVisibility(View.GONE);
                    holder.name.setVisibility(View.INVISIBLE);
                }
            }else if(position == getCount() - 2){//加号的处理
                if(mIsDeleteModel){
                    convertView.setVisibility(View.INVISIBLE);
                }else {
                    convertView.setVisibility(View.VISIBLE);
                    holder.photo.setImageResource(R.drawable.em_smiley_add_btn_pressed);
                    holder.delete.setVisibility(View.GONE);
                    holder.name.setVisibility(View.INVISIBLE);
                }
            }else {//群成员
                convertView.setVisibility(View.VISIBLE);
                holder.name.setVisibility(View.VISIBLE);

                holder.photo.setImageResource(R.drawable.em_default_avatar);
                holder.name.setText(userInfo.getName());

                if(mIsDeleteModel){
                    holder.delete.setVisibility(View.VISIBLE);
                }else{
                    holder.delete.setVisibility(View.GONE);
                }
            }

            //点击事件的处理
            if(position == getCount() - 1){//减号的位置
                holder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!mIsDeleteModel){
                            mIsDeleteModel = true;
                            notifyDataSetChanged();
                        }
                    }
                });

            }else if(position == getCount() - 2){//加号的位置
                holder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnGroupDetailListener.onAddMembers();
                    }
                });
            }else{
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnGroupDetailListener.onDeleteMembers(userInfo);
                    }
                });
            }

        }else{//普通群成员
            if(position == getCount() - 1  || position == getCount() - 2){
                convertView.setVisibility(View.GONE);
            }else{
                convertView.setVisibility(View.VISIBLE);
                //名称
                holder.name.setText(userInfo.getName());
                //头像
                holder.photo.setImageResource(R.drawable.em_default_avatar);
                //删除
                holder.delete.setVisibility(View.GONE);
            }
        }

        //返回view
        return convertView;
    }

    private class ViewHolder{
        private ImageView photo ;
        private ImageView delete;
        private TextView name;
    }

    public interface OnGroupDetailListener{
        //添加群成员
        void onAddMembers();

        //删除群成员方法
        void onDeleteMembers(UserInfo user);
    }
}
