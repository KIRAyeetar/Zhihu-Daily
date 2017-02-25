package com.example.administrator.internet.Editor;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/2/21 0021.
 */

public class Editors {
    private Bitmap avatar;
    private String name;
    private String url;
    private String bio;
    public Editors(Bitmap avatar,String name,String url,String bio){
        this.avatar=avatar;this.bio=bio;this.name=name;this.url=url;
    }
    public String getName(){return name;}
    public String getUrl(){return url;}
    public String getBio(){return bio;}
    public Bitmap getAvatar(){return avatar;}
}
