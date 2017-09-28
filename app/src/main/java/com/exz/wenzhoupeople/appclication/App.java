package com.exz.wenzhoupeople.appclication;


import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;

import com.exz.wenzhoupeople.entity.User;
import com.exz.wenzhoupeople.greendao.gen.DaoMaster;
import com.exz.wenzhoupeople.greendao.gen.DaoSession;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.mob.MobSDK;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cn.com.szw.lib.myframework.app.MyApplication;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by pc on 2017/6/22.
 */

public class App extends MyApplication {
    private static User user;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static App application;
    @Override
    public void onCreate() {
        super.onCreate();
        salt="9E127A4F0BAB43B3";
        application = this;
        MultiDex.install(this);
        com.blankj.utilcode.util.Utils.init(this);
        Fresco.initialize(this);
        CustomActivityOnCrash.install(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        MobSDK.init(getApplicationContext(), "1f3e9ba83f879", "1b7617b3297b6df104520345c1158de7");
        setDatabase();
    }

    @Override
    public String getSalt() {
        return "9E127A4F0BAB43B3";
    }


    public static boolean checkUserLogin() {
        return user != null && !"".equals(user.getUserId());
    }

    public static String getLoginUserId() {
        return user == null ? "" : user.getUserId();
    }

    public static User getUserInfo() {
        return user == null ? new User() : user;
    }

    public static void setUserInfo(User user) {
        App.user = user;
        setUser(user);
    }

    private void setDatabase() {

        // 通过DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为greenDAO 已经帮你做了。
        // 注意：默认的DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();

    }

    public static App getApplication() {
        return application;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }


    public SQLiteDatabase getDb() {
        return db;
    }

}
