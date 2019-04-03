package com.zs.im.model;


import android.content.Context;

import com.zs.im.model.bean.UserInfo;
import com.zs.im.model.dao.UserAccountDao;
import com.zs.im.model.db.DBManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//数据模型层全局类
public class Model {
    private Context mContext;
    private DBManager dbManager;
    private ExecutorService executors = Executors.newCachedThreadPool();
    //创建对象
    private static Model model=new Model();
    private UserAccountDao userAccountDao;

    //私有化构造
    private Model(){

    }
    //获取单例对象
    public static Model getInstance(){
        return model;
    }
    public void init(Context context){
        mContext = context;

        //创建用户账号数据库的操作类对象
        userAccountDao = new UserAccountDao(mContext);

        //开启全局监听
        EventListener eventListener = new EventListener(mContext);
    }
    //获取全局线程池
    public ExecutorService getGlobalThreadPool(){
        return executors;
    }

    //用户登录成功后的处理方法
    public void loginSuccess(UserInfo account){
        //校验
        if(account == null){
            return;
        }
        if(dbManager != null){
            dbManager.close();
        }
        dbManager = new DBManager(mContext,account.getName());
    }
    public DBManager getDBManager(){
        return dbManager;
    }

    //获取用户账号数据库的操作类的对象
    public UserAccountDao getUSerAccountDao(){
        return userAccountDao;
    }


}
