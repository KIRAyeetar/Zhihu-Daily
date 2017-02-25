package com.example.administrator.internet.Menu;

/**
 * Created by Administrator on 2017/2/18 0018.
 */

public class Menu {
    private String id;
    private String name;
    private String description;
    private String img_url;
    public  Menu(String id,String name,String description,String img_url){
        this.id=id;
        this.name=name;
        this.description=description;
        this.img_url=img_url;
    }
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public String getImg_url(){ return img_url; }
}
