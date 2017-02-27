package com.example.administrator.internet.News;

/**
 * Created by Administrator on 2017/1/18 0018.
 */

public class News {
    private String newsTitle;
    private String bitmap_url;
    private String id;
    public News(String newsTitle,String bitmap_url,String id){
        this.id=id;
        this.newsTitle=newsTitle;
        this.bitmap_url=bitmap_url;
    }
    public String getId(){return id;}
    public String getNews(){
        return newsTitle;
    }
    public String getBitmap_url(){
        return bitmap_url;
    }
    public void setBitmap_url(String bitmap_url){this.bitmap_url=bitmap_url;}
    public void setId(String id){this.id=id;}
}
