package com.example.administrator.internet.User;

/**
 * Created by Administrator on 2017/3/2 0002.
 */

public class User {
    public static String name=null;
    public String getName(){
        return  name;
    }
    public static boolean isLog(){
        if (name==null||name.length()+""=="0")
            return false;
        return true;
    }
}
