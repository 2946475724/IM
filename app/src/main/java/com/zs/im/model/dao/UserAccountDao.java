package com.zs.im.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zs.im.model.bean.UserInfo;
import com.zs.im.model.db.UserAccountDB;

//用户账号数据库的操作类
public class UserAccountDao {
    private final UserAccountDB mHelper;

    public UserAccountDao(Context context) {
        mHelper = new UserAccountDB( context );
    }

    //添加用户到数据库
    public void addAccount(UserInfo user) {
        //获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //执行添加操作
        ContentValues values = new ContentValues();
        values.put( UserAccoutTable.COL_HXID, user.getHxid() );
        values.put( UserAccoutTable.COL_NAME, user.getName() );
        values.put( UserAccoutTable.COL_NICK, user.getNick() );
        values.put( UserAccoutTable.COL_PHOTO, user.getPhoto() );

        db.replace( UserAccoutTable.TAB_NAME, null, values );

    }

    //根据环信id获取所有用户信息
    public UserInfo getAccountByHxId(String hxId) {
        //获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行查询语句
        String sql = "select * from " + UserAccoutTable.TAB_NAME + " where " + UserAccoutTable.COL_HXID + "=?";
        Cursor cursor = db.rawQuery( sql, new String[]{hxId} );

        UserInfo userInfo = null;
        if (cursor.moveToNext()) {
            userInfo = new UserInfo();
            userInfo.setHxid( cursor.getString( cursor.getColumnIndex( UserAccoutTable.COL_HXID ) ) );
            userInfo.setName( cursor.getString( cursor.getColumnIndex( UserAccoutTable.COL_NAME ) ) );
            userInfo.setNick( cursor.getString( cursor.getColumnIndex( UserAccoutTable.COL_NICK ) ) );
            userInfo.setPhoto( cursor.getString( cursor.getColumnIndex( UserAccoutTable.COL_PHOTO ) ) );
        }

        //关闭资源
        cursor.close();

        //返回数据
        return userInfo;

    }
}
