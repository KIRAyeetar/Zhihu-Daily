package com.example.administrator.internet.Comment;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/2/20 0020.
 */

public class Comment {
    private String author;
    private int time;
    private Bitmap avatar;
    private String content;
    private String likes;
    public Comment(String author, int time, Bitmap avatar, String content, String likes){
        this.author=author;
        this.avatar=avatar;
        this.content=content;
        this.likes=likes;
        this.time=time;
    }
    public String getAuthor(){
        return  author;
    }
    public int getTime(){
        return time;
    }
    public String getContent(){
        return content;
    }
    public String getLikes(){
        return  likes;
    }
    public Bitmap getAvatar(){
        return avatar;
    }


}
