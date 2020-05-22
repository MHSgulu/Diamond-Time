package com.tool.calendar;

import android.app.Application;
import android.content.Context;


/**

 */
public class MyApplication extends Application{



    @Override
    public void onCreate() {
        super.onCreate();

        //初始化全局的 用户信息管理类，记录个人信息。
        User.getInstance().initContent(getApplicationContext());

    }




}
