package com.example.administrator.internet;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/19 0019.
 */

public class NewsSetter {

    private String responseDate;
    private String news_URL;
    public List<News> newsList=new ArrayList<>();
    private String title[];
    private String news_IMG_URL[];
    private String id[];
    private int LOAD_FINISHED=1;
    private Activity homepage;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==LOAD_FINISHED){
                setNews(homepage);
            }
        }
    };

    public void setIMGFromInternet(final String url, final Activity homePage) {
        this.homepage=homePage;
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONGetter jsonGetter=new JSONGetter();
                responseDate=jsonGetter.getResponseDate(url);
                try {
                    JSONArray jsonArray1=new JSONArray("["+responseDate+"]");

                    for (int i=0;i<jsonArray1.length();i++){
                        JSONObject jsonObject=jsonArray1.getJSONObject(i);
                        news_URL=jsonObject.getString("stories");
                    }
                    JSONArray jsonArray2=new JSONArray(news_URL);
                    news_IMG_URL=new String[jsonArray2.length()];
                    title=new String[jsonArray2.length()];
                    id=new String[jsonArray2.length()];

                    for (int i=0;i<jsonArray2.length();i++){
                        JSONObject jsonObject=jsonArray2.getJSONObject(i);
                        news_IMG_URL[i]=jsonObject.getString("images");
                        news_IMG_URL[i]=news_IMG_URL[i].substring(2,news_IMG_URL[0].length()-2);
                        title[i]=jsonObject.getString("title");
                        id[i]=jsonObject.getString("id");
                        News news=new News(title[i],news_IMG_URL[i],id[i]);
                        newsList.add(news);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
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

    public void setNews(Activity homePage){
        RecyclerView recyclerView=(RecyclerView) homePage.findViewById(R.id.home_recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(homePage);
        recyclerView.setLayoutManager(layoutManager);
        News.NewsAdapter adapter=new News.NewsAdapter(newsList);
        recyclerView.setAdapter(adapter);
    }
}
