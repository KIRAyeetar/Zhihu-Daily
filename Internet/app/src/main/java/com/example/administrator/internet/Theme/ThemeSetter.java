package com.example.administrator.internet.Theme;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.internet.Editor.Editors;
import com.example.administrator.internet.News.News;
import com.example.administrator.internet.ToolClass.JSONGetter;
import com.example.administrator.internet.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/19 0019.
 */

public class ThemeSetter {
    public List<News> newsList=new ArrayList<>();
    public static List<Editors> editorsList=new ArrayList<>();
    private static Bitmap theme_img;
    private String responseDate;
    private String news_URL;
    private String editor_URL;
    private Bitmap avatar;
    private String name;
    private String editor_url;
    private String bio;
    private String title[];
    private String news_IMG_URL[];
    private String id[];
    private String background_URL;
    private int LOAD_FINISHED=1;
    private Activity homepage;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==LOAD_FINISHED){
                setNews(homepage);
            }
        }
    };

    public void setIMGFromInternet(final String url, final ThemeContent homePage,final String title_img_url) {
        this.homepage=homePage;
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONGetter jsonGetter=new JSONGetter();
                responseDate=jsonGetter.getResponseDate(url);
                try {
                    JSONArray jsonArray=new JSONArray("["+responseDate+"]");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        news_URL=jsonObject.getString("stories");
                        editor_URL=jsonObject.getString("editors");
                        background_URL=jsonObject.getString("background");
                    }
                    jsonArray=new JSONArray(news_URL);
                    news_IMG_URL=new String[jsonArray.length()];
                    title=new String[jsonArray.length()];
                    id=new String[jsonArray.length()];
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        title[i]=jsonObject.getString("title");
                        id[i]=jsonObject.getString("id");
                        if (jsonObject.has("images")){
                            news_IMG_URL[i]=jsonObject.getString("images");
                            news_IMG_URL[i]=news_IMG_URL[i].substring(2,news_IMG_URL[i].length()-2);
                        }else {
                            news_IMG_URL[i]="NO_IMG";
                        }
                        News news=new News(title[i],news_IMG_URL[i],id[i]);
                        newsList.add(news);
                    }
                    jsonArray=new JSONArray(editor_URL);
                    editorsList.clear();
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        avatar=BitmapFactory.decodeStream(new URL(jsonObject.getString("avatar")).openStream());
                        bio=jsonObject.getString("bio");
                        name=jsonObject.getString("name");
                        editor_url=jsonObject.getString("url");
                        editorsList.add(new Editors(avatar,name, editor_url,bio));
                    }
                    theme_img= BitmapFactory.decodeStream(new URL(background_URL).openStream());
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
        RecyclerView recyclerView=(RecyclerView) homePage.findViewById(R.id.theme_rc);
        LinearLayoutManager layoutManager=new LinearLayoutManager(homePage);
        recyclerView.setLayoutManager(layoutManager);
        ThemeAdapter themeAdapter=new ThemeAdapter(newsList,editorsList);
        recyclerView.setAdapter(themeAdapter);

    }
    public static Bitmap getBitmap(){
        return theme_img;
    }
    public static List getEditorList(){
        return editorsList;
    }

}
