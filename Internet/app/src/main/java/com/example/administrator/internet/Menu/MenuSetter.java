package com.example.administrator.internet.Menu;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.internet.ToolClass.AppContext;
import com.example.administrator.internet.ToolClass.JSONGetter;
import com.example.administrator.internet.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/18 0018.
 */

public class MenuSetter {
    public List<Menu> menuList=new ArrayList<>();
    int position;
    private String responseDate;
    private String id;
    private String name;
    private String description;
    private String img_url;
    private int LOAD_FINISHED=1;
    private Activity activity;

    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==LOAD_FINISHED){
                setMenu(activity);
            }
        }
    };

    public void getMenuList(Activity activity,int position){
        this.position=position;
        this.activity=activity;
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONGetter jsonGetter=new JSONGetter();
                responseDate=jsonGetter.getResponseDate("http://news-at.zhihu.com/api/4/themes");
                try {
                    JSONArray jsonArray=new JSONArray("["+responseDate+"]");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        responseDate=jsonObject.getString("others");
                    }
                    jsonArray=new JSONArray(responseDate);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        id=jsonObject.getString("id");
                        img_url=jsonObject.getString("thumbnail");
                        description=jsonObject.getString("description");
                        name=jsonObject.getString("name");
                        Menu menu=new Menu(id,name,description,img_url);
                        menuList.add(menu);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message message=new Message();
                            message.what=LOAD_FINISHED;
                            handler.sendMessage(message);
                        }
                    }).start();
                }
            }
        }).start();
    }

    private void setMenu(Activity activity){

        RecyclerView recyclerView=(RecyclerView) activity.findViewById(R.id.rc);
        LinearLayoutManager layoutManager=new LinearLayoutManager(AppContext.getContext());
        recyclerView.setLayoutManager(layoutManager);
        MenuAdapter adapter=null;
        adapter=new MenuAdapter(menuList,activity,position);
        recyclerView.setAdapter(adapter);
    }
}
