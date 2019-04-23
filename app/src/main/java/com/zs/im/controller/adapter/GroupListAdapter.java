package com.zs.im.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMGroup;
import com.zs.im.R;

import java.util.ArrayList;
import java.util.List;

//群组列表的适配器
public class GroupListAdapter extends BaseAdapter {
    private Context mContext;
    private List<EMGroup> mGroups = new ArrayList<>();
    public GroupListAdapter(Context context){
        mContext = context;
    }

    //刷新方法
    public void refresh(List<EMGroup> groups){
        if(groups != null && groups.size() >= 0){
            mGroups.clear();
            mGroups.addAll(groups);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mGroups == null ? 0 : mGroups.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //创建或获取viewholder
        ViewHolder hodler = null;
        if(convertView == null){
            hodler = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_grouplist,null);

            hodler.name = (TextView)convertView.findViewById(R.id.tv_grouplist_name);
            convertView.setTag(hodler);
        }else {
            hodler =(ViewHolder) convertView.getTag();
        }

        //获取当前item数据
        EMGroup emGroup = mGroups.get(position);

        //显示数据
        hodler.name.setText(emGroup.getGroupName());
        //返回数据

        return convertView;
    }

    private class ViewHolder{
        TextView name;
    }
}
