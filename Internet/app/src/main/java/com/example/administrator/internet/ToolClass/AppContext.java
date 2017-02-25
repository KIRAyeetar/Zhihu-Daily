package com.example.administrator.internet.ToolClass;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/2/8 0008.
 */
//获取全局Context
public class AppContext extends Application {
    private static Context instance;

    @Override
    public void onCreate()
    {
        instance = getApplicationContext();
    }

    public static Context getContext()
    {
        return instance;
    }

}