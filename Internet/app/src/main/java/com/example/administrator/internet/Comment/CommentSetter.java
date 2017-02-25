package com.example.administrator.internet.Comment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.internet.ToolClass.JSONGetter;
import com.example.administrator.internet.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/20 0020.
 */

public class CommentSetter {
    private String author[];
    private int time[];
    private String avatar_url[];
    private String content[];
    private String likes[];
    private String comments_url;
    private int LOAD_FINISHED=1;
    private Activity commentContent;
    private Bitmap avatar[];
    private List<Comment> commentsList=new ArrayList();
    private String responseDate;

    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==LOAD_FINISHED){
                setComment(commentContent);
            }
        }
    };


    public void setIMGFromInternet(final String id, final CommentContent commentContent) {

        this.commentContent=commentContent;
        commentsList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONGetter jsonGetter=new JSONGetter();
                responseDate=jsonGetter.getResponseDate("http://news-at.zhihu.com/api/4/story/"+id+"/"+CommentContent.getType()+"-comments");
                try {
                    JSONArray jsonArray=new JSONArray("["+responseDate+"]");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        comments_url=jsonObject.getString("comments");
                    }
                    jsonArray=new JSONArray(comments_url);
                    author=new String[jsonArray.length()];
                    time=new int[jsonArray.length()];
                    likes=new String[jsonArray.length()];
                    avatar_url=new String[jsonArray.length()];
                    content=new String[jsonArray.length()];
                    avatar=new Bitmap[jsonArray.length()];
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        author[i]=jsonObject.getString("author");
                        likes[i]=jsonObject.getString("likes");
                        time[i]=jsonObject.getInt("time");
                        avatar_url[i]=jsonObject.getString("avatar");
                        content[i]=jsonObject.getString("content");
                        avatar[i]= BitmapFactory.decodeStream(new URL(avatar_url[i]).openStream());
                        Comment comment=new Comment(author[i],time[i],avatar[i],content[i],likes[i]);
                        commentsList.add(comment);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally{

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
    public void setComment(Activity commentContent){
        RecyclerView recyclerView=(RecyclerView) commentContent.findViewById(R.id.comment_rc);
        LinearLayoutManager layoutManager=new LinearLayoutManager(commentContent);
        recyclerView.setLayoutManager(layoutManager);
        CommentAdapter adapter=new CommentAdapter(commentsList);
        recyclerView.setAdapter(adapter);
    }
}
